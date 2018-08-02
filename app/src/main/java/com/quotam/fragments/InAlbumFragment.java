package com.quotam.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.adapters.Items;
import com.quotam.model.Album;
import com.quotam.model.Artist;
import com.quotam.model.Constants;
import com.quotam.utils.ArtistDialog;
import com.quotam.utils.Helper;
import com.quotam.listeners.FabLikeOnClickListener;
import com.quotam.activities.MainMenuActivity;
import com.quotam.listeners.AppLayoutOffsetListener;
import com.quotam.adapters.ViewPagerAdapter;

public class InAlbumFragment extends Fragment {

    private Album album;
    RequestOptions options;
    private AppBarLayout appbarLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            album = bundle.getParcelable(Constants.PARCELABLE.ADAPTER_OBJECT);
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
        View rootView = inflater.inflate(R.layout.fragment_in_album,
                container, false);
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        activity.setDrawerEnabled(true);
        appbarLayout = rootView.findViewById(R.id.inner_appbar_layout);
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        RecyclerView grid = rootView.findViewById(R.id.grid);
        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
        final TabLayout tabs = rootView.findViewById(R.id.tabs);
        final CoordinatorLayout albumHeader = rootView.findViewById(R.id.album_header);

        setupAlbumInfo(rootView);
        activity.fixToolbarPadding(toolbar);
        activity.setupToolbar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        activity.changeTitleParams(collapsingToolbarLayout);

        ViewPager viewPager = rootView.findViewById(R.id.albums_viewpager);
        setupTabs(tabs, viewPager,activity);
        appbarLayout.addOnOffsetChangedListener(new AppLayoutOffsetListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int threshold = -appBarLayout.getTotalScrollRange() / 4;
                //animateInfo(verticalOffset, threshold, albumInfo);
                animateTabs(verticalOffset, appBarLayout.getTotalScrollRange(), albumHeader);
                animateExpand(verticalOffset, threshold, toolbar.findViewById(R.id.expand));
                animateFab(verticalOffset, threshold, fab);
            }
        });
        fab.setOnClickListener(new FabLikeOnClickListener(false, getActivity().getResources().getColor(R.color.accent_color), getActivity().getResources().getColor(R.color.white)) {

            @Override
            public void onClick() {

            }
        });
        appbarLayout.setExpanded(true);
        return rootView;
    }

    private void setupTabs(TabLayout tabs, ViewPager viewPager,MainMenuActivity activity) {

        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GridFragment(), getString(R.string.collection));
        adapter.addFragment(new CommentsFragment(),getString(R.string.comments));

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

    public void setupAlbumInfo(View rootView) {
        TextView likes = rootView.findViewById(R.id.album_item_likes_count);
        TextView pictures = rootView.findViewById(R.id.album_item_pictures_count);
        TextView artistName = rootView.findViewById(R.id.album_item_artist);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_layout);
        ImageView paralaxImage = rootView.findViewById(R.id.album_item_image);

        likes.setText(album.getLikesCount());
        pictures.setText(album.getPicturesCount());
        Glide.with(getActivity())
                .load(Helper.transform(album.getBackgroundUrl()))
                .apply(options)
                .into(paralaxImage);
        collapsingToolbarLayout.setTitle(album.getName());
        artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArtistDialog.openArtistDialog((AppCompatActivity) getActivity(),new Items().getArtist());
            }
        });
    }

}


