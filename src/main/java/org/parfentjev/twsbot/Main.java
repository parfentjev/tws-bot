package org.parfentjev.twsbot;

import org.parfentjev.twsbot.core.Scraper;
import org.parfentjev.twsbot.misc.Properties;
import org.parfentjev.twsbot.telegram.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        new Scraper(Properties.BASE_URL, Properties.POLLING_INTERVAL).start();
        new TelegramBotsApi(DefaultBotSession.class).registerBot(new TelegramBot(Properties.ADMIN_ID, Properties.BOT_TOKEN, Properties.BOT_USERNAME));
    }
}