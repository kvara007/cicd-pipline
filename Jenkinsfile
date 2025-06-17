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
    }
}