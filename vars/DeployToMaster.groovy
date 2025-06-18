def call(Map config) {
    script {
        def imageName = config.environment == "main" ? "kvara007/nodemain:${config.imageTag}" : "kvara007/nodedev:${config.imageTag}"
        def containerName = "node-${config.environment}"
        def port = config.environment == "main" ? "3000" : "3001"
        
        sh "docker pull ${imageName}"
        sh "docker stop ${containerName} || true"
        sh "docker rm ${containerName} || true"
        sh "docker run -d --name ${containerName} -p ${port}:3000 ${imageName}"
    }
}