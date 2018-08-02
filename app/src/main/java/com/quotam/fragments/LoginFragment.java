package com.quotam.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.quotam.R;
import com.quotam.model.Constants;
import com.quotam.activities.InitializationActivity;

import java.util.Arrays;

public class LoginFragment extends Fragment {

    public static final int MIN_CHARS = 6;
    private static final int GOOGLE_SIGN_IN = 1;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private boolean isFromRegister = false;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private CallbackManager facebookCallbackManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null)
            isFromRegister = bundle.getBoolean(Constants.Extra.IS_FROM_REGISTER, false);
        setupGoogle();
        setupFacebook();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,
                container, false);
        email = rootView.findViewById(R.id.login_email_edittext);
        emailLayout = rootView.findViewById(R.id.login_email_textinputlayout);
        password = rootView.findViewById(R.id.login_password_edittext);
        passwordLayout = rootView.findViewById(R.id.login_password_textinputlayout);
        Button forgotPasswordButton = rootView.findViewById(R.id.login_forgot_password_button);
        AppCompatCheckBox rememberCheckbox = rootView.findViewById(R.id.login_remember_checkbox);
        Button loginButton = rootView.findViewById(R.id.login_button);
        Button signupButton = rootView.findViewById(R.id.email_button);
        Button facebookButton = rootView.findViewById(R.id.facebook_button);
        Button googleButton = rootView.findViewById(R.id.google_button);
//
//        signupButton.setTypeface(font, Typeface.BOLD);
//        facebookButton.setTypeface(font, Typeface.BOLD);
//        googleButton.setTypeface(font, Typeface.BOLD);
//        loginButton.setTypeface(font, Typeface.BOLD);
//        rememberCheckbox.setTypeface(font);
//        forgotPasswordButton.setTypeface(font);
//        email.setTypeface(font);
//        emailLayout.setTypeface(font);
//        password.setTypeface(font);
//        passwordLayout.setTypeface(font);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmail();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterFragment();
            }
        });
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPasswordDialog();
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signWithGoogle();
            }
        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signWithFacebook();
            }
        });
        if (isFromRegister)
            setupAfterRegister();
        return rootView;
    }

    private void setupFacebook() {
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                showSnackBar(R.string.sign_in_failed);
            }
        });
    }

    private void signWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        final AlertDialog progressDialog = startProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            openActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                showSnackBar(R.string.email_exists);
                            else
                                showSnackBar(R.string.sign_in_failed);
                        }
                    }
                });
    }

    private void setupGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // TODO
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signWithGoogle() {
        if (googleApiClient != null) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showSnackBar(R.string.sign_in_failed);
            }
        }
        // Pass the activity result back to the Facebook SDK
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        final AlertDialog progressDialog = startProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            openActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                showSnackBar(R.string.email_exists);
                            else
                                showSnackBar(R.string.sign_in_failed);
                        }
                    }
                });
    }

    private void openForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme)
                .setView(R.layout.forget_password_layout)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextInputLayout layout = dialog.findViewById(R.id.forgot_password_email_layout);
        final TextInputEditText edit = dialog.findViewById(R.id.forgot_password_email_edit);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "coming_soon.ttf");
        layout.setTypeface(font);
        edit.setTypeface(font);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit.getText().toString();
                if (email.isEmpty() || !isValidEmail(email)) {
                    layout.setError("Enter valid email address");
                } else {
                    auth.sendPasswordResetEmail(email);
                    dialog.dismiss();
                }
            }
        });

    }

    private void setupAfterRegister() {
        email.setText(getArguments().getString(Constants.Extra.REGISTER_EMAIL));
        password.setText(getArguments().getString(Constants.Extra.REGISTER_PASSWORD));
    }

    private void startRegisterFragment() {
        Fragment registerFragment = new RegisterFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter_delay, R.anim.bottom_exit, R.anim.bottom_enter_delay, R.anim.bottom_exit)
                .replace(R.id.content_frame, registerFragment)
                .addToBackStack(Constants.FragmentNames.REGISTER_FRAGMENT)
                .commit();
    }


    private void loginWithEmail() {
        if (checkText()) {
            final AlertDialog progress = startProgressDialog();
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progress.dismiss();
                    if (task.isSuccessful()) {
                        openActivity();
                    } else {
                        checkError(task.getException());
                    }
                }
            });
        }
    }

    private void openActivity() {
        InitializationActivity activity = (InitializationActivity) getActivity();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter, R.anim.bottom_exit)
                .remove(LoginFragment.this)
                .commit();
        activity.init();
    }

    private void showSnackBar(int message) {
        Snackbar.make(email, message, Snackbar.LENGTH_LONG).show();
    }

    private void checkError(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException)
            passwordLayout.setError("Password must be at least 6 characters long");
        else if (exception instanceof FirebaseAuthInvalidCredentialsException)
            passwordLayout.setError("The password you entered is incorrect");
        else if (exception instanceof FirebaseAuthInvalidUserException)
            emailLayout.setError("No user with the specified email address exists");

    }

    private boolean checkText() {
        if (email.getText().toString().isEmpty() || !isValidEmail(email.getText().toString())) {
            emailLayout.setError("Enter valid email address");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            passwordLayout.setError("Enter valid password");
            return false;
        }
//        if (password.getText().toString().length() < MIN_CHARS) {
//            password.setError("Password must be at least 6 characters long");
//            return false;
//    }
        emailLayout.setError(null);
        passwordLayout.setError(null);
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
