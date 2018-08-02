package com.quotam.controller;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class FilterAdjuster {
    private final Adjuster<? extends GPUImageFilter> adjuster;

    public FilterAdjuster(final GPUImageFilter filter) {
        if (filter instanceof GPUImageSharpenFilter) {
            adjuster = new SharpnessAdjuster().filter(filter);
        } else if (filter instanceof GPUImageSepiaFilter) {
            adjuster = new SepiaAdjuster().filter(filter);
        } else if (filter instanceof GPUImageContrastFilter) {
            adjuster = new ContrastAdjuster().filter(filter);
        } else if (filter instanceof GPUImageGammaFilter) {
            adjuster = new GammaAdjuster().filter(filter);
        } else if (filter instanceof GPUImageBrightnessFilter) {
            adjuster = new BrightnessAdjuster().filter(filter);
        } else if (filter instanceof GPUImageEmbossFilter) {
            adjuster = new EmbossAdjuster().filter(filter);
        } else if (filter instanceof GPUImageHueFilter) {
            adjuster = new HueAdjuster().filter(filter);
        } else if (filter instanceof GPUImagePixelationFilter) {
            adjuster = new PixelationAdjuster().filter(filter);
        } else if (filter instanceof GPUImageSaturationFilter) {
            adjuster = new SaturationAdjuster().filter(filter);
        } else if (filter instanceof GPUImageExposureFilter) {
            adjuster = new ExposureAdjuster().filter(filter);
        } else if (filter instanceof GPUImageHighlightShadowFilter) {
            adjuster = new HighlightShadowAdjuster().filter(filter);
        } else if (filter instanceof GPUImageOpacityFilter) {
            adjuster = new OpacityAdjuster().filter(filter);
        } else if (filter instanceof GPUImageGaussianBlurFilter) {
            adjuster = new GaussianBlurAdjuster().filter(filter);
        } else if (filter instanceof GPUImageHazeFilter) {
            adjuster = new HazeAdjuster().filter(filter);
        } else {
            adjuster = null;
        }
    }

    public boolean canAdjust() {
        return adjuster != null;
    }

    public void adjust(final int percentage) {
        if (adjuster != null) {
            adjuster.adjust(percentage);
        }
    }

    public int getProgress() {
        if (adjuster != null)
            return adjuster.curProgress;
        return 0;
    }

    public int getDefault() {
        if (adjuster != null)
            return adjuster.getDefault();
        return 0;
    }

    public GPUImageFilter getFilter() {
        return adjuster.getFilter();
    }

    private abstract class Adjuster<T extends GPUImageFilter> {
        protected T filter;
        protected float defaultStart;
        protected float start;
        protected float end;
        protected int curProgress;

        public Adjuster() {

        }

        @SuppressWarnings("unchecked")
        public Adjuster<T> filter(final GPUImageFilter filter) {
            this.filter = (T) filter;
            setup();
            return this;
        }

        public T getFilter() {
            return filter;
        }

        public abstract void adjust(int percentage);

        public abstract void setup();

        protected float range(final int percentage) {
            return (end - start) * percentage / 100.0f + start;
        }

        protected int reverseRange(float defaultStart) {
            return (int) ((defaultStart - start) * 100 / (end - start));
        }

        public int getDefault() {
            return reverseRange(defaultStart);
        }

    }

    private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setSharpness(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = -4.0f;
            end = 4.0f;
            curProgress = getDefault();
            this.filter.setSharpness(defaultStart);
        }
    }

    private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setPixel(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 1.0f;
            start = 1.0f;
            end = 5.0f;
            curProgress = getDefault();
            this.filter.setPixel(defaultStart);
        }
    }

    private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setHue(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = 0.0f;
            end = 360.0f;
            curProgress = getDefault();
            this.filter.setHue(defaultStart);
        }
    }

    private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setContrast(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 1.0f;
            start = 0.5f;
            end = 4.0f;
            curProgress = getDefault();
            this.filter.setContrast(defaultStart);
        }
    }

    private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setGamma(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 1.0f;
            start = 0.5f;
            end = 3.0f;
            curProgress = getDefault();
            this.filter.setGamma(defaultStart);
        }
    }

    private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setBrightness(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = -0.5f;
            end = 0.5f;
            curProgress = getDefault();
            this.filter.setBrightness(defaultStart);
        }
    }

    private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setIntensity(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = 0.0f;
            end = 2.0f;
            curProgress = getDefault();
            this.filter.setIntensity(defaultStart);
        }
    }

    private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setIntensity(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = 0.0f;
            end = 4.0f;
            curProgress = getDefault();
            this.filter.setIntensity(defaultStart);
        }
    }

    private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setSaturation(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 1.0f;
            start = 0.0f;
            end = 2.0f;
            curProgress = getDefault();
            this.filter.setSaturation(defaultStart);
        }
    }

    private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setExposure(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = -2.0f;
            end = 2.0f;
            curProgress = getDefault();
            this.filter.setExposure(defaultStart);
        }
    }

    private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setShadows(range(percentage));
            //getFilter().setHighlights(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = 0.0f;
            end = 1.0f;
            curProgress = getDefault();
            this.filter.setShadows(defaultStart);
            this.filter.setHighlights(1.0f);
        }
    }

    private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setOpacity(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = 0.0f;
            end = 1.0f;
            curProgress = getDefault();
            this.filter.setOpacity(defaultStart);
        }
    }

    private class GaussianBlurAdjuster extends Adjuster<GPUImageGaussianBlurFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setBlurSize(range(percentage));
            //super.filter = new GPUImageGaussianBlurFilter(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = 0.0f;
            end = 10.0f;
            curProgress = getDefault();
            this.filter.setBlurSize(defaultStart);
        }
    }

    private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setDistance(range(percentage));
            getFilter().setSlope(range(percentage));
            curProgress = percentage;
        }

        @Override
        public void setup() {
            defaultStart = 0.0f;
            start = -0.3f;
            end = 0.3f;
            curProgress = getDefault();
            this.filter.setDistance(defaultStart);
            this.filter.setSlope(defaultStart);
        }
    }

}
