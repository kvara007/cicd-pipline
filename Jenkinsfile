pipeline {
    // agent any is equivalent to node 'master'
    agent any
    tools {nodejs "node"}
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "chmod +x ./scripts/build.sh"
                sh "./scripts/build.sh"
            }
        }
        stage("test") {
            // runs test.sh >> test.sh runs /src App.test.js
            steps {
                sh "chmod +x ./scripts/test.sh"
                sh "./scripts/test.sh"
            }
        }
        stage("Build Docker Image") {
            steps {
                script {
                sh "docker build -t cicd-app:${env.BRANCH_NAME} ."
                }
            }
        }
    }
}