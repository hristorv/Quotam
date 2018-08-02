package com.quotam.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.model.ImageDefault;
import com.quotam.utils.Helper;

import java.util.List;

public class ImageDefaultAdapter extends RecyclerView.Adapter {

    private final List<ImageDefault> items;
    private RequestOptions options;
    private DefaultImageOnItemClickListener onItemClickListner;

    public ImageDefaultAdapter(List<ImageDefault> items) {
        this.items = items;
        setImageLoaderOptions();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.default_image_item, parent, false);
        DefaultImageViewHolder viewHolder = new DefaultImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DefaultImageViewHolder defaultImageViewHolder = (DefaultImageViewHolder) holder;
        final ImageDefault imageDefault = items.get(position);
        if (imageDefault != null) {
            Glide.with(defaultImageViewHolder.itemView.getContext())
                    .load(Helper.transform(imageDefault.getUrl()))
                    .apply(options)
                    .into(defaultImageViewHolder.imageView);
        }
        defaultImageViewHolder.itemView.setOnClickListener(new OnClickListener(defaultImageViewHolder));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(DefaultImageOnItemClickListener onItemClickListener) {
        this.onItemClickListner = onItemClickListener;
    }

    public interface DefaultImageOnItemClickListener {
        void onItemClick(ImageDefault item);
    }

    private class OnClickListener implements View.OnClickListener {


        private final DefaultImageViewHolder holder;

        public OnClickListener(DefaultImageViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListner != null)
                onItemClickListner.onItemClick(items.get(position));
        }
    }

    private class DefaultImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public View itemView;

        public DefaultImageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.picture_imageview);
        }
    }

}
