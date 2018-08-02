package com.quotam.fragments;

import com.quotam.R;
import com.quotam.custom.IndicatorView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpFragment extends DialogFragment {

    public static final int TEXT_ANIMATION_LENGTH = 200;

    private Button buttonPrevious;
    private Button buttonNext;
    private ViewPager pager;

    Page[] pages = {
            new Page(R.drawable.illustration_help_categories, R.color.help_categories, R.string.help_title_categories, R.string.help_text_categories),
            new Page(R.drawable.illustration_help_create, R.color.help_create, R.string.help_title_create, R.string.help_text_create),
            new Page(R.drawable.illustration_help_albums, R.color.help_albums, R.string.help_title_albums, R.string.help_text_albums),
            new Page(R.drawable.illustration_help_timeline, R.color.help_timeline, R.string.help_title_timeline, R.string.help_text_timeline),
            new Page(R.drawable.illustration_help_search, R.color.help_search, R.string.help_title_search, R.string.help_text_search),
            new Page(R.drawable.illustration_help_schedulers, R.color.help_schedulers, R.string.help_title_schedulers, R.string.help_text_schedulers),
    };

    int numOfPages = pages.length;
    private IndicatorView[] indicators = new IndicatorView[numOfPages];
    private ArgbEvaluator evaluator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container,
                false);
        pager = rootView.findViewById(R.id.help_pager);
        LinearLayout indicatorLayout = rootView.findViewById(R.id.help_layout_dots);

        buttonPrevious = rootView.findViewById(R.id.help_button_previous);
        buttonPrevious.setOnClickListener(new PreviousClickListener());
        buttonNext = rootView.findViewById(R.id.help_button_next);
        buttonNext.setOnClickListener(new NextClickListener());

        evaluator = new ArgbEvaluator();
        addIndicators(indicatorLayout);
        setPagerListener(indicatorLayout);

        pager.setAdapter(new ScreenSlidePagerAdapter());

        return rootView;
    }

    private void addIndicators(LinearLayout indicatorLayout) {

        for (int i = 0; i < numOfPages; i++) {
            IndicatorView indicator = new IndicatorView(getActivity());
            indicator.setRadius(10);
            // indicator.setFilled(true);
            if (i == 0) {
                indicator.setColor(getActivity().getResources().getColor(R.color.help_indicator_selected));
            } else {
                indicator.setColor(getActivity().getResources().getColor(R.color.help_indicator_default));
            }
            indicators[i] = indicator;
            indicatorLayout.addView(indicator, i, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getActivity() != null)
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onDismiss(dialog);
    }

    private void setPagerListener(LinearLayout dotsLayout) {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int getPageColor(int position) {
                return getResources().getColor(pages[position].color);
            }

            @Override
            public void onPageSelected(int position) {
                swipeIndicator(position);
                pager.setBackgroundResource(pages[position].color);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, getPageColor(position), getPageColor(position == pages.length - 1 ? position : position + 1));
                pager.setBackgroundColor(colorUpdate);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void swipeIndicator(int position) {
        for (IndicatorView indicator : indicators) {
            indicator.setColor(getActivity().getResources().getColor(R.color.help_indicator_default));
        }
        indicators[position].setColor(getActivity().getResources().getColor(R.color.help_indicator_selected));

        if (position == 0) {
            buttonPrevious.animate()
                    .alpha(0.0f)
                    .setDuration(TEXT_ANIMATION_LENGTH)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            buttonPrevious.setVisibility(View.INVISIBLE);
                        }
                    });
        }
        if (position == 1 && buttonPrevious.getVisibility() == View.INVISIBLE) {
            buttonPrevious.animate()
                    .alpha(1.0f)
                    .setDuration(TEXT_ANIMATION_LENGTH)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            buttonPrevious.setVisibility(View.VISIBLE);
                        }
                    });
        }
        if (position == numOfPages - 1) {
            buttonNext.animate()
                    .alpha(0.0f)
                    .setDuration(TEXT_ANIMATION_LENGTH)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            buttonNext.setText(R.string.help_done);
                            buttonNext.animate()
                                    .alpha(1.0f)
                                    .setDuration(TEXT_ANIMATION_LENGTH);
                        }
                    });
        }
        if (position == numOfPages - 2 && buttonNext.getText().equals(getActivity().getResources().getString(R.string.help_done))) {
            buttonNext.animate()
                    .alpha(0.0f)
                    .setDuration(TEXT_ANIMATION_LENGTH)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            buttonNext.setText(R.string.help_next);
                            buttonNext.animate()
                                    .alpha(1.0f)
                                    .setDuration(TEXT_ANIMATION_LENGTH);
                        }
                    });
        }
    }

    private class NextClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (pager.getCurrentItem() == numOfPages - 1) {
                HelpFragment.this.dismiss();
            } else {
                int itemNum = pager.getCurrentItem() + 1;
                pager.setCurrentItem(itemNum, true);
            }

        }
    }

    private class PreviousClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int itemNum = pager.getCurrentItem() - 1;
            pager.setCurrentItem(itemNum, true);
        }

    }


    private class ScreenSlidePagerAdapter extends PagerAdapter {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        @Override
        public int getCount() {
            return numOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View pageLayout = inflater.inflate(R.layout.help_page_layout,
                    container, false);

            ImageView imageView = pageLayout
                    .findViewById(R.id.help_page_image);
            TextView titleView = pageLayout
                    .findViewById(R.id.help_page_title);
            TextView textView = pageLayout
                    .findViewById(R.id.help_page_text);
            FrameLayout background = pageLayout
                    .findViewById(R.id.help_background);

            Page currentPage = pages[position];
            //background.setBackgroundResource(currentPage.color);
            imageView.setImageResource(currentPage.image);
            titleView.setText(currentPage.title);
            textView.setText(currentPage.text);

            container.addView(pageLayout, 0);
            return pageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class Page {


        final int image;
        final int color;
        final int title;
        final int text;

        public Page(int image, int color, int title, int text) {
            this.image = image;
            this.color = color;
            this.title = title;
            this.text = text;
        }

    }

    public class ColorTransformer implements ViewPager.PageTransformer {

        public boolean inRange(final float position) {
            return position <= 1.0 && position >= -1.0;
        }

        public boolean isLeftPage(final float position) {
            return position < 0;
        }

        public boolean isRightPage(final float position) {
            return position > 0;
        }

        private int blendColors(int color1, int color2, float ratio) {
            final float inverseRation = 1f - ratio;
            float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
            float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
            float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
            return Color.rgb((int) r, (int) g, (int) b);
        }

        @Override
        public void transformPage(final View page, final float position) {
            int pageIndex = pager.getCurrentItem() + 1;
            if (inRange(position)) {
                if (isRightPage(position)) {

                    final int leftIndex = pageIndex - 1;
                    final int rightIndex = pageIndex;

                    final int leftColor = getPageColor(leftIndex);
                    final int rightColor = getPageColor(rightIndex);

                    final int composedColor = blendColors(leftColor, rightColor, position);
                    page.setBackgroundColor(composedColor);
                } else if (isLeftPage(position)) {

                    final int leftIndex = pageIndex;
                    final int rightIndex = pageIndex + 1;

                    final int leftColor = getPageColor(leftIndex);
                    final int rightColor = getPageColor(rightIndex);

                    final int composedColor = blendColors(leftColor, rightColor, 1 - Math.abs(position));
                    page.setBackgroundColor(composedColor);
                } else {
                    page.setBackgroundColor(getPageColor(pageIndex));
                }
            } else {
                page.setBackgroundColor(getPageColor(pageIndex));
            }
        }

        private int getPageColor(int pageIndex) {
            return getResources().getColor(pages[pageIndex].color);
        }

    }

}
