package main

import (
	"fmt"
	"log"
	"sync"
	"time"

	"codeberg.org/parfentjev/tws-bot/internal/config"
	"codeberg.org/parfentjev/tws-bot/internal/db"
	"codeberg.org/parfentjev/tws-bot/pkg/extractor"
)

func main() {
	var (
		waitGroup   sync.WaitGroup
		dataChannel chan extractor.Item = make(chan extractor.Item)
	)

	waitGroup.Add(1)
	go func() {
		defer waitGroup.Done()
		fetchItems(dataChannel)
	}()

	waitGroup.Add(1)
	go func() {
		defer waitGroup.Done()
		postItems(dataChannel)
	}()

	log.Println("successfully initialized")
	waitGroup.Wait()
}

func fetchItems(dataChannel chan extractor.Item) {
	db := db.New()

	for {
		items, err := extractor.GetItems(config.Url, config.Selector, config.RegEx)
		if err != nil {
			log.Println(err)
		}

		for _, item := range items {
			if !db.ItemExists(item.Id) {
				db.InsertItem(item.Id)
				dataChannel <- item
			}
		}

		time.Sleep(60 * time.Second)
	}
}

func postItems(dataChannel chan extractor.Item) {
	for item := range dataChannel {
		fmt.Println("posting to telegram", item)
	}
}
