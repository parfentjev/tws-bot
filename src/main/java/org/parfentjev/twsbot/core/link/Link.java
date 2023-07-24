package org.parfentjev.twsbot.core.link;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.Instant;

@DatabaseTable
public class Link {
    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private Long addedTime;

    private String title;

    private String url;

    public Link() {
        // empty constructor
    }

    public Link(String id, String title, String url) {
        this.title = title;
        this.id = id;
        this.addedTime = Instant.now().getEpochSecond();
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Long addedTime) {
        this.addedTime = addedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
