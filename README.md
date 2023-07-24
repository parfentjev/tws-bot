# Telegram Web Scraper Bot

The Telegram Web Scraper Bot is a straightforward application designed to fetch links from a specified website and post them to a chosen Telegram chat.

## Build and Run

To use this bot, follow these steps:

1. Clone this repository.
2. Create an environment file (e.g., `myenv.env`), and set your required properties. You can use the provided `.env` file as a reference.

The environment file should include these properties:

```properties
# URL of the webpage from which to extract links
BASE_URL="https://rus.err.ee"
# CSS selector that identifies the <a> tags to collect
LINK_CSS_SELECTOR=".left-block .article > article h2 a"
# Regex pattern used to validate links, as some might lead to other websites, etc.
CORRECT_URL_PATTERN="(?:https:)?\/\/rus\.err\.ee\/(\\d+)\/.+"
# Frequency (in milliseconds) to check the BASE_URL
POLLING_INTERVAL="60000"
# (For internal use) path to the database
DATABASE_PATH="volumes/"
# Your Telegram user ID, needed to access chat commands
ADMIN_ID="YOUR_TELEGRAM_USER_ID"
# Token for your Telegram bot
BOT_TOKEN="YOUR_BOTS_TOKEN"
# Username of your Telegram bot
BOT_USERNAME="YOUR_BOTS_USERNAME"
```

After setting up the environment file, run the following command:

```shell
docker-compose --env-file myenv.env up --build -d
```

## Usage

The bot understands two commands:

* `/add <chatId>` - Adds a chat recipient for new articles.
* `/delete <chatId>` - Removes a chat.

To find the chat ID, you can forward a message from the chat to `@RawDataBot`.

## Update

To update the container after a `git pull` or a modification of properties, execute the following commands:

```shell
docker-compose down
docker-compose --env-file myenv.env up --build -d
```

## Known Limitations

Currently, the bot does not support relative links. If a link does not contain a domain (`href="/relative/path/index.html"`), the bot will not append the `BASE_URL`.
