pipeline {
    agent any
    environment {
        COMMIT_HASH="${sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
        DB_URL="$DB_URL"
        DB_USERNAME="$DB_USERNAME"
        DB_PASSWORD="$DB_PASSWORD"
    }

    stages {
        stage('Package') {
            steps {
                echo 'Packaging...'
                script {
                    sh "env"
                    sh "mvn clean package"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Build...'            
                sh "docker build --tag utopiaairportms:$COMMIT_HASH --build-arg DBUrl=$DB_URL --build-arg DBUsername=$DB_USERNAME --build-arg DBPassword=$DB_PASSWORD ."
                sh "docker tag utopiaairportms:$COMMIT_HASH 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/airportms:$COMMIT_HASH"
            }
        }
        stage('Deploy...') {
           steps {
               sh "docker run utopiaairportms:$COMMIT_HASH"
           }
        }
    }
}