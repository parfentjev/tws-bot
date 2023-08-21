package org.parfentjev.twsbot.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.parfentjev.twsbot.core.link.Link;

import java.util.ArrayList;
import java.util.List;

public class EditorialOffice {
    private static EditorialOffice instance;

    private static final List<Subscription> subscriptions = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger("EditorialOffice");

    private EditorialOffice() {
        // empty constructor
    }

    public static EditorialOffice getInstance() {
        if (instance == null) {
            instance = new EditorialOffice();
        }

        return instance;
    }

    public void post(Link link) {
        String postText = String.format("%s%n%n%s", link.getTitle(), link.getUrl());

        subscriptions.forEach(subscription -> {
            logger.info("Sending link: " + link.getUrl());
            subscription.sender().send(postText, subscription.chatId());
        });
    }

    public void subscribe(Subscription subscription) {
        subscriptions.add(subscription);
    }

    public void unsubscribe(Long chatId) {
        subscriptions.removeIf(subscription -> subscription.chatId().equals(chatId));
    }
}
