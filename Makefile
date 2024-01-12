-include deployment/Makefile

.PHONY: run

run:
	docker-compose --env-file .env up -d --build
