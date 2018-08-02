package com.quotam.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.quotam.R;
import com.quotam.model.Constants;

public class RegisterFragment extends Fragment {

    private static final int MIN_CHARS = 6;
    private TextInputEditText email;
    private TextInputLayout emailLayout;
    private TextInputEditText password;
    private TextInputLayout passwordLayout;
    private FirebaseAuth auth;
    private TextInputEditText passwordRepeat;
    private TextInputLayout passwordRepeatLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "coming_soon.ttf");
        email = rootView.findViewById(R.id.register_email_edittext);
        emailLayout = rootView.findViewById(R.id.register_email_textinputlayout);
        password = rootView.findViewById(R.id.register_password_edittext);
        passwordLayout = rootView.findViewById(R.id.register_password_textinputlayout);
        passwordRepeat = rootView.findViewById(R.id.register_password_repeat_edittext);
        passwordRepeatLayout = rootView.findViewById(R.id.register_password_repeat_textinputlayout);
        Button continueButton = rootView.findViewById(R.id.continue_button);

        email.setTypeface(font);
        emailLayout.setTypeface(font);
        password.setTypeface(font);
        passwordLayout.setTypeface(font);
        passwordRepeat.setTypeface(font);
        passwordRepeatLayout.setTypeface(font);
        continueButton.setTypeface(font, Typeface.BOLD);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueTo();
            }
        });


        return rootView;
    }

    private void startEditProfileFragment(String emailString, String passwordString) {
        getActivity().getSupportFragmentManager().popBackStack(getTag(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment editProfileFragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.Extra.IS_FROM_REGISTER, true);
        bundle.putString(Constants.Extra.REGISTER_EMAIL,emailString);
        bundle.putString(Constants.Extra.REGISTER_PASSWORD,passwordString);
        editProfileFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter_delay, R.anim.bottom_exit, R.anim.bottom_enter_delay, R.anim.bottom_exit)
                .replace(R.id.content_frame, editProfileFragment)
                .commit();
    }

    private void continueTo() {
        if (checkText()) {
            final AlertDialog progress = startProgressDialog();
            final String emailString = email.getText().toString();
            final String passwordString = password.getText().toString();
            auth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progress.dismiss();
                            if (task.isSuccessful()) {
                                startEditProfileFragment(emailString,passwordString);
                            } else {
                                checkError(task.getException());
                            }
                        }
                    });
        }
    }

    private void checkError(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException)
            passwordLayout.setError("Password must be at least 6 characters long");
        if (exception instanceof FirebaseAuthUserCollisionException)
            emailLayout.setError("The email address you entered is already in use");


}

    private boolean checkText() {
        emailLayout.setError(null);
        passwordLayout.setError(null);
        passwordRepeatLayout.setError(null);
        if (email.getText().toString().isEmpty() || !isValidEmail(email.getText().toString())) {
            emailLayout.setError("Enter valid email address");
            return false;
        }
        if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
            passwordRepeatLayout.setError("Password does not match");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            passwordLayout.setError("Enter valid password");
            return false;
        }
        if (password.getText().toString().length() < MIN_CHARS) {
            passwordLayout.setError("Password must be at least 6 characters long");
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
