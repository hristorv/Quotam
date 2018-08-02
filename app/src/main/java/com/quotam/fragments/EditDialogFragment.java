package com.quotam.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quotam.R;

public class EditDialogFragment extends DialogFragment {

    private static final int BACKGROUND_REQUEST_CODE = 1;
    private static final int AVATAR_REQUEST_CODE = 2;
    private static final int MIN_CHARS = 6;

    public EditDialogFragment() {
    }

    public static EditDialogFragment newInstance() {
        EditDialogFragment frag = new EditDialogFragment();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "coming_soon.ttf");
        TextInputEditText username = rootView.findViewById(R.id.register_username_edittext);
        TextInputLayout usernameLayout = rootView.findViewById(R.id.register_username_textinputlayout);
        TextInputEditText email = rootView.findViewById(R.id.register_email_edittext);
        TextInputLayout emailLayout = rootView.findViewById(R.id.register_email_textinputlayout);
        TextInputEditText password = rootView.findViewById(R.id.register_password_edittext);
        TextInputLayout passwordLayout = rootView.findViewById(R.id.register_password_textinputlayout);
        Button registerButton = rootView.findViewById(R.id.email_button);
        ImageView backgroundImage = rootView.findViewById(R.id.register_image_background);
        ImageView avatarImage = rootView.findViewById(R.id.register_image_avatar);
        Glide.with(getActivity()).load(R.drawable.default_cover_photo).into(backgroundImage);
        Glide.with(getActivity()).load(R.drawable.default_profile_photo_light).into(avatarImage);

        username.setTypeface(font);
        usernameLayout.setTypeface(font);
        email.setTypeface(font);
        emailLayout.setTypeface(font);
        password.setTypeface(font);
        passwordLayout.setTypeface(font);
        registerButton.setTypeface(font, Typeface.BOLD);

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

