package com.quotam.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.quotam.R;

public class ImageDialog {

    public void showDialog(Context context, int imageUrl) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_image)
                .setCancelable(true)
                .create();
        dialog.show();

        ImageView imageView = dialog.findViewById(R.id.dialog_image_view);
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
