package com.quotam.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.controller.FiltersHelper;
import com.quotam.controller.effects.PictureEffectsController;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class FiltersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int THUMB_WIDTH = 100;
    public static final int THUMB_HEIGHT = 100;
    private final GPUImage gpuImage;
    private final PictureEffectsController pictureEffectsController;
    private final FiltersHelper filtersHelper;
    private final Bitmap thumbnail;
    private FilterList curFilters = new FilterList();
    private int selectedPosition;
    private int lastSelectedPosition;
    private FiltersHelper.FilterCategory curCategory;
    private FiltersHelper.FilterCategory selectedCategory;

    public FiltersAdapter(Context context, PictureEffectsController pictureEffectsController, Bitmap bitmap) {
        this.pictureEffectsController = pictureEffectsController;
        gpuImage = new GPUImage(context);
        gpuImage.setImage(getThumbnail(bitmap));
        filtersHelper = new FiltersHelper(context);
        thumbnail = getThumbnail(bitmap);
    }

    public List<CharSequence> getCategories() {
        return filtersHelper.getCategories();
    }

    private void processCategory(FiltersHelper.FilterCategory filterCategory) {
        AsyncTask processCategoryAsync = new ProcessCategoryAsync();
        processCategoryAsync.execute(thumbnail, filterCategory, this);
    }

    public void changeCategory(CharSequence categoryTitle) {
        FiltersHelper.FilterCategory filterCategory = filtersHelper.getCategory(categoryTitle);
        curCategory = filterCategory;
        if (selectedCategory == null)
            selectedCategory = curCategory;
        if (filterCategory.isInit()) {
            curFilters = filterCategory.getFiltersList();
            notifyDataSetChanged();
        } else {
            curFilters = new FilterList();
            notifyDataSetChanged();
            processCategory(filterCategory);
        }
    }

    private static Bitmap getThumbnail(Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_WIDTH, THUMB_HEIGHT, false);
        return scaledBitmap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View filterView = inflater.inflate(R.layout.filter_item, parent, false);
        FilterViewHolder filterViewHolder = new FilterViewHolder(filterView);
        return filterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FilterViewHolder filterViewHolder = (FilterViewHolder) holder;

        Bitmap filteredBitmap = curFilters.bitmaps.get(position);
        if (filteredBitmap != null) {
            filterViewHolder.filterImage.setImageBitmap(filteredBitmap);
            filterViewHolder.filterName.setText(curFilters.names.get(position));
            filterViewHolder.itemView.setOnClickListener(new FilterOnClickListener(filterViewHolder));
            // Select the default when the adapter is first created.
            if (position == selectedPosition && curCategory.isEqual(selectedCategory))
                setViewSelected(filterViewHolder, true);
            else
                setViewSelected(filterViewHolder, false);

        }
    }

    private void setViewSelected(FilterViewHolder filterViewHolder, boolean selected) {
        int newColorID;
        if (selected)
            newColorID = R.color.light_primary_color;
        else
            newColorID = R.color.primary_color;
        Context context = filterViewHolder.itemView.getContext();
        int toColor = context.getResources().getColor(newColorID);
        filterViewHolder.itemView.setBackgroundColor(toColor);
    }

    @Override
    public int getItemCount() {
        return curFilters.names.size();
    }

    public void selectPosition(int newSelectedPosition, FiltersHelper.FilterCategory newCategory) {
        lastSelectedPosition = selectedPosition;
        selectedPosition = newSelectedPosition;
        // Deselect last position
        notifyItemChanged(lastSelectedPosition);
        // Select new position
        notifyItemChanged(selectedPosition);
        // Set selected item category.
        if (newCategory != null)
            selectedCategory = newCategory;
        else selectedCategory = filtersHelper.getDefaultCategory();
    }

    private void changeFilter(int position) {
        GPUImageFilter selectedFilter = curFilters.filters.get(position);
        pictureEffectsController.changeFilter(selectedFilter, pictureEffectsController.getFullFilterIndex());
    }

    public boolean isAtSelectedCategory() {
        return curCategory.isEqual(selectedCategory);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public FiltersHelper.FilterCategory getSelectedCategory() {
        return selectedCategory;
    }

    public int getSelectedCategoryPosition() {
        return filtersHelper.getCategoryPosition(selectedCategory);
    }

    private static class ProcessCategoryAsync extends AsyncTask {

        @Override
        protected Object doInBackground(final Object[] objects) {
            final Bitmap bitmap = (Bitmap) objects[0];
            final FiltersHelper.FilterCategory filterCategory = (FiltersHelper.FilterCategory) objects[1];
            final FiltersAdapter adapter = (FiltersAdapter) objects[2];

            filterCategory.init();
            FilterList filterList = filterCategory.getFiltersList();
            publishProgress(filterList, adapter);
            GPUImage.getBitmapForMultipleFilters(bitmap, filterList.filters, new GPUImage.ResponseListener<Bitmap>() {
                int position = 0;

                @Override
                public void response(final Bitmap bitmap) {
                    publishProgress(bitmap, filterList, adapter, position);
                    position++;
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] objects) {
            // Check if we need to update filters or bitmaps.
            if (objects.length == 2) {
                // Update filters list
                FilterList filterList = (FilterList) objects[0];
                FiltersAdapter adapter = (FiltersAdapter) objects[1];
                adapter.curFilters = filterList;
            } else {
                // Update bitmaps
                Bitmap bitmap = (Bitmap) objects[0];
                FilterList filterList = (FilterList) objects[1];
                FiltersAdapter adapter = (FiltersAdapter) objects[2];
                int position = (int) objects[3];

                filterList.bitmaps.set(position, bitmap);
                adapter.notifyItemChanged(position);
            }
        }
    }

    private class FilterOnClickListener implements View.OnClickListener {
        private final FilterViewHolder filterViewHolder;

        public FilterOnClickListener(FilterViewHolder filterViewHolder) {
            this.filterViewHolder = filterViewHolder;
        }

        @Override
        public void onClick(final View view) {
            int position = filterViewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Set new selected.
                if (position != selectedPosition)
                    selectPosition(position, curCategory);
                // Change filter.
                changeFilter(position);
            }
        }
    }

    public static class FilterList {
        public List<String> names = new LinkedList<>();
        public List<GPUImageFilter> filters = new LinkedList<>();
        public List<Bitmap> bitmaps = new LinkedList<>();

        public void addFilter(final String name, final GPUImageFilter filter) {
            names.add(name);
            filters.add(filter);
            bitmaps.add(null);
        }

        public boolean hasEmptyBitmaps() {
            if (bitmaps.size() > 0) {
                Bitmap firstBitmap = bitmaps.get(0);
                if (firstBitmap == null)
                    return true;
            }
            return false;
        }
    }

    private class FilterViewHolder extends RecyclerView.ViewHolder {
        public ImageView filterImage;
        public TextView filterName;
        public View itemView;

        public FilterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            filterImage = itemView.findViewById(R.id.filter_image);
            filterName = itemView.findViewById(R.id.filter_name);
        }
    }

}
