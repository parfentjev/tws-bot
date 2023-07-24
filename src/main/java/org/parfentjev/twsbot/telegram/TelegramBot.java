package org.parfentjev.twsbot.telegram;

import org.parfentjev.twsbot.core.EditorialOffice;
import org.parfentjev.twsbot.core.Subscription;
import org.parfentjev.twsbot.core.chat.ChatService;
import org.parfentjev.twsbot.core.exceptions.DatabaseHelperException;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static org.parfentjev.twsbot.misc.Utils.isLong;

public class TelegramBot extends AbilityBot {
    private final Long adminId;
    private final ChatService chatService;

    public TelegramBot(Long adminId, String botToken, String botUsername) {
        super(botToken, botUsername);

        this.adminId = adminId;
        this.chatService = new ChatService();

        chatService.getChats().forEach(chat -> EditorialOffice.getInstance().subscribe(new Subscription(silent, chat.getId())));
    }

    @Override
    public long creatorId() {
        return adminId;
    }

    public Ability addCommand() {
        return Ability.builder()
                .name("add")
                .info("Send messages to this chat id")
                .locality(Locality.ALL)
                .privacy(Privacy.CREATOR)
                .action(this::addChat)
                .build();
    }

    public Ability deleteCommand() {
        return Ability.builder()
                .name("delete")
                .info("Don't send messages to this chat id anymore")
                .locality(Locality.ALL)
                .privacy(Privacy.CREATOR)
                .action(this::deleteChat)
                .build();
    }

    private void addChat(MessageContext messageContext) {
        Message message = messageContext.update().getMessage();
        Optional<Long> chatId = extractChatId(message);

        if (chatId.isEmpty()) {
            silent.send("Format: /add <chatId>", message.getChatId());

            return;
        }

        try {
            chatService.add(chatId.get());
            EditorialOffice.getInstance().subscribe(new Subscription(silent, chatId.get()));
            silent.send("Chat is added.", message.getChatId());
        } catch (DatabaseHelperException e) {
            silent.send("Failed to add this chat.", message.getChatId());
        }
    }

    private void deleteChat(MessageContext messageContext) {
        Message message = messageContext.update().getMessage();
        Optional<Long> chatId = extractChatId(message);

        if (chatId.isEmpty()) {
            silent.send("Format: /delete <chatId>", message.getChatId());

            return;
        }

        try {
            chatService.delete(chatId.get());
            EditorialOffice.getInstance().unsubscribe(chatId.get());
            silent.send("Chat is deleted.", message.getChatId());
        } catch (DatabaseHelperException e) {
            silent.send("Failed to delete this chat.", message.getChatId());
        }
    }

    private Optional<Long> extractChatId(Message message) {
        String[] messageText = message.getText().split(" ");

        if (messageText.length == 2 && isLong(messageText[1])) {
            return Optional.of(Long.parseLong(messageText[1]));
        }

        return Optional.empty();
    }
}
