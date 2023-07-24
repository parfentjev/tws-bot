package org.parfentjev.twsbot.misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
    private static final Logger logger = LogManager.getLogger("Utils");

    public static void await(Integer milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    public static boolean isLong(String text) {
        try {
            Long.parseLong(text);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
