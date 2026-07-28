# Jenkins Pipeline Shared Library

Reusable Jenkins shared library for Docker builds, Kubernetes deployments, tests, and Slack notifications.

## Shared steps

| Step | File | Purpose |
|------|------|---------|
| `buildDocker` | `vars/buildDocker.groovy` | Build and optionally push images |
| `deployToK8s` | `vars/deployToK8s.groovy` | Apply manifests and rollout |
| `runTests` | `vars/runTests.groovy` | Run tests and publish JUnit |
| `sendSlackNotification` | `vars/sendSlackNotification.groovy` | Post build status to Slack |

## Usage

```groovy
@Library('devops-pipeline-library') _
buildDocker imageName: 'myapp:1.0', registry: 'docker.io/myorg'
deployToK8s namespace: 'production', manifest: 'k8s/'
```

## Local Jenkins

```bash
make start
open http://localhost:8080
```

Configure the shared library in **Manage Jenkins → Global Pipeline Libraries**.

## License

MIT
