package com.quotam.fragments.steppers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.custom.CircularSlider;
import com.quotam.custom.CustomTabsLayout;
import com.quotam.model.Scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class SchedulerTime extends Fragment implements StepperPageFragment {

    private static final int MINUTE_DEFAULT_PROGRESS = 0;
    private static final String ALARM_DEFAULT_TEXT = "00";
    private static final int HOUR_DEFAULT_PROGRESS = 1;
    private static final int INTERVAL_DEFAULT_PROGRESS = 1;
    public static final int SHOW_HIDE_ANIMATION_DURATION = 150;
    private CircularSlider slider;
    private TextView interval_text;
    private TextView intervalTab;
    private TextView alarmTab;
    private FrameLayout sliderBase;
    private RelativeLayout alarmTextsBase;
    private TextView alarm_text_ampm;
    private TextView alarm_text_hour;
    private TextView alarm_text_minute;
    private ArrayList<String> hoursValues;
    private ArrayList<String> minutesValues;
    private int minuteProgress;
    private int hourProgress;
    private Scheduler.AMPM timeOfDay = Scheduler.AMPM.PM;
    private CustomTabsLayout alarm_days;
    private LinkedHashMap<String, Integer> intervalValues = new LinkedHashMap<>();
    {
        intervalValues.put("1 hour", 1);
        intervalValues.put("2 hour", 2);
        intervalValues.put("3 hours", 3);
        intervalValues.put("6 hours", 6);
        intervalValues.put("12 hours", 12);
        intervalValues.put("1 day", 24);
        intervalValues.put("2 days", 48);
    }
    private ArrayList<String> intervalValuesList = new ArrayList<String>(intervalValues.keySet());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_time,
                container, false);
        slider = rootView.findViewById(R.id.stepper_slider);
        interval_text = rootView.findViewById(R.id.stepper_interval_text);
        intervalTab = rootView.findViewById(R.id.stepper_interval_tab);
        alarmTab = rootView.findViewById(R.id.stepper_alarm_tab);
        sliderBase = rootView.findViewById(R.id.stepper_slider_base);
        alarmTextsBase = rootView.findViewById(R.id.stepper_alarm_texts_base);
        alarm_text_hour = rootView.findViewById(R.id.stepper_alarm_text_hour);
        alarm_text_minute = rootView.findViewById(R.id.stepper_alarm_text_minute);
        alarm_text_ampm = rootView.findViewById(R.id.stepper_alarm_text_ampm);
        alarm_days = rootView.findViewById(R.id.stepper_alarm_days);
        slider.setOnSlideChangeListener(new CircularSlider.OnSlideChangeListener() {
            @Override
            public void onProgressChanged(CircularSlider circularSlider, int progress, boolean fromUser) {
                sliderOnProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(CircularSlider circularSlider) {

            }

            @Override
            public void onStopTrackingTouch(CircularSlider circularSlider) {

            }
        });
        intervalTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectInterval();
            }
        });
        alarmTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlarm();
            }
        });
        alarm_text_ampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchAMPM();
            }
        });
        alarm_text_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHour(true);
            }
        });
        alarm_text_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMinute(true);
            }
        });
        setupAlarmDays();
        // Select interval for first time
        selectInterval();
        return rootView;
    }

    private void setupAlarmDays() {

        alarm_days.addMenu(R.menu.alarm_days, false, getActivity());
        alarm_days.setDefaultItem();


    }

    private void selectMinute(boolean fromUser) {
        if (!alarm_text_minute.isSelected() || !fromUser) {
            // Select minute
            select(alarm_text_minute, R.color.accent_color);
            // Deselect hour
            deselect(alarm_text_hour, R.color.primary_color);
            // Change slider
            slider.ignoreZeroPosition = false;
            slider.setMax(59);
            slider.setProgress(minuteProgress);
        }
    }

    private void selectHour(boolean fromUser) {
        if (!alarm_text_hour.isSelected() || !fromUser) {
            // Select hour
            select(alarm_text_hour, R.color.accent_color);
            // Deselect minute
            deselect(alarm_text_minute, R.color.primary_color);
            // Change slider
            slider.ignoreZeroPosition = true;
            slider.setMax(12);
            slider.setProgress(hourProgress);
        }
    }

    private void switchAMPM() {
        if (timeOfDay == Scheduler.AMPM.PM)
            timeOfDay = Scheduler.AMPM.AM;
        else if (timeOfDay == Scheduler.AMPM.AM)
            timeOfDay = Scheduler.AMPM.PM;
        alarm_text_ampm.setText(timeOfDay.toString());
    }

    private void animateHide(final View view) {
        view.animate()
                .alpha(0.0f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(SHOW_HIDE_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void animateShow(final View view) {
        view.animate()
                .alpha(1.0f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(SHOW_HIDE_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void animateHideShow(final View hideView, final View showView) {
        hideView.animate()
                .alpha(0.0f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(SHOW_HIDE_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        hideView.setVisibility(View.INVISIBLE);
                        animateShow(showView);
                    }
                });
    }

    private void selectAlarm() {
        if (!alarmTab.isSelected()) {
            deselectBackgroundTint(intervalTab, R.color.primary_color);
            selectBackgroundTint(alarmTab, R.color.accent_color);
            animateHideShow(interval_text, alarmTextsBase);
            alarmTab.setSelected(true);
            intervalTab.setSelected(false);
            // Select ampm for first time. Default is PM.
            alarm_text_ampm.setText(timeOfDay.toString());
            // Set defaults
            minuteProgress = MINUTE_DEFAULT_PROGRESS;
            hourProgress = HOUR_DEFAULT_PROGRESS;
            selectHour(false);
            setWithLeadingZero(alarm_text_minute, MINUTE_DEFAULT_PROGRESS);
            setWithLeadingZero(alarm_text_hour, HOUR_DEFAULT_PROGRESS);
        }
    }

    private void selectInterval() {
        if (!intervalTab.isSelected()) {
            deselectBackgroundTint(alarmTab, R.color.primary_color);
            selectBackgroundTint(intervalTab, R.color.accent_color);
            animateHideShow(alarmTextsBase, interval_text);
            intervalTab.setSelected(true);
            alarmTab.setSelected(false);
            // Change slider
            slider.ignoreZeroPosition = true;
            slider.setMax(intervalValuesList.size());
            slider.setProgress(INTERVAL_DEFAULT_PROGRESS);

        }
    }

    private void setWithLeadingZero(TextView tv, int value) {
        // This adds leading zero to all number from 1-9.
        String textValue = String.format(Locale.US, "%02d", value);
        tv.setText(textValue);
    }

    private void selectBackgroundTint(TextView view, int colorID) {
        view.setBackgroundColor(getContext().getResources().getColor(colorID));
        view.setSelected(true);
    }

    private void deselectBackgroundTint(TextView view, int colorID) {
        view.setBackgroundColor(getContext().getResources().getColor(colorID));
        view.setSelected(false);
    }

    private void select(TextView view, int colorID) {
        view.setTextColor(getContext().getResources().getColor(colorID));
        view.setSelected(true);
    }

    private void deselect(TextView view, int colorID) {
        view.setTextColor(getContext().getResources().getColor(colorID));
        view.setSelected(false);
    }

    private void sliderOnProgressChanged(int progress) {
        // Progress is from 1 to max, so we need to reduce it by 1.
        if (intervalTab.isSelected()) {
            progress -= 1;
            interval_text.setText(intervalValuesList.get(progress));
        }
        if (alarmTab.isSelected()) {
            if (alarm_text_hour.isSelected()) {
                hourProgress = progress;
                setWithLeadingZero(alarm_text_hour, progress);
            }
            if (alarm_text_minute.isSelected()) {
                minuteProgress = progress;
                setWithLeadingZero(alarm_text_minute, progress);
            }
        }
    }


    @Override
    public String isDone() {
        return null;
    }

    @Override
    public void applyChanges(Object objectToChange) {
        if (objectToChange instanceof Scheduler) {
            Scheduler scheduler = (Scheduler) objectToChange;
            if (intervalTab.isSelected()) {
                int intervalHours = intervalValues.get(interval_text.getText());
                scheduler.setInterval(scheduler.new Interval(intervalHours));
            } else if (alarmTab.isSelected()) {
                int hour = Integer.parseInt(alarm_text_hour.getText().toString());
                int minute = Integer.parseInt(alarm_text_minute.getText().toString());
                scheduler.setAlarm(scheduler.new Alarm(hour, minute, timeOfDay,getSelectedDays()));
            }
        }
    }

    private ArrayList<Scheduler.DaysOfWeek> getSelectedDays() {
        ArrayList<Scheduler.DaysOfWeek> daysOfWeek = new ArrayList<>();
        ArrayList<MenuItem> checkedItems = alarm_days.getCheckedItems();
        for (MenuItem item : checkedItems) {
            switch (item.getItemId()) {
                case R.id.monday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.MONDAY);
                    break;
                case R.id.tuesday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.TUESDAY);
                    break;
                case R.id.wednesday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.WEDNESDAY);
                    break;
                case R.id.thursday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.THURSDAY);
                    break;
                case R.id.friday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.FRIDAY);
                    break;
                case R.id.saturday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.SATURDAY);
                    break;
                case R.id.sunday:
                    daysOfWeek.add(Scheduler.DaysOfWeek.SUNDAY);
                    break;
            }
        }
        return daysOfWeek;
    }

}
