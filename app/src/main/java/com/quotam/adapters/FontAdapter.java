package com.quotam.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.model.Font;
import com.quotam.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class FontAdapter extends RecyclerView.Adapter {

    private final FontListner fontListener;
    List<Font> fonts = new ArrayList<>();
    private RequestOptions options;

    public FontAdapter(FontListner fontListener) {
        this.fontListener = fontListener;
        setImageLoaderOptions();
        fonts = new Items().getFonts();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder fontViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View fontView = inflater.inflate(R.layout.font_item, parent, false);
        fontViewHolder = new FontViewHolder(fontView);
        return fontViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FontViewHolder filterViewHolder = (FontViewHolder) holder;
        Font font = fonts.get(position);
        Context context = filterViewHolder.itemView.getContext();
        if (font!=null) {
            Glide.with(context)
                    .load(Helper.transform(font.getUrl()))
                    .apply(options)
                    .into(filterViewHolder.filterImage);
            filterViewHolder.itemView.setOnClickListener(new FontOnClickListener(filterViewHolder));
        }
    }

    @Override
    public int getItemCount() {
        return fonts.size();
    }

    private class FontViewHolder extends RecyclerView.ViewHolder {
        public ImageView filterImage;
        public View itemView;

        public FontViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            filterImage = itemView.findViewById(R.id.font_image);
        }
    }

    public interface FontListner {
        void onFontReady(Font font);
    }

    private class FontOnClickListener implements View.OnClickListener {

        private final FontViewHolder filterViewHolder;

        public FontOnClickListener(FontViewHolder filterViewHolder) {
            this.filterViewHolder = filterViewHolder;
        }

        @Override
        public void onClick(View v) {
            int position = filterViewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                fontListener.onFontReady(fonts.get(position));
            }
        }
    }
}
