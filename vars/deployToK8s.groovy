#!/usr/bin/env groovy

def call(Map config = [:]) {
    def namespace = config.namespace ?: 'default'
    def manifest = config.manifest ?: 'manifests/'
    def image = config.image ?: env.DOCKER_IMAGE
    def kubeconfig = config.kubeconfig ?: 'kubeconfig'
    def rolloutTimeout = config.timeout ?: '300s'

    stage('Deploy to Kubernetes') {
        withCredentials([file(credentialsId: config.credentialsId ?: 'kubeconfig', variable: 'KUBECONFIG_FILE')]) {
            sh """
                export KUBECONFIG=\$KUBECONFIG_FILE
                kubectl config current-context
                if [ -n "${image ?: ''}" ]; then
                  kubectl -n ${namespace} set image deployment/\${DEPLOYMENT_NAME:-app} app=${image} --record || true
                fi
                kubectl apply -f ${manifest} -n ${namespace}
                kubectl -n ${namespace} rollout status deployment --timeout=${rolloutTimeout}
            """
        }
    }
}
