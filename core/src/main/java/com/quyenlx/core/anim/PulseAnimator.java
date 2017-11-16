package com.quyenlx.core.anim;

import android.animation.ObjectAnimator;
import android.view.View;

import com.quyenlx.core.BaseViewAnimator;

/**
 * Created by quyenlx on 10/5/2017.
 */

public class PulseAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "scaleY", 1, 1.1f, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.1f, 1)
        );
    }
}