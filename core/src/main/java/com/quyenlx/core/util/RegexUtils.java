package com.quyenlx.core.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by quyenlx on 10/5/2017.
 */

public class RegexUtils {
    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isMobileSimple(String string) {
        return isMatch(ConstUtils.Regex.MOBILE_SIMPLE, string);
    }

    public static boolean isMobileExact(String string) {
        return isMatch(ConstUtils.Regex.MOBILE_EXACT, string);
    }

    public static boolean isTel(String string) {
        return isMatch(ConstUtils.Regex.TEL, string);
    }

    public static boolean isEmail(String string) {
        return isMatch(ConstUtils.Regex.EMAIL, string);
    }

    public static boolean isUsername(String string) {
        return isMatch(ConstUtils.Regex.USERNAME, string);
    }

    public static boolean isDate(String string) {
        return isMatch(ConstUtils.Regex.DATE, string);
    }

    private static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }
}
