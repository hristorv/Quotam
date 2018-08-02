package com.quotam.adapters;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.model.Image;
import com.quotam.utils.Helper;
import com.quotam.custom.CustomViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    List<Image> images;
    private LayoutInflater inflater;
    RequestOptions options;
    CustomViewPager viewPager;
    private View mCurrentView;

    public ImagePagerAdapter(List<Image> images, CustomViewPager viewPager) {
        inflater = LayoutInflater.from(viewPager.getContext());
        this.viewPager = viewPager;
        this.images = images;
        initializeDisplayOptions(viewPager.getContext());
    }

    private void initializeDisplayOptions(Context context) {
        options = new RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public int getCount() {
        if (images != null)
            return images.size();
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.fragment_image_fullscreen,
                view, false);
        ImageView imageView = imageLayout
                .findViewById(R.id.image_hq);
        final ProgressBar spinner = imageLayout
                .findViewById(R.id.loading);
        getImage(position, imageView, spinner);
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    private void getImage(final int position, ImageView imageView,
                          final ProgressBar spinner) {
        Glide.with(imageView.getContext())
                .load(Helper.transform(getHQimageURL(position)))
                .apply(options)
                .into(imageView);
    }

    private String getHQimageURL(int position) {
        //StringBuilder hq_url = new StringBuilder(images.get(position).getUrl());
        //hq_url.delete(hq_url.length() - 7, hq_url.length() - 4);
        //return hq_url.toString();
        return images.get(position).getUrl();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View) object;
        super.setPrimaryItem(container, position, object);
    }

    public Bitmap getCurrentBitmap() {
        ImageView currentImageView = mCurrentView
                .findViewById(R.id.image_hq);
        BitmapDrawable bd = (BitmapDrawable) currentImageView
                .getDrawable();
        if (bd == null)
            return null;
        Bitmap bmp = bd.getBitmap();
        return bmp;
    }

    public View getBaseView() {
        return mCurrentView;
    }
}
