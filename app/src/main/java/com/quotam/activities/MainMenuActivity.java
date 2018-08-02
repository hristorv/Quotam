package com.quotam.activities;

import com.google.firebase.auth.FirebaseAuth;
import com.quotam.R;
import com.quotam.controller.PreferencesListener;
import com.quotam.fragments.steppers.SchedulersStepperFragment;
import com.quotam.fragments.steppers.StepperFragment;
import com.quotam.fragments.steppers.CreateImageStepperFragment;
import com.quotam.model.Constants;
import com.quotam.utils.CustomTypefaceSpan;
import com.quotam.fragments.AlbumsFragment;
import com.quotam.fragments.ArtistsFragment;
import com.quotam.fragments.CategoriesFragment;
import com.quotam.fragments.HelpFragment;
import com.quotam.fragments.ProfileFragment;
import com.quotam.fragments.SchedulersFragment;
import com.quotam.fragments.TimelineFragment;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TITLE_KEY = "title";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences prefs;
    private boolean isAlbum;
    private NavigationView nvDrawer;
    private boolean isOffline;
    private View expandItem;
    private CharSequence mTitle;
    private Typeface typeface;
    private Typeface boldFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        listener = new PreferencesListener(this);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        isOffline = getIntent().getExtras().getBoolean(Constants.Extra.IS_OFFLINE);

        typeface = ResourcesCompat.getFont(this, R.font.coming_soon);
        boldFont = Typeface.create(typeface, Typeface.BOLD);

        drawerLayout = findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = findViewById(R.id.nav_view);

        // Set custom font to drawer items.
        //changeDrawerItemsFont(boldFont);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.app_name, // navigation menu toggle icon
                R.string.app_name
                // accessibility
        ) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            // on first time display view for first navigation item
            openDrawerItem(getResources().getString(R.string.drawer_timeline),
                    R.id.drawer_timeline);
            nvDrawer.setCheckedItem(R.id.drawer_timeline);
            // Open help fragment, if the user opens application for the first
            // time.
            if (prefs.getBoolean(Constants.PreferencesKeys.KEY_FIRST_TIME_HELP,
                    true)) {
                openHelp();
                prefs.edit()
                        .putBoolean(
                                Constants.PreferencesKeys.KEY_FIRST_TIME_HELP,
                                false).commit();
            }
        }
    }

    public void changeTitleParams(CollapsingToolbarLayout collapsingToolbarLayout) {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleTypeface(boldFont);
        collapsingToolbarLayout.setCollapsedTitleTypeface(boldFont);
    }

    public void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();
    }

    public void fixToolbarPadding(Toolbar toolbar) {
        View setupedToolbar = findViewById(R.id.toolbar);
        if (setupedToolbar != null) {
            int paddingTop = setupedToolbar.getPaddingTop();
            toolbar.setPadding(toolbar.getPaddingLeft(), paddingTop, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
        }
    }


//    private void changeDrawerItemsFont(Typeface boldFont) {
//        CustomTypefaceSpan span = new CustomTypefaceSpan("bold", boldFont);
//        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
//        for (int i = 0; i < nvDrawer.getMenu().size(); i++) {
//            MenuItem menuItem = nvDrawer.getMenu().getItem(i);
//            menuItems.add(menuItem);
//            if (menuItem.hasSubMenu()) {
//                for (int j = 0; j < menuItem.getSubMenu().size(); j++) {
//                    menuItems.add(menuItem.getSubMenu().getItem(j));
//                }
//            }
//        }
//        for (MenuItem menuItem : menuItems) {
//            SpannableStringBuilder title = new SpannableStringBuilder(menuItem.getTitle());
//            title.setSpan(span, 0, title.length(), 0);
//            menuItem.setTitle(title);
//        }
//    }

    private void changeDrawerItemsFont(Typeface boldFont) {
        CustomTypefaceSpan span = new CustomTypefaceSpan("bold", boldFont);
        for (int i = 0; i < nvDrawer.getMenu().size(); i++) {
            MenuItem menuItem = nvDrawer.getMenu().getItem(i);
            SpannableStringBuilder title = new SpannableStringBuilder(menuItem.getTitle());
            title.setSpan(span, 0, title.length(), 0);
            menuItem.setTitle(title);
        }
    }

    public void setTabFont(TabLayout tabs) {
//        ViewGroup viewGroup = (ViewGroup) tabs.getChildAt(0);
//        int tabsCount = viewGroup.getChildCount();
//        for (int i = 0; i < tabsCount; i++) {
//            ViewGroup viewGroupTab = (ViewGroup) viewGroup.getChildAt(i);
//            int tabChildsCount = viewGroupTab.getChildCount();
//            for (int j = 0; j < tabChildsCount; j++) {
//                View tabViewChild = viewGroupTab.getChildAt(j);
//                if (tabViewChild instanceof TextView) {
//                    ((TextView) tabViewChild).setTypeface(typeface, Typeface.BOLD);
//                    ((TextView) tabViewChild).setTextSize(16);
//                }
//            }
//        }
    }

    public void checkIfOffline() {
        if (isOffline) {
//            Snackbar.make(toolbarTitle,
//                    R.string.offline_alert, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.snackbar_action_reconnect,
//                            new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    Intent i = new Intent(MainMenuActivity.this,
//                                            InitializationActivity.class);
//                                    startActivity(i);
//                                    MainMenuActivity.this.finish();
//                                }
//                            }).show();
        }
    }

    private void setupDrawerContent(final NavigationView navDrawer) {
        navDrawer
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        nvDrawer.setCheckedItem(menuItem.getItemId());
                        drawerLayout.closeDrawer(navDrawer, true);
                        openDrawerItem(menuItem.getTitle(), menuItem.getItemId());
                        return true;
                    }
                });
    }

    /**
     * Displaying fragment view for selected navigation drawer list item
     */
    private void displayView(Fragment fragment) {
        // update the main content by replacing fragments
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    //  .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left)
                    .replace(R.id.content_frame, fragment).commitNow();
        }
    }

    private void openDrawerItem(CharSequence title, int id) {
        clearBackStackEntries();
        switch (id) {
            case R.id.drawer_timeline:
                displayView(new TimelineFragment());
                break;
            case R.id.drawer_categories:
                displayView(new CategoriesFragment());
                break;
            case R.id.drawer_albums:
                displayView(new AlbumsFragment());
                break;
            case R.id.drawer_artists:
                displayView(new ArtistsFragment());
                break;
            case R.id.drawer_create:
                openCreate();
                break;
            case R.id.drawer_schedulers:
                displayView(new SchedulersFragment());
                break;
            case R.id.drawer_profile:
                displayView(new ProfileFragment());
                break;
            case R.id.drawer_feedback:
                openFeedback();
                break;
            case R.id.drawer_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void openCreate() {
        Fragment fragment = new CreateImageStepperFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, fragment)
                .addToBackStack(
                        Constants.FragmentNames.BACKSTACK_FRAGMENT)
                .commit();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainMenuActivity.this,
                InitializationActivity.class);
        startActivity(i);
        MainMenuActivity.this.finish();
    }

    private void clearBackStackEntries() {
        // Using Immediate to prevent bug, showing
        // backstack fragment after selecting navigation item.
        getSupportFragmentManager().popBackStackImmediate(
                Constants.FragmentNames.BACKSTACK_FRAGMENT,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public View getExpandItem() {
        return expandItem;
    }

    /**
     * Handles the Action bar items.Both settings and about items are from the
     * overflow menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle navigation drawer on selecting action bar application
        // icon/title
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.expand:
                AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
                // First check appbarlayout for the inner fragments (InAlbum,InArtist etc.)
                AppBarLayout innerAppBarLayout = findViewById(R.id.inner_appbar_layout);
                if (innerAppBarLayout != null)
                    innerAppBarLayout.setExpanded(true, true);
                else if (appBarLayout != null)
                    appBarLayout.setExpanded(true, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TITLE_KEY, mTitle);
    }

    /**
     * Pass any configuration change to the drawer toggles.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Starts the Feedback dialog.
     */
    private void openFeedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                .setView(R.layout.feedback_layout)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout editError = dialog.findViewById(R.id.feedback_text_input_layout);
                TextInputEditText edit = dialog.findViewById(R.id.feedback_edit_text);
                RadioGroup radioGroup = dialog.findViewById(R.id.feedback_radio_group);
                TextInputLayout radioGroupError = dialog.findViewById(R.id.feedback_radio_group_error);
                radioGroupError.setError(null);
                editError.setError(null);
                if (edit.getText().toString().trim().equals("")) {
                    editError.setError(getResources().getString(R.string.feedback_error));
                    return;
                }
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    radioGroupError.setError(getResources().getString(R.string.choose_type));
                    return;
                }
                // Send message
                dialog.dismiss();

            }
        });
    }

    private void openHelp() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        HelpFragment frag = new HelpFragment();
        frag.show(ft, Constants.FragmentNames.HELP_FRAGMENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(listener);
        checkIfOffline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setDrawerEnabled(boolean enabled) {
        if (drawerLayout != null && drawerToggle != null) {
            int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
            drawerLayout.setDrawerLockMode(lockMode);
            drawerToggle.setDrawerIndicatorEnabled(enabled);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 && checkFragment()) {
            getSupportFragmentManager().popBackStack();
        }
        //super.onBackPressed();
    }

    private boolean checkFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof CreateImageStepperFragment) {
            CreateImageStepperFragment createImageStepperFragment = (CreateImageStepperFragment) fragment;
            if (createImageStepperFragment.isBottomSheetVisible()) {
                createImageStepperFragment.closeBottomSheet();
            } else
                showExitConfirmDialog();
            return false;
        }
        if (fragment instanceof SchedulersStepperFragment) {
            showExitConfirmDialog();
            return false;
        }
        return true;
    }

    private void showExitConfirmDialog() {
        new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                .setView(R.layout.exit_confirm)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                })
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        if (requestCode == Constants.Extra.REQUEST_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
//                Snackbar.make(toolbarTitle, R.string.warning_deleted,
//                        Snackbar.LENGTH_LONG)
//                        .setAction(R.string.snackbar_action_undo,
//                                new View.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(View v) {
//                                        Image lastDeletedImage = data
//                                                .getExtras()
//                                                .getParcelable(
//                                                        Constants.Extra.IMAGE_PARCEABLE);
//                                        Image[] images = new AlbumsController()
//                                                .reverseRemoveImageFromAlbum(
//                                                        MainMenuActivity.this,
//                                                        lastDeletedImage);
//                                        Fragment currentFragment = getSupportFragmentManager()
//                                                .findFragmentByTag(
//                                                        "ImageGridFragment");
//                                        if (currentFragment instanceof ImageGridFragment) {
//                                            ImageGridRecyclerAdapter adapter = ((ImageGridFragment) currentFragment)
//                                                    .getAdapter();
//                                            adapter.setImages(images);
//                                            adapter.notifyItemInserted(GRID_IMAGE_STARTING_INDEX);
//                                        }
//                                    }
//                                }).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setIsAlbum(boolean isAlbum) {
        this.isAlbum = isAlbum;
    }

}
