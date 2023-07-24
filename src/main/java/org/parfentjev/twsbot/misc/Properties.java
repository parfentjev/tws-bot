package org.parfentjev.twsbot.misc;

import org.parfentjev.twsbot.core.exceptions.PropertiesInitializationException;

import java.util.ResourceBundle;
import java.util.function.Function;

public class Properties {
    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(System.getenv("PROPERTY_FILE") == null ? "bot" : System.getenv("PROPERTY_FILE"));

        Function<String, String> getProperty = propertyName -> {
            String propertyValue = System.getProperty(propertyName, resourceBundle.getString(propertyName));

            if (propertyValue.isBlank()) {
                throw new PropertiesInitializationException("Property '" + propertyName + "' is not set, all properties must be defined.");
            }

            return propertyValue;
        };

        BASE_URL = getProperty.apply("baseUrl");
        LINK_CSS_SELECTOR = getProperty.apply("linkCssSelector");
        CORRECT_URL_PATTERN = getProperty.apply("correctUrlPattern");
        ADMIN_ID = Long.valueOf(getProperty.apply("adminId"));
        BOT_TOKEN = getProperty.apply("botToken");
        BOT_USERNAME = getProperty.apply("botUsername");
        POLLING_INTERVAL = Integer.parseInt(getProperty.apply("pollingInterval"));
        DATABASE_PATH = getProperty.apply("databasePath");
    }

    public static final String BASE_URL;
    public static final String LINK_CSS_SELECTOR;
    public static final String CORRECT_URL_PATTERN;
    public static final Long ADMIN_ID;
    public static final String BOT_TOKEN;
    public static final String BOT_USERNAME;
    public static final Integer POLLING_INTERVAL;
    public static final String DATABASE_PATH;
}
