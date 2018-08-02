package com.quotam.fragments.steppers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.quotam.R;
import com.quotam.adapters.CompactItemAdapter;
import com.quotam.adapters.InnerSectionAdapter;
import com.quotam.adapters.Items;
import com.quotam.adapters.SectionAdapter;
import com.quotam.custom.CustomAnimations;
import com.quotam.custom.CustomSearchView;
import com.quotam.model.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class SchedulerItems extends Fragment implements StepperPageFragment {


    private static final int ANIMATION_DURATION = 200;
    private static final int MAX_ITEMS = 6;
    private CompactItemAdapter selectedGridAdapter;
    private RecyclerView selectedGrid;
    private CustomAnimations.SlideAnimation animationShow;
    private CustomAnimations.SlideAnimation animationHide;
    private RecyclerView itemsGrid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_items,
                container, false);
        CustomSearchView searchView = rootView.findViewById(R.id.search_view);
        selectedGrid = rootView.findViewById(R.id.stepper_items_selected);
        itemsGrid = rootView.findViewById(R.id.stepper_items_grid);
        selectedGridAdapter = new CompactItemAdapter(new ArrayList<Object>(), CompactItemAdapter.ManagerType.LINEAR);
        selectedGridAdapter.setOnItemClickListener(new CompactItemAdapter.CompactOnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                removeItemFromSelected(item);
            }
        });
        selectedGrid.setAdapter(selectedGridAdapter);
        setupItemsGrid();
        initSelectedGridAnimation();
        adjustGridItems(itemsGrid);
        adjustGridSelected(selectedGrid);
        return rootView;
    }

    private void setupItemsGrid() {
        Items items = new Items();
        SectionAdapter adapter = new SectionAdapter(items.getSchedulerItemsSections(), true, MAX_ITEMS) {
            @Override
            protected InnerSectionAdapter createInnerAdapter() {
                CompactItemAdapter compactItemAdapter = new CompactItemAdapter(CompactItemAdapter.ManagerType.GRID);
                compactItemAdapter.setOnItemClickListener(new CompactItemAdapter.CompactOnItemClickListener() {
                    @Override
                    public void onItemClick(Object item) {
                        addItemToSelected(item);
                    }
                });
                return compactItemAdapter;
            }

            @Override
            protected RecyclerView.LayoutManager createInnerLayoutManager() {
                return new GridLayoutManager(getContext(), 3);
            }
        };
        adapter.setHasStableIds(true);
        itemsGrid.setAdapter(adapter);
    }

    private void addItemToSelected(final Object item) {
        if (!selectedGridAdapter.isAlreadyAdded(item)) {
            if (selectedGridAdapter.getItemCount() == 0) {
                animationShow.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        selectedGridAdapter.addItem(item);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                selectedGrid.startAnimation(animationShow);
                itemsGrid.invalidate();

            } else
                selectedGridAdapter.addItem(item);
        }
    }

    private void removeItemFromSelected(Object item) {
        selectedGridAdapter.removeItem(item);
        if (selectedGridAdapter.getItemCount() == 0) {
            selectedGrid.startAnimation(animationHide);
            itemsGrid.invalidate();
        }
    }

    private void initSelectedGridAnimation() {
        int viewHeight = (int) getContext().getResources().getDimension(R.dimen.selected_grid_height);
        animationShow = new CustomAnimations.SlideAnimation(selectedGrid, 0, viewHeight);
        animationShow.setInterpolator(new DecelerateInterpolator());
        animationShow.setDuration(ANIMATION_DURATION);
        animationHide = new CustomAnimations.SlideAnimation(selectedGrid, viewHeight, 0);
        animationHide.setInterpolator(new DecelerateInterpolator());
        animationHide.setDuration(ANIMATION_DURATION);
    }

    private List<Object> getDefaultItems() {
        Items items = new Items();
        return items.getRandomAlbums(50);
    }

    private void adjustGridSelected(RecyclerView grid) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);

    }

    private void adjustGridItems(RecyclerView grid) {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        itemsGrid.setLayoutManager(manager);
        itemsGrid.setHasFixedSize(true);
        //new SystemUI().adjustGridColumnNum(getActivity(), grid, false);
    }

    public List<Object> getSelectedItems() {
        return selectedGridAdapter.getItems();
    }

    @Override
    public String isDone() {
        if (selectedGridAdapter.getItemCount() < 1)
            return getResources().getString(R.string.schedulers_items_error);
        return null;
    }

    @Override
    public void applyChanges(Object objectToChange) {
        if (objectToChange instanceof Scheduler) {
            Scheduler scheduler = (Scheduler) objectToChange;
            scheduler.setCollections(selectedGridAdapter.getItems());
        }
    }

}
