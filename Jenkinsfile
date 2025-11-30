pipeline {
    agent any

    tools {
        jdk "jdk17"
        maven "maven"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build App') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Start App') {
            steps {
                bat '''
                    echo Starting Spring Boot...
                    start "vegana-app" /B mvn spring-boot:run > app.log 2>&1

                    echo Waiting for app on 9090...

                    for /l %%i in (1,1,40) do (
                        curl -s http://localhost:9090 >nul
                        if !errorlevel! == 0 (
                            echo App is ready!
                            goto ready
                        )
                        echo Waiting (%%i/40)...
                        ping -n 2 127.0.0.1 >nul
                    )

                    :ready
                '''
            }
        }

        stage('Run UI Tests') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {
        always {
            echo "Stopping Spring Boot..."

            bat '''
                for /f "tokens=2" %%p in ('tasklist /v ^| findstr /i "vegana-app"') do taskkill /F /PID %%p
            '''

            echo "Done."
        }
    }
}
