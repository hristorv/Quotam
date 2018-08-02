package com.quotam.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.quotam.R;

public class CustomAnimations {

    public static void animateColorsBackground(View view, int duration,int previousColor, int toColor) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(previousColor,toColor);
        valueAnimator.setEvaluator(new ArgbEvaluator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int color = (Integer) valueAnimator.getAnimatedValue();
                view.setBackgroundColor(color);
            }
        });

        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void animateTextChanged(final TextView textView, final String text) {
        textView.animate()
                .alpha(0.0f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        textView.setText(text);
                        textView.animate()
                                .alpha(1.0f)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .setDuration(200);
                    }
                });
    }


    public static class SlideAnimation extends Animation {

        int mFromHeight;
        int mToHeight;
        View mView;

        public SlideAnimation(View view, int fromHeight, int toHeight) {
            this.mView = view;
            this.mFromHeight = fromHeight;
            this.mToHeight = toHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation transformation) {
            int newHeight;

            if (mView.getHeight() != mToHeight) {
                newHeight = (int) (mFromHeight + ((mToHeight - mFromHeight) * interpolatedTime));
                mView.getLayoutParams().height = newHeight;
                mView.requestLayout();
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

}
