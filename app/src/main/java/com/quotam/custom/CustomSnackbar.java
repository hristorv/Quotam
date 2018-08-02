package com.quotam.custom;


import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.quotam.R;

public class CustomSnackbar {

    public static Snackbar create (Context context, View view, int message, int duration) {
        Snackbar snackbar = Snackbar.make(view,message,duration);
        changeColors(context, snackbar);
        return snackbar;
    }

    public static Snackbar create (Context context, View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view,message,duration);
        changeColors(context, snackbar);
        return snackbar;
    }

    private static void changeColors(Context context, Snackbar snackbar) {
        snackbar.setActionTextColor(context.getResources().getColor(R.color.accent_color));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.dark_primary_color));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.primary_text_color));
        //Typeface font = Typeface.createFromAsset(context.getAssets(), "coming_soon.ttf");
        //textView.setTypeface(font,Typeface.BOLD);
    }

}
