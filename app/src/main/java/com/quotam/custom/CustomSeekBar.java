package com.quotam.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.quotam.R;
import com.quotam.utils.Convertor;

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    private Rect rect;
    private Paint paint;
    private int seekbar_height_default;
    private int seekbar_height_progress;
    private int defaultProgress = 0;

    public CustomSeekBar(Context context) {
        super(context);
        init();
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        rect = new Rect();
        paint = new Paint();
        seekbar_height_default = Convertor.convertDpToPixel(2);
        seekbar_height_progress = Convertor.convertDpToPixel(3);
        this.setWillNotDraw(false);
    }

    public void setDefaultProgress(int defaultProgress) {
        if (defaultProgress >= 0)
            this.defaultProgress = defaultProgress;
    }

    public void resetProgress() {
        setProgress(this.defaultProgress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int topProgress = getHeight() / 2 - (seekbar_height_progress / 2);
        int bottomProgress = getHeight() / 2 + (seekbar_height_progress / 2);
        int topDefault = getHeight() / 2 - (seekbar_height_default / 2);
        int bottomDefault = getHeight() / 2 + (seekbar_height_default / 2);

        rect.set(getThumbOffset(), topDefault, getWidth() - getThumbOffset(), bottomDefault);
        paint.setColor(getContext().getResources().getColor(R.color.white));
        canvas.drawRect(rect, paint);

        float maxProgress = getMax();
        float width = getWidth() - 2 * getThumbOffset();
        float widthStart = getThumbOffset();
        if (defaultProgress != 0)
            widthStart += width / (maxProgress / defaultProgress);

        if (this.getProgress() > defaultProgress) {
            int left = (int) widthStart;
            int right = (int) (widthStart + (width / maxProgress) * (getProgress() - defaultProgress));
            rect.set(left, topProgress, right, bottomProgress);
            paint.setColor(getContext().getResources().getColor(R.color.accent_color));
            canvas.drawRect(rect, paint);
        }
        if (this.getProgress() < defaultProgress) {
            int left = (int) (widthStart - ((width / maxProgress) * (defaultProgress - getProgress())));
            int right = (int) widthStart;
            rect.set(left, topProgress, right, bottomProgress);
            paint.setColor(getContext().getResources().getColor(R.color.accent_color));
            canvas.drawRect(rect, paint);
        }
        super.onDraw(canvas);
    }
}