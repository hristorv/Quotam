package com.quotam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.adapters.ItemAdapter;
import com.quotam.model.Category;
import com.quotam.model.Constants;
import com.quotam.utils.Helper;
import com.quotam.listeners.FabLikeOnClickListener;
import com.quotam.listeners.SystemUI;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.Items;

public class InCategoryFragment extends Fragment {


    private Category category;
    private RequestOptions options;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null)
            category = bundle.getParcelable(Constants.PARCELABLE.ADAPTER_OBJECT);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
        setImageLoaderOptions();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_in_category,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        AppBarLayout appbarLayout = rootView.findViewById(R.id.inner_appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        final CoordinatorLayout paralax_base = rootView.findViewById(R.id.category_paralax_base);
        RecyclerView grid = rootView.findViewById(R.id.grid);
        final FloatingActionButton fab = rootView.findViewById(R.id.fab);

        setupGrid(grid);
        activity.fixToolbarPadding(toolbar);
        activity.setupToolbar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        setupCategoryInfo(rootView);

        activity.changeTitleParams(collapsingToolbarLayout);
        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = -appBarLayout.getTotalScrollRange() / 4;
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
                animateFab(verticalOffset, threshold, fab);
                //animateTabs(verticalOffset,threshold,rootView.findViewById(R.id.category_header));
            }
        });
        fab.setOnClickListener(new FabLikeOnClickListener(false, getActivity().getResources().getColor(R.color.accent_color), getActivity().getResources().getColor(R.color.white)) {

            @Override
            public void onClick() {
                fab.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.custom_tabs_scale));
            }
        });
        appbarLayout.setExpanded(true);
        return rootView;
    }

    private void setupCategoryInfo(View rootView) {
        ImageView paralaxImage = rootView.findViewById(R.id.album_item_image);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        Glide.with(getActivity())
                .load(Helper.transform(category.getBackgroundUrl()))
                .apply(options)
                .into(paralaxImage);
        collapsingToolbarLayout.setTitle(category.getName());
    }

    private void adjustGrid(RecyclerView grid, boolean isLargeGridItems) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        new SystemUI().adjustGridColumnNum(getActivity(), grid, isLargeGridItems);
    }

    private void setupGrid(RecyclerView grid) {
        Items items = new Items();
        grid.setAdapter(new ItemAdapter(items.getRandomPictures()));
        adjustGrid(grid, false);
    }


}


