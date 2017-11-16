package com.quyenlx.core;

/**
 * Created by quyenlx on 10/5/2017.
 */

public interface BasePresenter<T extends BaseView> {
    void onAttachView(T view);

    void onDetach();
}
