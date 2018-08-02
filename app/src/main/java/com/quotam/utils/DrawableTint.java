package com.quotam.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableTint {

    public static Drawable tintDrawable(Context context, Drawable drawableInput, int colorID) {
        Drawable drawable = DrawableCompat.wrap(drawableInput).mutate();
        ColorStateList colorSelector = context.getResources().getColorStateList(colorID);
        DrawableCompat.setTintList(drawable, colorSelector);
        return drawable;
    }

    public static Drawable tintDrawable(Context context, int drawableID, int colorID) {
        Drawable drawable = DrawableCompat.wrap(context.getResources().getDrawable(drawableID)).mutate();
        ColorStateList colorSelector = context.getResources().getColorStateList(colorID);
        DrawableCompat.setTintList(drawable, colorSelector);
        return drawable;
    }

}
