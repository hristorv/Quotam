package com.quotam.listeners;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.quotam.R;
import com.quotam.custom.CustomTabsLayout;

public class FabFilterOnClickListener implements View.OnClickListener {

    public static final int ANIMATION_DURATION = 200;
    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_MIN = 0;
    private final CustomTabsLayout tabs;
    private final FloatingActionButton fab;

    public FabFilterOnClickListener(CustomTabsLayout tabs, FloatingActionButton fab) {
        super();
        this.tabs = tabs;
        this.fab = fab;
    }

    @Override
    public void onClick(View view) {
        if (tabs.getHeight() > 0) {
            changeFabIcon(R.drawable.ic_filter);
            tabs.animateHide();
        } else {
            changeFabIcon(R.drawable.ic_done);
            tabs.setAlpha(1f);
            tabs.animateShow();
        }
    }

    private void changeFabIcon(final int res) {
        ObjectAnimator hideAnimator = ObjectAnimator.ofInt(fab.getDrawable(), "alpha", ALPHA_MAX, ALPHA_MIN);
        hideAnimator.setDuration(ANIMATION_DURATION);
        hideAnimator.start();
        hideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fab.setImageResource(res);
                ObjectAnimator showAnimator = ObjectAnimator.ofInt(fab.getDrawable(), "alpha", ALPHA_MIN, ALPHA_MAX);
                showAnimator.setDuration(ANIMATION_DURATION);
                showAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

}