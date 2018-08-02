package com.quotam.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.model.Artist;
import com.quotam.model.Constants;
import com.quotam.utils.Helper;
import com.quotam.listeners.FabLikeOnClickListener;
import com.quotam.listeners.SystemUI;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.ViewPagerAdapter;
import com.quotam.utils.ImageDialog;

public class InArtistFragment extends Fragment {


    private Artist artist;
    RequestOptions options;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null)
            artist = bundle.getParcelable(Constants.PARCELABLE.ADAPTER_OBJECT);
        setImageLoaderOptions();
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        AppBarLayout appbarLayout = rootView.findViewById(R.id.inner_appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        RecyclerView grid = rootView.findViewById(R.id.grid);
        coordinatorLayout = rootView.findViewById(R.id.coordinator_layout);
        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
        final TabLayout tabs = rootView.findViewById(R.id.tabs);
        final CoordinatorLayout paralax_account_base = rootView.findViewById(R.id.paralax_account_base);
        final ImageView profile_pic = rootView.findViewById(R.id.profile_image_avatar);
        ViewPager viewPager = rootView.findViewById(R.id.profile_viewpager);


        fab.setImageResource(R.drawable.thumbs_up_white);
        setupArtistInfo(rootView);

        activity.fixToolbarPadding(toolbar);

        activity.setupToolbar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        activity.changeTitleParams(collapsingToolbarLayout);
        setupTabs(tabs, viewPager);
        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = 0;
                animateScale(verticalOffset, threshold, profile_pic);
                animateTabs(verticalOffset, appBarLayout.getTotalScrollRange(), paralax_account_base);
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
                animateFab(verticalOffset, threshold, fab);
            }
        });
        fab.setOnClickListener(new FabLikeOnClickListener(false, getActivity().getResources().getColor(R.color.accent_color), getActivity().getResources().getColor(R.color.white)) {

            public void onClick() {

            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageDialog().showDialog(view.getContext(),Helper.transform(artist.getAvatarUrl()));
            }
        });
        appbarLayout.setExpanded(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void setupArtistInfo(View rootView) {
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        TextView likesCount = rootView.findViewById(R.id.profile_count_likes);
        TextView picsCount = rootView.findViewById(R.id.profile_count_pictures);
        TextView albumsCount = rootView.findViewById(R.id.profile_count_albums);
        ImageView profileBackground = rootView.findViewById(R.id.profile_image_background);
        ImageView profileAvatar = rootView.findViewById(R.id.profile_image_avatar);

        collapsingToolbarLayout.setTitle(artist.getName());
        likesCount.setText(artist.getLikesCount());
        picsCount.setText(artist.getPicturesCount());
        albumsCount.setText(artist.getAlbumsCount());
        Glide.with(getActivity())
                .load(Helper.transform(artist.getBackgroundUrl()))
                .apply(options)
                .into(profileBackground);
        Glide.with(getActivity())
                .load(Helper.transform(artist.getAvatarUrl()))
                .apply(options)
                .into(profileAvatar);

    }

    private void setupTabs(TabLayout tabs, ViewPager viewPager) {

        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GridFragment(), getString(R.string.collection));
        adapter.addFragment(new GridFragment(), getString(R.string.favorites));
        adapter.addFragment(new CommentsFragment(), getString(R.string.comments));

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

    private void adjustGrid(final RecyclerView grid, boolean isLargeGridItems) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        new SystemUI().adjustGridColumnNum(getActivity(), grid, isLargeGridItems);
    }

}


