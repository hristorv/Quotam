package com.quotam.model;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    public enum Type {
        Wallpaper, Notification, Widget
    }

    public enum AMPM {
        AM,
        PM
    }

    Type type;
    TimeType timeType;

    Alarm alarm = null;
    Interval interval = null;
    List<Object> collections = new ArrayList<>();
    boolean enabled = true;

    public enum TimeType {
        INTERVAL,
        ALARM
    }

    public enum DaysOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public class Alarm {
        public int hour;
        public int minute;
        public AMPM ampm;
        ArrayList<DaysOfWeek> daysOfWeek;

        public Alarm(int hour, int minute, AMPM ampm, ArrayList<DaysOfWeek> daysOfWeek) {
            this.hour = hour;
            this.minute = minute;
            this.ampm = ampm;
            this.daysOfWeek = daysOfWeek;
            timeType = TimeType.ALARM;
        }
    }

    public class Interval {
        public int intervalInHours;

        public Interval(int intervalInHours) {
            this.intervalInHours = intervalInHours;
            timeType = TimeType.INTERVAL;
        }
    }

    public Scheduler() {

    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCollections(List<Object> collections) {
        this.collections = collections;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Type getType() {
        return type;
    }

    public List<Object> getCollections() {
        return collections;
    }

}