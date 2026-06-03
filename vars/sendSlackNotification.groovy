#!/usr/bin/env groovy

def call(Map config = [:]) {
    def channel = config.channel ?: '#ci-cd'
    def webhookId = config.credentialsId ?: 'slack-webhook'
    def status = config.status ?: currentBuild.currentResult ?: 'UNKNOWN'
    def color = status == 'SUCCESS' ? 'good' : (status == 'UNSTABLE' ? 'warning' : 'danger')

    def payload = [
        channel: channel,
        attachments: [[
            color: color,
            title: "${env.JOB_NAME} #${env.BUILD_NUMBER}",
            title_link: env.BUILD_URL,
            fields: [
                [title: 'Status', value: status, short: true],
                [title: 'Branch', value: env.BRANCH_NAME ?: 'N/A', short: true],
                [title: 'Duration', value: currentBuild.durationString, short: true]
            ],
            footer: 'Jenkins Pipeline Library'
        ]]
    ]

    withCredentials([string(credentialsId: webhookId, variable: 'SLACK_WEBHOOK')]) {
        sh """
            curl -fsS -X POST -H 'Content-type: application/json' \\
              --data '${groovy.json.JsonOutput.toJson(payload)}' \\
              "\$SLACK_WEBHOOK"
        """
    }
}
