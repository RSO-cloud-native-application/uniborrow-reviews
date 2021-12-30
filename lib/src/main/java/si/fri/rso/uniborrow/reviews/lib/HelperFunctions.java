package si.fri.rso.uniborrow.reviews.lib;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HelperFunctions {

    public static String sanitizeString(String input) {
        return Jsoup.clean(
                StringEscapeUtils.escapeHtml3(StringEscapeUtils.escapeEcmaScript(StringEscapeUtils.escapeJava(input))),
                Safelist.basic());
    }

    public static boolean checkStars(Integer stars) {
        return stars <= 5 && stars >= 1;
    }
}
