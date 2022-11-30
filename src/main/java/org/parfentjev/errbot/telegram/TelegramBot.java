package org.parfentjev.errbot.telegram;

import org.parfentjev.errbot.core.EditorialOffice;
import org.parfentjev.errbot.core.Subscription;
import org.parfentjev.errbot.core.chat.ChatService;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.parfentjev.errbot.misc.Utils.isLong;

public class TelegramBot extends AbilityBot {
    private final Long creatorId;
    private final ChatService chatService;

    public TelegramBot(Long creatorId, String username, String token) {
        super(token, username);

        this.creatorId = creatorId;
        this.chatService = new ChatService();

        chatService.getChats().forEach(chat -> EditorialOffice.getInstance().subscribe(new Subscription(silent, chat.getId())));
    }

    @Override
    public long creatorId() {
        return creatorId;
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
        String[] messageText = message.getText().split(" ");

        if (messageMatchesAddDeleteFormat(messageText)) {
            long addChatId = Long.parseLong(messageText[1]);
            boolean added = chatService.add(addChatId);

            if (added) {
                EditorialOffice.getInstance().subscribe(new Subscription(silent, addChatId));
                silent.send("Chat is added.", message.getChatId());
            } else {
                silent.send("Failed to add this chat.", message.getChatId());
            }
        } else {
            silent.send("Format: /add <chatId>", message.getChatId());
        }
    }

    private void deleteChat(MessageContext messageContext) {
        Message message = messageContext.update().getMessage();
        String[] messageText = message.getText().split(" ");

        if (messageMatchesAddDeleteFormat(messageText)) {
            long addChatId = Long.parseLong(messageText[1]);
            boolean deleted = chatService.delete(Long.parseLong(messageText[1]));

            if (deleted) {
                EditorialOffice.getInstance().unsubscribe(addChatId);
                silent.send("Chat is deleted.", message.getChatId());
            } else {
                silent.send("Failed to delete this chat.", message.getChatId());
            }
        } else {
            silent.send("Format: /delete <chatId>", message.getChatId());
        }
    }

    private boolean messageMatchesAddDeleteFormat(String[] messageText) {
        return messageText.length == 2 && isLong(messageText[1]);
    }
}
