package com.quotam.fragments.sheets;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.adapters.CompactItemAdapter;
import com.quotam.model.Album;
import com.quotam.model.Category;
import com.quotam.adapters.Items;
import com.quotam.custom.CustomSnackbar;

public class BottomSheetAddToAlbum extends BottomSheetDialogFragment {
    RequestOptions options;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImageLoaderOptions();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_base, container,
                false);
        View base = inflater.inflate(R.layout.bottom_sheet_grid, (ViewGroup) rootView, true);
        RecyclerView grid = base.findViewById(R.id.bottom_sheet_grid);
        TextView titleView = base.findViewById(R.id.bottom_sheet_title);
        titleView.setText(R.string.action_add_album);

        CompactItemAdapter adapter = new CompactItemAdapter(new Items().getRandomAlbums(10), CompactItemAdapter.ManagerType.GRID);
        adapter.setOnItemClickListener(new CompactItemAdapter.CompactOnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                String name = "";
                if (item instanceof Album)
                    name = ((Album) item).getName();
                if (item instanceof Category)
                    name = ((Category) item).getName();
                selectAlbum();
                BottomSheetAddToAlbum.this.dismiss();
                String message = getString(R.string.added_to_album) + " " + name;
                View rootView = getActivity().getWindow().getDecorView();
                CustomSnackbar.create(getActivity(), rootView, message, Snackbar.LENGTH_SHORT).show();
            }
        });
        grid.setAdapter(adapter);
        adjustGrid(grid);
        return rootView;
    }

    private void selectAlbum() {

    }

    private void adjustGrid(RecyclerView grid) {
        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        //new SystemUI().adjustGridColumnNum(getActivity(), grid, false);
    }
}
