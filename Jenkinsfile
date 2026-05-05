pipeline {
    agent any

    environment {
        ACR_NAME = 'smartloanacr'
        ACR_LOGIN_SERVER = 'smartloanacr.azurecr.io'
        IMAGE_NAME = 'smartloan-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Atharva031/smartloan.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build & Push to ACR') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'acr-credentials',
                    usernameVariable: 'ACR_USER',
                    passwordVariable: 'ACR_PASS'
                )]) {
                    sh '''
                        echo "$ACR_PASS" | docker login $ACR_LOGIN_SERVER -u $ACR_USER --password-stdin
                        docker build -t $ACR_LOGIN_SERVER/$IMAGE_NAME:$IMAGE_TAG .
                        docker build -t $ACR_LOGIN_SERVER/$IMAGE_NAME:latest .
                        docker push $ACR_LOGIN_SERVER/$IMAGE_NAME:$IMAGE_TAG
                        docker push $ACR_LOGIN_SERVER/$IMAGE_NAME:latest
                        docker logout $ACR_LOGIN_SERVER
                    '''
                }
            }
        }

        stage('Docker Deploy Locally') {
            steps {
                sh 'docker compose down'
                sh 'docker compose up --build -d'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! Image pushed to ACR.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
