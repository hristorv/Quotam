package com.quotam.listeners;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.quotam.R;
import com.quotam.custom.CustomSnackbar;

public abstract class FabLikeOnClickListener implements View.OnClickListener {

    public static final int ANIMATION_DURATION = 200;
    private boolean toggled;
    private int backgroundColor;
    private int iconColor;

    public FabLikeOnClickListener(boolean toggled, int backgroundColor, int iconColor) {
        this.toggled = toggled;
        this.backgroundColor = backgroundColor;
        this.iconColor = iconColor;
    }

    @Override
    public void onClick(View view) {
        toggled = !toggled;
        if (view instanceof FloatingActionButton)
            animateFab((FloatingActionButton) view);
        onClick();
        int message = toggled ? R.string.liked : R.string.unliked;
        CustomSnackbar.create(view.getContext(),view,message, Snackbar.LENGTH_SHORT);
    }

    private void animateFab(final FloatingActionButton fab) {
        final Drawable icon = fab.getDrawable();
        ObjectAnimator animatorBackground;
        ObjectAnimator animatorDrawable;
        if (toggled) {
            animatorBackground = ObjectAnimator.ofInt(fab, "backgroundTint", backgroundColor, iconColor);
            animatorDrawable = ObjectAnimator.ofInt(icon, "backgroundTint", iconColor, backgroundColor);
        } else {
            animatorBackground = ObjectAnimator.ofInt(fab, "backgroundTint", iconColor, backgroundColor);
            animatorDrawable = ObjectAnimator.ofInt(icon, "backgroundTint", backgroundColor, iconColor);
        }
        animatorBackground.setDuration(ANIMATION_DURATION);
        animatorBackground.setEvaluator(new ArgbEvaluator());
        animatorBackground.setInterpolator(new DecelerateInterpolator());
        animatorBackground.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                fab.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
            }
        });
        animatorDrawable.setDuration(ANIMATION_DURATION);
        animatorDrawable.setEvaluator(new ArgbEvaluator());
        animatorDrawable.setInterpolator(new DecelerateInterpolator());
        animatorDrawable.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                fab.setImageDrawable(tintDrawable(icon,animatedValue));
            }
        });
        animatorDrawable.start();
        animatorBackground.start();
    }

    private Drawable tintDrawable(Drawable drawableInput, int colorID) {
        Drawable drawable = DrawableCompat.wrap(drawableInput);
        ColorStateList colorSelector = ColorStateList.valueOf(colorID);
        DrawableCompat.setTintList(drawable, colorSelector);
        return drawable.mutate();
    }

    public abstract void onClick();

}
