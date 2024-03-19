package extractor

import (
	"fmt"
	"log"
	"net/http"
	"regexp"
	"strings"

	"github.com/PuerkitoBio/goquery"
)

type Item struct {
	Id  string
	Url string
}

func GetItems(url, selector string, regex string) ([]Item, error) {
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}

	defer resp.Body.Close()
	doc, err := goquery.NewDocumentFromReader(resp.Body)
	if err != nil {
		return nil, err
	}

	return extractItems(doc, selector, regex), nil
}

func extractItems(doc *goquery.Document, selector string, regex string) []Item {
	var items []Item

	doc.Find(selector).Each(func(i int, s *goquery.Selection) {
		url, urlExists := s.Attr("href")
		id := extractId(url, regex)

		if urlExists && len(id) > 0 {
			if strings.HasPrefix(url, "//") {
				url = fmt.Sprint("https:", url)
			}

			items = append(items, Item{id, url})
		}
	})

	return items
}

func extractId(url, regex string) string {
	re, err := regexp.Compile(regex)
	if err != nil {
		log.Print(err)
		return ""
	}

	match := re.FindStringSubmatch(url)
	if len(match) == 2 && len(match[1]) > 0 {
		return match[1]
	} else {
		return ""
	}
}
