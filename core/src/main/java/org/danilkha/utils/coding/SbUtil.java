package org.danilkha.utils.coding;

public class SbUtil {

    public static boolean endsWith(StringBuilder sb, String suffix) {
        return startsWith(sb, suffix, sb.length() - suffix.length());
    }

    public static boolean startsWith(StringBuilder sb, String prefix) {
        return startsWith(sb, prefix, 0);
    }

    public static boolean startsWith(StringBuilder sb, String prefix, int offset) {
        if (offset < 0 || sb.length() - offset < prefix.length())
            return false;

        int len = prefix.length();
        for (int i = 0; i < len; ++i) {
            if (sb.charAt(offset + i) != prefix.charAt(i))
                return false;
        }
        return true;
    }
}
