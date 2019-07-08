package com.sk.commons.utils;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Interpolator;


/**
 * @author sk on 2016-10-17.
 */

public abstract class ObjectAnimatorHelper {

    private static ObjectAnimator animator;
    private static AnimatorSet set;

    public static final int TOP_TO_VISIBLE = 0;
    public static final int TOP_TO_GONE = 1;
    public static final int BOTTOM_TO_VISIBLE = 2;
    public static final int BOTTOM_TO_GONE = 3;

    public static void animEnd(OnAnimListener animListener) {
        animator.addListener(animListener);
    }

    public static void alphaAnim(View view, Interpolator interpolator, int duration, boolean isVisible) {
        if (isVisible) {
            animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        } else {
            animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        }
        if (null != interpolator) { animator.setInterpolator(interpolator);}
        animator.setDuration(duration);
        animator.start();
    }

    public static void rotateAnim(View view, int duration) {
        set = new AnimatorSet();
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", 0f, 359f).setDuration(duration);
        anim.setRepeatCount(-1);

        set.play(anim);
        set.start();
    }

    public static void translationYAnim(View view, Interpolator interpolator, int duration, boolean isVisible, int toWhere) {
        set = new AnimatorSet();
        switch (toWhere) {
            case TOP_TO_VISIBLE:
                animator = ObjectAnimator.ofFloat(view, "translationY", -360f, 0f);
                break;
            case TOP_TO_GONE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 0f, -360f);
                break;
            case BOTTOM_TO_VISIBLE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 360f, 0f);
                break;
            case BOTTOM_TO_GONE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 360f);
                break;
            default:
                break;
        }

        if (null != interpolator) {animator.setInterpolator(interpolator);}
        animator.setDuration(duration);

        ObjectAnimator alphaAnim;
        if (isVisible) {
            alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        } else {
            alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        }
        if (null != interpolator) { alphaAnim.setInterpolator(interpolator);}
        alphaAnim.setDuration(duration);

        set.play(animator).with(alphaAnim);
        set.start();
    }

    public static void translationYBottomAnim(View view, Interpolator interpolator, int duration, int toWhere, float offset) {
        set = new AnimatorSet();
        switch (toWhere) {
            case BOTTOM_TO_VISIBLE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 360f, 0f);
                break;
            case BOTTOM_TO_GONE:
                // 540是因为实景引导View是一直显示的
                animator = ObjectAnimator.ofFloat(view, "translationY", 0f, offset);
                break;
            default:
                break;
        }

        if (null != interpolator) {animator.setInterpolator(interpolator);}
        animator.setDuration(duration);
        set.play(animator);
        set.start();
    }

    public static void translationXAnim(View view, Interpolator interpolator, int duration, boolean isVisible, boolean toLeft) {
        set = new AnimatorSet();
        if (toLeft) {
            animator = ObjectAnimator.ofFloat(view, "translationX", -360f, 0f);
        } else {
            animator = ObjectAnimator.ofFloat(view, "translationX", 0f, -360f);
        }
        if (null != interpolator) { animator.setInterpolator(interpolator);}
        animator.setDuration(duration);

        ObjectAnimator alphaAnim;
        if (isVisible) {
            alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        } else {
            alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        }
        if (null != interpolator) { alphaAnim.setInterpolator(interpolator);}
        alphaAnim.setDuration(duration);

        set.play(animator).with(alphaAnim);
        set.start();
    }


    public static void stopAnim() {
        if (null != set) { set.end();}
    }

}
