package com.quotam.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.fragments.InArtistFragment;
import com.quotam.model.Artist;
import com.quotam.model.Constants;

public class ArtistDialog {

    public static void openArtistDialog(final AppCompatActivity activity, final Artist artist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomDialogTheme)
                .setView(R.layout.profile_dialog)
                .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openArtistFragment(activity,artist);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        ImageView profileAvatar = dialog.findViewById(R.id.profile_image_avatar);
        ImageView profileBackground = dialog.findViewById(R.id.profile_image_background);
        TextView profileName = dialog.findViewById(R.id.profile_name);
        TextView likesCount = dialog.findViewById(R.id.profile_count_likes);
        TextView picturesCount = dialog.findViewById(R.id.profile_count_pictures);
        TextView albumsCount = dialog.findViewById(R.id.profile_count_albums);

        if (artist != null) {
            Glide.with(activity)
                    .load(Helper.transform(artist.getBackgroundUrl()))
                    .into(profileBackground);
            Glide.with(activity)
                    .load(Helper.transform(artist.getAvatarUrl()))
                    .into(profileAvatar);
            profileName.setText(artist.getName());
            likesCount.setText(artist.getLikesCount());
            picturesCount.setText(artist.getPicturesCount());
            albumsCount.setText(artist.getAlbumsCount());
        }

    }

    private static void openArtistFragment(AppCompatActivity activity,Artist artist) {
        Fragment fragment = new InArtistFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PARCELABLE.ADAPTER_OBJECT, artist);
        fragment.setArguments(bundle);
        if (fragment != null && !fragment.isAdded()) {
            FragmentManager fragmentManager = activity
                    .getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .addToBackStack(
                            Constants.FragmentNames.BACKSTACK_FRAGMENT)
                    .commit();
        }
    }

}
