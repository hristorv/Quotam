package com.quotam.listeners;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.quotam.R;
import com.quotam.utils.Convertor;
import com.quotam.custom.CustomTabsLayout;

public class AppLayoutOffsetListener implements AppBarLayout.OnOffsetChangedListener {
    public static final int HIDE_SHOW_DURATION = 200;
    public static final int MAX_ALPHA = 255;
    private static final float INFO_HEIGHT = 40;
    private boolean infoShown = true;
    private boolean fabShown = true;
    private boolean tabsShown = false;
    private int TABS_HEIGHT = 60;
    private boolean expandItemShow;


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    public void animateTabs(int verticalOffset, int threshold, View view) {
        float percentage = 1 - (float) Math.abs(verticalOffset) / (float) threshold;
        view.setScrollY(verticalOffset);
        view.setAlpha(percentage);
    }

    public void animateToolbarTitle(int verticalOffset, int totalScrollrange, CollapsingToolbarLayout collapsingToolbarLayout, AppCompatEditText searchView) {
        totalScrollrange = -totalScrollrange;
//        Log.e("asd", "inAnimate " + "totalScroll:" + totalScrollrange + " verticalOffset:" + verticalOffset);
//        // Show title
//        if (verticalOffset == totalScrollrange && collapsingToolbarLayout.getTitle().equals(" ")) {
//            collapsingToolbarLayout.setTitle(searchView.getEditableText());
//            Log.e("asd", "show "+searchView.getEditableText());
//        }
//        // Hide title
//        if (verticalOffset > totalScrollrange && !collapsingToolbarLayout.getTitle().equals(" ")) {
//            collapsingToolbarLayout.setTitle(" ");
//            Log.e("asd", "hide");
//        }
        if (verticalOffset == 0) {
            collapsingToolbarLayout.setTitle(" ");
            searchView.setVisibility(View.VISIBLE);
        }
        if (verticalOffset < 0) {
            collapsingToolbarLayout.setTitle(searchView.getEditableText());
            searchView.setVisibility(View.GONE);
        }

    }

    public void animateSearchView(int verticalOffset, int threshold, AppCompatEditText searchView) {
        float percentage = 1 - (float) Math.abs(verticalOffset) / (float) threshold;
        searchView.setAlpha(percentage);
    }

    public void animateExpand(int verticalOffset, int threshold, View expandItem) {
        if (expandItem != null) {
            if (verticalOffset < threshold && !expandItemShow) {
                expandItemShow = true;
                expandItem.animate()
                        .alpha(1f)
                        .setDuration(HIDE_SHOW_DURATION);
                return;
            } else if (verticalOffset >= threshold && expandItemShow) {
                expandItemShow = false;
                expandItem.animate()
                        .alpha(0f)
                        .setDuration(HIDE_SHOW_DURATION);
                return;
            } else if (verticalOffset == 0 && expandItem != null) {
                expandItemShow = false;
                expandItem.setAlpha(0f);
            }
        }
    }

    public void animateFilterTabs(int verticalOffset, int threshold, CustomTabsLayout tabs, FloatingActionButton fab) {
        float percentage = 1 - (float) Math.abs(verticalOffset) / (float) threshold;
        int newHeight = (int) (percentage * Convertor.convertDpToPixel(TABS_HEIGHT));
        tabs.getLayoutParams().height = newHeight;
        tabs.requestLayout();
        // Reset the fab after filter bar is hidden.
        if (newHeight == 0) {
            fab.setImageResource(R.drawable.ic_filter);
            fab.getDrawable().setAlpha(MAX_ALPHA);
        }
        tabs.setAlpha(percentage);
    }

    public void animateScale(int verticalOffset, int threshold, View view) {
        if (verticalOffset >= threshold && !infoShown) {
            infoShown = true;
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(HIDE_SHOW_DURATION);
        } else if (verticalOffset < threshold && infoShown) {
            infoShown = false;
            view.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(HIDE_SHOW_DURATION);
        }
    }

    public void animateAlpha(int verticalOffset, int threshold, View view) {
//        float percentage = 1 - (float) Math.abs(verticalOffset) / (float) threshold;
//        int newHeight = (int) (percentage * Convertor.convertDpToPixel(INFO_HEIGHT));
//        albumInfo.getLayoutParams().height = newHeight;
//        albumInfo.requestLayout();
//        albumInfo.setAlpha(percentage);
        if (verticalOffset >= threshold && !infoShown) {
            infoShown = true;
            view.animate()
                    .alpha(1f)
                    .setDuration(HIDE_SHOW_DURATION);
        } else if (verticalOffset < threshold && infoShown) {
            infoShown = false;
            view.animate()
                    .alpha(0f)
                    .setDuration(HIDE_SHOW_DURATION);
        }
    }

    public void animateFab(int verticalOffset, int threshold, FloatingActionButton fab) {
        if (verticalOffset >= threshold && !fabShown) {
            fabShown = true;
            fab.show();
        } else if (verticalOffset < threshold && fabShown) {
            fabShown = false;
            fab.hide();
        }
    }
}
