pipeline {
    agent {
       docker {
           image 'node:16'
           // Mounts Docker socket, the container can runn Docker commands (Docker-in-Docker)
           args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
       }
   }
    stages {
        stage('Install Docker') {
           steps {
               sh 'apt-get update && apt-get install -y docker.io'
           }
       }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Dockerfile Lint') {
            steps {
                // Checks Docker fille syntax and security issues.    is not it ending?!!!!!!
                sh "docker run --rm -i hadolint/hadolint < Dockerfile"
        }
    }
        stage('Build') {
            steps {
                sh "chmod +x ./scripts/build.sh"
                sh "./scripts/build.sh"
            }
        }
        stage("test") {
            // runs test.sh >> test.sh runsSSSS /src App.test.js
            steps {
                sh "chmod +x ./scripts/test.sh"
                sh "./scripts/test.sh"
            }
        }
        stage("Build Docker Image") {
            steps {
                script {
                def imageName = env.BRANCH_NAME == "main" ? "kvara007/nodemain:v1.0" : "kvara007/nodedev:v1.0"
                sh "docker build -t ${imageName} ."
                }
            }
        }
        stage("Push Image") {
            steps {
                script {
                    def imageName = env.BRANCH_NAME == "main" ? "kvara007/nodemain:v1.0" : "kvara007/nodedev:v1.0"
                    docker.withRegistry('', 'dockerhub_creds') {
                      sh "docker push ${imageName}"
                    }
                }
            }
        }    
        stage('Scan Docker Image for Vulnerabilities') {
            steps {
                script {
                    def imageName = env.BRANCH_NAME == "main" ? "kvara007/nodemain:v1.0" : "kvara007/nodedev:v1.0"
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image --exit-code 0 --severity HIGH,MEDIUM,LOW --no-progress ${imageName}"
                }
            }
        }

        stage('Trigger Deploy') {
            steps { 
                echo "Triggering deployments based on branch..."
                script {
                    if (env.BRANCH_NAME == 'main') {
                        build job: 'Deploy_to_main', wait: false
                    } else if (env.BRANCH_NAME == 'dev') {
                        build job: 'Deploy_to_dev', wait: false
                    }
                }
            }
        }
    }
}