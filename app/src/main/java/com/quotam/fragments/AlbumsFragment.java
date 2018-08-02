package com.quotam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.quotam.R;
import com.quotam.custom.CustomSearchView;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.ViewPagerAdapter;

public class AlbumsFragment extends Fragment {

    private Toolbar toolbar;
    private MainMenuActivity activity;
    private FrameLayout album_search_fragment;
    private boolean searchFragmentOpened = false;
    private GridFragment searchFragment;
    private CustomSearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums,
                container, false);
        activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        final AppBarLayout appbarLayout = rootView.findViewById(R.id.appbar_layout);
        album_search_fragment = rootView.findViewById(R.id.album_search_fragment);
        final TabLayout tabs = rootView.findViewById(R.id.tabs);
        toolbar = rootView.findViewById(R.id.toolbar);
        RecyclerView grid = rootView.findViewById(R.id.grid);
        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
        searchView = rootView.findViewById(R.id.search_view);
        final RelativeLayout extended_title_layout = rootView.findViewById(R.id.extended_title_layout);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        // Fix Keyboard for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        activity.setupToolbar(toolbar);
        activity.changeTitleParams(collapsingToolbarLayout);
        ViewPager viewPager = rootView.findViewById(R.id.albums_viewpager);
        setupTabs(tabs, viewPager);
        setSearchViewListener(searchView, viewPager, tabs);

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

    private void setupTabs(final TabLayout tabs, final ViewPager viewPager) {

        viewPager.setOffscreenPageLimit(0);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GridFragment(), getString(R.string.latest));
        adapter.addFragment(new GridFragment(), getString(R.string.trending));
        adapter.addFragment(new GridFragment(), getString(R.string.top));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        activity.setTabFont(tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                closeSearchFragment(tabs);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                closeSearchFragment(tabs);
            }
        });
    }

    private void closeSearchFragment(TabLayout tabs) {
        if (searchFragmentOpened == true) {
            getChildFragmentManager().beginTransaction().remove(searchFragment).commit();
            searchFragment = null;
            searchFragmentOpened = false;
            tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.accent_color));
            tabs.setTabTextColors(getResources().getColor(R.color.secondary_text_color), getResources().getColor(R.color.accent_color));
            activity.setTabFont(tabs);
            searchView.clear();
        }
    }

    private void openSearchFragment(TabLayout tabs) {
        searchFragment = new GridFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.album_search_fragment, searchFragment).commit();
        searchFragmentOpened = true;
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.primary_color));
        tabs.setTabTextColors(getResources().getColor(R.color.secondary_text_color), getResources().getColor(R.color.secondary_text_color));
        activity.setTabFont(tabs);
    }

    private void setSearchViewListener(CustomSearchView searchView, final ViewPager viewPager, final TabLayout tabs) {
        searchView.setOnActionDoneListener(new CustomSearchView.OnActionDoneListener() {
            @Override
            public void onActionDone() {
                openSearchFragment(tabs);
            }
        });
    }


}


