package org.parfentjev.errbot.core.chat;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Chat {
    @DatabaseField(id = true)
    private Long id;

    public Chat() {
        // empty constructor
    }

    public Chat(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
