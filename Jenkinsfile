pipeline {
    agent any

    environment {
        DOCKER_USER = credentials('dockerhub-user')
        DOCKER_PASS = credentials('dockerhub-pass')
        MYSQL_ROOT_PASSWORD = "123456"
        MYSQL_DATABASE = "vegana_store"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Start MySQL') {
            steps {
                bat """
                echo Removing old MySQL...
                docker rm -f mysql-vegana || exit 0

                echo Starting MySQL container...
                docker run -d --name mysql-vegana ^
                    -e MYSQL_ROOT_PASSWORD=123456 ^
                    -e MYSQL_DATABASE=vegana_store ^
                    -p 3306:3306 ^
                    mysql:8.0

                echo Waiting for MySQL...

                FOR /L %%i IN (1,1,40) DO (
                    docker exec mysql-vegana mysqladmin ping -uroot -p123456 --silent && goto :mysql_ok
                    powershell -Command "Start-Sleep -Seconds 2"
                )

                :mysql_ok
                echo MySQL READY!
                """
            }
        }

        stage('Build Spring Boot') {
            steps {
                bat """
                mvn -DskipTests clean package
                """
            }
        }

        stage('Start Backend') {
            steps {
                bat """
                echo Starting Spring Boot...
                taskkill /F /IM java.exe || echo No running Spring Boot

                start /B mvn spring-boot:run > app.log 2>&1

                echo Waiting for backend...
                powershell -Command "Start-Sleep -Seconds 40"

                powershell -Command "try { Invoke-WebRequest http://localhost:9090 -UseBasicParsing } catch { exit 1 }"
                """
            }
        }

        stage('Automation Tests') {
            steps {
                bat """
                mkdir test-output
                mkdir test-output\\reports
                mkdir test-output\\screenshots
                mkdir test-output\\logs

                mvn test -DsuiteXmlFile=src/test/resources/testng.xml
                """
            }
            post {
                always {
                    archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'app.log', allowEmptyArchive: true
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                bat """
                echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin

                docker build -t %DOCKER_USER%/vegana-shop:latest .

                docker push %DOCKER_USER%/vegana-shop:latest
                """
            }
        }

        stage('Deploy') {
            steps {
                bat """
                docker rm -f vegana || exit 0

                docker run -d --name vegana -p 9090:9090 %DOCKER_USER%/vegana-shop:latest
                """
            }
        }
    }

    post {
        always {
            bat """
            taskkill /F /IM java.exe || echo No Java to kill
            docker rm -f mysql-vegana || exit 0
            """
        }
    }
}
