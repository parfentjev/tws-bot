package query

const (
	CreateItemsTable string = "CREATE TABLE IF NOT EXISTS items (id TEXT NOT NULL, CONSTRAINT items_UN UNIQUE (id))"
	SelectCountItems string = "select count(*) from items where id = ?"
	InsertItem       string = "insert into items(id) values(?)"
)
