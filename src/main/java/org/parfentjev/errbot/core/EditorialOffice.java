package org.parfentjev.errbot.core;

import org.parfentjev.errbot.core.article.Article;

import java.util.ArrayList;
import java.util.List;

import static org.parfentjev.errbot.misc.Utils.await;

public class EditorialOffice {
    private static EditorialOffice instance;

    private static final List<Subscription> subscriptions = new ArrayList<>();

    private EditorialOffice() {
        // empty constructor
    }

    public static EditorialOffice getInstance() {
        if (instance == null) {
            instance = new EditorialOffice();
        }

        return instance;
    }

    public void post(Article article) {
        String postText = String.format("%s%n%n%s", article.getTitle(), article.getUrl());

        subscriptions.forEach(subscription -> {
            subscription.sender().send(postText, subscription.chatId());

            await(10000);
        });
    }

    public void subscribe(Subscription subscription) {
        subscriptions.add(subscription);
    }

    public void unsubscribe(Long chatId) {
        subscriptions.removeIf(subscription -> subscription.chatId().equals(chatId));
    }
}
