pipeline {
    agent any
    environment {
        COMMIT_HASH="${sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
    }

    stages {
        stage('Package') {
            steps {
                echo 'Packaging...'
                script {
                    sh "mvn clean package"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Build...'            
                sh "docker build --tag utopiaairportms:$COMMIT_HASH ."
                sh "docker tag utopiaairportms:$COMMIT_HASH 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/airportms:$COMMIT_HASH"
            }
        }
        stage('Deploy...') {
           steps {
               sh "docker run utopiaairportms:$COMMIT_HASH"
           }
        }
        stage('Cleanup...') {
            steps {
                sh "docker system prune -f"
            }
        }
    }
}
