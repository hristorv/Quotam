package com.quotam.services;

import java.util.Set;

import com.quotam.R;
import com.quotam.controller.MyAppWidgetProvider;
import com.quotam.model.Constants;
import com.quotam.model.Image;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetService extends BaseService {
	private static final String serviceTag = "Widget Service";
	public static final String KEY_PREF_WIDGET_FILTER = "pref_widget_filter";
	int categoryIndex;
	String curCategory;
	int imageIndex;
	SharedPreferences prefs;
	String[] filterArray;
	private Context context;
	private String curLikes;

	public WidgetService() {
		super(serviceTag);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		this.context = this;
		// SharedPreferences sharedPref = this.getSharedPreferences(
		// DEFAULT_PREFS_NAME, Context.MODE_PRIVATE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Get the category index from the preferences
		categoryIndex = prefs.getInt(
				Constants.PreferencesKeys.CATEGORY_INDEX_WIDGET,
				INDEX_DEFAULT_VALUE);
		imageIndex = prefs.getInt(Constants.PreferencesKeys.IMAGE_INDEX_WIDGET,
				INDEX_DEFAULT_VALUE);

		Set<String> filterSet = prefs
				.getStringSet(KEY_PREF_WIDGET_FILTER, null);
		filterArray = filterSet.toArray(new String[filterSet.size()]);
		if (filterArray.length > 0) {
			curCategory = filterArray[categoryIndex];
			new GetCategoryJson(this, curCategory).execute();
		}

	}

	private void updateIndexes(int categoryArraySize) {
		if (imageIndex == categoryArraySize - 1) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(Constants.PreferencesKeys.IMAGE_INDEX_WIDGET,
					INDEX_DEFAULT_VALUE);
			editor.commit();
			int nextCategoryIndex;
			if (categoryIndex != filterArray.length - 1) {
				nextCategoryIndex = categoryIndex + 1;
			} else {
				nextCategoryIndex = INDEX_DEFAULT_VALUE;
			}
			editor.putInt(Constants.PreferencesKeys.CATEGORY_INDEX_WIDGET,
					nextCategoryIndex);
			editor.commit();
		} else {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(Constants.PreferencesKeys.IMAGE_INDEX_WIDGET,
					imageIndex + 1);
			editor.commit();
		}

	}

	private void setImageWidget(Bitmap loadedBitmap, Image[] categoryArray) {

		if (loadedBitmap != null) {
			/* get the handle on your widget */
			RemoteViews views = new RemoteViews(getPackageName(),
					R.layout.appwidget);
			/* replace the image */
			views.setImageViewBitmap(R.id.widget_image, loadedBitmap);
			views.setTextViewText(R.id.widget_likesBar, curLikes);
			// Send image array and image index
			Intent intent = new Intent(context, MyAppWidgetProvider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(Constants.Extra.POSITION, imageIndex);
			intent.putExtra(Constants.Extra.IMAGE_ARRAY, categoryArray);
			int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
					new ComponentName(context, MyAppWidgetProvider.class));
			if (ids != null && ids.length > 0) {
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
				context.sendBroadcast(intent);
			}
			/* update your widget */
			ComponentName thisWidget = new ComponentName(context,
					MyAppWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			manager.updateAppWidget(thisWidget, views);

		}
	}

	@Override
	void getCurImage(final Image[] categoryArray) {
		if (categoryArray != null && categoryArray.length > 0) {
			// Get the HQ image URL
			StringBuilder hq_url = new StringBuilder(
					categoryArray[imageIndex].getUrl());
			hq_url.delete(hq_url.length() - 7, hq_url.length() - 4);
			String curImageUrl = hq_url.toString();
//			ImageLoader.getInstance().loadImage(curImageUrl, options,
//					new SimpleImageLoadingListener() {
//
//						@Override
//						public void onLoadingFailed(String imageUri, View view,
//								FailReason failReason) {
//							new ServiceController().addErrorMessage(context);
//						}
//
//						@Override
//						public void onLoadingComplete(String imageUri,
//								View view, Bitmap loadedImage) {
//							if (loadedImage != null) {
//								curLikes = categoryArray[imageIndex].getLikes();
//								setImageWidget(loadedImage, categoryArray);
//								updateIndexes(categoryArray.length);
//							} else {
//								new ServiceController().addErrorMessage(context);
//							}
//						}
//					});
		}
	}
}
