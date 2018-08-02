package com.quotam.adapters;

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
import com.quotam.model.Album;
import com.quotam.model.Category;
import com.quotam.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class CompactItemAdapter extends InnerSectionAdapter {

    public final int ALBUM = 0;
    public final int CATEGORY = 1;
    private CompactOnItemClickListener onItemClickListner;
    RequestOptions options;
    private List<Object> items;
    ManagerType managerType = ManagerType.GRID;

    public enum ManagerType {
        LINEAR,
        GRID
    }

    public CompactItemAdapter(ManagerType managerType) {
        this.managerType = managerType;
        this.items = new ArrayList<Object>();
        setImageLoaderOptions();
    }

    public CompactItemAdapter(List<Object> items, ManagerType managerType) {
        this.managerType = managerType;
        this.items = items;
        setImageLoaderOptions();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }

    @Override
    public void addItems(List<Object> addedItems) {
        int previousItemsCount = items.size();
        items.addAll(addedItems);
        notifyItemRangeInserted(previousItemsCount, addedItems.size());
    }

    @Override
    public void changeItems(List<Object> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    public List<Object> getItems() {
        return items;
    }

    public void addItem(Object item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(Object item) {
        notifyItemRemoved(items.indexOf(item));
        items.remove(item);
    }

    public boolean isAlreadyAdded(Object item) {
        if (items.contains(item))
            return true;
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View compactView = inflater.inflate(R.layout.compact_grid_item, parent, false);
        adjustWidth(compactView);
        CompactViewHolder viewHolder = new CompactViewHolder(compactView);
        return viewHolder;
    }

    private void adjustWidth(View compactView) {
        if (managerType == ManagerType.LINEAR) {
            ViewGroup.LayoutParams params = compactView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            compactView.setLayoutParams(params);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CompactViewHolder compactViewHolder = (CompactViewHolder) holder;
        switch (holder.getItemViewType()) {
            case ALBUM:
                configureAlbumViewHolder(compactViewHolder, position);
                compactViewHolder.itemView.setOnClickListener(new OnClickListener(compactViewHolder));
                break;
            case CATEGORY:
                configureCategoryViewHolder(compactViewHolder, position);
                compactViewHolder.itemView.setOnClickListener(new OnClickListener(compactViewHolder));
                break;
        }
    }

    private void configureCategoryViewHolder(CompactViewHolder compactViewHolder, int position) {
        final Category category = (Category) items.get(position);
        if (category != null) {
            Glide.with(compactViewHolder.itemView.getContext())
                    .load(Helper.transform(category.getBackgroundUrl()))
                    .apply(options)
                    .into(compactViewHolder.imageView);
            compactViewHolder.titleBar.setText(category.getName());
        }
    }

    private void configureAlbumViewHolder(CompactViewHolder compactViewHolder, int position) {
        final Album album = (Album) items.get(position);
        if (album != null) {
            Glide.with(compactViewHolder.itemView.getContext())
                    .load(Helper.transform(album.getBackgroundUrl()))
                    .apply(options)
                    .into(compactViewHolder.imageView);
            compactViewHolder.titleBar.setText(album.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Album)
            return ALBUM;
        else if (item instanceof Category)
            return CATEGORY;
        return -1;

    }

    public void setOnItemClickListener(CompactOnItemClickListener onItemClickListener) {
        this.onItemClickListner = onItemClickListener;
    }

    public interface CompactOnItemClickListener {
        void onItemClick(Object item);
    }

    private class OnClickListener implements View.OnClickListener {


        private final CompactViewHolder holder;

        public OnClickListener(CompactViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListner != null)
                onItemClickListner.onItemClick(items.get(position));
        }
    }

    private class CompactViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleBar;
        public View itemView;

        public CompactViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.compact_item_background);
            titleBar = itemView.findViewById(R.id.compact_item_title);
        }
    }
}
