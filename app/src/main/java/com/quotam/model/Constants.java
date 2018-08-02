package com.quotam.model;

public final class Constants {

    public final class Extra {
        public static final int REQUEST_CODE_IMAGE = 1;
        public static final String POSITION = "com.quotam.POSITION";
        public static final String SERVICE_TAG_KEY = "service";
        public static final String IMAGE_ARRAY = "com.quotam.IMAGE_ARRAY";
        public static final String DEFAULT_PREFS_NAME = "com.quotam_preferences";
        public static final String IS_FROM_WIDGET = "com.quotam.IS_FROM_WIDGET";
        public static final String IS_OFFLINE = "com.quotam.IS_OFFLINE";
        public static final String IS_SHOWING = "com.quotam.IS_SHOWING";
        public static final String IS_FROM_REGISTER = "com.quotam.IS_FROM_REGISTER";
        public static final String REGISTER_EMAIL = "com.quotam.REGISTER_EMAIL";
        public static final String REGISTER_PASSWORD = "com.quotam.REGISTER_PASSWORD";
    }

    public final class PreferencesKeys {
        public static final String KEY_FIRST_TIME_HELP = "com.quotam.IS_FOR_FIRST_TIME";
        public static final String KEY_PREF_WALLPAPER_ACTIVE = "pref_wallpaper_active";
        public static final String KEY_PREF_NOTIFICATION_ACTIVE = "pref_notification_active";
        public static final String KEY_PREF_WALLPAPER_TIME = "pref_wallapper_interval";
        public static final String KEY_PREF_WALLPAPER_FILTER = "pref_wallapper_filter";
        public static final String KEY_PREF_WIDGET_TIME = "pref_widget_interval";
        public static final String KEY_PREF_WIDGET_FILTER = "pref_widget_filter";
        public static final String KEY_PREF_NOTIFICATION_TIME = "pref_notification_interval";
        public static final String KEY_PREF_NOTIFICATION_FILTER = "pref_notification_filter";
        public static final String CATEGORY_INDEX_WALLPAPER = "category_index_wallpaper";
        public static final String CATEGORY_INDEX_WIDGET = "category_index_widget";
        public static final String CATEGORY_INDEX_NOTIFICATION = "category_index_notification";
        public static final String IMAGE_INDEX_WALLPAPER = "image_index_wallpaper";
        public static final String IMAGE_INDEX_WIDGET = "image_index_widget";
        public static final String IMAGE_INDEX_NOTIFICATION = "image_index_notification";
        public static final String KEY_PREF_WIDGET_ACTIVE = "pref_widget_active";
        public static final String WIDTH_PREF = "pref_width";
        public static final String HEIGHT_PREF = "pref_height";
        public static final String REAL_WIDTH_PREF = "pref_real_width";
        public static final String REAL_HEIGHT_PREF = "pref_real_height";
        public static final String KEY_PREF_WALLPAPER_SCALING = "pref_wallpaper_scaling";
    }

    public final class ServicesID {
        public static final int NOTIFICATION_ALARM_ID = 2;
        public static final int WIDGET_ALARM_ID = 1;
        public static final int WALLPAPER_ALARM_ID = 0;
    }

    public final class FragmentNames {
        public static final String HELP_FRAGMENT = "com.quotam.help_fragment";
        public static final String BACKSTACK_FRAGMENT = "com.quotam.backstack_fragment";

        public static final String REGISTER_FRAGMENT = "com.quotam.register_fragment";
    }

    public final class WallpaperScaling {
        public static final String DEFAULT = "Default (Recommended)";
        public static final String STRETCH = "Stretch";
        public static final String FIT = "Fit";
        public static final String CENTER = "Center";
    }

    public final class NotificationIDs {
        public static final int NOTIFICATION_IMAGE = 001;
        public static final int NOTIFICATION_NOTIF_ERROR = 002;
        public static final int NOTIFICATION_WALLPAPER_ERROR = 003;
    }

    public final class PARCELABLE {
        public static final String ADAPTER_OBJECT = "com.quotam.adapter_object";
        public static final String ADAPTER_LIST = "com.quotam.adapter_list";
        public static final String BITMAP = "com.quotam.bitmap";
    }
}
