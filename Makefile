.PHONY: start stop logs backup

start:
	docker compose up -d

stop:
	docker compose down

logs:
	docker compose logs -f --tail=100

backup:
	docker run --rm -v jenkins-pipeline-library_jenkins_home:/data -v $$(pwd)/backups:/backup alpine \
		tar czf /backup/jenkins-$$(date +%Y%m%d).tar.gz -C /data .
