package com.quotam.custom;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import com.quotam.R;
import com.quotam.utils.Convertor;

public class CustomTabsLayout extends LinearLayout {

    public static final int TABS_HEIGHT = 60;
    public static final int ANIMATION_DURATION = 300;
    private static final int ANIMATION_TAB_DURATION = 200;
    boolean singleSelect;
    private OnMenuClickListener onMenuClickListener;
    private Menu menu;
    private Context context;
    private SlideAnimation animationShow;
    private SlideAnimation animationHide;


    public CustomTabsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void update() {
        if (menu != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                LinearLayout base = (LinearLayout) inflater.inflate(R.layout.custom_tabs_item, this, false);
                TextView view = base.findViewById(R.id.tab_text);
                view.setText(item.getTitle());
                // view.setCompoundDrawablesWithIntrinsicBounds(null, item.getIcon(), null, null);
                this.addView(base, i);
                base.setOnClickListener(new ClickListener(i));
            }
            initAnimation();
        }
    }

    public void clear() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View curView = this.getChildAt(i);
            if (curView.isSelected()) {
                animateDeselected(curView);
                curView.setSelected(false);
            }
        }
        clearFocus();
    }

    public void setDefaultItem(int position) {
        if (singleSelect) {
            View defaultView = this.getChildAt(position);
            defaultView.callOnClick();
        }
    }

    public void setDefaultItem() {
        if (!singleSelect) {
            for (int i = 0; i < menu.size(); i++) {
                View defaultView = this.getChildAt(i);
                defaultView.callOnClick();
            }
            //onMenuClickListener.onMenuItemClick(null);
        }
    }

    public ArrayList<MenuItem> getCheckedItems() {
        if (!singleSelect) {
            ArrayList<MenuItem> checkedItems = new ArrayList<MenuItem>();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (item.isChecked())
                    checkedItems.add(item);
            }
            return checkedItems;
        }
        return null;
    }

    public void addMenu(int menuID, boolean singleSelect, Activity activity) {
        this.singleSelect = singleSelect;
        context = activity;
        PopupMenu p = new PopupMenu(activity, null);
        menu = p.getMenu();
        activity.getMenuInflater().inflate(menuID, menu);
        update();
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

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public interface OnMenuClickListener {
        void onMenuItemClick(MenuItem item);
    }

    private void animateSelected(View selectedView) {
        if (selectedView != null) {
            animateColors(selectedView, getResources().getColor(R.color.secondary_text_color), getResources().getColor(R.color.accent_color));
            animateColorsBackground(selectedView, getResources().getColor(R.color.accent_color), getResources().getColor(R.color.primary_color));
        }
    }


    private void animateDeselected(View lastSelectedView) {
        if (lastSelectedView != null) {
            animateColors(lastSelectedView, getResources().getColor(R.color.accent_color), getResources().getColor(R.color.secondary_text_color));
            animateColorsBackground(lastSelectedView, getResources().getColor(R.color.primary_color), getResources().getColor(R.color.accent_color));

        }
    }

    private void animateColorsBackground(View view, int previousColor, int toColor) {
        final View indicator = view.findViewById(R.id.tab_indicator);
        ValueAnimator anim2 = new ValueAnimator();
        anim2.setIntValues(toColor, previousColor);
        anim2.setEvaluator(new ArgbEvaluator());

        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int color = (Integer) valueAnimator.getAnimatedValue();
                indicator.setBackgroundColor(color);
            }
        });

        anim2.setDuration(ANIMATION_TAB_DURATION);
        anim2.start();
    }

    private void animateColors(View view, int previousColor, int toColor) {
        final TextView textView = view.findViewById(R.id.tab_text);
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(previousColor, toColor);
        anim.setEvaluator(new ArgbEvaluator());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int color = (Integer) valueAnimator.getAnimatedValue();
                textView.setTextColor(color);
            }
        });
        anim.setDuration(ANIMATION_TAB_DURATION);
        anim.start();
    }


    private class ClickListener implements OnClickListener {

        int position;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View selectedView) {
            MenuItem item = menu.getItem(position);
            boolean isSelected = selectedView.isSelected();
            if (singleSelect && !isSelected) {
                View lastSelectedView = null;
                for (int i = 0; i < CustomTabsLayout.this.getChildCount(); i++) {
                    View curView = CustomTabsLayout.this.getChildAt(i);
                    if (curView.isSelected())
                        lastSelectedView = curView;
                }
                animateSelected(selectedView);
                selectedView.setSelected(true);
                if (lastSelectedView != null) {
                    animateDeselected(lastSelectedView);
                    lastSelectedView.setSelected(false);
                }
                onMenuClickListener.onMenuItemClick(item);
            }
            if (!singleSelect) {
                if (isSelected) {
                    // Prevent deselecting all items. There always needs to be one item selected.
                    if (getNumberOfSelectedItems() > 1) {
                        animateDeselected(selectedView);
                        selectedView.setSelected(false);
                        item.setChecked(false);
                    }
                } else {
                    animateSelected(selectedView);
                    selectedView.setSelected(true);
                    item.setChecked(true);
                }
            }

        }

        private int getNumberOfSelectedItems() {
            int selectedViews = 0;
            for (int i = 0; i < CustomTabsLayout.this.getChildCount(); i++) {
                View curView = CustomTabsLayout.this.getChildAt(i);
                if (curView.isSelected())
                    selectedViews++;
            }
            return selectedViews;
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
