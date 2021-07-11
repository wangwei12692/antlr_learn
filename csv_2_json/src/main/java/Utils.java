public class Utils {
    public static String stripQuotes(String str) {
        if (str == null) return str;
        if (str.length() == 0) return str;

        int beginIndex = 0;
        final char openQuote = str.charAt(beginIndex);
        if ( openQuote == '\"' || openQuote=='\'' ) beginIndex += 1;

        int endIndex = str.length();
        final char closeQuote = str.charAt(endIndex - 1);
        if ( closeQuote == '\"' || closeQuote=='\'' ) endIndex -= 1;

        if ((endIndex - beginIndex) < 0) return str;

        // note that String.substring returns "this" if beginIndex == 0 && endIndex == count
        // or a new string that shares the character buffer with the original string.
        return str.substring(beginIndex, endIndex);
    }
}
