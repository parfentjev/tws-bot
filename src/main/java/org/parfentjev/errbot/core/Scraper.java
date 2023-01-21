package org.parfentjev.errbot.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.parfentjev.errbot.core.article.Article;
import org.parfentjev.errbot.core.article.ArticleService;
import org.parfentjev.errbot.misc.Properties;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.parfentjev.errbot.misc.Utils.await;

public class Scraper extends Thread {
    private static final Logger logger = LogManager.getLogger("Scraper");
    private static final Pattern pattern = Pattern.compile(Properties.CORRECT_URL_PATTERN);

    private final String baseUrl;
    private final Integer pollingInterval;
    private final ArticleService articleService;

    public Scraper(String baseUrl, Integer pollingInterval) {
        this.baseUrl = baseUrl;
        this.pollingInterval = pollingInterval;
        this.articleService = new ArticleService();
    }

    @Override
    public void run() {
        while (true) {
            getArticles()
                    .stream()
                    .filter(this::newArticle)
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(this::postArticle);

            await(pollingInterval);
        }
    }

    private List<Article> getArticles() {
        Document document = getDocument();

        if (document == null) {
            return Collections.emptyList();
        }

        return document
                .select(Properties.POST_LINK_CSS_SELECTOR)
                .stream()
                .filter(this::elementTextOrLinkNotEmpty)
                .map(this::mapElementToArticle)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Document getDocument() {
        try {
            return Jsoup.connect(baseUrl).get();
        } catch (IOException e) {
            logger.error(e);

            return null;
        }
    }

    private boolean elementTextOrLinkNotEmpty(Element element) {
        return !(element.text().isBlank() || element.attr("href").isBlank());
    }

    private Article mapElementToArticle(Element element) {
        var href = element.attr("href");
        var matcher = pattern.matcher(href);

        if (!matcher.matches()) {
            return null;
        }

        return new Article(Long.valueOf(matcher.group(1)), element.text(), href.startsWith("//") ? "https:" + href : href);
    }

    private boolean newArticle(Article article) {
        return articleService.getArticleByUrl(article.getId()) == null;
    }

    private void postArticle(Article article) {
        if (!articleService.saveArticle(article)) {
            return;
        }

        EditorialOffice.getInstance().post(article);
    }
}
