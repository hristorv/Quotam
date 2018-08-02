package com.quotam.fragments.steppers;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quotam.R;
import com.quotam.adapters.ImageDefaultAdapter;
import com.quotam.adapters.Items;
import com.quotam.custom.CustomSearchView;
import com.quotam.model.Constants;
import com.quotam.model.CreateImage;
import com.quotam.model.ImageDefault;

public class CreatePictureFragment extends Fragment implements StepperPageFragment,FragmentWithFab {

    private static final int UPLOAD_REQUEST_CODE = 3;
    private RecyclerView itemsGrid;
    private CreatePictureCrop pictureFragment;
    public FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_create_picture, container, false);
        CustomSearchView searchView = rootView.findViewById(R.id.search_view);
        coordinatorLayout = rootView.findViewById(R.id.stepper_picture_coordinator_layout);
        fab = rootView.findViewById(R.id.fab);

        itemsGrid = rootView.findViewById(R.id.stepper_picture_items);

        setupFab();
        setupItemsGrid();
        adjustGridItems();
        return rootView;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), UPLOAD_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            openImage(new ImageDefault(data.getData(), ImageDefault.Type.UPLOAD));
        }

    }

    private void setupItemsGrid() {
        Items items = new Items();
        ImageDefaultAdapter adapter = new ImageDefaultAdapter(items.getRandomImageDefault());
        adapter.setOnItemClickListener(new ImageDefaultAdapter.DefaultImageOnItemClickListener() {
            @Override
            public void onItemClick(ImageDefault item) {
                openImage(item);
            }
        });
        itemsGrid.setAdapter(adapter);
    }

    private void adjustGridItems() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        itemsGrid.setLayoutManager(manager);
        itemsGrid.setHasFixedSize(true);
        //new SystemUI().adjustGridColumnNum(getActivity(), grid, false);
    }

    private void openImage(ImageDefault item) {
        pictureFragment = CreatePictureCrop.newInstance(item);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.stepper_picture_frame, pictureFragment)
                .addToBackStack(
                        Constants.FragmentNames.BACKSTACK_FRAGMENT)
                .commit();
    }


    @Override
    public String isDone() {
        if (pictureFragment != null && pictureFragment.isVisible() && pictureFragment.getImage() != null)
            return null;
        else
            return getResources().getString(R.string.create_choose_picture_error);
    }

    @Override
    public void applyChanges(Object objectToChange) {
        if (objectToChange instanceof CreateImage) {
            CreateImage createImage = (CreateImage) objectToChange;
            Bitmap bitmap = pictureFragment.getImage();
            if (createImage.getBitmap() != bitmap)
                createImage.setBitmap(bitmap);
        }
    }

}
