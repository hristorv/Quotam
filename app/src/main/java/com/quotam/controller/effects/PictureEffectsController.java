package com.quotam.controller.effects;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.quotam.R;
import com.quotam.adapters.FiltersAdapter;
import com.quotam.adapters.ImageItemAdapter;
import com.quotam.adapters.Items;
import com.quotam.controller.FilterAdjuster;
import com.quotam.controller.FiltersHelper;
import com.quotam.custom.CustomBottomBar;
import com.quotam.custom.CustomSeekBar;
import com.quotam.fragments.steppers.CreateImageEffects;
import com.quotam.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;

public class PictureEffectsController implements EffectsController {


    private final Context context;
    private final CreateImageEffects fragment;
    private final LayoutInflater layoutInflater;
    private final CustomBottomBar bottomBar;
    public boolean resetFiltersAdapter;
    private FiltersAdapter filtersAdapter;
    private FilterAdjuster filterAdjuster;
    private CustomSeekBar seekBar;
    private ArrayList<FilterAdjuster> adjusters;
    private ArrayList<GPUImageFilter> filters;
    private int[] rawDimens = {480, 540, 600, 720, 800, 900, 1080, 1280, 1440, 1600, 1920};
    private ArrayList<Size> sizes;
    private Size defaultSize;
    ///////////////////////////////////////
    private FiltersProgress filtersProgress;
    private AdjustProgress adjusterProgress;
    private CropProgress cropProgress;
    private ResizeProgress resizeProgress;
    private OverlayProgress overlayProgress;

    public PictureEffectsController(CreateImageEffects fragment, CustomBottomBar bottomBar) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.bottomBar = bottomBar;
        this.layoutInflater = fragment.getLayoutInflater();
        initFiltersList();
        initProgress();
    }

    private void initProgress() {
        filtersProgress = new FiltersProgress();
        adjusterProgress = new AdjustProgress();
        cropProgress = new CropProgress();
        resizeProgress = new ResizeProgress();
        overlayProgress = new OverlayProgress();
    }

    @Override
    public void addMenu() {
        bottomBar.addMenu(R.menu.create_effects_menu_image, context, true, CustomBottomBar.Mode.NORMAL,
                new CustomBottomBar.OnMenuClickListener() {
                    @Override
                    public void onMenuItemClick(MenuItem item) {
                        CharSequence title = item.getTitle();
                        BottomSheetController bottomSheet = null;
                        switch (item.getItemId()) {
                            case R.id.create_effects_filters:
                                bottomSheet = new FiltersBottomSheet();
                                break;
                            case R.id.create_effects_adjust:
                                bottomSheet = new AdjustBottomSheet();
                                break;
                            case R.id.create_effects_crop:
                                bottomSheet = new CropBottomSheet();
                                break;
                            case R.id.create_effects_resize:
                                bottomSheet = new ResizeBottomSheet();
                                break;
                            case R.id.create_effects_overlay:
                                bottomSheet = new OverlayBottomSheet();
                                break;
                        }
                        if (bottomSheet != null)
                            bottomSheet.show(fragment, title);
                    }
                });
    }

    public ArrayList<GPUImageFilter> getFilterList() {
        GPUImageFilterGroup filterGroup = (GPUImageFilterGroup) fragment.getGPUImageView().getFilter();
        ArrayList<GPUImageFilter> filters = (ArrayList) filterGroup.getFilters();
        return filters;
    }

    public void setFiltersList(List<GPUImageFilter> filterList) {
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup(filterList);
        fragment.getGPUImageView().setFilter(filterGroup);
    }

    public void resetAdjust(boolean resetAdjust) {
        if (resetAdjust)
            initFiltersList();
        else {
            setFiltersList(filters);
        }
    }

    public void initFiltersList() {
        filters = new ArrayList<>();
        // Add the adjust filters
        filters.add(new GPUImageGaussianBlurFilter());
        filters.add(new GPUImageBrightnessFilter());
        filters.add(new GPUImageHazeFilter());
        filters.add(new GPUImageExposureFilter());
        filters.add(new GPUImageContrastFilter());
        filters.add(new GPUImageSaturationFilter());
        filters.add(new GPUImagePixelationFilter());
        filters.add(new GPUImageGammaFilter());
        filters.add(new GPUImageHueFilter());
        filters.add(new GPUImageSharpenFilter());
        filters.add(new GPUImageEmbossFilter());
        filters.add(new GPUImageHighlightShadowFilter());

        // Setup the adjusters
        adjusters = new ArrayList<>();
        for (GPUImageFilter filter : filters) {
            adjusters.add(new FilterAdjuster(filter));
        }
        // Add the blank first filter for the full filters.
        filters.add(getFullFilterIndex(), new GPUImageFilter());
        // Add the overlay filter before last position.
        filters.add(new GPUImageFilter());
        // Add the frame filter at the last position.
        filters.add(new GPUImageFilter());

        setFiltersList(filters);
    }

    public int getFullFilterIndex() {
        // 0 indicates the first position in the filter list.
        return 0;
    }

    private int getOverlaysFilterIndex() {
        return filters.size() - 2;
    }

    private int getFramesFilterIndex() {
        return filters.size() - 1;
    }

    public void changeFilter(GPUImageFilter filter, int index) {
        ArrayList<GPUImageFilter> filterList = getFilterList();
        filterList.set(index, filter);
        setFiltersList(filterList);
    }

    private void hideImageCrop() {
        fragment.imageCrop.setVisibility(View.GONE);
        fragment.imageRoot.setVisibility(View.VISIBLE);
    }

    private void showImageCrop() {
        int width = fragment.image.getWidth();
        int height = fragment.image.getHeight();
        ViewGroup.LayoutParams imageCropParams = fragment.imageCrop.getLayoutParams();
        imageCropParams.height = height;
        imageCropParams.width = width;
        fragment.imageCrop.setLayoutParams(imageCropParams);
        fragment.imageCrop.setImageBitmap(fragment.getDefaultBitmap());
        fragment.imageCrop.setVisibility(View.VISIBLE);
        fragment.imageRoot.setVisibility(View.GONE);
    }

    public ArrayList<Size> setupSizes() {
        sizes = new ArrayList<>();
        // Get aspect ratio.
        int width = fragment.getDefaultBitmap().getWidth();
        int height = fragment.getDefaultBitmap().getHeight();
        Log.e("asd", "width: " + width + "   height: " + height);
        float aspectRatio;
        if (width > height)
            aspectRatio = (float) width / (float) height;
        else
            aspectRatio = (float) height / (float) width;
        Log.e("asd", "aspect: " + aspectRatio);
        // Calculate and add sizes.
        for (int dimen : rawDimens) {
            int widthCalc;
            int heightCalc;
            if (width > height) {
                widthCalc = dimen;
                heightCalc = (int) (dimen / aspectRatio);
            } else {
                heightCalc = dimen;
                widthCalc = (int) (dimen / aspectRatio);
            }
            sizes.add(new Size(widthCalc, heightCalc));
        }
        // Add the default size.
        defaultSize = new Size(width, height);
        Size firstSize = sizes.get(0);
        Size lastSize = sizes.get(sizes.size() - 1);
        // If its smaller than the smallest size, resize it.
        if (defaultSize.isSmaller(firstSize))
            defaultSize = firstSize;
        // Or larger than the largest size, resize it.
        if (!defaultSize.isSmaller(lastSize))
            defaultSize = lastSize;
        // Check if the size is already in the array.
        for (int i = 0; i < sizes.size(); i++) {
            Size curSize = sizes.get(i);
            if (defaultSize.isSame(curSize)) {
                defaultSize = curSize;
                break;
            }
            if (defaultSize.isSmaller(curSize)) {
                sizes.add(i, defaultSize);
                break;
            }
        }
        resizeProgress = new ResizeProgress(defaultSize);
        return sizes;
    }

    public Size getCurSize() {
        return this.resizeProgress.curSize;
    }

    private class FiltersBottomSheet extends BottomSheetController {

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            LinearLayout filtersLayout = (LinearLayout) layoutInflater.inflate(R.layout.create_effects_filters, null);
            // Setup recyclerview for filter items.
            RecyclerView recyclerView = filtersLayout.findViewById(R.id.create_effects_filters_recyclerview);
            // Disable blinking when notifyitemchanged() is called.
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            // Setup adapter.
            setupAdapter(context);
            recyclerView.setAdapter(filtersAdapter);
            // Setup categories.
            CustomBottomBar categoriesBar = filtersLayout.findViewById(R.id.create_effects_filters_categories_bar);
            categoriesBar.addMenu(filtersAdapter.getCategories(), context, true, CustomBottomBar.Mode.SINGLE, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    changeCategory(item, layoutManager);
                }
            });
            // Select previous category
            categoriesBar.setSelectedItem(filtersAdapter.getSelectedCategoryPosition());
            return filtersLayout;
        }

        private void changeCategory(MenuItem item, LinearLayoutManager layoutManager) {
            if (filtersAdapter != null) {
                filtersAdapter.changeCategory(item.getTitle());
                // Set proper scroll position.
                if (filtersAdapter.isAtSelectedCategory()) {
                    layoutManager.scrollToPositionWithOffset(filtersAdapter.getSelectedPosition(), 0);
                } else {
                    layoutManager.scrollToPosition(0);
                }
            }
        }

        private void setupAdapter(Context context) {
            if (resetFiltersAdapter || filtersAdapter == null) {
                filtersAdapter = new FiltersAdapter(context, PictureEffectsController.this, fragment.getDefaultBitmap());
                resetFiltersAdapter = false;
            }
        }

        @Override
        public void onDone() {
            fragment.addProgress(filtersProgress);
            filtersProgress = new FiltersProgress(getFilterList().get(getFullFilterIndex()), filtersAdapter.getSelectedPosition(), filtersAdapter.getSelectedCategory());
        }

        @Override
        public void onClose() {
            setFilter(filtersProgress.curFilter, filtersProgress.curSelectedPosition, filtersProgress.curSelectedCategory);
        }

        private void setFilter(GPUImageFilter curFilter, int curSelectedPosition, FiltersHelper.FilterCategory curSelectedCategory) {
            changeFilter(curFilter, getFullFilterIndex());
            filtersAdapter.selectPosition(curSelectedPosition, curSelectedCategory);
        }
    }

    private class FiltersProgress implements Progress {

        private final GPUImageFilter curFilter;
        private final int curSelectedPosition;
        private final FiltersHelper.FilterCategory curSelectedCategory;

        public FiltersProgress() {
            curFilter = new GPUImageFilter();
            curSelectedPosition = 0;
            curSelectedCategory = null;
        }

        public FiltersProgress(GPUImageFilter curFilter, int curSelectedPosition, FiltersHelper.FilterCategory curSelectedCategory) {
            this.curFilter = curFilter;
            this.curSelectedPosition = curSelectedPosition;
            this.curSelectedCategory = curSelectedCategory;
        }

        private void setFilter(GPUImageFilter curFilter, int curSelectedPosition, FiltersHelper.FilterCategory curSelectedCategory) {
            changeFilter(curFilter, getFullFilterIndex());
            filtersAdapter.selectPosition(curSelectedPosition, curSelectedCategory);
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = filtersProgress;
            filtersProgress = this;
            setFilter(curFilter, curSelectedPosition, curSelectedCategory);
            return previousProgress;
        }
    }

    private class AdjustBottomSheet extends BottomSheetController {

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            RelativeLayout adjustBar = (RelativeLayout) layoutInflater.inflate(R.layout.create_effects_adjust_bar, null);
            ImageButton refreshButton = adjustBar.findViewById(R.id.adjust_bar_refresh);
            seekBar = adjustBar.findViewById(R.id.adjust_bar_slider);
            CustomBottomBar bottomBar = adjustBar.findViewById(R.id.adjust_bar_custombar);
            bottomBar.addMenu(R.menu.create_effects_adjust, context, true, CustomBottomBar.Mode.SINGLE, new AdjustBarMenuItemClickListener());
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int defaultStart = filterAdjuster.getDefault();
                    seekBar.setProgress(defaultStart);
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    adjustOnProgressChanged(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            return adjustBar;
        }

        private void adjustOnProgressChanged(int progress) {
            if (filterAdjuster != null)
                filterAdjuster.adjust(progress);
            fragment.getGPUImageView().requestRender();
        }

        @Override
        public void onDone() {
            fragment.addProgress(adjusterProgress);
            adjusterProgress = new AdjustProgress();
        }

        @Override
        public void onClose() {
            adjusterProgress.restore();
        }

    }

    private class AdjustProgress implements Progress {

        private final ArrayList<Integer> progressList;

        public AdjustProgress() {
            progressList = save();
        }

        private ArrayList<Integer> save() {
            ArrayList<Integer> previousProgress = new ArrayList<>();
            for (FilterAdjuster adjuster : adjusters)
                previousProgress.add(adjuster.getProgress());
            return previousProgress;
        }

        private void restore() {
            for (int i = 0; i < adjusters.size(); i++)
                adjusters.get(i).adjust(progressList.get(i));
            fragment.getGPUImageView().requestRender();
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = adjusterProgress;
            adjusterProgress = this;
            restore();
            return previousProgress;
        }
    }

    private class AdjustBarMenuItemClickListener implements CustomBottomBar.OnMenuClickListener {
        @Override
        public void onMenuItemClick(MenuItem item) {
            Class<?> filterClass = null;
            switch (item.getItemId()) {
                case R.id.adjust_blur:
                    filterClass = GPUImageGaussianBlurFilter.class;
                    break;
                case R.id.adjust_brightness:
                    filterClass = GPUImageBrightnessFilter.class;
                    break;
                case R.id.adjust_haze:
                    filterClass = GPUImageHazeFilter.class;
                    break;
                case R.id.adjust_exposure:
                    filterClass = GPUImageExposureFilter.class;
                    break;
                case R.id.adjust_contrast:
                    filterClass = GPUImageContrastFilter.class;
                    break;
                case R.id.adjust_saturation:
                    filterClass = GPUImageSaturationFilter.class;
                    break;
                case R.id.adjust_pixelation:
                    filterClass = GPUImagePixelationFilter.class;
                    break;
                case R.id.adjust_gamma:
                    filterClass = GPUImageGammaFilter.class;
                    break;
                case R.id.adjust_hue:
                    filterClass = GPUImageHueFilter.class;
                    break;
                case R.id.adjust_sharpness:
                    filterClass = GPUImageSharpenFilter.class;
                    break;
                case R.id.adjust_emboss:
                    filterClass = GPUImageEmbossFilter.class;
                    break;
                case R.id.adjust_shadow:
                    filterClass = GPUImageHighlightShadowFilter.class;
                    break;

            }
            filterAdjuster = getAdjuster(filterClass);
            if (seekBar != null) {
                seekBar.setDefaultProgress(filterAdjuster.getDefault());
                seekBar.setProgress(filterAdjuster.getProgress());
                seekBar.invalidate();
            }
        }

        private FilterAdjuster getAdjuster(Class<?> filterClass) {
            for (FilterAdjuster curAdjuster : adjusters) {
                if (filterClass.isInstance(curAdjuster.getFilter())) {
                    return curAdjuster;
                }
            }
            return null;
        }

    }

    private class CropBottomSheet extends BottomSheetController {

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            View cropView = layoutInflater.inflate(R.layout.create_effects_crop, null);
            CustomBottomBar cropBar = cropView.findViewById(R.id.create_effects_crop_bar);
            cropBar.addMenu(R.menu.aspect_menu, context, true, CustomBottomBar.Mode.SINGLE, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.aspect_bar_1x1:
                            setAspect(1, 1);
                            break;
                        case R.id.aspect_bar_3x2:
                            setAspect(3, 2);
                            break;
                        case R.id.aspect_bar_4x3:
                            setAspect(4, 3);
                            break;
                        case R.id.aspect_bar_5x4:
                            setAspect(5, 4);
                            break;
                        case R.id.aspect_bar_16x9:
                            setAspect(16, 9);
                            break;
                        case R.id.aspect_bar_16x10:
                            setAspect(16, 10);
                            break;
                        case R.id.aspect_bar_10x16:
                            setAspect(10, 16);
                            break;
                        case R.id.aspect_bar_9x16:
                            setAspect(9, 16);
                            break;
                        case R.id.aspect_bar_4x5:
                            setAspect(4, 5);
                            break;
                        case R.id.aspect_bar_3x4:
                            setAspect(3, 4);
                            break;
                        case R.id.aspect_bar_2x3:
                            setAspect(2, 3);
                            break;
                    }
                }
            });
            cropBar.setTextSize(20);

            return cropView;
        }

        @Override
        protected void onShow() {
            showImageCrop();
        }

        private void setAspect(int X, int Y) {
            fragment.imageCrop.setAspectRatio(X, Y);
        }

        @Override
        public void onDone() {
            if (cropProgress.curBitmap == null)
                cropProgress.curBitmap = fragment.getDefaultBitmap();
            fragment.addProgress(cropProgress);
            Bitmap croppedBitmap = fragment.imageCrop.getCroppedImage();
            fragment.changeImage(croppedBitmap, false);
            cropProgress = new CropProgress(croppedBitmap);
            hideImageCrop();
        }

        @Override
        public void onClose() {


            hideImageCrop();
        }
    }

    private class CropProgress implements Progress {

        private Bitmap curBitmap;

        public CropProgress() {
            this.curBitmap = fragment.getDefaultBitmap();
        }

        public CropProgress(Bitmap bitmap) {
            curBitmap = bitmap;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = cropProgress;
            cropProgress = this;
            fragment.changeImage(curBitmap, false);
            return previousProgress;
        }
    }

    private class ResizeBottomSheet extends BottomSheetController {

        private CustomSeekBar resizeSeekBar;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            View resizeView = layoutInflater.inflate(R.layout.create_effects_resize, null);
            ImageButton refreshButton = resizeView.findViewById(R.id.resize_refresh);
            AppCompatTextView widthText = resizeView.findViewById(R.id.resize_text_width);
            AppCompatTextView heightText = resizeView.findViewById(R.id.resize_text_height);
            AppCompatTextView defaultText = resizeView.findViewById(R.id.resize_text_default);
            resizeSeekBar = resizeView.findViewById(R.id.resize_slider);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resizeSeekBar.resetProgress();
                }
            });
            resizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Size curSize = sizes.get(progress);
                    widthText.setText(curSize.widthString);
                    heightText.setText(curSize.heightString);
                    if (curSize.equals(defaultSize))
                        defaultText.setVisibility(View.VISIBLE);
                    else
                        defaultText.setVisibility(View.GONE);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            resizeSeekBar.setMax(sizes.size() - 1);
            int progress = getProgressFromSize(resizeProgress.curSize);
            resizeSeekBar.setDefaultProgress(getProgressFromSize(defaultSize));
            resizeSeekBar.setProgress(progress);
            resizeSeekBar.invalidate();
            return resizeView;
        }

        private int getProgressFromSize(Size size) {
            return sizes.indexOf(size);
        }

        @Override
        public void onDone() {
            fragment.addProgress(resizeProgress);
            resizeProgress = new ResizeProgress(sizes.get(resizeSeekBar.getProgress()));
        }

        @Override
        public void onClose() {

        }
    }

    private class ResizeProgress implements Progress {

        private final Size curSize;

        public ResizeProgress() {
            this.curSize = null;
        }

        public ResizeProgress(Size newSize) {
            this.curSize = newSize;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = resizeProgress;
            resizeProgress = this;
            return previousProgress;
        }
    }

    public class Size {
        String widthString;
        String heightString;
        int width;
        int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
            this.widthString = String.valueOf(width);
            this.heightString = String.valueOf(height);
        }

        public Bitmap getResized(Bitmap origBitmap) {
            return Bitmap.createScaledBitmap(origBitmap, this.width,
                    this.height, true);
        }

        public boolean isSame(Size size) {
            if (width > height) {
                if (width == size.width)
                    return true;
                else return false;
            } else {
                if (height == size.height)
                    return true;
                else return false;
            }
        }

        public boolean isSmaller(Size compareSize) {
            if (width > height) {
                if (width < compareSize.width)
                    return true;
                else
                    return false;
            } else {
                if (height < compareSize.height)
                    return true;
                else
                    return false;
            }
        }
    }

    private class OverlayBottomSheet extends BottomSheetController {

        private GPUImageAddBlendFilter curOverlayFilter;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            View imageItemView = layoutInflater.inflate(R.layout.create_effects_image_item, null);
            CustomBottomBar categoriesBar = imageItemView.findViewById(R.id.create_effects_image_item_categories_bar);
            RecyclerView imageItemRecyclerView = imageItemView.findViewById(R.id.create_effects_image_item_recyclerview);

            List<ImageItem> imageItems = getImageItems();
            ImageItemAdapter imageItemAdapter = new ImageItemAdapter(imageItems, new ImageItemAdapter.ImageItemListener() {
                @Override
                public void onImageItemSelected(ImageItem imageItem) {
                    Glide.with(context)
                            .asBitmap()
                            .load(imageItem.getUrl())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    onResource(resource);
                                }
                            });
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            imageItemRecyclerView.setLayoutManager(layoutManager);
            imageItemRecyclerView.setAdapter(imageItemAdapter);

            categoriesBar.addMenu(getImageItemCategories(imageItems), context, true, CustomBottomBar.Mode.SINGLE, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    imageItemAdapter.changeCategory(context, item.getTitle());
                }
            });

            return imageItemView;
        }

        private List<CharSequence> getImageItemCategories(List<ImageItem> imageItems) {
            List<CharSequence> imageItemCategories = new ArrayList<>();
            imageItemCategories.add(context.getString(R.string.all));
            imageItemCategories.add(context.getString(R.string.favorites));
            for (ImageItem imageItem : imageItems) {
                String category = imageItem.getCategory();
                if (!imageItemCategories.contains(category))
                    imageItemCategories.add(category);
            }
            return imageItemCategories;
        }

        private void onResource(Bitmap resource) {
            // Create the frame filter.
            GPUImageAddBlendFilter overlayFilter = new GPUImageAddBlendFilter();
            overlayFilter.setBitmap(resource);
            // Replace the overlay item.
            setOverlayFilter(overlayFilter);
            this.curOverlayFilter = overlayFilter;
        }

        private void setOverlayFilter(GPUImageFilter overlayFilter) {
            changeFilter(overlayFilter, getOverlaysFilterIndex());
            fragment.getGPUImageView().requestRender();
        }

        private List<ImageItem> getImageItems() {
            List<ImageItem> overlays = new Items().getOverlays();
            return overlays;
        }

        @Override
        public void onDone() {
            fragment.addProgress(overlayProgress);
            overlayProgress = new OverlayProgress(curOverlayFilter);
        }

        @Override
        public void onClose() {
            setOverlayFilter(overlayProgress.curOverlayFilter);
        }
    }

    private class OverlayProgress implements Progress {

        private final GPUImageFilter curOverlayFilter;

        public OverlayProgress() {
            this.curOverlayFilter = new GPUImageFilter();
        }

        public OverlayProgress(GPUImageFilter newOverlayFilter) {
            this.curOverlayFilter = newOverlayFilter;
        }

        private void setOverlayFilter(GPUImageFilter overlayFilter) {
            changeFilter(overlayFilter, getOverlaysFilterIndex());
            fragment.getGPUImageView().requestRender();
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = overlayProgress;
            overlayProgress = this;
            setOverlayFilter(curOverlayFilter);
            return previousProgress;
        }
    }


}
