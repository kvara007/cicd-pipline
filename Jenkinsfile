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
                def imageName = env.BRANCH_NAME == "main" ? "nodemain:v1.0" : "nodedev:v1.0"
                sh "docker build -t ${imageName} ."
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    // The container from previous deployment is running, so we need to stop and remove it, before starting new one
                    def containerName = "node-${env.BRANCH_NAME}"
                    sh "docker stop ${containerName} || true"   // If command fails run true, always succeeeds.
                    sh "docker rm ${containerName} || true"

                    def port = env.BRANCH_NAME == "main" ? "3000" : "3001"
                    def imageName = env.BRANCH_NAME == "main" ? "nodemain:v1.0" : "nodedev:v1.0"
                    sh "docker run -d --name ${containerName} -p ${port}:3000 ${imageName}"

                    }
                }
            }
        }
    }