package telegram

import (
	"fmt"
	"log"
	"net/http"
	"net/url"
	"strings"
)

type Bot struct {
	baseUrl string
}

func New(token string) *Bot {
	return &Bot{baseUrl: fmt.Sprintf("https://api.telegram.org/bot%v", token)}
}

func (b *Bot) SendMessage(chatId string, text string) {
	params := url.Values{}
	params.Add("chat_id", chatId)
	params.Add("text", text)

	req, err := http.NewRequest("POST", fmt.Sprintf("%v/sendMessage?%v", b.baseUrl, params.Encode()), strings.NewReader(""))
	if err != nil {
		log.Println(err)
	}

	client := &http.Client{}
	_, err = client.Do(req)
	if err != nil {
		log.Println(err)
	}
}
