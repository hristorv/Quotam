package com.quotam.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.utils.Convertor;
import com.quotam.utils.DrawableTint;

import java.util.List;

public class CustomStepper extends LinearLayout {

    public static final int TABS_HEIGHT = 70;
    public static final int ANIMATION_DURATION = 200;
    private static final int ANIMATION_TAB_DURATION = 200;
    private static final int TEXT_ANIMATION_LENGTH = 200;
    boolean singleSelect;
    private SlideAnimation animationShow;
    private SlideAnimation animationHide;
    private ViewPager viewPager;
    private Button nextButton;


    public CustomStepper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(ViewPager viewPager) {
        this.viewPager = viewPager;
        update();
        initAnimation();
        disableAllTabs();
        setCurrentTab(0);
    }

    private void update() {
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < getSize(); i++) {
                CharSequence pageTitle = adapter.getPageTitle(i);
                LinearLayout base = (LinearLayout) inflater.inflate(R.layout.stepper_item, this, false);
                TextView title = base.findViewById(R.id.stepper_item_title);
                RelativeLayout iconBase = base.findViewById(R.id.stepper_item_icon);
                TextView iconCount = iconBase.findViewById(R.id.stepper_item_icon_count);

                iconCount.setText(String.valueOf(i + 1));
                title.setText(pageTitle);
                removeLines(i, base);

                this.addView(base, i);
                base.setOnClickListener(new ClickListener(i));
            }
        }
    }

    private void removeLines(int position, LinearLayout base) {
        if (position == 0) {
            View leftView = base.findViewById(R.id.stepper_left_line);
            leftView.setVisibility(View.INVISIBLE);
        }
        if (position == getSize() - 1) {
            View rightView = base.findViewById(R.id.stepper_right_line);
            rightView.setVisibility(View.INVISIBLE);
        }
    }

    public void disableAllTabs() {
        for (int i = 0; i < getChildCount(); i++) {
            disableTab(i);
        }
    }

    private void changeTabColors(View item, int colorID) {
        TextView title = item.findViewById(R.id.stepper_item_title);
        title.setTextColor(getResources().getColor(colorID));
        RelativeLayout iconBase = item.findViewById(R.id.stepper_item_icon);
        TextView iconCount = iconBase.findViewById(R.id.stepper_item_icon_count);
        iconCount.setTextColor(getResources().getColor(colorID));
        iconBase.setBackground(DrawableTint.tintDrawable(getContext(), R.drawable.circle_step_background, colorID));
    }

    public void disableTab(int position) {
        View item = getChildAt(position);
        item.setClickable(false);
        changeTabColors(item, R.color.disabled_text_color);
    }

    public void enableTab(int position) {
        View item = getChildAt(position);
        item.setClickable(true);
        changeTabColors(item, R.color.primary_text_color);
    }

    public void setCurrentTab(int position) {
        View item = this.getChildAt(position);
        if (!item.isClickable())
            enableTab(position);
        item.callOnClick();
    }

    public void setNextTab(List<Integer> noSkip) {
        if (!isAtLastPosition()) {
            int nextPosition = viewPager.getCurrentItem() + 1;
            for (int i = nextPosition + 1; i < getSize(); i++) {
                if (getChildAt(i).isClickable() && !noSkip.contains(i))
                    nextPosition += 1;
                else
                    break;
            }
            setDoneIcon(viewPager.getCurrentItem());
            setCurrentTab(nextPosition);
        }
    }

    private void setDoneIcon(int position) {
        View item = this.getChildAt(position);
        RelativeLayout iconBase = item.findViewById(R.id.stepper_item_icon);
        final TextView iconCount = iconBase.findViewById(R.id.stepper_item_icon_count);
        ImageView iconDone = iconBase.findViewById(R.id.stepper_icon_done);
        iconDone.setImageDrawable(DrawableTint.tintDrawable(getContext(), R.drawable.ic_done_white_18dp, R.color.primary_text_color));
        if (iconDone.getAlpha() == 0.0f) {
            iconCount.animate()
                    .alpha(0.0f)
                    .setDuration(200)
                    .start();
            iconDone.animate()
                    .alpha(1.0f)
                    .setDuration(200)
                    .start();
        }
    }

    private int getSize() {
        return viewPager.getAdapter().getCount();
    }

    public boolean isAtLastPosition() {
        return viewPager.getCurrentItem() == getSize() - 1;
    }

    private void initAnimation() {
        animationShow = new SlideAnimation(this, 0, Convertor.convertDpToPixel(TABS_HEIGHT));
        animationShow.setInterpolator(new DecelerateInterpolator());
        animationShow.setDuration(ANIMATION_DURATION);
        animationHide = new SlideAnimation(this, Convertor.convertDpToPixel(TABS_HEIGHT), 0);
        animationHide.setInterpolator(new DecelerateInterpolator());
        animationHide.setDuration(ANIMATION_DURATION);
    }

    public void animateShow() {
        setAnimation(animationShow);
        startAnimation(animationShow);
    }

    public void animateHide() {
        setAnimation(animationHide);
        startAnimation(animationHide);
    }

    private void animateSelected(View selectedView) {
        if (selectedView != null) {
            final TextView textView = selectedView.findViewById(R.id.stepper_item_title);
            RelativeLayout iconBase = selectedView.findViewById(R.id.stepper_item_icon);
            final TextView iconCount = iconBase.findViewById(R.id.stepper_item_icon_count);
            ImageView iconDone = iconBase.findViewById(R.id.stepper_icon_done);

            animateColors(textView, R.color.primary_text_color, R.color.accent_color);
            animateColors(iconCount, R.color.primary_text_color, R.color.accent_color);
            iconBase.setBackground(DrawableTint.tintDrawable(getContext(), R.drawable.circle_step_background, R.color.accent_color));
            iconDone.setImageDrawable(DrawableTint.tintDrawable(getContext(), R.drawable.ic_done_white_18dp, R.color.accent_color));
        }
    }


    private void animateDeselected(View lastSelectedView) {
        if (lastSelectedView != null) {
            final TextView textView = lastSelectedView.findViewById(R.id.stepper_item_title);
            RelativeLayout iconBase = lastSelectedView.findViewById(R.id.stepper_item_icon);
            final TextView iconCount = iconBase.findViewById(R.id.stepper_item_icon_count);
            animateColors(textView, R.color.accent_color, R.color.primary_text_color);
            animateColors(iconCount, R.color.accent_color, R.color.primary_text_color);
            iconBase.setBackground(DrawableTint.tintDrawable(getContext(), R.drawable.circle_step_background, R.color.primary_text_color));
            ImageView iconDone = iconBase.findViewById(R.id.stepper_icon_done);
            iconDone.setImageDrawable(DrawableTint.tintDrawable(getContext(), R.drawable.ic_done_white_18dp, R.color.primary_text_color));
        }
    }

    private void animateColors(final TextView textView, int previousColor, int toColor) {
//        ValueAnimator anim = new ValueAnimator();
//        anim.setIntValues(getResources().getColor(previousColor), getResources().getColor(toColor));
//        anim.setEvaluator(new ArgbEvaluator());
//
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int color = (Integer) valueAnimator.getAnimatedValue();
        textView.setTextColor(getResources().getColor(toColor));
//            }
//        });
//        anim.setDuration(ANIMATION_TAB_DURATION);
//        anim.start();
    }

    public void addNextButton(Button nextButton) {
        this.nextButton = nextButton;
    }

    private void changeNextButton() {
        if (nextButton != null)
            if (isAtLastPosition()) {
                // Transform to Done string
                animateButton(R.string.done);
            } else if (nextButton.getText().equals(getResources().getString(R.string.done))) {
                // Transform to Next string
                animateButton(R.string.next);
            }
    }

    private void animateButton(final int stringID) {
        if (nextButton != null)
            nextButton.animate()
                    .alpha(0.0f)
                    .setDuration(TEXT_ANIMATION_LENGTH)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            nextButton.setText(stringID);
                            nextButton.animate()
                                    .alpha(1.0f)
                                    .setDuration(TEXT_ANIMATION_LENGTH);
                        }
                    });
    }

    private class ClickListener implements OnClickListener {

        int position;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View selectedView) {
            boolean isSelected = selectedView.isSelected();
            if (!isSelected) {
                View lastSelectedView = null;
                for (int i = 0; i < CustomStepper.this.getChildCount(); i++) {
                    View curView = CustomStepper.this.getChildAt(i);
                    if (curView.isSelected())
                        lastSelectedView = curView;
                }
                animateSelected(selectedView);
                selectedView.setSelected(true);
                if (lastSelectedView != null) {
                    animateDeselected(lastSelectedView);
                    lastSelectedView.setSelected(false);
                }
                viewPager.setCurrentItem(position, true);
                changeNextButton();
            }

        }
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
