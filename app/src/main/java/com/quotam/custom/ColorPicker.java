package com.quotam.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.quotam.R;
import com.quotam.utils.Convertor;

import java.util.Arrays;


public class ColorPicker extends LinearLayout {

    public static final int MAX_ALPHA_PROGRESS = 100;
    private static final int PROGRESS_RADIUS = Convertor.convertDpToPixel(2);
    public static final int ALPHA_MAX_VALUE = 255;

    public interface OnColorBarListener {
        void moveBar(int color);
    }

    private int[] mainBarColors = new int[]{Color.rgb(255, 0, 0),
            Color.rgb(255, 255, 0), Color.rgb(0, 255, 0),
            Color.rgb(0, 255, 255), Color.rgb(0, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 0)};
    private int[] subBarColors = new int[]{Color.WHITE, Color.RED, Color.BLACK};

    private float[] mainBarPositions;
    private float[] subBarPositions;

    private AppCompatSeekBar mainBar;
    private AppCompatSeekBar subBar;
    private AppCompatSeekBar alphaBar;
    private int curSubBarColor = Color.WHITE;
    private int curSubBarProgress = 0;
    private int curAlphaBarProgress = 100;

    public ColorPicker(Context context) {
        super(context);
        init(context);
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        linearInit();
        subBarPositions = initPositions(subBarColors);
        mainBarPositions = initPositions(mainBarColors);
        Log.e("asd", "subBar: " + Arrays.toString(subBarPositions));
        Log.e("asd", "mainBar: " + Arrays.toString(mainBarPositions));
        barInit(context);
        addView(mainBar);
        addView(subBar);
        addView(alphaBar);
        alphaBar.setVisibility(View.GONE);
    }

    private float[] initPositions(int[] colors) {
        int colorsLength = colors.length;
        float[] positions = new float[colorsLength];
        float diff = 1f / (float) (colorsLength - 1);
        for (int i = 0; i < colorsLength; i++) {
            positions[i] = (float) i * diff;
        }
        return positions;
    }

    public void showAlphaBar() {
        alphaBar.setVisibility(View.VISIBLE);
        alphaBar.setProgress(0);
    }

    private void barInit(Context context) {
        mainBar = (AppCompatSeekBar) inflate(context, R.layout.color_picker_seekbar, null);
        subBar = (AppCompatSeekBar) inflate(context, R.layout.color_picker_seekbar, null);
        alphaBar = (AppCompatSeekBar) inflate(context, R.layout.color_picker_seekbar, null);
        alphaBar.setProgress(MAX_ALPHA_PROGRESS);
        initMainBarBg();
        setSubBarBg(Color.RED);
        setAlphaBarBg(Color.WHITE);
        changeThumbColor(mainBar, mainBarColors[0]);
        changeThumbColor(subBar, subBarColors[0]);
        changeThumbColor(alphaBar, getAlphaColor(Color.WHITE, alphaBar.getProgress()));
    }

    private void initMainBarBg() {
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        mainBarColors, mainBarPositions, Shader.TileMode.REPEAT);
                return linearGradient;
            }
        };
        PaintDrawable paintMain = new PaintDrawable();
        paintMain.setShape(new RectShape());
        paintMain.setCornerRadius(PROGRESS_RADIUS);
        paintMain.setShaderFactory(shaderFactory);
        mainBar.setProgressDrawable(paintMain);
    }

    private void setSubBarBg(final int color) {
        subBarColors[1] = color;
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        subBarColors, subBarPositions, Shader.TileMode.REPEAT);
                return linearGradient;
            }
        };
        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setCornerRadius(PROGRESS_RADIUS);
        paint.setShaderFactory(shaderFactory);
        // Fix for the resizing progress when setting the new drawable.
        Rect bounds = subBar.getProgressDrawable().getBounds();
        subBar.setProgressDrawable(paint);
        subBar.getProgressDrawable().setBounds(bounds);
    }

    private void setAlphaBarBg(final int color) {
        int colorStart = getAlphaColor(color, 0);
        int colorEnd = getAlphaColor(color, alphaBar.getMax());
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        colorStart, colorEnd, Shader.TileMode.CLAMP);
                return linearGradient;
            }
        };
        AlphaPatternDrawable alphaPatternDrawable = new AlphaPatternDrawable(Convertor.convertDpToPixel(4));

        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setCornerRadius(PROGRESS_RADIUS);
        paint.setShaderFactory(shaderFactory);

        LayerDrawable finalDrawable = new LayerDrawable(new Drawable[]{alphaPatternDrawable, paint});

        // Fix for the resizing progress when setting the new drawable.
        Rect bounds = alphaBar.getProgressDrawable().getBounds();
        alphaBar.setProgressDrawable(finalDrawable);
        alphaBar.getProgressDrawable().setBounds(bounds);
    }

    private void linearInit() {
        setWeightSum(2);
        setOrientation(VERTICAL);
        setGravity(Gravity.TOP);
    }

    private void changeThumbColor(AppCompatSeekBar seekBar, int color) {
        Drawable thumb = seekBar.getThumb();
        ((GradientDrawable) thumb).setColor(color);
    }

    public void setBarListener(final OnColorBarListener listener) {
        mainBar.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float radio = (float) progress / subBar.getMax();
                int mainBarColor = getColor(radio, mainBarColors, mainBarPositions);
                setSubBarBg(mainBarColor);
                calculateCurSubBarColor();
                setAlphaBarBg(curSubBarColor);
                int alphaBarColor = getAlphaColor(curSubBarColor, curAlphaBarProgress);

                listener.moveBar(alphaBarColor);
                changeThumbColor(mainBar, mainBarColor);
                changeThumbColor(subBar, curSubBarColor);
                changeThumbColor(alphaBar, alphaBarColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        subBar.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curSubBarProgress = progress;
                calculateCurSubBarColor();
                setAlphaBarBg(curSubBarColor);
                int alphaBarColor = getAlphaColor(curSubBarColor, curAlphaBarProgress);
                listener.moveBar(alphaBarColor);
                changeThumbColor(subBar, curSubBarColor);
                changeThumbColor(alphaBar, alphaBarColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curAlphaBarProgress = progress;
                int alphaBarColor = getAlphaColor(curSubBarColor, curAlphaBarProgress);
                listener.moveBar(alphaBarColor);
                changeThumbColor(alphaBar, alphaBarColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int calculateCurSubBarColor() {
        float radioSub = (float) curSubBarProgress / subBar.getMax();
        curSubBarColor = getColor(radioSub, subBarColors, subBarPositions);
        return curSubBarColor;
    }

//    private void defaultBarListener() {
//        mainBar.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                float radio = (float) progress / subBar.getMax();
//                setSubBarBg(getColor(radio, mainBarColors, mainBarPositions));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }

    private int getAlphaColor(int subBarColor, int curAlphaBarProgress) {
        float alpha = (float) curAlphaBarProgress * ((float) ALPHA_MAX_VALUE / (float) alphaBar.getMax());
        int red = Color.red(subBarColor);
        int green = Color.green(subBarColor);
        int blue = Color.blue(subBarColor);
        return Color.argb((int) alpha, red, green, blue);
    }

    public int getColor(float radio, int[] colorArr, float[] positionArr) {
        int startColor;
        int endColor;
        if (radio >= 1) {
            return colorArr[colorArr.length - 1];
        }
        for (int i = 0; i < positionArr.length; i++) {
            if (radio <= positionArr[i]) {
                if (i == 0) {
                    return colorArr[0];
                }
                startColor = colorArr[i - 1];
                endColor = colorArr[i];
                float areaRadio = getAreaRadio(radio, positionArr[i - 1], positionArr[i]);
                return getColorFrom(startColor, endColor, areaRadio);
            }
        }
        return -1;
    }

    private float getAreaRadio(float radio, float startPosition, float endPosition) {
        return (radio - startPosition) / (endPosition - startPosition);
    }

    public int getColorFrom(int startColor, int endColor, float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

    public ColorPickerProgress getProgress() {
        return new ColorPickerProgress(this);
    }

    public void setProgress(ColorPickerProgress colorPickerProgress) {
        mainBar.setProgress(colorPickerProgress.mainProgress);
        subBar.setProgress(colorPickerProgress.subProgress);
        alphaBar.setProgress(colorPickerProgress.alphaProgress);
    }

    public static class ColorPickerProgress {
        public int mainProgress;
        public int subProgress;
        public int alphaProgress;

        public ColorPickerProgress() {
            mainProgress = 0;
            subProgress = 0;
            alphaProgress = 100;
        }

        public ColorPickerProgress(ColorPicker colorPicker) {
            this.mainProgress = colorPicker.mainBar.getProgress();
            this.subProgress = colorPicker.subBar.getProgress();
            this.alphaProgress = colorPicker.alphaBar.getProgress();
        }

    }

}
