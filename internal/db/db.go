package db

import (
	"database/sql"
	"fmt"
	"log"

	"codeberg.org/parfentjev/tws-bot/internal/config"
	"codeberg.org/parfentjev/tws-bot/internal/db/query"
	_ "github.com/mattn/go-sqlite3"
)

var Database *sql.DB

func Init() {
	var err error

	Database, err = sql.Open("sqlite3", fmt.Sprintf("file:%vbot.db", config.DbPath))
	if err != nil {
		log.Panic(err)
	}

	Database.SetMaxOpenConns(1)
	Database.SetMaxIdleConns(1)
	createTable()
}

func createTable() {
	_, err := Database.Exec(query.CreateItemsTable)
	if err != nil {
		log.Panic(err)
	}
}

func ItemExists(id string) bool {
	statement, err := Database.Prepare(query.SelectCountItems)
	if err != nil {
		log.Print(err)
		return true
	}

	defer statement.Close()
	var count int
	statement.QueryRow(id).Scan(&count)

	return count > 0
}

func InsertItem(id string) {
	statement, err := Database.Prepare(query.InsertItem)
	if err != nil {
		log.Print(err)
		return
	}

	statement.Exec(id)
	statement.Close()
}
