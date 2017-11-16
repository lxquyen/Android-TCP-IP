package com.quyenlx.core.util;

import android.annotation.SuppressLint;

import com.bumptech.glide.request.RequestOptions;
import com.quyenlx.core.R;

/**
 * Created by quyenlx on 10/26/2017.
 */

public class GlideOptionUtils {
    @SuppressLint("CheckResult")
    public static RequestOptions fitCenter() {
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        return options;
    }

    @SuppressLint("CheckResult")
    public static RequestOptions centerCrop() {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.error(R.drawable.ic_thumb_default);
        return options;
    }
}
