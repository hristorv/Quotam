package com.quotam.custom;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.FrameLayout;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.quotam.utils.Convertor;

public class Badge extends AppCompatTextView {
    private final View view;
    private int count;
    private boolean isVisible = false;
    private int buttonIndex;

    public Badge(Context context, View view, int index, int badge_background, int primary_text_color) {
        super(context);
        this.buttonIndex = index;
        this.view = view;
        attachToTab(getResources().getColor(badge_background));
        setTextColor(getResources().getColor(primary_text_color));
    }


    /**
     * Set the unread / new item / whatever count for this Badge.
     *
     * @param count the value this Badge should show.
     */
    public void setCount(int count) {
        this.count = count;
        setText(String.valueOf(count));
    }

    /**
     * Get the currently showing count for this Badge.
     *
     * @return current count for the Badge.
     */
    public int getCount() {
        return count;
    }

    /**
     * Shows the badge with a neat little scale animation.
     */
    public void show() {
        if (!isVisible) {
            isVisible = true;
            ViewCompat.animate(this)
                    .setDuration(150)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1)
                    .start();
        }
    }

    /**
     * Hides the badge with a neat little scale animation.
     */
    public void hide() {
        if (isVisible) {
            isVisible = false;
            ViewCompat.animate(this)
                    .setDuration(150)
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .start();
        }
    }

    /**
     * Is this badge currently visible?
     *
     * @return true is this badge is visible, otherwise false.
     */
    public boolean isVisible() {
        return isVisible;
    }

    private void attachToTab(int backgroundColor) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(params);
        setGravity(Gravity.CENTER);

        setColoredCircleBackground(backgroundColor);
        wrapTabAndBadgeInSameContainer();
    }

    private void setColoredCircleBackground(int circleColor) {
        int innerPadding = Convertor.convertDpToPixel(1);
        ShapeDrawable backgroundCircle = make(innerPadding * 3, circleColor);
        setPadding(innerPadding, innerPadding, innerPadding, innerPadding);
        setBackground(backgroundCircle);
    }

    private void wrapTabAndBadgeInSameContainer() {
        ViewGroup buttonContainer = (ViewGroup) view.getParent();
        buttonContainer.removeView(view);

        final BadgeContainer badgeContainer = new BadgeContainer(getContext());
        badgeContainer.setLayoutParams(view.getLayoutParams());
        badgeContainer.setClipChildren(false);
        badgeContainer.setClipToPadding(false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        badgeContainer.addView(view);
        badgeContainer.addView(this);

        buttonContainer.addView(badgeContainer, buttonIndex);

        badgeContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getWidth() > 0 || view.getHeight() > 0) {
                    badgeContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    adjustPositionAndSize(view);
                }
            }
        });
    }

    void adjustPositionAndSize(View view) {
        ViewGroup.LayoutParams params = getLayoutParams();

        int size = Math.max(getWidth(), getHeight());
        float xOffset = (float) (view.getWidth() / 1.7);

        setX(view.getX() + xOffset);
        //setTranslationY(-Convertor.convertDpToPixel(5));

        if (params.width != size || params.height != size) {
            params.width = size;
            params.height = size;
            setLayoutParams(params);
        }
    }

    void removeFromTab(ImageButton button) {
        BadgeContainer badgeAndTabContainer = (BadgeContainer) getParent();
        ViewGroup originalTabContainer = (ViewGroup) badgeAndTabContainer.getParent();

        badgeAndTabContainer.removeView(button);
        originalTabContainer.removeView(badgeAndTabContainer);
        button.setLayoutParams(badgeAndTabContainer.getLayoutParams());
        originalTabContainer.addView(button, buttonIndex);
    }

    /**
     * Creates a new circle for the Badge background.
     *
     * @param size  the width and height for the circle
     * @param color the activeIconColor for the circle
     * @return a nice and adorable circle.
     */
    @NonNull
    private ShapeDrawable make(@IntRange(from = 0) int size, @ColorInt int color) {
        ShapeDrawable indicator = new ShapeDrawable(new OvalShape());
        indicator.setIntrinsicWidth(size);
        indicator.setIntrinsicHeight(size);
        indicator.getPaint().setColor(color);
        return indicator;
    }
    class BadgeContainer extends FrameLayout {
        public BadgeContainer(@NonNull Context context) {
            super(context);
        }
    }
}

