#!/usr/bin/env groovy

def call(Map config = [:]) {
    def imageName = config.imageName ?: "${env.JOB_NAME}:${env.BUILD_NUMBER}"
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'
    def registry = config.registry ?: ''
    def push = config.push != false

    stage('Build Docker Image') {
        script {
            def tag = registry ? "${registry}/${imageName}" : imageName
            sh """
                docker build -f ${dockerfile} -t ${tag} ${context}
            """
            if (push && registry) {
                withCredentials([usernamePassword(
                    credentialsId: config.credentialsId ?: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                        docker push ${tag}
                    """
                }
            }
            env.DOCKER_IMAGE = tag
            echo "Built image: ${tag}"
        }
    }
}
