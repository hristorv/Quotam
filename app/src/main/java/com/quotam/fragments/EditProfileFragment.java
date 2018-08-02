package com.quotam.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quotam.R;
import com.quotam.model.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;

public class EditProfileFragment extends Fragment {
    private static final int BACKGROUND_REQUEST_CODE = 1;
    private static final int AVATAR_REQUEST_CODE = 2;
    private static final int MIN_CHARS = 6;
    private boolean isFromRegister = false;
    private TextInputEditText username;
    private TextInputLayout usernameLayout;
    private ImageView backgroundImage;
    private ImageView avatarImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            isFromRegister = bundle.getBoolean(Constants.Extra.IS_FROM_REGISTER, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "coming_soon.ttf");
        username = rootView.findViewById(R.id.register_username_edittext);
        usernameLayout = rootView.findViewById(R.id.register_username_textinputlayout);
        Button continueButton = rootView.findViewById(R.id.continue_button);
        backgroundImage = rootView.findViewById(R.id.register_image_background);
        avatarImage = rootView.findViewById(R.id.register_image_avatar);
        Glide.with(getActivity()).load(R.drawable.default_cover_photo).into(backgroundImage);
        Glide.with(getActivity()).load(R.drawable.default_profile_photo_light).into(avatarImage);

        username.setTypeface(font);
        usernameLayout.setTypeface(font);
        continueButton.setTypeface(font, Typeface.BOLD);

        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), BACKGROUND_REQUEST_CODE);
            }
        });

        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), AVATAR_REQUEST_CODE);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueTo();
            }
        });


        return rootView;
    }

    private void startLoginFragment() {
        Fragment loginFragment = new LoginFragment();
        loginFragment.setArguments(getArguments());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter_delay, R.anim.bottom_exit, R.anim.bottom_enter_delay, R.anim.bottom_exit)
                .replace(R.id.content_frame, loginFragment)
                .commit();
    }

    private void continueTo() {
        if (checkText()) {
            //final AlertDialog progress = startProgressDialog();
            startLoginFragment();
            Snackbar.make(username,R.string.register_successful,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BACKGROUND_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = getBitmap(data);
            if (bitmap != null)
                backgroundImage.setImageBitmap(bitmap);
        }
        if (requestCode == AVATAR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = getBitmap(data);
            if (bitmap != null)
                avatarImage.setImageBitmap(bitmap);
        }

    }

    private Bitmap getBitmap(Intent data) {
        Uri selectedImage = data.getData();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private boolean checkText() {
        if (username.getText().toString().isEmpty()) {
            usernameLayout.setError("Enter valid username");
            return false;
        }
        username.setError(null);
        return true;
    }

    private AlertDialog startProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme)
                .setView(R.layout.progress_bar)
                .setCancelable(false);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();
        return progressDialog;

    }

}
