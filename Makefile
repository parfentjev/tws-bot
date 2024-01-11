.PHONY: run
.PHONY: image
.PHONY: deploy

run:
	docker-compose --env-file .env up -d --build

image:
	docker buildx build --platform linux/arm64 -t tws-bot:latest --output type=docker,dest=image.tar .

deploy:
	scp image.tar ${TWS_BOT_REMOTE_HOST}:${TWS_BOT_REMOTE_PATH}image.tar && \
	ssh ${TWS_BOT_REMOTE_HOST} "cd ${TWS_BOT_REMOTE_PATH} && \
		docker load < image.tar && \
		docker-compose --env-file rus_err_ee.env up -d"
