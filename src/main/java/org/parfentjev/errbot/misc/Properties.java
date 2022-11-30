package org.parfentjev.errbot.misc;

import java.util.ResourceBundle;

public class Properties {
    private static final ResourceBundle BUNDLE;

    static {
        String propertyFile = System.getenv("PROPERTY_FILE");

        if (propertyFile == null || propertyFile.isBlank()) {
            throw new RuntimeException("PROPERTY_FILE must be defined");
        }

        BUNDLE = ResourceBundle.getBundle(propertyFile);
    }

    public static final String BASE_URL = BUNDLE.getString("baseUrl");
    public static final String POST_LINK_CSS_SELECTOR = BUNDLE.getString("postLinkCssSelector");
    public static final String CORRECT_URL_PATTERN = BUNDLE.getString("correctUrlPattern");
    public static final Long CREATOR_ID = Long.parseLong(System.getenv("CREATOR_ID"));
    public static final String BOT_TOKEN = System.getenv("BOT_TOKEN");
    public static final String BOT_USERNAME = System.getenv("BOT_USERNAME");
    public static final Integer POLLING_INTERVAL = Integer.parseInt(BUNDLE.getString("pollingInterval"));
    public static final String DATABASE_PATH = BUNDLE.getString("databasePath");
}
