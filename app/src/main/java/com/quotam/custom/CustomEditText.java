package com.quotam.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.utils.DrawableTint;

public class CustomEditText extends AppCompatEditText {

    private Drawable closeDrawable = getResources().getDrawable(R.drawable.ic_close_white_24dp);
    private boolean closeActive = false;
    private CharSequence hint;
    private OnActionDoneListener onActionDoneListener;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] == null)
            return super.onTouchEvent(event);

        int drawableLeftPos = getWidth() - getPaddingRight() - closeDrawable.getIntrinsicWidth();
        int drawableBottomPos = getHeight() / 2 - getPaddingBottom() - closeDrawable.getIntrinsicHeight() / 2;
        int drawableTopPos = getHeight() / 2 + getPaddingTop() + closeDrawable.getIntrinsicHeight() / 2;
        if (event.getX() > drawableLeftPos
                && event.getY() > drawableBottomPos
                && event.getY() < drawableTopPos) {
            clearText();
            closeAll();
            return false;
        }
        return super.onTouchEvent(event);

    }

    public void clearText() {
        setText("");
    }

    private void init() {
        addTextChangedListener(new MyTextWatcher());
        initParams();
        hint = getHint();
        setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    setHint("");
                else {
                    closeAll();
                    setHint(hint);
                }
            }
        });
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEARCH || id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_ACTION_SEND) {
                    closeAll();
                    if (!textView.getText().toString().trim().equals("")) {
                        if (onActionDoneListener != null)
                            onActionDoneListener.onActionDone();
                    } else
                        textView.setText("");
                }
                return false;
            }
        });
    }

    public void setOnActionDoneListener(OnActionDoneListener onActionDoneListener) {
        this.onActionDoneListener = onActionDoneListener;
    }

    public interface OnActionDoneListener {
        void onActionDone();
    }

    public void callOnActionDone() {
        if (onActionDoneListener != null)
            onActionDoneListener.onActionDone();
    }

    protected void initParams() {
    }

    public void clear() {
        clearText();
        clearFocus();
    }

    public void closeAll() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
        clearFocus();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
            clearFocus();
        return false;
    }

    private void handleButton() {
        if (this.getText().toString().equals("") && closeActive) {
            //remove clear button
            closeActive = false;
            this.setCompoundDrawablesWithIntrinsicBounds(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
            return;
        }
        if (!this.getText().toString().equals("") && !closeActive) {
            // add the clear button
            closeActive = true;
            this.setCompoundDrawablesWithIntrinsicBounds(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], closeDrawable, this.getCompoundDrawables()[3]);
            return;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            handleButton();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
