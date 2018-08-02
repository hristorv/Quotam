package com.quotam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.quotam.R;
import com.quotam.model.Constants;
import com.quotam.custom.CustomSearchView;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.ViewPagerAdapter;

public class ArtistsFragment extends Fragment {

    private Toolbar toolbar;
    private MainMenuActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artists,
                container, false);
        activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        final AppBarLayout appbarLayout = rootView.findViewById(R.id.appbar_layout);
        final TabLayout tabs = rootView.findViewById(R.id.tabs);
        toolbar = rootView.findViewById(R.id.toolbar);
        final CustomSearchView searchView = rootView.findViewById(R.id.search_view);
        final RelativeLayout extended_title_layout = rootView.findViewById(R.id.extended_title_layout);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        // Fix Keyboard for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        activity.setupToolbar(toolbar);
        activity.changeTitleParams(collapsingToolbarLayout);
        ViewPager viewPager = rootView.findViewById(R.id.artists_viewpager);
        setupTabs(tabs, viewPager);
        setSearchViewListener(searchView, tabs);

        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = -appBarLayout.getTotalScrollRange() / 2;
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
                animateTabs(verticalOffset, appBarLayout.getTotalScrollRange(), extended_title_layout);
                //animateSearchView(verticalOffset,threshold,searchView);
            }
        });

        appbarLayout.setExpanded(true);
        return rootView;
    }

    private void setupTabs(final TabLayout tabs, ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(0);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        GridFragment trendingFragment = new GridFragment();
        GridFragment topFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARCELABLE.ADAPTER_OBJECT, "Artist");
        trendingFragment.setArguments(bundle);
        topFragment.setArguments(bundle);

        adapter.addFragment(trendingFragment, getString(R.string.trending));
        adapter.addFragment(topFragment, getString(R.string.top));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        activity.setTabFont(tabs);
    }


    private void setSearchViewListener(CustomSearchView searchView, final TabLayout tabs) {
        searchView.setOnActionDoneListener(new CustomSearchView.OnActionDoneListener() {
            @Override
            public void onActionDone() {

            }
        });
    }

}


