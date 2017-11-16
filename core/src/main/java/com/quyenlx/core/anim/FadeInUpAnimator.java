package com.quyenlx.core.anim;

import android.animation.ObjectAnimator;
import android.view.View;

import com.quyenlx.core.BaseViewAnimator;

/**
 * Created by quyenlx on 10/5/2017.
 */

public class FadeInUpAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "translationY", 200, 0),
                ObjectAnimator.ofFloat(target, "alpha", 0, 1)
        );
    }
}