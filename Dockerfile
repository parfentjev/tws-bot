FROM alpine:latest
WORKDIR /tws-bot

RUN apk --no-cache add openjdk17 maven

COPY pom.xml .
COPY src src
RUN mvn clean compile assembly:single -DskipTests

ARG BASE_URL
ARG LINK_CSS_SELECTOR
ARG CORRECT_URL_PATTERN
ARG POLLING_INTERVAL
ARG DATABASE_PATH
ARG ADMIN_ID
ARG BOT_TOKEN
ARG BOT_USERNAME

ENV BASE_URL=$BASE_URL
ENV LINK_CSS_SELECTOR=$LINK_CSS_SELECTOR
ENV CORRECT_URL_PATTERN=$CORRECT_URL_PATTERN
ENV POLLING_INTERVAL=$POLLING_INTERVAL
ENV DATABASE_PATH=$DATABASE_PATH
ENV ADMIN_ID=$ADMIN_ID
ENV BOT_TOKEN=$BOT_TOKEN
ENV BOT_USERNAME=$BOT_USERNAME

CMD java -DbaseUrl="${BASE_URL}" \
        -DlinkCssSelector="${LINK_CSS_SELECTOR}" \
        -DcorrectUrlPattern="${CORRECT_URL_PATTERN}" \
        -DpollingInterval="${POLLING_INTERVAL}" \
        -DdatabasePath="${DATABASE_PATH}" \
        -DadminId="${ADMIN_ID}" \
        -DbotToken="${BOT_TOKEN}" \
        -DbotUsername="${BOT_USERNAME}" \
        -jar target/tws-bot-1.0.0-SNAPSHOT-jar-with-dependencies.jar
