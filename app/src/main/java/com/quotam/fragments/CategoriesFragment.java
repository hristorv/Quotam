package com.quotam.fragments;

import com.quotam.R;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.listeners.SystemUI;
import com.quotam.activities.MainMenuActivity;
import com.quotam.adapters.ItemAdapter;
import com.quotam.adapters.Items;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CategoriesFragment extends Fragment {
    private RecyclerView grid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setIsAlbum(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        AppBarLayout appbarLayout = rootView.findViewById(R.id.appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);

        populateGrid(rootView);

        activity.setupToolbar(toolbar);
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
        grid.setAdapter(new ItemAdapter(items.getCategories()));

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        new SystemUI().adjustGridColumnNum(getActivity(), grid, true);
    }

}
