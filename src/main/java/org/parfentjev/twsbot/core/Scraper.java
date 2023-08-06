package org.parfentjev.twsbot.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.parfentjev.twsbot.core.link.Link;
import org.parfentjev.twsbot.core.link.LinkService;
import org.parfentjev.twsbot.core.exceptions.DatabaseHelperException;
import org.parfentjev.twsbot.misc.Properties;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;
import static org.parfentjev.twsbot.misc.Utils.await;

public class Scraper extends Thread {
    private static final Logger logger = LogManager.getLogger("Scraper");
    private static final Pattern pattern = Pattern.compile(Properties.CORRECT_URL_PATTERN);

    private final String baseUrl;
    private final Integer pollingInterval;
    private final LinkService linkService;

    public Scraper(String baseUrl, Integer pollingInterval) {
        this.baseUrl = baseUrl;
        this.pollingInterval = pollingInterval;
        this.linkService = new LinkService();
    }

    @Override
    public void run() {
        while (true) {
            getArticles()
                    .stream()
                    .filter(link -> linkService.getLinkByUrl(link.getId()).isEmpty())
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(this::postArticle);

            await(pollingInterval);
        }
    }

    private List<Link> getArticles() {
        Document document = getDocument();

        if (document == null) {
            return Collections.emptyList();
        }

        return document
                .select(Properties.LINK_CSS_SELECTOR)
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

    private Link mapElementToArticle(Element element) {
        var href = element.attr("href");
        var matcher = pattern.matcher(href);

        if (!matcher.matches()) {
            return null;
        }

        return new Link(matcher.group(1), element.text(), href.startsWith("//") ? "https:" + href : href);
    }

    private void postArticle(Link link) {
        try {
            linkService.saveLink(link);
            EditorialOffice.getInstance().post(link);
        } catch (DatabaseHelperException | IllegalArgumentException e) {
            logger.error(format("Failed to save: {} [{}}]", link.getUrl(), link.getId()));
        }
    }
}
