package com.quotam.utils;

import com.quotam.model.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

public class ScaleBitmapProcessor {

	private int height;
	private int width;

	public ScaleBitmapProcessor(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		this.width = prefs.getInt(Constants.PreferencesKeys.WIDTH_PREF, 0);
		this.height = prefs.getInt(Constants.PreferencesKeys.HEIGHT_PREF, 0);
		getCalculatedDimension();
	}

	public Bitmap process(Bitmap defaultBitmap) {
		if (defaultBitmap != null && width != 0 && height != 0) {
			Bitmap scaledBitmap;
			if (isPortrait(defaultBitmap)) {
				scaledBitmap = Bitmap.createScaledBitmap(defaultBitmap, height,
						width, true);
			} else {
				scaledBitmap = Bitmap.createScaledBitmap(defaultBitmap, width,
						height, true);
			}
			if (!defaultBitmap.equals(scaledBitmap))
				defaultBitmap.recycle();
			return scaledBitmap;
		}
		return defaultBitmap;
	}

	private boolean isPortrait(Bitmap defaultBitmap) {
		return defaultBitmap.getHeight() > defaultBitmap.getWidth();
	}

	private void getCalculatedDimension() {
		if (height > width) {
			int x = height;
			height = width;
			width = x;
		}
	}

}
