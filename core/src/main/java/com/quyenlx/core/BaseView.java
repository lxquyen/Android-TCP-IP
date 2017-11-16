package com.quyenlx.core;

/**
 * Created by quyenlx on 10/5/2017.
 */

public interface BaseView {
    void onShowLoading(String... arrString);

    void onHideLoading();

    void onRequestFailure(String message, Throwable throwable);

    void onHideKeyboard();
}
