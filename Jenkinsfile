@Library('devops-pipeline-library') _

pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = 'docker.io/your-org'
        K8S_NAMESPACE = 'staging'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Test') {
            steps {
                runTests(command: 'pip install -r requirements.txt && pytest -v || true')
            }
        }

        stage('Build') {
            steps {
                buildDocker(
                    imageName: "sample-app:${env.BUILD_NUMBER}",
                    registry: env.DOCKER_REGISTRY,
                    push: false
                )
            }
        }

        stage('Deploy') {
            when { branch 'main' }
            steps {
                deployToK8s(
                    namespace: env.K8S_NAMESPACE,
                    manifest: 'k8s/'
                )
            }
        }
    }

    post {
        always {
            sh 'bash resources/scripts/cleanup.sh || true'
        }
        success { sendSlackNotification(status: 'SUCCESS') }
        failure { sendSlackNotification(status: 'FAILURE') }
    }
}
