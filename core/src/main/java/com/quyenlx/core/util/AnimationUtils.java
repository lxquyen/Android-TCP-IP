package com.quyenlx.core.util;

import android.animation.Animator;
import android.view.View;

import com.quyenlx.core.BaseViewAnimator;
import com.quyenlx.core.Techniques;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quyenlx on 10/5/2017.
 */

public class AnimationUtils {
    private static final long DURATION = BaseViewAnimator.DURATION;
    private static final long NO_DELAY = 0;

    public static AnimationComposer with(Techniques techniques) {
        return new AnimationComposer(techniques);
    }


    public static final class AnimationComposer {
        private List<Animator.AnimatorListener> callbacks = new ArrayList<>();
        private BaseViewAnimator animator;
        private long duration = DURATION;
        private long delay = NO_DELAY;
        private boolean isStop = false;

        private AnimationComposer(Techniques techniques) {
            this.animator = techniques.getAnimator();
        }

        public AnimationComposer duration(long duration) {
            this.duration = duration;
            return this;
        }

        public AnimationComposer delay(long delay) {
            this.delay = delay;
            return this;
        }


        public AnimationComposer onEnd(final AnimatorCallback callback) {
            callbacks.add(new EmptyAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isStop)
                        callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer playOn(View target) {
            animator.setTarget(target);
            animator.setDuration(duration)
                    .setStartDelay(delay);
            if (callbacks.size() > 0) {
                for (Animator.AnimatorListener callback : callbacks) {
                    animator.addAnimatorListener(callback);
                }
            }
            animator.animate();
            return this;
        }

        public void stop() {
            isStop = true;
            animator.cancel();
        }


    }

    public interface AnimatorCallback {
        void call(Animator animator);
    }

    private static class EmptyAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
