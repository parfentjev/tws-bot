# ERR Telegram Bot

This is a Telegram bot that takes news from ERR websites and posts them to a specified Telegram chat. ERR Telegram Bot is a third-party tool that is not related to Estonian Public Broadcasting (Eesti Rahvusringhääling).

## Build

To produce a jar with dependencies `target/errbot-{version}-jar-with-dependencies.jar`, use the command below:

```shell
mvn clean compile assembly:single
```

## Run

Non-sensitive settings are located in `src/main/resources/*.properties` files. But other data should be passed using environmental values:

* BOT_TOKEN - token of your Telegram bot from `@BotFather`;
* BOT_USERNAME - username of your Telegram bot, e.g. `myerr_bot`;
* CREATOR_ID - id of your Telegram account, you can find it by messaging `@RawDataBot`;
* PROPERTY_FILE - name of a property file you'd like to use, e.g. `rus-err`.

Here's an example of a script:

```shell
#!/bin/bash
cd /path/to/err-telegram-bot/
BOT_TOKEN=<token> BOT_USERNAME=<username> CREATOR_ID=<id> PROPERTY_FILE=<property> java -jar errbot-{version}-jar-with-dependencies.jar
```

That can be coupled with systemd:

```
[Unit]
Description=Run Telegram bot

[Service]
Type=simple
RemainAfterExit=yes
ExecStart=/path/to/err-telegram-bot/run.sh
TimeoutStartSec=0

[Install]
WantedBy=default.target
```

## Usage

The bot knows 2 commands:

* /add <chatId> - to add a chat-recipient of new articles;
* /delete <chatId> - to delete a chat.

To find the chat id, you can forward a message from the chat to `@RawDataBot`.

## Potential issues

Since this is a third-party tool, any changes on the ERR side can and will break the bot with no prior announcement. Theoretically, a simple update of `postLinkCssSelector` value in a site property file should fix issues caused by changes in the HTML/CSS structure.