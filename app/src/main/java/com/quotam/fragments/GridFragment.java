package com.quotam.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quotam.R;
import com.quotam.adapters.ItemAdapter;
import com.quotam.model.Constants;
import com.quotam.listeners.SystemUI;
import com.quotam.adapters.Items;

public class GridFragment extends Fragment {

    private String items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null)
            items = bundle.getString(Constants.PARCELABLE.ADAPTER_OBJECT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid,
                container, false);
        RecyclerView grid = (RecyclerView) rootView;
        fillGrid(grid);

        return rootView;
    }

    private void fillGrid(RecyclerView grid) {
        if (items == null) {
            final Items items = new Items();
            grid.setAdapter(new ItemAdapter(items.getRandomAlbums(100)));
        } else {
            grid.setAdapter(new ItemAdapter(new Items().getRandomArtists()));
        }
        adjustGrid(grid, false);
    }

    private void adjustGrid(RecyclerView grid, boolean isLargeGridItems) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);

//        grid.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_bottom),
//        0.4f));

        new SystemUI().adjustGridColumnNum(getActivity(), grid, isLargeGridItems);
    }


}
