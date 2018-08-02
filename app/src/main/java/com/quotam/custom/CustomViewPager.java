package com.quotam.custom;

import java.util.List;

import com.quotam.R;
import com.quotam.controller.ImageController;
import com.quotam.model.Constants;
import com.quotam.model.Image;
import com.quotam.model.ImageData;
import com.quotam.custom.touchimage.ImageViewTouch;
import com.quotam.listeners.SystemUI;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class CustomViewPager extends ViewPager implements
        ViewPager.PageTransformer {

    public static final String VIEW_PAGER_OBJECT_TAG = "image#";
    private int previousPosition;
    private OnPageSelectedListener onPageSelectedListener;
    private Context context;
    private MenuItem likeItem;
    private List<Image> images;
    private TextView likesView;
    private boolean isShowing = false;
    private GestureDetector detector;
    private CoordinatorLayout bottomBar;
    private FloatingActionButton fab;

    public CustomViewPager(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void setOnPageSelectedListener(OnPageSelectedListener listener) {
        onPageSelectedListener = listener;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ImageViewTouch) {
            return ((ImageViewTouch) v).canScroll(dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }

    public void setBottomBar(CoordinatorLayout bottomBar) {
        this.bottomBar = bottomBar;
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    public interface OnPageSelectedListener {

        public void onPageSelected(int position);

    }

    private void init() {
        detector = new GestureDetector(context, new GestureTap());
        previousPosition = getCurrentItem();
        this.setPageTransformer(true, this);
        AppCompatActivity activity = (AppCompatActivity) context;
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedListener != null) {
                    onPageSelectedListener.onPageSelected(position);
                }
                //adjustLikeButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_SETTLING
                        && previousPosition != getCurrentItem()) {
                    try {
                        ImageViewTouch imageViewTouch = findViewWithTag(VIEW_PAGER_OBJECT_TAG
                                + getCurrentItem());
                        if (imageViewTouch != null) {
                            imageViewTouch.zoomTo(1f, 300);
                        }

                        previousPosition = getCurrentItem();
                    } catch (ClassCastException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        //setVisibilityChangeListener();
    }

    protected void adjustLikeButton(int position) {
        Image curImage = images.get(position);
        likesView.setText(curImage.getLikes());
        Image[] favoritesArray;
        if (ImageData.getInstance().getImagesFavorites() != null) {
            favoritesArray = ImageData.getInstance().getImagesFavorites();
        } else {
            favoritesArray = new ImageController().getFavorites(context);
        }
        for (Image image : favoritesArray) {
            if (image.getUrl().equals(curImage.getUrl())) {
                setLikeEnabled(false);
                return;
            }
        }
        setLikeEnabled(true);
    }

    private void setLikeEnabled(boolean enabled) {
        likeItem.setEnabled(enabled);
    }

    public void setLikeItem(MenuItem likeItem) {
        this.likeItem = likeItem;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


    public void setLikeView(TextView likesView) {
        this.likesView = likesView;

    }

    @Override
    public void transformPage(View view, float position) {
        final float MIN_SCALE = 0.85f;
        final float MIN_ALPHA = 0.5f;
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putBoolean(Constants.Extra.IS_SHOWING, isShowing);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            this.isShowing = bundle.getBoolean(Constants.Extra.IS_SHOWING);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onInterceptTouchEvent(event);

    }


    class GestureTap extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            SystemUI systemUI = new SystemUI();
            if (isShowing) {
                systemUI.hideSystemUi(context);
                systemUI.hideView(context, bottomBar, R.anim.bottom_exit);
                fab.hide();
                isShowing = false;
            } else {
                systemUI.showSystemUi(context);
                systemUI.showView(context, bottomBar, R.anim.bottom_enter,fab);
                isShowing = true;
            }
            return true;
        }
    }

}