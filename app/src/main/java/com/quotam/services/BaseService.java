package com.quotam.services;

import com.quotam.model.Image;
import com.quotam.utils.ScaleBitmapProcessor;

import android.app.IntentService;

public abstract class BaseService extends IntentService {
	protected static final int INDEX_DEFAULT_VALUE = 0;


	public BaseService(String name) {
		super(name);
	}
	
	abstract void getCurImage(final Image[] categoryArray);


}
