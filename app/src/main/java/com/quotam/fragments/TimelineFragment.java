package com.quotam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quotam.R;
import com.quotam.adapters.ItemAdapter;
import com.quotam.adapters.InnerSectionAdapter;
import com.quotam.adapters.SectionAdapter;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.Items;

public class TimelineFragment extends Fragment {


    public static final int MAX_ITEMS = 8;
    private RecyclerView grid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        AppBarLayout appbarLayout = rootView.findViewById(R.id.appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);

        grid = rootView.findViewById(R.id.grid);
        populateGrid(rootView);
        activity.setupToolbar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);

        activity.changeTitleParams(collapsingToolbarLayout);
        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = -appBarLayout.getTotalScrollRange() / 2;
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
            }
        });
        appbarLayout.setExpanded(true);
        return rootView;
    }

    private void populateGrid(View rootView) {
        Items items = new Items();
        grid = rootView.findViewById(R.id.grid);
        //grid.setAdapter(new ItemAdapter(getActivity(), items.getSections()));
        SectionAdapter adapter = new SectionAdapter(items.getRandomSectionObjectList(), false, MAX_ITEMS) {
            @Override
            protected InnerSectionAdapter createInnerAdapter() {
                return new ItemAdapter();
            }

            @Override
            protected RecyclerView.LayoutManager createInnerLayoutManager() {
                return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            }
        };
        adapter.setHasStableIds(true);
        grid.setAdapter(adapter);


//      StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        //new SystemUI().adjustGridColumnNum(getActivity(), grid, false);
    }

}


