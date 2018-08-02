package com.quotam.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.utils.Convertor;
import com.quotam.utils.DrawableTint;

public class CustomSearchView extends CustomEditText {

    private Drawable closeDrawable = getResources().getDrawable(R.drawable.ic_close_white_24dp);
    private boolean closeActive = false;

    public CustomSearchView(Context context) {
        super(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        setParams();
        setIcon();
    }

    private void setParams() {
        setBackgroundColor(getContext().getResources().getColor(R.color.light_primary_color));
        setCompoundDrawablePadding(Convertor.convertDpToPixel(10));
        setHint(R.string.search_hint);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        setInputType(InputType.TYPE_CLASS_TEXT);
        setLongClickable(false);
        setMaxLines(1);
        setPadding(Convertor.convertDpToPixel(10), 0, Convertor.convertDpToPixel(10), 0);
        setTextColor(getContext().getResources().getColor(R.color.search_text));
        setHighlightColor(getContext().getResources().getColor(R.color.search_text));
        setHintTextColor(getContext().getResources().getColor(R.color.search_text));
        setTextIsSelectable(false);
        setTextSize(20);
        //setCursorVisible(false);
        //android:textSelectHandle="@drawable/empty_drawable"
    }

    private void setIcon() {
        Drawable drawableSearch = DrawableTint.tintDrawable(getContext(), R.drawable.ic_search_white_24dp, R.color.search_text);
        this.setCompoundDrawablesWithIntrinsicBounds(drawableSearch, null, null, null);
    }

}

