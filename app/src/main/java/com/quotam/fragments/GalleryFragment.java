package com.quotam.fragments;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.quotam.R;
import com.quotam.activities.MainMenuActivity;
import com.quotam.adapters.ImagePagerAdapter;
import com.quotam.adapters.Items;
import com.quotam.controller.ImageController;
import com.quotam.custom.CustomBottomBar;
import com.quotam.custom.CustomViewPager;
import com.quotam.fragments.sheets.BottomSheetAddToAlbum;
import com.quotam.fragments.sheets.BottomSheetComments;
import com.quotam.fragments.sheets.BottomSheetReport;
import com.quotam.fragments.sheets.BottomSheetShare;
import com.quotam.listeners.FabLikeOnClickListener;
import com.quotam.listeners.SystemUI;
import com.quotam.model.Constants;
import com.quotam.model.Image;
import com.quotam.utils.ArtistDialog;

import java.util.List;

public class GalleryFragment extends Fragment {

    private int imagePosition;
    private List<Image> images;
    private CustomViewPager viewPager;
    private CoordinatorLayout bottom_bar_layout;
    private FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(false);
        getImagePosition();
        getImages();
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
        ;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.expand).setVisible(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onStart() {
        super.onStart();
        SystemUI systemUI = new SystemUI();
        if (!viewPager.isShowing()) {
            systemUI.hideSystemUi(getActivity());
            if (bottom_bar_layout.getVisibility() == View.VISIBLE)
                systemUI.hideView(getActivity(), bottom_bar_layout, R.anim.bottom_exit);
            if (fab.getVisibility() == View.VISIBLE)
                fab.hide();

        } else {
            if (bottom_bar_layout.getVisibility() != View.VISIBLE)
                bottom_bar_layout.setVisibility(View.VISIBLE);
            if (fab.getVisibility() != View.VISIBLE)
                fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        new SystemUI().showSystemUi(getActivity());
    }

    private void getImagePosition() {
        this.imagePosition = getArguments().getInt(
                Constants.Extra.POSITION, 0);
    }

    private void getImages() {
        //this.images = (List<Image>)(Object) new Items().getRandomPictures();
        this.images = getArguments().getParcelableArrayList(Constants.PARCELABLE.ADAPTER_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery,
                container, false);
        bottom_bar_layout = rootView.findViewById(R.id.bottom_bar_layout);
        fab = rootView.findViewById(R.id.fab);
        CustomBottomBar bottomBar = rootView.findViewById(R.id.bottom_bar);
        addMenu(bottomBar);
        TextView artistName = rootView.findViewById(R.id.gallery_artist);
        artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArtistDialog.openArtistDialog((AppCompatActivity) getActivity(), new Items().getArtist());
            }
        });

        viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setBottomBar(bottom_bar_layout);
        viewPager.setFab(fab);
        viewPager.setImages(images);
        ImagePagerAdapter adapter = new ImagePagerAdapter(
                images, viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagePosition, false);
        viewPager.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new FabLikeOnClickListener(false, getActivity().getResources().getColor(R.color.accent_color), getActivity().getResources().getColor(R.color.white)) {

            @Override
            public void onClick() {

            }
        });
        return rootView;
    }

    private void addMenu(CustomBottomBar bottomBar) {
        bottomBar.addMenu(R.menu.bottom_bar_menu, getActivity(), false, CustomBottomBar.Mode.NORMAL, new CustomBottomBar.OnMenuClickListener() {
            @Override
            public void onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_bar_comments:
                        showComments();
                        break;
                    case R.id.bottom_bar_share:
                        showShare();
                        break;
                    case R.id.bottom_bar_add:
                        showAdd();
                        break;
                    case R.id.bottom_bar_report:
                        //showReport();
                        Log.e("asd", "________________________________________");
                        Bitmap bitmap = getCurrentBitmap();
                        Log.e("asd", "width: " + bitmap.getWidth() + "  " + "height: " + bitmap.getHeight());
                        testOcr(bitmap);
                        Log.e("asd", "___________ RESCALED _____________");
                        Bitmap rescaledBitmap = resize(bitmap, 1500, 1500);
                        Log.e("asd", "width: " + rescaledBitmap.getWidth() + "  " + "height: " + rescaledBitmap.getHeight());
                        testOcr(rescaledBitmap);
                        break;
                }
            }
        });
        bottomBar.addBadge(0);
        bottomBar.setBadgeCount(0, 4);
        bottomBar.getBadge(0).show();
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private void testOcr(Bitmap bitmap) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                .getVisionTextDetector();
        Task<FirebaseVisionText> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // ...

                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

//        TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        if (!textRecognizer.isOperational()) {
//            Log.e("asd", "Detector dependencies are not yet available.");
//
//            // Check for low storage.  If there is low storage, the native library will not be
//            // downloaded, so detection will not become operational.
//            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
//            boolean hasLowStorage = getContext().registerReceiver(null, lowstorageFilter) != null;
//
//            if (hasLowStorage) {
//                Log.e("asd", "Low storage error");
//            }
//        }
//        SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
//
//        for (int i = 0; i < textBlocks.size(); i++) {
//            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
//            Log.e("asd", "" + textBlock.getValue());
//        }

    }

    private void showReport() {
        if (isImageLoaded()) {
            BottomSheetReport bottomSheetReport = new BottomSheetReport();
            bottomSheetReport.show(getActivity().getSupportFragmentManager(), bottomSheetReport.getTag());

        }
    }

    private void showShare() {
        if (isImageLoaded()) {
            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetShare();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.PARCELABLE.BITMAP, new ImageController()
                    .getImageUri(getCurrentBitmap()));
            bottomSheetDialogFragment.setArguments(bundle);
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }

    private void showComments() {
        if (isImageLoaded()) {
            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetComments();
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }

    private void showAdd() {
        if (isImageLoaded()) {
            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetAddToAlbum();
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }


    private Bitmap getCurrentBitmap() {
        return ((ImagePagerAdapter) viewPager.getAdapter()).getCurrentBitmap();
    }

    private boolean isImageLoaded() {
        return ((ImagePagerAdapter) viewPager.getAdapter()).getCurrentBitmap() != null;
    }

    private Image getCurrentImage() {
        return images.get(viewPager.getCurrentItem());
    }

}
