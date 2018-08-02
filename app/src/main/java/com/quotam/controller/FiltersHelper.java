package com.quotam.controller;


import android.content.Context;
import android.graphics.BitmapFactory;

import com.quotam.R;
import com.quotam.adapters.FiltersAdapter;
import com.quotam.filters.IF1977Filter;
import com.quotam.filters.IFAmaroFilter;
import com.quotam.filters.IFBrannanFilter;
import com.quotam.filters.IFEarlybirdFilter;
import com.quotam.filters.IFHefeFilter;
import com.quotam.filters.IFHudsonFilter;
import com.quotam.filters.IFInkwellFilter;
import com.quotam.filters.IFLomoFilter;
import com.quotam.filters.IFLordKelvinFilter;
import com.quotam.filters.IFNashvilleFilter;
import com.quotam.filters.IFRiseFilter;
import com.quotam.filters.IFSierraFilter;
import com.quotam.filters.IFSutroFilter;
import com.quotam.filters.IFToasterFilter;
import com.quotam.filters.IFValenciaFilter;
import com.quotam.filters.IFWaldenFilter;
import com.quotam.filters.IFXprollFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;

public class FiltersHelper {

    private final Context context;
    private List<FilterCategory> filterCategories = new ArrayList<>();

    public FiltersHelper(Context context) {
        this.context = context;
        initFilterCategories();
    }

    private void initFilterCategories() {
        filterCategories.add(new SoftFilterCategory());
        filterCategories.add(new VintageFilterCategory());
        filterCategories.add(new WarmFilterCategory());
        filterCategories.add(new ColdFilterCategory());
        filterCategories.add(new ColorfulFilterCategory());
    }

    public FilterCategory getDefaultCategory() {
        return filterCategories.get(0);
    }

    public FilterCategory getCategory(CharSequence categoryTitle) {
        for (FilterCategory filterCategory : filterCategories) {
            if (filterCategory.getName().equals(categoryTitle)) {
                return filterCategory;
            }
        }
        return null;
    }

    public List<CharSequence> getCategories() {
        List<CharSequence> categories = new ArrayList<>();
        for (FilterCategory filterCategory : filterCategories)
            categories.add(filterCategory.getName());
        return categories;
    }

    public int getCategoryPosition(FilterCategory selectedCategory) {
        for (int i = 0; i < filterCategories.size(); i++) {
            if (filterCategories.get(i).isEqual(selectedCategory))
                return i;
        }
        return 0;
    }

    public GPUImageFilter getToneCurve(int curveID) {
        GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
        toneCurveFilter.setFromCurveFileInputStream(context.getResources().openRawResource(curveID));
        return toneCurveFilter;
    }


    public GPUImageFilter getLookup(int drawableID) {
        GPUImageLookupFilter imageLookupFilter = new GPUImageLookupFilter();
        imageLookupFilter.setBitmap(BitmapFactory.decodeResource(context.getResources(), drawableID));
        return imageLookupFilter;
    }


    public abstract class FilterCategory {
        protected FiltersAdapter.FilterList filters = null;
        private boolean init = false;

        abstract String getName();

        protected abstract void addFilters();

        public FiltersAdapter.FilterList getFiltersList() {
            return filters;
        }

        public void init() {
            if (filters == null) {
                filters = new FiltersAdapter.FilterList();
                // Add original
                filters.addFilter("Original", new GPUImageFilter());
                addFilters();
                init = true;
            }
        }

        public boolean isInit() {
            return init;
        }

        public boolean isEqual(FilterCategory filterCategory) {
            if (this.getName().equals(filterCategory.getName()))
                return true;
            else
                return false;
        }

    }

    private class SoftFilterCategory extends FilterCategory {

        @Override
        public String getName() {
            return "Soft";
        }

        @Override
        public void addFilters() {
            filters.addFilter("Comfy", getLookup(R.drawable.soft_comfy));
            filters.addFilter("Creamy", getLookup(R.drawable.soft_creamy));
            filters.addFilter("Glow", getLookup(R.drawable.soft_glow));
            filters.addFilter("Fog", getToneCurve(R.raw.soft_fog));
            filters.addFilter("Rize", getLookup(R.drawable.soft_rize));
            filters.addFilter("Delicate", getLookup(R.drawable.soft_delicate));

            filters.addFilter("Snow", getToneCurve(R.raw.soft_snow));
            filters.addFilter("Doki", getToneCurve(R.raw.soft_doki));
            filters.addFilter("Hymm", getToneCurve(R.raw.soft_hymm));

            filters.addFilter("Hude", new IFBrannanFilter(context));
            filters.addFilter("Nia", new IFRiseFilter(context));
            filters.addFilter("Oram", new IFValenciaFilter(context));
            filters.addFilter("Nosi", new IFAmaroFilter(context));
            filters.addFilter("Rival", new IFHudsonFilter(context));
        }
    }

    private class VintageFilterCategory extends FilterCategory {

        @Override
        public String getName() {
            return "Vintage";
        }

        @Override
        public void addFilters() {
            filters.addFilter("Ancient", getLookup(R.drawable.vintage_ancient));
            filters.addFilter("Raw", getLookup(R.drawable.vintage_raw));
            filters.addFilter("Decrep", getLookup(R.drawable.vintage_decrep));
            filters.addFilter("Mature", getLookup(R.drawable.vintage_mature));
            filters.addFilter("Versed", getLookup(R.drawable.vintage_versed));
            filters.addFilter("Smort", getLookup(R.drawable.vintage_smort));

            filters.addFilter("Vertran", getToneCurve(R.raw.vintage_vertran));
            filters.addFilter("Retro", getToneCurve(R.raw.vintage_retro));
            filters.addFilter("Lewit", getToneCurve(R.raw.vintage_lewit));

            filters.addFilter("Sepia", new GPUImageSepiaFilter());
            filters.addFilter("Rird", new IFEarlybirdFilter(context));
            filters.addFilter("Effis", new IFHefeFilter(context));
            filters.addFilter("Ink", new IFInkwellFilter(context));
            filters.addFilter("Rind", new IFSierraFilter(context));
        }
    }

    private class WarmFilterCategory extends FilterCategory {

        @Override
        public String getName() {
            return "Warm";
        }

        @Override
        public void addFilters() {
            filters.addFilter("Mild", getLookup(R.drawable.warm_mild));
            filters.addFilter("Swelt", getLookup(R.drawable.warm_swelt));
            filters.addFilter("Curves", getLookup(R.drawable.warm_curves));
            filters.addFilter("Happy", getLookup(R.drawable.warm_happy));
            filters.addFilter("Light", getLookup(R.drawable.warm_light));
            filters.addFilter("Tepid", getLookup(R.drawable.warm_tepid));

            filters.addFilter("Milk", getToneCurve(R.raw.warm_milk));
            filters.addFilter("Summer", getToneCurve(R.raw.warm_summer));
            filters.addFilter("Sunset", getToneCurve(R.raw.warm_sunset));
            filters.addFilter("Whales", getToneCurve(R.raw.warm_whales));
            filters.addFilter("Birds", getToneCurve(R.raw.warm_birds));
            filters.addFilter("Golden", getToneCurve(R.raw.warm_colden));
            filters.addFilter("Parades", getToneCurve(R.raw.warm_parades));
            filters.addFilter("Melt", getToneCurve(R.raw.warm_melt));

            filters.addFilter("Toast", new IFToasterFilter(context));
        }
    }

    private class ColdFilterCategory extends FilterCategory {

        @Override
        public String getName() {
            return "Cold";
        }

        @Override
        public void addFilters() {
            filters.addFilter("Breeze", getLookup(R.drawable.cold_breeze));
            filters.addFilter("Croz", getLookup(R.drawable.cold_croz));
            filters.addFilter("Evening", getLookup(R.drawable.cold_evening));
            filters.addFilter("Brisk", getLookup(R.drawable.cold_brisk));
            filters.addFilter("Crisp", getLookup(R.drawable.cold_crisp));

            filters.addFilter("Aurora", getToneCurve(R.raw.cold_aurora));
            filters.addFilter("Frosty", getToneCurve(R.raw.cold_frosty));
            filters.addFilter("Desert", getToneCurve(R.raw.cold_desert));
            filters.addFilter("Frozen", getToneCurve(R.raw.cold_frozen));
            filters.addFilter("Boreal", getToneCurve(R.raw.cold_boreal));
            filters.addFilter("Polar", getToneCurve(R.raw.cold_polar));
            filters.addFilter("Surtu", getToneCurve(R.raw.cold_surtu));
            filters.addFilter("Sea", getToneCurve(R.raw.cold_sea));


            filters.addFilter("Rota", getLookup(R.drawable.lookup_amatorka));
            filters.addFilter("Molem", new IFLomoFilter(context));
            filters.addFilter("Otram", new IFSutroFilter(context));
            filters.addFilter("Roll", new IFXprollFilter(context));
        }
    }

    private class ColorfulFilterCategory extends FilterCategory {

        @Override
        public String getName() {
            return "Colorful";
        }

        @Override
        public void addFilters() {
            filters.addFilter("Intense", getLookup(R.drawable.colorful_intense));
            filters.addFilter("Broken", getLookup(R.drawable.colorful_broken));
            filters.addFilter("Stain", getLookup(R.drawable.colorful_stain));
            filters.addFilter("Xtreme", getLookup(R.drawable.colorful_xtreme));
            filters.addFilter("Neer", getLookup(R.drawable.colorful_neer));
            filters.addFilter("Blush", getLookup(R.drawable.colorful_blush));
            filters.addFilter("Cast", getLookup(R.drawable.colorful_cast));
            filters.addFilter("Chroma", getLookup(R.drawable.colorful_chroma));
            filters.addFilter("Dye", getLookup(R.drawable.colorful_dye));

            filters.addFilter("Iride", getToneCurve(R.raw.colorful_iride));
            filters.addFilter("Field", getToneCurve(R.raw.colorful_field));
            filters.addFilter("Lume", getToneCurve(R.raw.colorful_lume));
            filters.addFilter("Pigment", getToneCurve(R.raw.colorful_pigment));
            filters.addFilter("Nyle", getToneCurve(R.raw.colorful_nyle));
            filters.addFilter("Tinge", getToneCurve(R.raw.colorful_tinge));
            filters.addFilter("Smile", getToneCurve(R.raw.colorful_smile));
            filters.addFilter("Eyka", getToneCurve(R.raw.colorful_eyka));

            filters.addFilter("Wash", new IF1977Filter(context));
            filters.addFilter("Nivle", new IFLordKelvinFilter(context));
            filters.addFilter("Villen", new IFNashvilleFilter(context));
            filters.addFilter("Ranten", new IFWaldenFilter(context));

        }
    }

}
