package org.parfentjev.errbot;

import org.parfentjev.errbot.core.Scraper;
import org.parfentjev.errbot.misc.Properties;
import org.parfentjev.errbot.telegram.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        new Scraper(Properties.BASE_URL, Properties.POLLING_INTERVAL).start();
        new TelegramBotsApi(DefaultBotSession.class).registerBot(new TelegramBot(Properties.CREATOR_ID, Properties.BOT_USERNAME, Properties.BOT_TOKEN));
    }
}