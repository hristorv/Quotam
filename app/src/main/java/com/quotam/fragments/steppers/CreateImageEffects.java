package com.quotam.fragments.steppers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.quotam.R;
import com.quotam.controller.effects.EffectsController;
import com.quotam.controller.effects.PictureEffectsController;
import com.quotam.controller.effects.Progress;
import com.quotam.controller.effects.TextViewEffectsController;
import com.quotam.custom.CustomBottomBar;
import com.quotam.custom.MovableTextViewLayout;
import com.quotam.model.CreateImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayDeque;
import java.util.Deque;

import jp.co.cyberagent.android.gpuimage.GPUImageView;


public class CreateImageEffects extends Fragment implements StepperPageFragment {


    public FrameLayout imageRoot;
    public GPUImageView image;
    private MovableTextViewLayout movableTextViewLayout;
    public TextView primaryTextView;
    private Bitmap defaultBitmap;
    private PictureEffectsController pictureEffectsController;
    private View imageBackground;
    public CropImageView imageCrop;
    private boolean isTextViewSelected = false;
    private TextView currentSelectedTextView;
    private TextViewEffectsController textviewEffectsController;
    private CustomBottomBar bottomBar;
    private Animation animationOut;
    private Animation animationIn;
    public UndoRedoManager undoRedoManager;
    private ImageButton undoButton;
    private ImageButton redoButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_effects, container, false);
        bottomBar = rootView.findViewById(R.id.stepper_effects_bottombar);
        image = rootView.findViewById(R.id.stepper_effects_image);
        imageRoot = rootView.findViewById(R.id.stepper_effects_image_root);
        imageBackground = rootView.findViewById(R.id.stepper_effects_image_background);
        imageCrop = rootView.findViewById(R.id.stepper_effects_image_crop);
        undoButton = rootView.findViewById(R.id.create_effects_undo);
        redoButton = rootView.findViewById(R.id.create_effects_redo);
        undoRedoManager = new UndoRedoManager();
        undoRedoManager.disableButton(undoButton);
        undoRedoManager.disableButton(redoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRedoManager.undo();
            }
        });
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRedoManager.redo();
            }
        });
        setupTextViewLayout();
        setupAnimations();
        movableTextViewLayout.setSelectListener(new MovableTextViewLayout.SelectListener() {
            @Override
            public void onSelected() {
                selectTextView(movableTextViewLayout.getTextView());
            }

            @Override
            public void onUnselected() {
                unselectTextView();
            }
        });

        pictureEffectsController = new PictureEffectsController(this, bottomBar);
        pictureEffectsController.addMenu();
        textviewEffectsController = new TextViewEffectsController(this, bottomBar, primaryTextView);

        return rootView;
    }

    private void setupAnimations() {
        animationOut = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_bar_menu_out);
        animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_bar_menu_in);
    }

    private void unselectTextView() {
        isTextViewSelected = false;
        currentSelectedTextView = null;
        changeMenu(pictureEffectsController);
    }

    private void selectTextView(TextView textView) {
        isTextViewSelected = true;
        currentSelectedTextView = textView;
        changeMenu(textviewEffectsController);

    }

    private void changeMenu(EffectsController effectsController) {
        bottomBar.clearAnimation();
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                effectsController.addMenu();
                bottomBar.startAnimation(animationIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bottomBar.startAnimation(animationOut);
    }


    private void setupTextViewLayout() {
        movableTextViewLayout = new MovableTextViewLayout(getContext());
        movableTextViewLayout.setupWithParent(imageRoot);
        primaryTextView = movableTextViewLayout.getTextView();
    }

    public void showBottomSheet(View view, CharSequence title, CreateImageStepperFragment.BottomSheetListener bottomSheetListener, CreateImageStepperFragment.AnimationShowListener animationShowListener) {
        CreateImageStepperFragment createImageStepperFragment = (CreateImageStepperFragment) getParentFragment();
        createImageStepperFragment.showBottomSheet(view, title, bottomSheetListener);
    }

    public View getBottomSheet() {
        CreateImageStepperFragment createImageStepperFragment = (CreateImageStepperFragment) getParentFragment();
        return createImageStepperFragment.getBottomSheet();
    }

    public void setImageBitmap(Bitmap bitmap) {
        image.getGPUImage().deleteImage();
        image.setImage(bitmap);
    }

    public GPUImageView getGPUImageView() {
        return image;
    }

    public FrameLayout getImageRoot() {
        return imageRoot;
    }

    private void createColorDialog() {
        ColorPickerDialog dialog = ColorPickerDialog.newBuilder()
                .setShowAlphaSlider(true)
                .setShowColorShades(true)
                .setAllowPresets(true)
                .setAllowCustom(true)
                .create();
        dialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                primaryTextView.setTextColor(color);
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
        dialog.show(getActivity().getFragmentManager(), dialog.getTag());
    }

    public void changeImage(Bitmap bitmap, boolean resetFilters) {
        defaultBitmap = bitmap;
        setImageBitmap(bitmap);
        // Calculate the image width/height, while keeping the aspect ratio and fitting into parent.
        int parentHeight = imageBackground.getHeight();
        int parentWidth = imageBackground.getWidth();
        int width = parentWidth;
        float scale = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        int height = (int) ((float) width / scale);
        if (height > parentHeight) {
            height = parentHeight;
            width = (int) ((float) height * scale);
        }
        // Change imageview height
        ViewGroup.LayoutParams imageParams = image.getLayoutParams();
        imageParams.height = height;
        imageParams.width = width;
        image.setLayoutParams(imageParams);
        // Change Framelayout height
        ViewGroup.LayoutParams imageRootParams = imageRoot.getLayoutParams();
        imageRootParams.height = height;
        imageRootParams.width = width;
        imageRoot.setLayoutParams(imageRootParams);
        // Reset TextView
        movableTextViewLayout.resetAll();
        // Reset the filters
        pictureEffectsController.resetFiltersAdapter = resetFilters;
        // Reset sizes.
        pictureEffectsController.setupSizes();
        // Reset adjust
        pictureEffectsController.resetAdjust(resetFilters);
    }

    private Bitmap getFullImage() {
        // Clear focus so we wont get the textview border on the final image.
        primaryTextView.clearFocus();
        // Get the bitmap from the layout with the textviews
        imageRoot.setDrawingCacheEnabled(true);
        imageRoot.buildDrawingCache();
        Bitmap bitmapLayout = imageRoot.getDrawingCache();
        // Get the bitmap from the GLSurfaceView
        Bitmap bitmapImage = null;
        try {
            bitmapImage = getGPUImageView().capture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Combine both bitmaps. The image bitmap should be always first.
        Bitmap bitmapResult = overlay(bitmapImage, bitmapLayout);
        Log.e("asd", "Before: " + bitmapResult.getWidth() + "  :  " + bitmapResult.getHeight());
        // Resize
        PictureEffectsController.Size curSize = pictureEffectsController.getCurSize();
        if (curSize != null)
            bitmapResult = curSize.getResized(bitmapResult);
        Log.e("asd", "After: " + bitmapResult.getWidth() + "  :  " + bitmapResult.getHeight());
        return bitmapResult;
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public Bitmap getDefaultBitmap() {
        return this.defaultBitmap;
    }

    private String getPrimaryText() {
        return (String) primaryTextView.getText();
    }

    @Override
    public void onObjectChanged(Object changedObject) {
        if (changedObject instanceof CreateImage) {
            CreateImage createImage = (CreateImage) changedObject;
            Bitmap newBitmap = createImage.getBitmap();
            if (newBitmap != null && !newBitmap.equals(defaultBitmap)) {
                changeImage(newBitmap, true);
            }
            String newPrimaryText = createImage.getPrimaryText();
            if (!newPrimaryText.equals(getPrimaryText())) {
                primaryTextView.setText(newPrimaryText);
            }
        }
    }

    @Override
    public String isDone() {
        return null;
    }

    @Override
    public void applyChanges(Object objectToChange) {
        if (objectToChange instanceof CreateImage) {
            CreateImage createImage = (CreateImage) objectToChange;
            createImage.setFullImage(getFullImage());
        }
    }

    public void addProgress(Progress progress) {
        this.undoRedoManager.addProgress(progress);
    }

    private class UndoRedoManager {

        Deque<Progress> undoDeque = new ArrayDeque<>();
        Deque<Progress> redoDeque = new ArrayDeque<>();

        private void addProgress(Progress progress) {
            undoDeque.push(progress);
            // Clear redo after making a change.
            redoDeque = new ArrayDeque<>();
            checkButtonVisibility();
        }

        private void undo() {
            if (!undoDeque.isEmpty()) {
                Progress firstProgress = undoDeque.pop();
                Progress previousProgress = firstProgress.restoreProgress();
                redoDeque.push(previousProgress);
                checkButtonVisibility();
            }
        }

        private void redo() {
            if (!redoDeque.isEmpty()) {
                Progress firstProgress = redoDeque.pop();
                Progress previousProgress = firstProgress.restoreProgress();
                undoDeque.push(previousProgress);
                checkButtonVisibility();
            }
        }

        private void checkButtonVisibility() {
            if (undoDeque.isEmpty() && undoButton.isEnabled())
                disableButton(undoButton);
            if (!undoDeque.isEmpty() && !undoButton.isEnabled())
                enableButton(undoButton);
            if (redoDeque.isEmpty() && redoButton.isEnabled())
                disableButton(redoButton);
            if (!redoDeque.isEmpty() && !redoButton.isEnabled())
                enableButton(redoButton);
        }

        private void disableButton(ImageButton button) {
            button.setAlpha(0.7f);
            button.setEnabled(false);
        }

        private void enableButton(ImageButton button) {
            button.setAlpha(1.0f);
            button.setEnabled(true);
        }

    }
}
