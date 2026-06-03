#!/usr/bin/env groovy

def call(Map config = [:]) {
    def testCommand = config.command ?: 'make test'
    def junitPattern = config.junitPattern ?: '**/target/surefire-reports/*.xml,**/test-results.xml'
    def coverage = config.coverage ?: false

    stage('Run Tests') {
        sh testCommand
        if (coverage) {
            publishHTML(target: [
                reportName: 'Coverage Report',
                reportDir: config.coverageDir ?: 'htmlcov',
                reportFiles: 'index.html',
                keepAll: true,
                allowMissing: true
            ])
        }
        junit allowEmptyResults: true, testResults: junitPattern
    }
}
