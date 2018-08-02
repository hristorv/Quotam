package com.quotam.adapters;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class ImageItemAdapter extends RecyclerView.Adapter {

    private final ImageItemListener imageItemListener;
    private final List<ImageItem> allImageItems;
    private List<ImageItem> curImageItems = new ArrayList<>();
    private RequestOptions options;

    public ImageItemAdapter(List<ImageItem> imageItems, ImageItemListener imageItemListener) {
        setImageLoaderOptions();
        this.imageItemListener = imageItemListener;
        this.curImageItems = imageItems;
        this.allImageItems = imageItems;
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder frameViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View frameView = inflater.inflate(R.layout.image_item_view, parent, false);
        frameViewHolder = new ImageItemViewHolder(frameView);
        return frameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageItemViewHolder imageItemViewHolder = (ImageItemViewHolder) holder;
        ImageItem imageItem = curImageItems.get(position);
        Context context = imageItemViewHolder.itemView.getContext();
        if (imageItem != null) {
            Glide.with(context)
                    .load(Uri.parse(imageItem.getThumbUrl()))
                    .apply(options)
                    .into(imageItemViewHolder.imageItemImage);
            imageItemViewHolder.itemView.setOnClickListener(new ImageItemOnClickListener(imageItemViewHolder));
        }
    }

    @Override
    public int getItemCount() {
        return curImageItems.size();
    }

    public void changeCategory(Context context, CharSequence changeCategory) {
        if (changeCategory.equals(context.getString(R.string.all)))
            curImageItems = allImageItems;
        else if (changeCategory.equals(context.getString(R.string.favorites))) {
            //curImageItems = ;
        } else {
            List<ImageItem> categoryImageItems = new ArrayList<>();
            for (ImageItem imageItem : allImageItems) {
                if (imageItem.getCategory().equals(changeCategory))
                    categoryImageItems.add(imageItem);
            }
            curImageItems = categoryImageItems;
        }
        notifyDataSetChanged();
    }

    private class ImageItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageItemImage;
        public View itemView;

        public ImageItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageItemImage = itemView.findViewById(R.id.image_item_image);
        }
    }

    public interface ImageItemListener {
        void onImageItemSelected(ImageItem imageItem);
    }

    private class ImageItemOnClickListener implements View.OnClickListener {

        private final ImageItemViewHolder imageItemViewHolder;

        public ImageItemOnClickListener(ImageItemViewHolder imageItemViewHolder) {
            this.imageItemViewHolder = imageItemViewHolder;
        }

        @Override
        public void onClick(View v) {
            int position = imageItemViewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                imageItemListener.onImageItemSelected(curImageItems.get(position));
            }
        }
    }

}
