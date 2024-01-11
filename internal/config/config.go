package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

var (
	Token    string
	ChatId   string
	Url      string
	Selector string
	RegEx    string
	DbPath   string
)

func init() {
	godotenv.Load()

	Token = getValue("TOKEN")
	ChatId = getValue("CHAT_ID")
	Url = getValue("URL")
	Selector = getValue("SELECTOR")
	RegEx = getValue("REGEX")
	DbPath = getValue("DB_PATH")
}

func getValue(key string) string {
	value, exists := os.LookupEnv(key)

	if !exists {
		log.Panic(key, " must be defined")
	}

	return value
}
