package org.devops

class Utils implements Serializable {
    def steps

    Utils(steps) {
        this.steps = steps
    }

    String sanitizeBranch(String branch) {
        return branch?.replaceAll(/[^a-zA-Z0-9._-]/, '-')?.toLowerCase() ?: 'unknown'
    }

    String imageTag(String prefix = 'build') {
        def ts = new Date().format('yyyyMMdd-HHmmss')
        return "${prefix}-${ts}-${steps.env.BUILD_NUMBER}"
    }

    boolean isMainBranch() {
        def branch = steps.env.BRANCH_NAME ?: steps.env.GIT_BRANCH ?: ''
        return branch == 'main' || branch == 'master' || branch.endsWith('/main')
    }

    void archiveArtifacts(String pattern = '**/target/*.jar') {
        steps.archiveArtifacts artifacts: pattern, allowEmptyArchive: true
    }
}
