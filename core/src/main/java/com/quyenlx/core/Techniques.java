package com.quyenlx.core;

import com.quyenlx.core.anim.FadeInUpAnimator;
import com.quyenlx.core.anim.PulseAnimator;

/**
 * Created by quyenlx on 10/5/2017.
 */

public enum Techniques {
    FadeInUp(FadeInUpAnimator.class),
    Pulse(PulseAnimator.class);


    private Class animatorClazz;

    Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
