package com.quotam.controller;

import com.quotam.model.Constants;
import com.quotam.services.NotificationService;
import com.quotam.services.ServiceController;
import com.quotam.services.WallpaperService;
import com.quotam.services.WidgetService;
import com.quotam.utils.ConnectionManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int serviceTag = intent.getExtras().getInt(
				Constants.Extra.SERVICE_TAG_KEY);
		if (intent != null
				&& intent.getAction() != null
				&& intent.getAction().equals(
						"android.intent.action.BOOT_COMPLETED")) {
			setupAlarms(context);
		} else {
			startServiceByTag(serviceTag, context);
		}
	}

	private void setupAlarms(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constants.Extra.DEFAULT_PREFS_NAME, Context.MODE_PRIVATE);
		AlarmController alarmController = new AlarmController();
		if (sharedPref.getBoolean(
				Constants.PreferencesKeys.KEY_PREF_WALLPAPER_ACTIVE, false)) {
			alarmController.setupAlarmByID(context,
					Constants.ServicesID.WALLPAPER_ALARM_ID,
					alarmController.getRepeatTimeInMillSec(context,
							Constants.PreferencesKeys.KEY_PREF_WALLPAPER_TIME));
		}
		if (sharedPref.getBoolean(
				Constants.PreferencesKeys.KEY_PREF_WIDGET_ACTIVE, false)) {
			alarmController.setupAlarmByID(context,
					Constants.ServicesID.WIDGET_ALARM_ID,
					alarmController.getRepeatTimeInMillSec(context,
							Constants.PreferencesKeys.KEY_PREF_WIDGET_TIME));
		}
		if (sharedPref.getBoolean(
				Constants.PreferencesKeys.KEY_PREF_NOTIFICATION_ACTIVE, false)) {
			alarmController
					.setupAlarmByID(
							context,
							Constants.ServicesID.NOTIFICATION_ALARM_ID,
							alarmController
									.getRepeatTimeInMillSec(
											context,
											Constants.PreferencesKeys.KEY_PREF_NOTIFICATION_TIME));
		}
	}

	private void startServiceByTag(int serviceTag, Context context) {
		if (serviceTag == Constants.ServicesID.WALLPAPER_ALARM_ID)
			startWallpaperService(context);
		if (serviceTag == Constants.ServicesID.WIDGET_ALARM_ID)
			startWidgetService(context);
		if (serviceTag == Constants.ServicesID.NOTIFICATION_ALARM_ID)
			startNotificationService(context);
	}

	private void startNotificationService(Context context) {
		ServiceController serviceController = new ServiceController();
		if (ConnectionManager.isConnected(context)) {
Log.e("STARTING SERVICE", "asd");
			Intent serviceIntent = new Intent(context,
					NotificationService.class);
			context.startService(serviceIntent);
			serviceController.removeErrorNotification(context,
					Constants.NotificationIDs.NOTIFICATION_NOTIF_ERROR);
		} else {
			serviceController.makeErrorNotification(context, ServiceController.NOTIFICATION);
		}
	}

	private void startWallpaperService(Context context) {
		ServiceController serviceController = new ServiceController();
		if (ConnectionManager.isConnected(context)) {
			Intent serviceIntent = new Intent(context, WallpaperService.class);
			context.startService(serviceIntent);
			serviceController.removeErrorNotification(context,
					Constants.NotificationIDs.NOTIFICATION_WALLPAPER_ERROR);
		} else {
			serviceController.makeErrorNotification(context, ServiceController.WALLPAPER);
		}
	}

	private void startWidgetService(Context context) {
		ServiceController serviceController = new ServiceController();
		if (ConnectionManager.isConnected(context)) {
			serviceController.removeErrorMessage(context);
			Intent serviceIntent = new Intent(context, WidgetService.class);
			context.startService(serviceIntent);
		} else {
			serviceController.addErrorMessage(context);
		}
	}

}