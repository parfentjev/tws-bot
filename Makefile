.PHONY: run
run:
	docker-compose --env-file .env up -d --build

.PHONY: build
build:
	docker buildx build --platform linux/arm64 -t tws-bot:latest . --load
	docker save tws-bot:latest > image.tar
