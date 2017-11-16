package com.quyenlx.core.util;

/**
 * Created by quyenlx on 10/5/2017.
 */

public interface ConstUtils {
    interface Memory {
        int BYTE = 1;
        int KB = 1024;
        int MB = 1048576;
        int GB = 1073741824;
    }

    interface Time {
        int MSEC = 1;
        int SEC = 1000;
        int MIN = 60000;
        int HOUR = 3600000;
        int DAY = 86400000;
    }

    interface Regex {
        String MOBILE_SIMPLE = "^[1]\\d{10}$";
        String MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
        String TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
        String IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
        String USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
        String DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    }

    enum TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY
    }

}
