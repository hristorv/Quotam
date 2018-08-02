package com.quotam.activities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quotam.R;
import com.quotam.model.Category;
import com.quotam.model.Constants;
import com.quotam.model.ImageData;
import com.quotam.fragments.LoginFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.google.gson.Gson;

public class InitializationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        //if (checkLogged()) {
            init();
//        } else {
//            Fragment fragment = new LoginFragment();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.bottom_enter, R.anim.bottom_exit, R.anim.bottom_enter, R.anim.bottom_exit)
//                    .replace(R.id.content_frame, fragment)
//                    .commit();
//        }
    }

    public void init() {
        applyAnimation(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //adjustNotNavBars();
                checkScreenDimensions();
                downloadCategories();
            }
        });

    }

    private boolean checkLogged() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    private void applyAnimation(Animator.AnimatorListener listener) {
        ImageView image = findViewById(R.id.logo);
        image.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setStartDelay(100)
                .setDuration(400)
                .translationYBy(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(listener)
                .start();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            //super.onBackPressed();
        }

    }

    private void downloadCategories() {
        // if (ConnectionManager.isConnected(this)) {
        new GetJson().execute();
        //  } else {
        //      showOfflineDialog();
        // }
    }

    private void showOfflineDialog() {
//        if (offlineDialog == null) {
//            offlineDialog = new MaterialDialog.Builder(this).cancelable(false)
//                    .title(R.string.no_connection_dialog_title)
//                    .content(R.string.no_connection_dialog_content)
//                    .positiveText(R.string.action_retry)
//                    .negativeText(R.string.action_go_offline)
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog,
//                                            @NonNull DialogAction which) {
//                            downloadCategories();
//                        }
//                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
//
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog,
//                                            @NonNull DialogAction which) {
//                            new ImageController()
//                                    .setImageArraysDefault(InitializationActivity.this);
//                            // Start MainMenuActivity and finish this one.
//                            Intent i = new Intent(InitializationActivity.this,
//                                    MainMenuActivity.class);
//                            i.putExtra(Constants.Extra.IS_OFFLINE, true);
//                            startActivity(i);
//                            InitializationActivity.this.finish();
//                        }
//                    }).show();
//        } else {
//            offlineDialog.show();
//        }
    }

    @Override
    protected void onDestroy() {
//        if (offlineDialog != null && offlineDialog.isShowing()) {
//            offlineDialog.dismiss();
//        }
        super.onDestroy();
    }

    private void checkScreenDimensions() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (!prefs.contains(Constants.PreferencesKeys.WIDTH_PREF))
            getScreenDimensions(prefs);
    }

    @SuppressLint("NewApi")
    public void getScreenDimensions(SharedPreferences prefs) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int width_real = 0;
        int height_real = 0;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Point size_real = new Point();
            display.getRealSize(size_real);
            width_real = size_real.x;
            height_real = size_real.y;
            if (height_real > height) {
                int nav_bar_height = height_real - height;
                if (!isNavBarOnTheRight()) {
                    height = height_real;
                    width = width - nav_bar_height;
                }
            }
        }
        if (width_real == 0 || height_real == 0) {
            width_real = width;
            height_real = height;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.PreferencesKeys.WIDTH_PREF, width);
        editor.putInt(Constants.PreferencesKeys.HEIGHT_PREF, height);
        editor.putInt(Constants.PreferencesKeys.REAL_WIDTH_PREF, width_real);
        editor.putInt(Constants.PreferencesKeys.REAL_HEIGHT_PREF, height_real);
        editor.commit();
    }

    @SuppressLint("NewApi")
    private boolean isNavBarOnTheRight() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        Point size_real = new Point();
        display.getRealSize(size_real);
        int height_real = size_real.y;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return height_real == height;
    }

    private void adjustNotNavBars() {
        View decorView = getWindow().getDecorView();
        // Hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private class GetJson extends AsyncTask<Void, Void, Void> {

        private static final int TIMEOUT = 5000;

        @Override
        protected Void doInBackground(Void... params) {
            //  try {

            //getCategories();
            Category[] categories = new Category[18];
            categories[0] = new Category("Family", "drawable://" + R.drawable.illustration_categories_family);
            categories[1] = new Category("Fashion", "drawable://" + R.drawable.illustration_categories_fashion);
            categories[2] = new Category("Friendship", "drawable://" + R.drawable.illustration_categories_friendship);
            categories[3] = new Category("Funny", "drawable://" + R.drawable.illustration_categories_funny);
            categories[4] = new Category("Happiness", "drawable://" + R.drawable.illustration_categories_happiness);
            categories[5] = new Category("Inspirational", "drawable://" + R.drawable.illustration_categories_inspirational);
            categories[6] = new Category("Life", "drawable://" + R.drawable.illustration_categories_life);
            categories[7] = new Category("Love", "drawable://" + R.drawable.illustration_categories_love);
            categories[8] = new Category("Minions", "drawable://" + R.drawable.illustration_categories_minions);
            categories[9] = new Category("Movies", "drawable://" + R.drawable.illustration_categories_movies);
            categories[10] = new Category("Music", "drawable://" + R.drawable.illustration_categories_music);
            categories[11] = new Category("Proverbs", "drawable://" + R.drawable.illustration_categories_proverbs);
            categories[12] = new Category("Spirituality", "drawable://" + R.drawable.illustration_categories_spirituality);
            categories[13] = new Category("Sport", "drawable://" + R.drawable.illustration_categories_sport);
            categories[14] = new Category("Success", "drawable://" + R.drawable.illustration_categories_success);
            categories[15] = new Category("Travel", "drawable://" + R.drawable.illustration_categories_travel);
            categories[16] = new Category("Wisdom", "drawable://" + R.drawable.illustration_categories_wisdom);
            categories[17] = new Category("Workout", "drawable://" + R.drawable.illustration_categories_workout);

            int[] images = {R.drawable.cat_favourites, R.drawable.cat_funny, R.drawable
                    .cat_happiness, R.drawable.cat_inspirational, R.drawable.cat_life, R.drawable
                    .cat_love, R.drawable.cat_success, R.drawable.cat_travel, R.drawable.cat_wisdom,
                    R.drawable.test_albums1, R.drawable.test_albums2, R.drawable.test_albums3, R
                    .drawable.test_albums4, R.drawable.test_albums5, R.drawable.test_albums6, R
                    .drawable.test_albums7, R.drawable.test_albums8, R.drawable.test_albums9, R
                    .drawable.test_albums10, R.drawable.test_albums11, R.drawable.test_albums12, R
                    .drawable.test_albums13, R.drawable.test_albums14, R.drawable.test_albums15, R
                    .drawable.test_albums16, R.drawable.test_albums17, R.drawable.test_albums18};


//            Image[] imagesTest = new Image[images.length];
//            for (int i = 0; i < imagesTest.length; i++) {
//                Image image = new Image();
//                image.setLikes(String.valueOf(i));
//                image.setUrl("drawable://" + images[i]);
//                imagesTest[i] = image;
//            }

            ImageData.getInstance().setCategories(categories);

            //    } catch (Exception e) {
            //         showOfflineDialogOnMainThread();
            //     }
            // ImageData.getInstance().initImagesFavorites(
            //        InitializationActivity.this);
            ImageData.getInstance().initCategoryTitles(InitializationActivity.this);
            //AlbumsData.getInstance().initAlbums(InitializationActivity.this);


            return null;
        }

        private void getCategories() {
            Gson gson = new Gson();
            InputStream source = retrieveStream(getResources().getString(
                    R.string.get_category_url));
            Reader reader = new InputStreamReader(source);

            ImageData.getInstance().setCategories(
                    gson.fromJson(reader, Category[].class));
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // cacheCategoryImages();
            Intent i = new Intent(InitializationActivity.this,
                    MainMenuActivity.class);
            i.putExtra(Constants.Extra.IS_OFFLINE, false);
            startActivity(i);
            InitializationActivity.this.finish();
        }

        private InputStream retrieveStream(String urlText) {

            try {

                URL url = new URL(urlText);

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                InputStream input = connection.getInputStream();

                return input;

            } catch (IOException e) {
                showOfflineDialogOnMainThread();
                this.cancel(true);
            }
            return null;
        }

        private void showOfflineDialogOnMainThread() {
            InitializationActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showOfflineDialog();
                }
            });
        }
    }
}