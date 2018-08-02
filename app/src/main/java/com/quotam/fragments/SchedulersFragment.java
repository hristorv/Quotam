package com.quotam.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quotam.R;
import com.quotam.controller.Helper;
import com.quotam.controller.SchedulerController;
import com.quotam.fragments.steppers.SchedulersStepperFragment;
import com.quotam.model.Constants;
import com.quotam.adapters.Items;
import com.quotam.adapters.SchedulersAdapter;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.activities.MainMenuActivity;
import com.quotam.model.Scheduler;

import java.util.ArrayList;

public class SchedulersFragment extends Fragment {

    private RecyclerView grid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedulers,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        AppBarLayout appbarLayout = rootView.findViewById(R.id.appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        grid = rootView.findViewById(R.id.grid);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        final FloatingActionButton fab = rootView.findViewById(R.id.fab);

        ArrayList<Scheduler> schedulers = new SchedulerController().getSchedulers(getContext());
        setupGrid(schedulers);
        if (schedulers.isEmpty())
            Helper.setupHelper(rootView, R.string.help_schedulers_small, R.drawable.schedulers_helper);

        activity.setupToolbar(toolbar);
        activity.changeTitleParams(collapsingToolbarLayout);
        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = -appBarLayout.getTotalScrollRange() / 2;
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SchedulersStepperFragment();
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.content_frame, fragment)
                        .addToBackStack(
                                Constants.FragmentNames.BACKSTACK_FRAGMENT)
                        .commit();
            }
        });
        appbarLayout.setExpanded(true);
        return rootView;
    }

    public void onStepperDone() {
        grid.getAdapter().notifyDataSetChanged();
    }

    private void setupGrid(ArrayList<Scheduler> schedulers) {
        SchedulersAdapter adapter = new SchedulersAdapter(schedulers);
        grid.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        // Fixes bug. When scrolling onbindviewholder is not called and disabled view cant change its alpha.
        //grid.setItemViewCacheSize(0);

        //new SystemUI().adjustGridColumnNum(getActivity(), grid, true);

    }

}


