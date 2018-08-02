package com.quotam.controller;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quotam.R;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class EyeDropper {

    private static final Matrix INVERT_MATRIX = new Matrix();
    private static final int NO_COLOR = Color.TRANSPARENT;
    private final TouchViewOnTouch touchViewOnTouch;
    private final View touchView;
    private final ViewGroup viewLayout;

    private ColorSelectionListener colorListener;
    private int xTouch;
    private int yTouch;
    private SelectionListener selectionListener;
    private ImageView indicator;
    private boolean firstTouch;
    private Bitmap drawingCache;

    public EyeDropper(@NonNull View touchView, View background, ViewGroup viewLayout, @NonNull ColorSelectionListener listener) {
        this.colorListener = listener;
        this.touchView = touchView;
        this.viewLayout = viewLayout;
        setTargetView(background);
        touchViewOnTouch = new TouchViewOnTouch(background);
        touchView.setOnTouchListener(touchViewOnTouch);
        setViewIndicatorInLayout(viewLayout);
    }

    public void disable() {
        touchView.setOnTouchListener(null);
        viewLayout.removeView(indicator);
        indicator.setVisibility(View.GONE);
    }

    public void enable() {
        touchView.setOnTouchListener(touchViewOnTouch);
        setViewIndicatorInLayout(viewLayout);
    }

    private void setViewIndicatorInLayout(ViewGroup viewLayout) {
        if (indicator == null) {
            indicator = new ImageView(viewLayout.getContext());
            indicator.setImageResource(R.drawable.eyedropper_indicator);
            indicator.setVisibility(View.GONE);
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        firstTouch = true;
        viewLayout.addView(indicator);
    }

    private void handleIndicatorPosition(int xTouch, int yTouch) {
        // Move the indicator to the selected position
        int xOffset = indicator.getWidth() / 2;
        int yOffset = indicator.getHeight() / 2;
        indicator.setX(xTouch - xOffset);
        indicator.setY(yTouch - yOffset);
        // Setup visibility
        if (firstTouch) {
            firstTouch = false;
            indicator.setVisibility(View.VISIBLE);
        }
    }

    private void handleIndicatorColor(int colorAtPoint) {
        // Change the color
        Drawable indicatorDrawable = indicator.getDrawable();
        ((GradientDrawable) indicatorDrawable).setColor(colorAtPoint);
    }

    private boolean shouldDrawingCacheBeEnabled(@NonNull View targetView) {
        return !(targetView instanceof ImageView) && !(targetView instanceof GPUImageView) && !targetView.isDrawingCacheEnabled();
    }

    private void handleSelectionEnd(MotionEvent event, int action) {
        if (selectionListener != null && action == MotionEvent.ACTION_UP) {
            selectionListener.onSelectionEnd(event);
        }
    }

    private void handleSelectionStart(MotionEvent event, int action) {
        if (selectionListener != null && action == MotionEvent.ACTION_DOWN) {
            selectionListener.onSelectionStart(event);
        }
    }

    private int getColorAtPoint(View background, int x, int y) {
        if (background instanceof ImageView) {
            return handleIfImageView(background, x, y);
        } else if (background instanceof GPUImageView) {
            return handleIfSurfaceView(background, x, y);
        } else {
            final Bitmap drawingCache = background.getDrawingCache();
            return getPixelAtPoint(drawingCache, x, y);
        }
    }

    private int handleIfSurfaceView(View background, int x, int y) {
        if (drawingCache == null) {
            try {
                drawingCache = ((GPUImageView) background).capture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return getPixelAtPoint(drawingCache, x, y);
    }

    private int handleIfImageView(View background, int x, int y) {
        final ImageView targetImageView = (ImageView) background;
        final Drawable drawable = targetImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            targetImageView.getImageMatrix().invert(INVERT_MATRIX);
            final float[] mappedPoints = new float[]{x, y};
            INVERT_MATRIX.mapPoints(mappedPoints);

            final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return getPixelAtPoint(bitmap, (int) mappedPoints[0], (int) mappedPoints[1]);
        }
        return NO_COLOR;
    }

    private int getPixelAtPoint(Bitmap bitmap, int x, int y) {
        if (isValidPoint(x, y, bitmap)) {
            return bitmap.getPixel(x, y);
        }
        return NO_COLOR;
    }

    private boolean isValidPoint(int x, int y, Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        return isValidCoordinate(x, width) && isValidCoordinate(y, height);
    }

    private boolean isValidCoordinate(int coordinate, int size) {
        return coordinate > 0 && coordinate < size;
    }

    private void notifyColorSelected(int color) {
        if (colorListener != null) {
            colorListener.onColorSelected(color);
        }
    }

    private void setTargetView(@NonNull final View targetView) {
        if (shouldDrawingCacheBeEnabled(targetView)) {
            targetView.setDrawingCacheEnabled(true);
            targetView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        }
    }

    /**
     * Register a callback to be invoked when the color selection begins or ends.
     */
    public void setSelectionListener(@NonNull SelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * Optional listener to listen to before and after selection events
     */
    public interface SelectionListener {
        /**
         * Invoked when the user touches the view to select a color. This corresponds to the {@link
         * MotionEvent#ACTION_DOWN} event.
         */
        void onSelectionStart(@NonNull MotionEvent event);

        /**
         * Invoked when the color selection is finished. This corresponds to the {@link MotionEvent#ACTION_UP}
         * event.
         */
        void onSelectionEnd(@NonNull MotionEvent event);
    }

    public interface ColorSelectionListener {
        /**
         * Invoked when a color is selected from the given view
         *
         * @param color the selected color
         */
        void onColorSelected(@ColorInt int color);
    }

    private class TouchViewOnTouch implements OnTouchListener {
        private final View background;

        public TouchViewOnTouch(View background) {
            this.background = background;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int action = event.getActionMasked();
            xTouch = (int) event.getX();
            yTouch = (int) event.getY();
            if (isPointInParent(background,xTouch,yTouch)) {
                handleSelectionStart(event, action);
                handleIndicatorPosition(xTouch, yTouch);
                int colorAtPoint = getColorAtPoint(background, xTouch, yTouch);
                handleIndicatorColor(colorAtPoint);
                notifyColorSelected(colorAtPoint);
                handleSelectionEnd(event, action);
            }
            return true;
        }

        private boolean isPointInParent(View background, int xTouch, int yTouch) {
            Rect backgroundRect = new Rect();
            background.getHitRect(backgroundRect);
            return backgroundRect.contains(xTouch,yTouch);
        }
    }
}
