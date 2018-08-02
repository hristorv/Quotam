package com.quotam.fragments.steppers;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.custom.touchimage.FastBitmapDrawable;
import com.quotam.model.ImageDefault;
import com.quotam.utils.Helper;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CreatePictureCrop extends Fragment {

    private static final String IMAGE = "com.quotam.DIALOG_IMAGE";
    private ImageDefault image;
    private ImageView imageView;
    private RequestOptions options;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDisplayOptions();
        Bundle bundle = getArguments();
        if (bundle != null)
            image = bundle.getParcelable(IMAGE);
    }

    public static CreatePictureCrop newInstance(ImageDefault image) {
        CreatePictureCrop fragment = new CreatePictureCrop();
        Bundle args = new Bundle();
        args.putParcelable(IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeDisplayOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_create_picture_dialog, container, false);
        imageView = rootView.findViewById(R.id.picture_dialog_image);
        fab = ((CreatePictureFragment) getParentFragment()).fab;

        setupFab();
        if (image.getType().equals(ImageDefault.Type.UPLOAD))
            imageView.setImageBitmap(getBitmap(image.getUri()));
        else
            Glide.with(getActivity())
                    .load(Helper.transform(image.getUrl()))
                    .apply(options)
                    .into(imageView);
        return rootView;
    }

    private Bitmap getBitmap(Uri uri) {
        Log.e("asd", "" + uri);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void setupFab() {
        changeIconWithAnimation(R.drawable.ic_close_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreatePictureFragment) getParentFragment()).setupFab();
                changeIconWithAnimation(R.drawable.ic_file_upload_white_24dp);
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void changeIconWithAnimation(int icon) {
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                fab.setImageResource(icon);
                fab.show();
            }
        });
    }

    public Bitmap getImage() {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap;
        if (drawable instanceof FastBitmapDrawable)
            bitmap = ((FastBitmapDrawable) drawable).getBitmap();
        else
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        return bitmap;
    }

}
