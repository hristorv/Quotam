package com.quotam.fragments.steppers;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.model.Scheduler;
import com.quotam.utils.DrawableTint;

public class SchedulerType extends Fragment implements StepperPageFragment {

    private static final long ANIMATION_DURATION = 150;
    private LinearLayout base;
    private View curSelectedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_type,
                container, false);
        base = (LinearLayout) rootView;
        addView(inflater, Scheduler.Type.Wallpaper, R.string.wallpaper, R.drawable.ic_wallpaper_white_48dp);
        addView(inflater, Scheduler.Type.Notification, R.string.notification, R.drawable.ic_notifications_white_48dp);
        addView(inflater, Scheduler.Type.Widget, R.string.widget, R.drawable.ic_widgets_white_48dp);

        return rootView;
    }

    private void addView(LayoutInflater inflater, Scheduler.Type typeTag, int stringID, int iconID) {
        View view = inflater.inflate(R.layout.type_item, base, false);
        TextView title = view.findViewById(R.id.type_title);
        title.setText(stringID);
        ImageView image = view.findViewById(R.id.type_icon);
        image.setImageDrawable(DrawableTint.tintDrawable(getContext(), iconID, R.color.secondary_text_color));
        LinearLayout linearBase = view.findViewById(R.id.type_linear_base);
        linearBase.setTag(typeTag);
        linearBase.setOnClickListener(new TypeOnClickListner());
        base.addView(view);
    }

    public String isDone() {
        if (curSelectedView == null)
            return getResources().getString(R.string.schedulers_type_error);
        return null;
    }

    @Override
    public void applyChanges(Object objectToChange) {
        if (objectToChange instanceof Scheduler) {
            Scheduler scheduler = (Scheduler) objectToChange;
            scheduler.setType((Scheduler.Type) curSelectedView.getTag());
        }
    }

    private void animateColors(final View view, int previousColor, int toColor) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(getResources().getColor(previousColor), getResources().getColor(toColor));
        anim.setEvaluator(new ArgbEvaluator());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int color = (Integer) valueAnimator.getAnimatedValue();
                view.setBackgroundColor(color);
            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    private void animateSelect(View baseView) {
        TextView title = baseView.findViewById(R.id.type_title);
        View divider = baseView.findViewById(R.id.type_divider);
        ImageView icon = baseView.findViewById(R.id.type_icon);
        baseView.setBackgroundColor(getContext().getResources().getColor(R.color.accent_color));
        //icon.setImageDrawable(DrawableTint.tintDrawable(getContext(),icon.getDrawable(),R.color.accent_color));
        //divider.setBackgroundColor(getContext().getResources().getColor(R.color.accent_color));
        //title.setTextColor(getContext().getResources().getColor(R.color.accent_color));
    }

    private void animateDeselect(View baseView) {
        TextView title = baseView.findViewById(R.id.type_title);
        View divider = baseView.findViewById(R.id.type_divider);
        ImageView icon = baseView.findViewById(R.id.type_icon);
        baseView.setBackgroundColor(getContext().getResources().getColor(R.color.primary_color));
        //icon.setImageDrawable(DrawableTint.tintDrawable(getContext(),icon.getDrawable(),R.color.secondary_text_color));
        //divider.setBackgroundColor(getContext().getResources().getColor(R.color.secondary_text_color));
        //title.setTextColor(getContext().getResources().getColor(R.color.primary_text_color));
    }

    private class TypeOnClickListner implements View.OnClickListener {

        @Override
        public void onClick(View selectedView) {
            boolean isSelected = selectedView.isSelected();
            if (!isSelected) {
                if (curSelectedView != null) {
                    animateColors(curSelectedView, R.color.accent_color, R.color.primary_color);
                    //animateDeselect(curSelectedView);
                    curSelectedView.setSelected(false);
                }
                curSelectedView = selectedView;
                animateColors(selectedView, R.color.primary_color, R.color.accent_color);
                //animateSelect(selectedView);
                selectedView.setSelected(true);
            }
        }
    }

}
