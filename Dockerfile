FROM golang:latest as builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY ./internal ./internal
COPY ./main.go ./main.go
RUN GOOS=linux go build -a -installsuffix cgo -o tws-bot .

FROM debian:bookworm-slim
RUN apt-get update
RUN apt-get install -y libsqlite3-0 ca-certificates
RUN rm -rf /var/lib/apt/lists/*
RUN mkdir /data
COPY --from=builder /app/tws-bot /usr/local/bin/tws-bot

CMD ["tws-bot"]
