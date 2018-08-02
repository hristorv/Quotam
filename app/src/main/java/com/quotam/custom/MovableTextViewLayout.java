package com.quotam.custom;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.utils.Convertor;

public class MovableTextViewLayout extends FrameLayout {

    private static final float BUTTON_SIZE_DP = 50;
    private static final float BUTTON_PADDING_DP = 10;
    private static final String RESIZE_BUTTON_TAG = "resize_button";
    private static final String ROTATE_BUTTON_TAG = "rotate_button";
    public static final int TEXTVIEW_PADDING = 10;
    public static final int DEFAULT_WIDTH_DP = 200;
    public static final int DEFAULT_HEIGHT_DP = 100;
    public ImageView resizeButton;
    private TextView textView;
    private int textViewMargin;
    private View parentView;
    private SelectListener selectListener;

    public MovableTextViewLayout(Context context) {
        super(context);
        init(context);
    }

    public MovableTextViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MovableTextViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initLayout();
        initTextView(context);
        initResizeButton(context);
        addView(textView);
        addView(resizeButton);
    }

    private void initLayout() {

    }

    private void initTextView(Context context) {
        this.textView = new AppCompatTextView(context);
        this.textView.setBackgroundResource(R.drawable.textview_border);
        this.textView.setFocusable(true);
        this.textView.setFocusableInTouchMode(true);
        int padding = Convertor.convertDpToPixel(TEXTVIEW_PADDING);
        this.textView.setPadding(padding, padding, padding, padding);
        this.textView.setTextColor(getResources().getColor(R.color.primary_text_color));
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(this.textView, 1, 50, 1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        this.textView.setTypeface(this.textView.getTypeface(), Typeface.NORMAL);

        FrameLayout.LayoutParams textViewParameters =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        textViewParameters.gravity = Gravity.TOP | Gravity.LEFT;
        // This textViewMargin is so the button is outside the textview.
        textViewMargin = Convertor.convertDpToPixel(BUTTON_SIZE_DP / 2.5f);
        textViewParameters.setMargins(textViewMargin, textViewMargin, textViewMargin, textViewMargin);
        this.textView.setLayoutParams(textViewParameters);

        this.textView.setOnFocusChangeListener(new TextViewFocusChangedListener());
        this.textView.setOnTouchListener(new TextViewOnTouchListener());
    }

    private void initResizeButton(Context context) {
        this.resizeButton = new ImageView(context);
        this.resizeButton.setTag(RESIZE_BUTTON_TAG);
        int size = Convertor.convertDpToPixel(BUTTON_SIZE_DP);
        int padding = Convertor.convertDpToPixel(BUTTON_PADDING_DP);
        FrameLayout.LayoutParams resizeButtonParams =
                new FrameLayout.LayoutParams(
                        size,
                        size);
        resizeButtonParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        this.resizeButton.setPadding(padding, padding, padding, padding);
        this.resizeButton.setLayoutParams(resizeButtonParams);
        this.resizeButton.setVisibility(View.INVISIBLE);
        this.resizeButton.setImageResource(R.drawable.resize_icon);
        this.resizeButton.setOnTouchListener(new ResizeButtonOnTouchListener());
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setParentView(View view) {
        this.parentView = view;
    }

    public void setupWithParent(ViewGroup parentView) {
        setParentView(parentView);
        resetAll();
        parentView.addView(this);
        // Disable clip so the button is visible outside of the image.
        disableClipOnParents(this);
        View parent = (View) parentView.getParent();
        parent.setOnTouchListener(new ParentOnTouchListener());
    }

    public void resetAll() {
        setDefaultSize();
        resetPosition();
    }

    public int getMargin() {
        return textViewMargin;
    }

    public void setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public void disableClipOnParents(View view) {
        if (view.getParent() == null) {
            return;
        }

        if (view instanceof ViewGroup) {
            ((ViewGroup) view).setClipChildren(false);
        }

        if (view.getParent() instanceof View) {
            disableClipOnParents((View) view.getParent());
        }
    }

    public void setDefaultSize() {
        int width = Convertor.convertDpToPixel(DEFAULT_WIDTH_DP);
        int height = Convertor.convertDpToPixel(DEFAULT_HEIGHT_DP);
        FrameLayout.LayoutParams movableTextViewLayoutParameters =
                new FrameLayout.LayoutParams(width,
                        height);
        setLayoutParams(movableTextViewLayoutParameters);
    }

    public void resetPosition() {
        Rect parentRect = getParentRectForMove();
        setX(parentRect.left);
        setY(parentRect.top);
    }

    @NonNull
    private Rect getParentRectForMove() {
        return new Rect(-textViewMargin, -textViewMargin, parentView.getWidth() + textViewMargin, parentView.getHeight() + textViewMargin);
    }

    private class TextViewFocusChangedListener implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                resizeButton.setVisibility(View.VISIBLE);
                if (selectListener != null)
                    selectListener.onSelected();
            } else {
                resizeButton.setVisibility(View.INVISIBLE);
                if (selectListener != null)
                    selectListener.onUnselected();
            }
        }
    }

    public interface SelectListener {
        void onSelected();

        void onUnselected();
    }

    private class TextViewOnTouchListener implements OnTouchListener {
        private int dX;
        private int dY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (textView.isFocusable()) {
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        view.requestFocus();
                        dX = (int) (getX() - rawX);
                        dY = (int) (getY() - rawY);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int toX = rawX + dX;
                        int toY = rawY + dY;
                        // Make sure we will still be the in parent's container
                        //View parentView = (View) MovableTextViewLayout.this.getParent();
                        Rect parent = getParentRectForMove();
                        int newLeft = toX;
                        int newTop = toY;
                        int newRight = newLeft + getWidth();
                        int newBottom = newTop + getHeight();

                        if (parent.contains(newLeft, 0, newRight, 0)) {
                            setX(toX);
                        } else if (newLeft < parent.left && getX() != parent.left) {
                            setX(parent.left);
                        } else if (newRight > parent.right && getX() != parent.right - getWidth()) {
                            setX(parent.right - getWidth());
                        }

                        if (parent.contains(0, newTop, 0, newBottom)) {
                            setY(toY);
                        } else if (newTop < parent.top && getY() != parent.top) {
                            setY(parent.top);
                        } else if (newBottom > parent.bottom && getY() != parent.bottom - getHeight()) {
                            setY(parent.bottom - getHeight());
                        }
                        break;
                }
                return true;
            } else
                return false;
        }
    }

    private class ResizeButtonOnTouchListener implements OnTouchListener {
        private int min = Convertor.convertDpToPixel(100);
        private int lastX;
        private int lastY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int X = (int) motionEvent.getRawX();
            int Y = (int) motionEvent.getRawY();
            int offsetX = lastX - X;
            int offsetY = lastY - Y;
            lastX = X;
            lastY = Y;
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int targetWidth = getLayoutParams().width - offsetX;
                    int targetHeight = getLayoutParams().height - offsetY;
                    int newRight = (int) getX() + targetWidth;
                    int newBottom = (int) getY() + targetHeight;
                    int curBottom = (int) getY() + getLayoutParams().height;
                    int curRight = (int) getX() + getLayoutParams().width;
                    Rect parent = new Rect(0, 0, parentView.getWidth() + textViewMargin, parentView.getHeight() + textViewMargin);
                    if (targetWidth > min) {
                        if (newRight < parent.right)
                            getLayoutParams().width = targetWidth;
                        else if (newRight > parent.right && curRight <= parent.right)
                            getLayoutParams().width = getLayoutParams().width + (parent.right - curRight);
                    }
                    if (targetHeight > min) {
                        if (newBottom < parent.bottom)
                            getLayoutParams().height = targetHeight;
                        else if (newBottom > parent.bottom && curBottom <= parent.bottom) {
                            getLayoutParams().height = getLayoutParams().height + (parent.bottom - curBottom);
                        }
                    }
                    requestLayout();
                    break;
            }
            return true;
        }
    }

    private class ParentOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int X = (int) event.getRawX();
            int Y = (int) event.getRawY();
            ImageView resizeButton = MovableTextViewLayout.this.resizeButton;
            // Get X and Y
            int[] loc = new int[2];
            resizeButton.getLocationOnScreen(loc);
            int resizeButtonX = loc[0];
            int resizeButtonY = loc[1];
            // Get rect
            int resizeButtonRight = resizeButtonX + resizeButton.getWidth();
            int resizeButtonBottom = resizeButtonY + resizeButton.getHeight();
            Rect rect = new Rect(resizeButtonX, resizeButtonY, resizeButtonRight, resizeButtonBottom);

            if (rect.contains(X, Y) && getTextView().isFocused()) {
                int newX = resizeButtonRight - getMargin();
                int newY = resizeButtonBottom - getMargin();
                //if (event.getAction()  == MotionEvent.ACTION_DOWN)
                event.setLocation(newX, newY);
                return resizeButton.dispatchTouchEvent(event);
            } else
                return false;
        }
    }

}
