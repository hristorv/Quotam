package com.quotam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quotam.R;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.ViewPagerAdapter;

public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        final AppBarLayout appbarLayout = rootView.findViewById(R.id.inner_appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
        RecyclerView grid = rootView.findViewById(R.id.grid);
        final TabLayout tabs = rootView.findViewById(R.id.tabs);
        final CoordinatorLayout paralax_account_base = rootView.findViewById(R.id.paralax_account_base);
        final ImageView profile_pic = rootView.findViewById(R.id.profile_image_avatar);
        activity.setupToolbar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        activity.changeTitleParams(collapsingToolbarLayout);

        ViewPager viewPager = rootView.findViewById(R.id.profile_viewpager);
        setupTabs(tabs, viewPager, activity);
        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = 0;
                animateScale(verticalOffset, threshold, profile_pic);
                animateTabs(verticalOffset, appBarLayout.getTotalScrollRange(), paralax_account_base);
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
                animateFab(verticalOffset, threshold, fab);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEdit();
            }
        });
        appbarLayout.setExpanded(true);
        return rootView;
    }

    private void openEdit() {
        //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //EditDialogFragment editDialogFragment = EditDialogFragment.newInstance();
        //editDialogFragment.show(fragmentManager, editDialogFragment.getTag());


    }

    private void setupTabs(TabLayout tabs, ViewPager viewPager, MainMenuActivity activity) {

        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GridFragment(), getString(R.string.collection));
        adapter.addFragment(new GridFragment(), getString(R.string.favorites));
        adapter.addFragment(new CommentsFragment(), getString(R.string.comments));

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        activity.setTabFont(tabs);

    }

}


