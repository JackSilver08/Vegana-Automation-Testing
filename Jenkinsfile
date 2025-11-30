pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8'
        jdk 'JDK-17'
    }
    
    environment {
        BASE_URL = 'http://localhost:8080'
        BROWSER = 'chrome'
        DB_HOST = 'localhost'
        DB_PORT = '3306'
        DB_NAME = 'vegana_store'
        DB_USERNAME = 'root'
        DB_PASSWORD = '123456'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Setup MySQL Database') {
            steps {
                echo 'Setting up MySQL database...'
                sh '''
                    # Kiểm tra MySQL connection
                    mysql -u root -p123456 -e "SELECT 1;" || {
                        echo "❌ Cannot connect to MySQL"
                        echo "Please ensure:"
                        echo "  - MySQL service is running"
                        echo "  - Username: root"
                        echo "  - Password: 123456"
                        exit 1
                    }
                    
                    # Tạo database nếu chưa có
                    mysql -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS vegana_store;" || {
                        echo "❌ Failed to create database"
                        exit 1
                    }
                    echo "✅ Database vegana_store created/verified"
                    
                    # Import schema nếu có file SQL
                    if [ -f vegana.sql ]; then
                        mysql -u root -p123456 vegana_store < vegana.sql
                        echo "✅ Database schema imported successfully"
                    else
                        echo "⚠️ Warning: vegana.sql not found, skipping import"
                    fi
                    
                    # Kiểm tra kết nối và tables
                    mysql -u root -p123456 -e "USE vegana_store; SHOW TABLES;" || {
                        echo "❌ Failed to verify database"
                        exit 1
                    }
                    echo "✅ MySQL database setup completed"
                '''
            }
        }
        
        stage('Start Application') {
            steps {
                echo 'Starting Spring Boot application...'
                sh '''
                    mvn spring-boot:run &
                    sleep 30
                    curl -f http://localhost:8080 || exit 1
                '''
            }
        }
        
        stage('Run Tests') {
            steps {
                echo 'Running automation tests...'
                sh 'mvn test -DsuiteXmlFile=src/test/resources/testng.xml'
            }
            post {
                always {
                    // Archive test results
                    archiveArtifacts artifacts: 'test-output/reports/*.html', fingerprint: true
                    archiveArtifacts artifacts: 'test-output/screenshots/*.png', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'test-output/logs/*.log', allowEmptyArchive: true
                    
                    // Publish test results
                    publishTestResults testResultsPattern: 'test-output/testng-results.xml'
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                echo 'Generating test reports...'
                script {
                    def reports = sh(
                        script: 'find test-output/reports -name "*.html" -type f',
                        returnStdout: true
                    ).trim()
                    
                    if (reports) {
                        echo "Test reports generated: ${reports}"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up...'
            sh 'pkill -f spring-boot:run || true'
        }
        success {
            echo '✅ Pipeline succeeded!'
            // Có thể thêm email notification ở đây
        }
        failure {
            echo '❌ Pipeline failed!'
            // Có thể thêm email notification ở đây
        }
        unstable {
            echo '⚠️ Pipeline unstable!'
        }
    }
}

