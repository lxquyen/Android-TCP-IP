package com.quyenlx.core.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by quyenlx on 10/5/2017.
 */

public class KeyboardUtils {
    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager input = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            assert null != input;
            input.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager input = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        assert null != input;
        input.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }
}
