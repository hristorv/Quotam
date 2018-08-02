package com.quotam.fragments.steppers;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.adapters.ViewPagerAdapter;
import com.quotam.model.CreateImage;

import java.util.ArrayList;
import java.util.List;


public class CreateImageStepperFragment extends StepperFragment {

    public static final int OPTIONS_FRAGMENT_POSITION = 3;
    CreateImage createImage = new CreateImage();
    private Animation animationShow;
    private Animation animationHide;
    private boolean bottomSheetVisible = false;
    private RelativeLayout bottomSheet;
    private BottomSheetListener bottomSheetListener;
    private FrameLayout bottomSheetContent;
    private TextView titleView;

    @Override
    protected void addFragments(ViewPagerAdapter adapter) {
        adapter.addFragment(new CreateTextFragment(), getResources().getString(R.string.text));
        adapter.addFragment(new CreatePictureFragment(), getResources().getString(R.string.picture));
        adapter.addFragment(new CreateImageEffects(), getResources().getString(R.string.effects));
        adapter.addFragment(new CreateImageOptions(), getResources().getString(R.string.options));
    }

    @Override
    protected List<Integer> getSkipPositions() {
        ArrayList skipPositions = new ArrayList();
        skipPositions.add(OPTIONS_FRAGMENT_POSITION);
        return skipPositions;
    }

    @Override
    protected void applyChanges() {
        StepperPageFragment curFragment = getCurrentFragment();
        curFragment.applyChanges(createImage);
        notifyObjectChanged(createImage);
    }

    @Override
    protected void finish() {

    }

    public void showBottomSheet(View view, CharSequence title, BottomSheetListener bottomSheetListener) {
        if (!bottomSheetVisible) {
            if (bottomSheet == null)
                addBottomSheet();
            bottomSheetContent.removeAllViews();
            // Reset custom button
            resetCustomButton(bottomSheetListener);
            bottomSheetContent.addView(view);
            titleView.setText(title);
            this.bottomSheetListener = bottomSheetListener;
            showBottomSheet();
        }
    }

    private void resetCustomButton(BottomSheetListener bottomSheetListener) {
        ImageButton customButton = bottomSheet.findViewById(R.id.create_effects_bottom_sheet_custom_button);
        customButton.setVisibility(View.GONE);
        customButton.setOnClickListener(null);
        customButton.setImageResource(R.drawable.empty_drawable);
        bottomSheetListener.setupCustomButton(customButton);
    }

    private void addBottomSheet() {
        bottomSheet = (RelativeLayout) getLayoutInflater().inflate(R.layout.create_effects_bottom_sheet, baseLayout, false);
        bottomSheetContent = bottomSheet.findViewById(R.id.create_effects_bottom_sheet_content);
        ImageButton close = bottomSheet.findViewById(R.id.create_effects_bottom_sheet_close);
        ImageButton done = bottomSheet.findViewById(R.id.create_effects_bottom_sheet_done);
        titleView = bottomSheet.findViewById(R.id.create_effects_bottom_sheet_title);
        setupBottomSheetAnimations();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetVisible) {
                    closeBottomSheet();
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetVisible) {
                    bottomSheetListener.onDone();
                    hideBottomSheet();
                }
            }
        });

        baseLayout.addView(bottomSheet);
    }

    public boolean isBottomSheetVisible() {
        return bottomSheetVisible;
    }

    private void showBottomSheet() {
        bottomSheet.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheet.startAnimation(animationShow);
    }

    public void hideBottomSheet() {
        bottomSheet.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        bottomSheet.startAnimation(animationHide);
    }

    private void setupBottomSheetAnimations() {
        animationShow = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        animationShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bottomSheet.setVisibility(View.VISIBLE);
                bottomSheetVisible = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomSheet.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationHide = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
        animationHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bottomSheetVisible = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomSheet.setLayerType(View.LAYER_TYPE_NONE, null);
                bottomSheetContent.removeAllViews();
                bottomSheet.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void closeBottomSheet() {
        bottomSheetListener.onClose();
        hideBottomSheet();
    }

    public RelativeLayout getBottomSheet() {
        return bottomSheet;
    }

    public interface AnimationShowListener {
        void onShow();
    }

    public interface BottomSheetListener {
        default void onDone() {
        }

        default void onClose() {
        }

        default void setupCustomButton(ImageButton customButton) {

        }
    }
}
