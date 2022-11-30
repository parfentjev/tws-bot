package org.parfentjev.errbot.core.chat;

import org.parfentjev.errbot.misc.DbHelper;

import java.util.List;

public class ChatService {
    private final DbHelper<Chat, Long> dbHelper = new DbHelper<>(Chat.class);

    public boolean add(Long id) {
        return dbHelper.save(new Chat(id));
    }

    public boolean delete(Long id) {
        return dbHelper.delete(new Chat(id));
    }

    public List<Chat> getChats() {
        return dbHelper.getAll();
    }
}
