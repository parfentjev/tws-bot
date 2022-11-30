package org.parfentjev.errbot.core.article;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.Instant;

@DatabaseTable
public class Article {
    @DatabaseField(id = true)
    private Long id;

    @DatabaseField
    private Long addedTime;

    private String title;

    private String url;

    public Article() {
        // empty constructor
    }

    public Article(Long id, String title, String url) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
