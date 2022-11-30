package org.parfentjev.errbot.core.article;

import org.parfentjev.errbot.misc.DbHelper;

public class ArticleService {
    private final DbHelper<Article, Long> dbHelper = new DbHelper<>(Article.class);

    public Article getArticleByUrl(Long id) {
        return dbHelper.queryById(id);
    }

    public boolean saveArticle(Article article) {
        return dbHelper.save(article);
    }
}
