package org.parfentjev.errbot.misc;

public class Utils {
    public static void await(Integer milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
