package db

import (
	"database/sql"
	"fmt"
	"log"

	"codeberg.org/parfentjev/tws-bot/internal/config"
	"codeberg.org/parfentjev/tws-bot/internal/db/query"
	_ "github.com/mattn/go-sqlite3"
)

type Database struct {
	conn *sql.DB
}

func New() *Database {
	conn, err := sql.Open("sqlite3", fmt.Sprintf("file:%vbot.db", config.DbPath))
	if err != nil {
		log.Panic(err)
	}

	createTable(conn)

	return &Database{conn}
}

func createTable(conn *sql.DB) {
	_, err := conn.Exec(query.CreateItemsTable)
	if err != nil {
		log.Panic(err)
	}
}

func (db *Database) ItemExists(id string) bool {
	statement, err := db.conn.Prepare(query.SelectCountItems)
	if err != nil {
		log.Print(err)
		return true
	}

	var count int
	statement.QueryRow(id).Scan(&count)

	return count > 0
}

func (db *Database) InsertItem(id string) {
	statement, err := db.conn.Prepare(query.InsertItem)
	if err != nil {
		log.Print(err)
		return
	}

	statement.Exec(id)
}
