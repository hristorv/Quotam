package com.quotam.activities;

import com.quotam.R;
import com.quotam.controller.AlarmController;
import com.quotam.controller.AlarmReceiver;
import com.quotam.controller.PreferencesListener;
import com.quotam.model.Constants;
import com.quotam.fragments.WidgetConfigFragment;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class WidgetConfigActivity extends Activity {
	SharedPreferences.OnSharedPreferenceChangeListener listener;
	int mAppWidgetId;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the application widget ID
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_CANCELED, resultValue);
		// Set the preferences listener
		listener = new PreferencesListener(this);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		// Launch the settings fragment
		WidgetConfigFragment fr = new WidgetConfigFragment();
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, fr, "widgetConfigFragment")
				.commit();

	}

	@Override
	protected void onResume() {
		super.onResume();
		prefs.registerOnSharedPreferenceChangeListener(listener);

	}

	@Override
	protected void onPause() {
		super.onPause();
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void onBackPressed() {
		startWidget();
		super.onBackPressed();
	}

	private void startWidget() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		AlarmController alarmController = new AlarmController();
		alarmController.setupBroadCastReceiver(this);
		alarmController
				.setupAlarmByID(this, Constants.ServicesID.WIDGET_ALARM_ID,
						alarmController.getRepeatTimeInMillSec(this,
								Constants.PreferencesKeys.KEY_PREF_WIDGET_TIME));
		SharedPreferences prefs = this.getSharedPreferences(
				Constants.Extra.DEFAULT_PREFS_NAME, Context.MODE_PRIVATE);
		prefs.edit()
				.putBoolean(Constants.PreferencesKeys.KEY_PREF_WIDGET_ACTIVE,
						true).commit();
		finishActivity(this);
	}

	private void startWidgetService(Context context) {
		int ID = Constants.ServicesID.WIDGET_ALARM_ID;
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(Constants.Extra.SERVICE_TAG_KEY, ID);
		context.sendBroadcast(intent);
	}

	private void finishActivity(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget);
		appWidgetManager.updateAppWidget(mAppWidgetId, views);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		this.setResult(Activity.RESULT_OK, resultValue);
		addThisIDAsReal();
		startWidgetService(context);
		finish();
	}

	private void addThisIDAsReal() {
		String key = ("appwidget" + mAppWidgetId + "_configured");
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		;
		prefs.edit().putBoolean(key, true).commit();

	}

}
