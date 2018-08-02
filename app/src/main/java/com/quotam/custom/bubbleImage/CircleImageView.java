package com.quotam.custom.bubbleImage;

import android.content.Context;
import android.util.AttributeSet;

public class CircleImageView extends ShaderImageView {

    private CircleShader shader;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new CircleShader();
        return shader;
    }

    public float getBorderRadius() {
        if (shader != null) {
            return shader.getBorderRadius();
        }
        return 0;
    }

    public void setBorderRadius(final float borderRadius) {
        if (shader != null) {
            shader.setBorderRadius(borderRadius);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Fix for the smaller image on profile bug. Sets the minimum and maximum heigh and width after they are computed from the percetage layout.
        setMinimumHeight(getHeight());
        setMinimumWidth(getWidth());
        setMaxHeight(getHeight());
        setMaxWidth(getWidth());
    }
}