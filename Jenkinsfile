pipeline {
    agent any

    environment {
        MYSQL_HOST = "mysql"
        MYSQL_USER = "root"
        MYSQL_PASS = "123456"
        MYSQL_DATABASE = "vegana_store"
        APP_PORT = "8080"
        BASE_URL = "http://localhost:8080"
        SELENIUM_HUB_URL = "http://selenium-hub:4444"
        GITHUB_ACTIONS = "true"
    }

    options {
        timeout(time: 40, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        ansiColor('xterm')
    }

    stages {

        /* ===========================
           🔍 CHECKOUT
        ============================ */
        stage('🔍 Checkout Code') {
            steps {
                echo '📥 Checking out code from repository...'
                checkout scm
                sh 'git rev-parse HEAD > .git/commit-id'
                sh 'cat .git/commit-id'
            }
        }

        /* ===========================
           ⚙️ ENV INFO
        ============================ */
        stage('⚙️ Setup Environment') {
            steps {
                echo '🔧 Setting up build environment...'
                script {
                    sh '''
                        echo "=== Environment Info ==="
                        java -version || echo "Java not found, will use Maven wrapper"
                        ./mvnw -version || mvn -version || echo "Maven not found"
                        docker --version || echo "Docker not available"
                        echo "========================"
                    '''
                }
            }
        }

        /* ===========================
           🐬 WAIT FOR MYSQL
        ============================ */
        stage('🐬 Wait for MySQL') {
            steps {
                echo '⏳ Waiting for MySQL...'
                script {
                    sh '''
                        echo "Checking MySQL connection..."
                        for i in $(seq 1 30); do
                            # Try using netcat first (faster)
                            if nc -z ${MYSQL_HOST} 3306 2>/dev/null; then
                                # Then verify MySQL is actually ready
                                export MYSQL_PWD=${MYSQL_PASS}
                                if mysqladmin ping -h ${MYSQL_HOST} -u${MYSQL_USER} --silent 2>/dev/null; then
                                    echo "✅ MySQL is ready!"
                                    unset MYSQL_PWD
                                    exit 0
                                fi
                                unset MYSQL_PWD
                            fi
                            echo "Waiting for MySQL ($i/30)..."
                            sleep 2
                        done
                        echo "❌ MySQL did not start!"
                        exit 1
                    '''
                }
            }
        }

        /* ===========================
           🗄️ SETUP DATABASE
        ============================ */
        stage('🗄️ Setup Database') {
            steps {
                script {
                    sh '''
                        echo "Creating database if not exists..."
                        export MYSQL_PWD=${MYSQL_PASS}
                        mysql -h ${MYSQL_HOST} -u${MYSQL_USER} \
                            -e "CREATE DATABASE IF NOT EXISTS ${MYSQL_DATABASE};" || true

                        if [ -f vegana.sql ]; then
                            echo "Importing DB schema..."
                            mysql -h ${MYSQL_HOST} -u${MYSQL_USER} ${MYSQL_DATABASE} < vegana.sql
                            echo "✅ Schema imported!"
                        else
                            echo "⚠️ vegana.sql not found → skipping import"
                        fi
                        unset MYSQL_PWD
                    '''
                }
            }
        }

        /* ===========================
           🔨 BUILD APP
        ============================ */
        stage('🔨 Build Application') {
            steps {
                script {
                    sh '''
                        echo "🏗️ Building Spring Boot app..."
                        if [ -f ./mvnw ]; then
                            ./mvnw clean package -DskipTests
                        else
                            mvn clean package -DskipTests
                        fi
                        echo "✅ Build done!"
                    '''
                }
            }
        }

        /* ===========================
           🚀 START APP
        ============================ */
        stage('🚀 Start Spring Boot Application') {
            steps {
                script {
                    sh '''
                        echo "Starting Spring Boot in background..."
                        if [ -f ./mvnw ]; then
                            nohup ./mvnw spring-boot:run \
                                -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=false -Dhibernate.hbm2ddl.auto=none" \
                                > app.log 2>&1 &
                        else
                            nohup mvn spring-boot:run \
                                -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=false -Dhibernate.hbm2ddl.auto=none" \
                                > app.log 2>&1 &
                        fi

                        echo $! > app.pid
                        echo "PID: $(cat app.pid)"
                    '''

                    sh '''
                        echo "⏳ Waiting for app to start..."
                        for i in {1..30}; do
                            if curl -f http://localhost:${APP_PORT}/ >/dev/null 2>&1; then
                                echo "✅ App started!"
                                exit 0
                            fi
                            echo "($i/30) App not ready, retrying..."
                            sleep 3
                        done

                        echo "❌ App failed! Tail log:"
                        tail -50 app.log || true
                        exit 1
                    '''
                }
            }
        }

        /* ===========================
           🌐 CHECK GRID
        ============================ */
        stage('🌐 Check Selenium Grid') {
            steps {
                sh '''
                    for i in {1..10}; do
                        if curl -s http://selenium-hub:4444/wd/hub/status >/dev/null; then
                            echo "✅ Selenium Grid Ready"
                            exit 0
                        fi
                        echo "Waiting Selenium Grid ($i/10)..."
                        sleep 2
                    done
                    echo "⚠️ Grid offline → tests will use local Chrome"
                '''
            }
        }

        /* ===========================
           🧪 RUN TESTS
        ============================ */
        stage('🧪 Run Automation Tests') {
            steps {
                script {
                    sh '''
                        mkdir -p test-output/reports test-output/screenshots test-output/logs
                        export GITHUB_ACTIONS=true
                        export SELENIUM_HUB_URL=${SELENIUM_HUB_URL}
                        
                        echo "Running TestNG tests..."
                        if [ -f ./mvnw ]; then
                            ./mvnw test -DsuiteXmlFile=src/test/resources/testng.xml || true
                        else
                            mvn test -DsuiteXmlFile=src/test/resources/testng.xml || true
                        fi
                        echo "✅ Tests completed!"
                    '''
                }
            }
        }

        /* ===========================
           📊 ARCHIVE RESULTS
        ============================ */
        stage('📊 Archive Test Results') {
            steps {
                archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
                archiveArtifacts artifacts: 'app.log', allowEmptyArchive: true

                publishTestNGResults(
                    testResultsPattern: 'target/surefire-reports/testng-results.xml',
                    escapeTestDescription: false,
                    escapeExceptionMsg: false
                )
            }
        }
    }

    /* ===========================
       🧹 POST ACTIONS
    ============================ */
    post {
        always {
            script {
                sh '''
                    if [ -f app.pid ]; then
                        PID=$(cat app.pid)
                        echo "Stopping application (PID: $PID)..."
                        kill $PID 2>/dev/null || true
                        sleep 2
                        kill -9 $PID 2>/dev/null || true
                        rm -f app.pid
                    fi
                    pkill -f "spring-boot:run" || true
                    echo "🧹 App stopped."
                '''
            }
        }
        success {
            echo "🎉 SUCCESS: CI/CD Pipeline Completed!"
        }
        failure {
            echo "❌ FAILURE: Check console log"
        }
    }
}
