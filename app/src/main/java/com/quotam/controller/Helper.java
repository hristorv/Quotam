package com.quotam.controller;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quotam.R;

public class Helper {

    public static void setupHelper(View rootView, int messageResource,int imageResource) {
        LinearLayout helper = rootView.findViewById(R.id.helper_layout);
        helper.setVisibility(View.VISIBLE);
        TextView messageView = helper.findViewById(R.id.helper_text);
        messageView.setText(messageResource);
        ImageView imageView = helper.findViewById(R.id.helper_image);
        imageView.setImageResource(imageResource);
    }

}
