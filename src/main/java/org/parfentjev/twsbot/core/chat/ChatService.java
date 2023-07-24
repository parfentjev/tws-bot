package org.parfentjev.twsbot.core.chat;

import org.parfentjev.twsbot.core.exceptions.DatabaseHelperException;
import org.parfentjev.twsbot.misc.DbHelper;

import java.util.List;

public class ChatService {
    private final DbHelper<Chat, Long> dbHelper = new DbHelper<>(Chat.class);

    public void add(Long id) throws DatabaseHelperException {
        dbHelper.save(new Chat(id));
    }

    public void delete(Long id) throws DatabaseHelperException {
        dbHelper.delete(new Chat(id));
    }

    public List<Chat> getChats() {
        return dbHelper.getAll();
    }
}
