# Telegram Web Scraper Bot

The Telegram Web Scraper Bot is a simple application designed to retrieve links from a specified webpage and post them to a designated Telegram chat.

Here's the [Java version](https://github.com/parfentjev/tws-bot/tree/java-1.0.0-SNAPSHOT) of the same bot.

## Configuration

The bot's configuration is managed through an `.env` file located in the root directory. The file should contain the following key-value pairs:

```yaml
TOKEN="token from @BotFather"
CHAT_ID="recipient's chat id"
URL="target webpage URL"
SELECTOR="CSS selector to find links"
REGEX="regex to extract an item id"
DB_PATH="directory where bot.db should be stored"
```

For example, to scrape [rus.err.ee](https://rus.err.ee/), your `.env` file might look like this:

```yaml
TOKEN="token from @BotFather"
CHAT_ID="recipient's chat id"
URL="https://rus.err.ee/"
SELECTOR=".left-block .article > article h2 a"
REGEX="(?:https:)?//rus\\.err\\.ee/(\\d+)/.+"
DB_PATH="/home/username/Projects/tws-bot/"
```

**Important Notes:**

- `DB_PATH="/data/"` is the expected directory path for the Docker image.
- The regular expression (`REGEX`) is utilized to validate and extract the item ID from a URL. It **must** contain a single capturing group, which the bot uses to determine the link's validity.

## Running the Bot

To run the bot locally, create an `.env` file with your configuration and execute the following command:

```shell
go run .
```

Alternatively, to run it in a Docker container:

```shell
docker-compose --env-file .env up -d --build
```

Or using:

```shell
make run
```
