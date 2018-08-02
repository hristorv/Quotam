package com.quotam.controller.effects;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.adapters.FontAdapter;
import com.quotam.controller.EyeDropper;
import com.quotam.custom.ColorPicker;
import com.quotam.custom.CustomBottomBar;
import com.quotam.custom.CustomSeekBar;
import com.quotam.custom.CustomSnackbar;
import com.quotam.fragments.steppers.CreateImageEffects;
import com.quotam.model.Font;
import com.quotam.utils.DrawableTint;

import java.util.ArrayList;
import java.util.List;

public class TextViewEffectsController implements EffectsController {

    private final CreateImageEffects fragment;
    private final Context context;
    private final CustomBottomBar bottomBar;
    private final TextView textView;
    ///////////////////////////////////////////////////////////
    private ColorProgress textColorProgress;
    private FormatProgress formatProgress;
    private StyleProgress styleProgress;
    private LineSpacingProgress lineSpacingProgress;
    private BackgroundColorProgress backgroundColorProgress;
    private ShadowProgress shadowProgress;
    private BlurProgress blurProgress;
    private RotateProgress rotateProgress;

    private enum Style {BOLD, ITALIC, CAPITAL, UNDERLINE, STRIKETHROUGH}

    private enum Format {LEFT, CENTER, RIGHT}

    public TextViewEffectsController(CreateImageEffects fragment, CustomBottomBar bottomBar, TextView textView) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.bottomBar = bottomBar;
        this.textView = textView;
        initProgress();
    }

    private void initProgress() {
        textColorProgress = new ColorProgress();
        formatProgress = new FormatProgress();
        styleProgress = new StyleProgress();
        lineSpacingProgress = new LineSpacingProgress();
        backgroundColorProgress = new BackgroundColorProgress();
        shadowProgress = new ShadowProgress();
        blurProgress = new BlurProgress();
        rotateProgress = new RotateProgress();
    }

    @Override
    public void addMenu() {
        bottomBar.addMenu(R.menu.create_effects_menu_textview, context, true, CustomBottomBar.Mode.NORMAL,
                new CustomBottomBar.OnMenuClickListener() {
                    @Override
                    public void onMenuItemClick(MenuItem item) {
                        CharSequence title = item.getTitle();
                        BottomSheetController bottomSheet = null;
                        switch (item.getItemId()) {
                            case R.id.create_effects_font:
                                bottomSheet = new FontBottomSheet();
                                break;
                            case R.id.create_effects_color:
                                bottomSheet = new ColorBottomSheet();
                                break;
                            case R.id.create_effects_format:
                                bottomSheet = new FormatBottomSheet();
                                break;
                            case R.id.create_effects_style:
                                bottomSheet = new StyleBottomSheet();
                                break;
                            case R.id.create_effects_line_spacing:
                                bottomSheet = new LineSpacingBottomSheet();
                                break;
                            case R.id.create_effects_background_color:
                                bottomSheet = new BackgroundColorBottomSheet();
                                break;
                            case R.id.create_effects_text_shadow:
                                bottomSheet = new ShadowBottomSheet();
                                break;
                            case R.id.create_effects_text_blur:
                                bottomSheet = new BlurBottomSheet();
                                break;
                            case R.id.create_effects_rotate:
                                bottomSheet = new RotateBottomSheet();
                                break;


                        }
                        if (bottomSheet != null)
                            bottomSheet.show(fragment, title);
                    }
                });
    }

    private void setTextViewFocusable(boolean isFocusable) {
        if (!isFocusable)
            textView.clearFocus();
        textView.setFocusable(isFocusable);
        textView.setFocusableInTouchMode(isFocusable);
    }

    private void setTextColor(int color) {
        textView.setTextColor(color);
    }

    private void setTextFont(Font font) {

    }

    private void setTextAlignment(Format textAlignment) {
        //textView.setTextAlignment(textAlignment);
        switch (textAlignment) {
            case LEFT:
                textView.setGravity(Gravity.START);
                break;
            case CENTER:
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case RIGHT:
                textView.setGravity(Gravity.END);
                break;
        }
    }

    private void setTextStyles(ArrayList<Style> styles) {
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(styles.contains(Style.BOLD));
        paint.setUnderlineText(styles.contains(Style.UNDERLINE));
        paint.setStrikeThruText(styles.contains(Style.STRIKETHROUGH));
        if (styles.contains(Style.ITALIC))
            paint.setTextSkewX(-0.25f);
        else paint.setTextSkewX(0f);
        if (styles.contains(Style.CAPITAL)) {
            //textView.setInputType(textView.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            //textView.setInputType(textView.getInputType() | ~InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            textView.setAllCaps(true);
        } else {
            textView.setAllCaps(false);
            //textView.setInputType(textView.getInputType() | ~InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            //textView.setInputType(textView.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        }
        textView.invalidate();
    }

    private void setTextLineSpacing(int lineSpacingProgress) {
        float lineSpacing = (float) lineSpacingProgress / 2;
        textView.setLineSpacing(lineSpacing, 1f);
    }

    private void setBackgroundColor(int color) {
        LayerDrawable backgroundDrawable = (LayerDrawable) textView.getBackground();
        GradientDrawable backgroundItem = (GradientDrawable) backgroundDrawable.getDrawable(0);
        backgroundItem.setColor(color);
    }

    private void setTextShadow(float radius, float dx, float dy, int color) {
        radius /= 10f;
        dx /= 4f;
        dy /= 4f;
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        textView.setShadowLayer(radius, dx, dy, color);
    }

    private void setTextBlur(float radius, BlurMaskFilter.Blur blurStyle) {
        if (radius > 0f) {
            radius /= 30;
            BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radius, blurStyle);
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            textView.getPaint().setMaskFilter(blurMaskFilter);
        } else {
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            textView.getPaint().setMaskFilter(null);
        }
    }

    private void setTextViewRotate(int rotate) {
        rotate /= 2;
        ((View) textView.getParent()).setRotation((float) rotate);
    }

    interface OnEyedropperColorSelectedListener {
        void onColorSelected(int color);
    }

    private class EyedropperController {
        private final OnEyedropperColorSelectedListener onColorSelectedListener;
        private Snackbar eyedropperSnackbar;
        private EyeDropper eyeDropper;
        protected boolean isEyedropperColor;
        private ImageButton eyedropperButton;

        EyedropperController(OnEyedropperColorSelectedListener onColorSelectedListener) {
            this.onColorSelectedListener = onColorSelectedListener;
        }

        private void disableEyedropper() {
            if (eyeDropper != null) {
                eyeDropper.disable();
                dismissSnackbar();
                setTextViewFocusable(true);
            }
        }

        private void showEyedropperButton() {
            if (eyedropperButton != null)
                eyedropperButton.setVisibility(View.VISIBLE);
        }

        private void hideEyedropperButton() {
            if (eyedropperButton != null)
                eyedropperButton.setVisibility(View.GONE);
        }

        private void setupEyedropperButton(ImageButton customButton) {
            this.eyedropperButton = customButton;
            customButton.setImageResource(R.drawable.ic_eyedropper_white_18dp);
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customButton.setSelected(!customButton.isSelected());
                    if (customButton.isSelected()) {
                        changeButtonColor(customButton, R.color.accent_color);
                        showSnackBar();
                        setupEyedropper();
                        setTextViewFocusable(false);
                    } else {
                        changeButtonColor(customButton, R.color.white);
                        disableEyedropper();
                    }
                }
            });
            customButton.setVisibility(View.VISIBLE);
        }

        private void showSnackBar() {
            if (eyedropperSnackbar == null)
                eyedropperSnackbar = CustomSnackbar.create(context, textView, R.string.eyedropper_message, Snackbar.LENGTH_LONG);
            if (!eyedropperSnackbar.isShownOrQueued())
                eyedropperSnackbar.show();
        }

        private void dismissSnackbar() {
            if (eyedropperSnackbar != null && eyedropperSnackbar.isShown()) {
                eyedropperSnackbar.getView().setVisibility(View.GONE);
                eyedropperSnackbar.dismiss();
            }

        }

        private void changeButtonColor(ImageButton customButton, int color) {
            Drawable icon = customButton.getDrawable();
            customButton.setImageDrawable(DrawableTint.tintDrawable(context, icon, color));
        }

        private void setupEyedropper() {
            if (eyeDropper == null) {
                View touchView = fragment.getGPUImageView().getChildAt(0);
                View backgroundView = fragment.getGPUImageView();
                ViewGroup viewLayout = fragment.getImageRoot();
                eyeDropper = new EyeDropper(touchView, backgroundView, viewLayout, new EyeDropper.ColorSelectionListener() {
                    @Override
                    public void onColorSelected(int color) {
                        onColorSelectedListener.onColorSelected(color);
                        Log.e("asd", "eyedropper " + String.format("#%06X", 0xFFFFFFFF & color));
                        isEyedropperColor = true;
                    }
                });
            } else {
                eyeDropper.enable();
            }
        }

    }

    private class FontBottomSheet extends BottomSheetController {

        private Typeface previousTypeface;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            LinearLayout fontLayout = (LinearLayout) layoutInflater.inflate(R.layout.create_effects_fonts, null);
            // Setup recyclerview for filter items.
            RecyclerView recyclerView = fontLayout.findViewById(R.id.create_effects_fonts_recyclerview);
            // Disable blinking when notifyitemchanged() is called.
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            FontAdapter fontAdapter = new FontAdapter(new FontAdapter.FontListner() {
                @Override
                public void onFontReady(Font font) {
                    setFont(font);
                }
            });
            recyclerView.setAdapter(fontAdapter);
            return fontLayout;
        }

        private void setFont(Font font) {
            setTextFont(font);
        }

        @Override
        protected void onShow() {
            this.previousTypeface = textView.getTypeface();
        }

        @Override
        public void onClose() {
            textView.setTypeface(previousTypeface);
        }
    }

    private class ColorBottomSheet extends BottomSheetController {
        protected int curColor;
        protected ColorPicker colorPicker;
        protected EyedropperController eyedropperController;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            LinearLayout colorLayout = (LinearLayout) layoutInflater.inflate(R.layout.create_effects_color, null);
            eyedropperController = new EyedropperController(new OnEyedropperColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    setColor(color);
                }
            });
            colorPicker = colorLayout.findViewById(R.id.create_effects_color_picker);
            colorPicker.setBarListener(new ColorPicker.OnColorBarListener() {
                @Override
                public void moveBar(int color) {
                    setColor(color);
                    Log.e("asd", "seekbar " + String.format("#%06X", 0xFFFFFFFF & color));
                    eyedropperController.isEyedropperColor = false;
                }
            });
            changeColorPicker();
            curColor = getProgress().curColor;
            eyedropperController.isEyedropperColor = getProgress().isEyedropperColor;
            if (!eyedropperController.isEyedropperColor)
                colorPicker.setProgress(getProgress().colorPickerProgress);
            return colorLayout;
        }

        protected void changeColorPicker() {

        }

        protected ColorProgress getProgress() {
            return textColorProgress;
        }

        @Override
        public void setupCustomButton(ImageButton customButton) {
            eyedropperController.setupEyedropperButton(customButton);
        }

        protected void setColor(int color) {
            setTextColor(color);
            this.curColor = color;
        }

        @Override
        public void onDone() {
            eyedropperController.disableEyedropper();
            setProgress();
        }

        protected void setProgress() {
            fragment.addProgress(textColorProgress);
            textColorProgress = new ColorProgress(curColor, colorPicker.getProgress(), eyedropperController.isEyedropperColor);
        }

        @Override
        public void onClose() {
            eyedropperController.disableEyedropper();
            setColor(getProgress().curColor);
        }
    }

    private class ColorProgress implements Progress {

        protected final int curColor;
        protected final ColorPicker.ColorPickerProgress colorPickerProgress;
        protected final boolean isEyedropperColor;

        public ColorProgress() {
            this.curColor = getDefaultColor();
            this.colorPickerProgress = new ColorPicker.ColorPickerProgress();
            this.isEyedropperColor = false;
        }

        public ColorProgress(int curColor, ColorPicker.ColorPickerProgress colorPickerProgress, boolean isEyedropperColor) {
            this.curColor = curColor;
            this.colorPickerProgress = colorPickerProgress;
            this.isEyedropperColor = isEyedropperColor;
        }

        protected void setColor() {
            setTextColor(curColor);
        }

        protected int getDefaultColor() {
            return Color.WHITE;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = textColorProgress;
            textColorProgress = this;
            setColor();
            return previousProgress;
        }
    }

    private class FormatBottomSheet extends BottomSheetController {
        private Format curTextAlignment;
        private CustomBottomBar formatBottomBar;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            FrameLayout formatLayout = (FrameLayout) layoutInflater.inflate(R.layout.create_effects_format, null);
            formatBottomBar = formatLayout.findViewById(R.id.create_effects_format_bottom_bar);
            formatBottomBar.addMenu(R.menu.create_effects_format, context, true, CustomBottomBar.Mode.SINGLE, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.format_left:
                            setAlignment(Format.LEFT);
                            break;
                        case R.id.format_center:
                            setAlignment(Format.CENTER);
                            break;
                        case R.id.format_right:
                            setAlignment(Format.RIGHT);
                            break;
                    }
                }
            });

            curTextAlignment = formatProgress.textAlignment;
            formatBottomBar.setSelectedItem(formatProgress.selectedItemPosition);

            return formatLayout;
        }

        private void setAlignment(Format textAlignment) {
            setTextAlignment(textAlignment);
            this.curTextAlignment = textAlignment;
        }

        @Override
        public void onClose() {
            setAlignment(formatProgress.textAlignment);
        }

        @Override
        public void onDone() {
            fragment.addProgress(formatProgress);
            formatProgress = new FormatProgress(curTextAlignment, formatBottomBar.getSelectedItemPosition());
        }
    }

    private class FormatProgress implements Progress {
        private final Format textAlignment;
        private final int selectedItemPosition;

        public FormatProgress() {
            this.textAlignment = Format.LEFT;
            this.selectedItemPosition = 0;
        }

        public FormatProgress(Format textAlignment, int selectedItemPosition) {
            this.textAlignment = textAlignment;
            this.selectedItemPosition = selectedItemPosition;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = formatProgress;
            formatProgress = this;
            setAlignment(textAlignment);
            return previousProgress;
        }

        private void setAlignment(Format textAlignment) {
            setTextAlignment(textAlignment);
        }
    }

    private class StyleBottomSheet extends BottomSheetController {

        private CustomBottomBar styleBottomBar;
        private ArrayList<Style> curStyles;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            FrameLayout styleLayout = (FrameLayout) layoutInflater.inflate(R.layout.create_effects_style, null);
            styleBottomBar = styleLayout.findViewById(R.id.create_effects_style_bottom_bar);
            styleBottomBar.addMenu(R.menu.create_effects_style, context, true, CustomBottomBar.Mode.MULTI, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.style_bold:
                            setStyle(Style.BOLD, item.isChecked());
                            break;
                        case R.id.style_italic:
                            setStyle(Style.ITALIC, item.isChecked());
                            break;
                        case R.id.style_underlined:
                            setStyle(Style.UNDERLINE, item.isChecked());
                            break;
                        case R.id.style_strikethrough:
                            setStyle(Style.STRIKETHROUGH, item.isChecked());
                            break;
                        case R.id.style_capital:
                            setStyle(Style.CAPITAL, item.isChecked());
                            break;
                    }
                }
            });

            curStyles = new ArrayList<>(styleProgress.styles);
            styleBottomBar.setSelectedItemsPositions(new ArrayList<>(styleProgress.selectedItemPositions));

            return styleLayout;
        }

        private void setStyle(Style style, boolean checked) {
            if (checked)
                curStyles.add(style);
            else curStyles.remove(style);

            setTextStyles(curStyles);
        }

        @Override
        public void onClose() {
            setTextStyles(styleProgress.styles);
        }

        @Override
        public void onDone() {
            fragment.addProgress(styleProgress);
            styleProgress = new StyleProgress(curStyles, styleBottomBar.getSelectedItemsPositions());
        }

    }

    private class StyleProgress implements Progress {

        private ArrayList<Integer> selectedItemPositions;
        private ArrayList<Style> styles;

        public StyleProgress() {
            styles = new ArrayList<>();
            selectedItemPositions = new ArrayList<>();
        }

        public StyleProgress(ArrayList<Style> styles, ArrayList<Integer> selectedItemPositions) {
            this.styles = styles;
            this.selectedItemPositions = selectedItemPositions;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = styleProgress;
            styleProgress = this;
            setStyles(styles);
            return previousProgress;
        }

        private void setStyles(ArrayList<Style> curStyles) {
            setTextStyles(curStyles);
        }
    }

    private class LineSpacingBottomSheet extends BottomSheetController {

        private CustomSeekBar lineSpacingSeekBar;
        private int curLineSpacing;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            RelativeLayout lineSpacingLayout = (RelativeLayout) layoutInflater.inflate(R.layout.create_effects_line_spacing, null);
            lineSpacingSeekBar = lineSpacingLayout.findViewById(R.id.create_effects_line_spacing_seekbar);
            lineSpacingSeekBar.setDefaultProgress(LineSpacingProgress.LINE_SPACING_SEEKBAR_PROGRESS_DEFAULT);
            lineSpacingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setLineSpacing(calculatedProgress(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            ImageButton refreshButton = lineSpacingLayout.findViewById(R.id.create_effects_line_spacing_refresh);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lineSpacingSeekBar.setProgress(LineSpacingProgress.LINE_SPACING_SEEKBAR_PROGRESS_DEFAULT);
                }
            });

            curLineSpacing = lineSpacingProgress.lineSpacing;
            lineSpacingSeekBar.setProgress(lineSpacingProgress.lineSpacingSeekbar);

            return lineSpacingLayout;
        }

        private int calculatedProgress(int progress) {
            int defaultProgress = LineSpacingProgress.LINE_SPACING_SEEKBAR_PROGRESS_DEFAULT;
            int calculatedProgress = progress - defaultProgress;

            return calculatedProgress;
        }

        @Override
        public void onDone() {
            fragment.addProgress(lineSpacingProgress);
            lineSpacingProgress = new LineSpacingProgress(curLineSpacing, lineSpacingSeekBar.getProgress());
        }

        @Override
        public void onClose() {
            setLineSpacing(lineSpacingProgress.lineSpacing);
        }

        private void setLineSpacing(int lineSpacing) {
            setTextLineSpacing(lineSpacing);
            curLineSpacing = lineSpacing;
        }
    }

    private class LineSpacingProgress implements Progress {

        public static final int LINE_SPACING_SEEKBAR_PROGRESS_DEFAULT = 50;
        private final int lineSpacing;
        private final int lineSpacingSeekbar;

        public LineSpacingProgress() {
            this.lineSpacing = 0;
            this.lineSpacingSeekbar = LINE_SPACING_SEEKBAR_PROGRESS_DEFAULT;
        }

        public LineSpacingProgress(int lineSpacing, int lineSpacingSeekbar) {
            this.lineSpacing = lineSpacing;
            this.lineSpacingSeekbar = lineSpacingSeekbar;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = lineSpacingProgress;
            lineSpacingProgress = this;
            setLineSpacing(lineSpacing);
            return previousProgress;
        }

        private void setLineSpacing(int lineSpacing) {
            setTextLineSpacing(lineSpacing);
        }
    }

    private class BackgroundColorBottomSheet extends ColorBottomSheet {

        @Override
        protected void changeColorPicker() {
            colorPicker.showAlphaBar();
        }

        @Override
        protected void setColor(int color) {
            setBackgroundColor(color);
            curColor = color;
        }

        @Override
        protected ColorProgress getProgress() {
            return backgroundColorProgress;
        }

        @Override
        protected void setProgress() {
            fragment.addProgress(backgroundColorProgress);
            backgroundColorProgress = new BackgroundColorProgress(curColor, colorPicker.getProgress(), eyedropperController.isEyedropperColor);
        }

        @Override
        public void onDone() {

        }
    }

    private class BackgroundColorProgress extends ColorProgress {

        public BackgroundColorProgress() {
            super();
            colorPickerProgress.alphaProgress = 0;
        }

        public BackgroundColorProgress(int curColor, ColorPicker.ColorPickerProgress colorPickerProgress, boolean isEyedropperColor) {
            super(curColor, colorPickerProgress, isEyedropperColor);
        }

        @Override
        protected int getDefaultColor() {
            return Color.TRANSPARENT;
        }

        @Override
        protected void setColor() {
            setBackgroundColor(curColor);
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = backgroundColorProgress;
            backgroundColorProgress = this;
            setColor();
            return previousProgress;
        }
    }

    private class ShadowBottomSheet extends BottomSheetController {
        public static final String POSITION = "Position";
        public static final String SIZE = "Size";
        public static final String COLOR = "Color";

        int radius;
        int dx;
        int dy;
        int curColor;
        private CustomSeekBar dxSeekbar;
        private CustomSeekBar dySeekbar;
        private AppCompatSeekBar radiusSeekbar;
        private ColorPicker colorPicker;
        private EyedropperController eyedropperController;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            RelativeLayout shadowLayout = (RelativeLayout) layoutInflater.inflate(R.layout.create_effects_text_shadow, null);
            // Setup eyedropper
            eyedropperController = new EyedropperController(new OnEyedropperColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    setShadow(radius, dx, dy, color);
                }
            });
            // Setup tabs
            RelativeLayout positionLayout = shadowLayout.findViewById(R.id.create_effects_text_shadow_position_layout);
            RelativeLayout sizeLayout = shadowLayout.findViewById(R.id.create_effects_text_shadow_size_layout);
            RelativeLayout colorLayout = shadowLayout.findViewById(R.id.create_effects_text_shadow_color_layout);
            CustomBottomBar bar = (CustomBottomBar) shadowLayout.findViewById(R.id.create_effects_text_shadow_bar);
            bar.addMenu(getBarTitles(), context, true, CustomBottomBar.Mode.SINGLE, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    int positionVisibility = View.GONE;
                    int sizeVisibility = View.GONE;
                    int colorVisibility = View.GONE;


                    if (item.getTitle().equals(POSITION))
                        positionVisibility = View.VISIBLE;
                    if (item.getTitle().equals(SIZE))
                        sizeVisibility = View.VISIBLE;
                    if (item.getTitle().equals(COLOR)) {
                        colorVisibility = View.VISIBLE;
                        eyedropperController.showEyedropperButton();
                    } else
                        eyedropperController.hideEyedropperButton();

                    positionLayout.setVisibility(positionVisibility);
                    sizeLayout.setVisibility(sizeVisibility);
                    colorLayout.setVisibility(colorVisibility);
                }
            });
            // Setup position seekbars.
            dxSeekbar = shadowLayout.findViewById(R.id.create_effects_text_shadow_dx_seekbar);
            dySeekbar = shadowLayout.findViewById(R.id.create_effects_text_shadow_dy_seekbar);
            dxSeekbar.setDefaultProgress(ShadowProgress.POSITION_SEEKBAR_PROGRESS_DEFAULT);
            dySeekbar.setDefaultProgress(ShadowProgress.POSITION_SEEKBAR_PROGRESS_DEFAULT);
            dxSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setShadow(radius, calculatePositionProgress(progress), dy, curColor);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            dySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setShadow(radius, dx, calculatePositionProgress(progress), curColor);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            // Setup size seekbar
            radiusSeekbar = shadowLayout.findViewById(R.id.create_effects_text_shadow_position_seekbar);
            radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setShadow(progress, dx, dy, curColor);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            // Setup colorpicker.
            colorPicker = shadowLayout.findViewById(R.id.create_effects_text_shadow_color_picker);
            colorPicker.setBarListener(new ColorPicker.OnColorBarListener() {
                @Override
                public void moveBar(int color) {
                    setShadow(radius, dx, dy, color);
                    eyedropperController.isEyedropperColor = false;
                }
            });
            // Setup from progress.
            radius = shadowProgress.radius;
            radiusSeekbar.setProgress(shadowProgress.radiusProgress);
            dx = shadowProgress.dx;
            dxSeekbar.setProgress(shadowProgress.dxProgress);
            dy = shadowProgress.dy;
            dySeekbar.setProgress(shadowProgress.dyProgress);
            // Color
            curColor = shadowProgress.color;
            eyedropperController.isEyedropperColor = shadowProgress.isEyedropperColor;
            if (!eyedropperController.isEyedropperColor)
                colorPicker.setProgress(shadowProgress.colorPickerProgress);
            setShadow(radius, dx, dy, curColor);
            return shadowLayout;
        }

        private int calculatePositionProgress(int progress) {
            int defaultProgress = ShadowProgress.POSITION_SEEKBAR_PROGRESS_DEFAULT;
            int calculatedProgress = progress - defaultProgress;
            return calculatedProgress;
        }

        private void setShadow(int radius, int dx, int dy, int color) {
            setTextShadow(radius, dx, dy, color);
            this.radius = radius;
            this.dx = dx;
            this.dy = dy;
            this.curColor = color;
        }

        private List<CharSequence> getBarTitles() {
            List<CharSequence> barTitles = new ArrayList<>();
            barTitles.add(SIZE);
            barTitles.add(POSITION);
            barTitles.add(COLOR);
            return barTitles;
        }

        @Override
        public void setupCustomButton(ImageButton customButton) {
            eyedropperController.setupEyedropperButton(customButton);
            eyedropperController.hideEyedropperButton();
        }

        @Override
        public void onDone() {
            fragment.addProgress(shadowProgress);
            shadowProgress = new ShadowProgress(radius, radiusSeekbar.getProgress(), dx, dxSeekbar.getProgress(), dy, dySeekbar.getProgress(), curColor, colorPicker.getProgress(), eyedropperController.isEyedropperColor);
            eyedropperController.disableEyedropper();
        }

        @Override
        public void onClose() {
            setShadow(shadowProgress.radius, shadowProgress.dx, shadowProgress.dy, shadowProgress.color);
            eyedropperController.disableEyedropper();
        }
    }

    private class ShadowProgress implements Progress {
        public static final int POSITION_SEEKBAR_PROGRESS_DEFAULT = 50;
        public static final int RADIUS_SEEKBAR_PROGRESS_DEFAULT = 0;
        public static final int RADIUS_DEFAULT = 0;
        public static final int DX_DEFAULT = 0;
        public static final int DY_DEFAULT = 0;
        private final int radius;
        private final int radiusProgress;
        private final int dx;
        private final int dxProgress;
        private final int dy;
        private final int dyProgress;
        private final int color;
        private final ColorPicker.ColorPickerProgress colorPickerProgress;
        private final boolean isEyedropperColor;

        public ShadowProgress() {
            this.radius = RADIUS_DEFAULT;
            this.radiusProgress = RADIUS_SEEKBAR_PROGRESS_DEFAULT;
            this.dx = DX_DEFAULT;
            this.dxProgress = POSITION_SEEKBAR_PROGRESS_DEFAULT;
            this.dy = DY_DEFAULT;
            this.dyProgress = POSITION_SEEKBAR_PROGRESS_DEFAULT;
            this.color = Color.WHITE;
            this.colorPickerProgress = new ColorPicker.ColorPickerProgress();
            this.isEyedropperColor = false;
        }

        public ShadowProgress(int radius, int radiusProgress, int dx, int dxProgress, int dy, int dyProgress, int color, ColorPicker.ColorPickerProgress colorPickerProgress, boolean isEyedropperColor) {
            this.radius = radius;
            this.radiusProgress = radiusProgress;
            this.dx = dx;
            this.dxProgress = dxProgress;
            this.dy = dy;
            this.dyProgress = dyProgress;
            this.color = color;
            this.colorPickerProgress = colorPickerProgress;
            this.isEyedropperColor = isEyedropperColor;
        }


        @Override
        public Progress restoreProgress() {
            Progress previousProgress = shadowProgress;
            shadowProgress = this;
            setTextShadow(radius, dx, dy, color);
            return previousProgress;
        }
    }

    private class BlurBottomSheet extends BottomSheetController {

        public static final String NORMAL = "Normal";
        public static final String INNER = "Inner";
        public static final String OUTER = "Outer";
        public static final String SOLID = "Solid";
        private AppCompatSeekBar radiusSeekbar;
        private CustomBottomBar styleBar;
        private float curRadius;
        private BlurMaskFilter.Blur curBlurStyle;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            RelativeLayout blurLayout = (RelativeLayout) layoutInflater.inflate(R.layout.create_effects_text_blur, null);
            radiusSeekbar = blurLayout.findViewById(R.id.create_effects_text_blur_radius_seekbar);
            styleBar = blurLayout.findViewById(R.id.create_effects_text_blur_style_bar);
            styleBar.addMenu(getStyleNames(), context, true, CustomBottomBar.Mode.SINGLE, new CustomBottomBar.OnMenuClickListener() {
                @Override
                public void onMenuItemClick(MenuItem item) {
                    BlurMaskFilter.Blur newBlurStyle = null;
                    String title = item.getTitle().toString();

                    if (title.equals(NORMAL))
                        newBlurStyle = BlurMaskFilter.Blur.NORMAL;
                    else if (title.equals(INNER))
                        newBlurStyle = BlurMaskFilter.Blur.INNER;
                    else if (title.equals(OUTER))
                        newBlurStyle = BlurMaskFilter.Blur.OUTER;
                    else if (title.equals(SOLID))
                        newBlurStyle = BlurMaskFilter.Blur.SOLID;

                    setBlur(curRadius, newBlurStyle);
                }
            });
            radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setBlur((float) progress, curBlurStyle);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            curRadius = blurProgress.curRadius;
            curBlurStyle = blurProgress.curBlurStyle;
            radiusSeekbar.setProgress(blurProgress.curRadiusProgress);

            return blurLayout;
        }

        private List<CharSequence> getStyleNames() {
            List<CharSequence> styleNames = new ArrayList<>();
            styleNames.add(NORMAL);
            styleNames.add(INNER);
            styleNames.add(OUTER);
            styleNames.add(SOLID);
            return styleNames;
        }

        private void setBlur(float radius, BlurMaskFilter.Blur blurStyle) {
            setTextBlur(radius, blurStyle);
            curRadius = radius;
            curBlurStyle = blurStyle;
        }


        @Override
        public void onDone() {
            fragment.addProgress(blurProgress);
            blurProgress = new BlurProgress(curRadius, radiusSeekbar.getProgress(), curBlurStyle);
        }

        @Override
        public void onClose() {
            setBlur(blurProgress.curRadius, blurProgress.curBlurStyle);
        }
    }

    private class BlurProgress implements Progress {

        int curRadiusProgress;
        float curRadius;
        BlurMaskFilter.Blur curBlurStyle;

        BlurProgress() {
            curRadius = 0f;
            curRadiusProgress = 0;
            curBlurStyle = BlurMaskFilter.Blur.NORMAL;
        }

        BlurProgress(float radius, int radiusProgress, BlurMaskFilter.Blur blurStyle) {
            curRadius = radius;
            curRadiusProgress = radiusProgress;
            curBlurStyle = blurStyle;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = blurProgress;
            blurProgress = this;
            setBlur();
            return previousProgress;
        }

        private void setBlur() {
            setTextBlur(curRadius, curBlurStyle);
        }
    }

    private class RotateBottomSheet extends BottomSheetController {

        private CustomSeekBar rotateSeekbar;
        private int curRotate;

        @Override
        protected View getContentView(LayoutInflater layoutInflater, Context context) {
            RelativeLayout rotateLayout = (RelativeLayout) layoutInflater.inflate(R.layout.create_effects_rotate, null);
            rotateSeekbar = rotateLayout.findViewById(R.id.create_effects_rotate_seekbar);
            rotateSeekbar.setDefaultProgress(RotateProgress.ROTATE_SEEKBAR_PROGRESS_DEFAULT);
            rotateSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setRotate(calculatedProgress(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            ImageButton refreshButton = rotateLayout.findViewById(R.id.create_effects_rotate_refresh);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateSeekbar.setProgress(RotateProgress.ROTATE_SEEKBAR_PROGRESS_DEFAULT);
                }
            });

            curRotate = rotateProgress.rotate;
            rotateSeekbar.setProgress(rotateProgress.rotateSeekbarProgress);

            return rotateLayout;
        }

        private int calculatedProgress(int progress) {
            int defaultProgress = RotateProgress.ROTATE_SEEKBAR_PROGRESS_DEFAULT;
            int calculatedProgress = progress - defaultProgress;
            return calculatedProgress;
        }

        private void setRotate(int rotate) {
            setTextViewRotate(rotate);
            curRotate = rotate;
        }

        @Override
        public void onDone() {
            fragment.addProgress(rotateProgress);
            rotateProgress = new RotateProgress(curRotate, rotateSeekbar.getProgress());
        }

        @Override
        public void onClose() {
            setRotate(rotateProgress.rotate);
        }
    }

    private class RotateProgress implements Progress {

        public static final int ROTATE_SEEKBAR_PROGRESS_DEFAULT = 50;
        private final int rotate;
        private final int rotateSeekbarProgress;

        RotateProgress() {
            this.rotate = 0;
            this.rotateSeekbarProgress = ROTATE_SEEKBAR_PROGRESS_DEFAULT;
        }

        RotateProgress(int rotate, int rotateProgress) {
            this.rotate = rotate;
            this.rotateSeekbarProgress = rotateProgress;
        }

        @Override
        public Progress restoreProgress() {
            Progress previousProgress = rotateProgress;
            rotateProgress = this;
            setTextViewRotate(rotate);
            return previousProgress;
        }
    }


}
