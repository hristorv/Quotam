package com.quotam.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quotam.model.Constants;
import com.quotam.model.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class SchedulerController {

    public static final String SCHEDULERS = "Schedulers";

    public ArrayList<Scheduler> getSchedulers(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(SCHEDULERS, null);
            ArrayList<Scheduler> schedulerList = gson.fromJson(json, new TypeToken<List<Scheduler>>(){}.getType());
        if (schedulerList == null)
            schedulerList = new ArrayList<>();

        return schedulerList;
    }

    public void addScheduler(Context context, Scheduler scheduler) {
        ArrayList<Scheduler> schedulerArrayList = getSchedulers(context);
        schedulerArrayList.add(scheduler);
        saveSchedulers(context, schedulerArrayList);
    }

    public void deleteScheduler(Context context, int schedulerPosition) {
        ArrayList<Scheduler> schedulerArrayList = getSchedulers(context);
        schedulerArrayList.remove(schedulerPosition);
        saveSchedulers(context, schedulerArrayList);
    }

    private void saveSchedulers(Context context, ArrayList<Scheduler> schedulerArrayList) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(schedulerArrayList);

        editor.putString(SCHEDULERS, json);
        editor.commit();
    }

}
