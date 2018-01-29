package com.rxmuhammadyoussef.socialmediahelper.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rxmuhammadyoussef.socialmediahelper.Provider;
import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.model.User;
import com.rxmuhammadyoussef.socialmediahelper.util.Preconditions;

/**
 TODO: Add class header
 */

public class GoogleHelper {
    private static final int REQUEST_CODE = 1001;
    private final SocialMediaListener socialMediaListener;
    private final GoogleSignInOptions googleSignInOptions;
    private final boolean shouldGetEmail;
    private GoogleSignInClient mGoogleSignInClient;

    private GoogleHelper(SocialMediaListener socialMediaListener, GoogleSignInOptions googleSignInOptions, boolean shouldGetEmail) {
        this.shouldGetEmail = shouldGetEmail;
        this.socialMediaListener = socialMediaListener;
        this.googleSignInOptions = googleSignInOptions;
    }

    public void login(Activity activity) {
        mGoogleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                if (shouldGetEmail) {
                    socialMediaListener.onLoggedIn(
                            Provider.GOOGLE,
                            new User(
                                    acct.getId(),
                                    acct.getDisplayName(),
                                    acct.getEmail()));
                } else {
                    socialMediaListener.onLoggedIn(
                            Provider.GOOGLE,
                            new User(
                                    acct.getId(),
                                    acct.getDisplayName(),
                                    ""));
                }
            } catch (ApiException e) {
                socialMediaListener.onError(Provider.GOOGLE, e);
            }
        }
    }

    public boolean isSessionActive(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

    public void logout() {
        mGoogleSignInClient.signOut();
        socialMediaListener.onLoggedOut(Provider.GOOGLE);
    }

    public static class Builder {

        private SocialMediaListener socialMediaListener;
        private boolean shouldGetEmail;
        private GoogleSignInOptions.Builder builder;

        public Builder(SocialMediaListener socialMediaListener) {
            Preconditions.checkNotNull(socialMediaListener);
            this.socialMediaListener = socialMediaListener;
            this.builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
        }

        public Builder getEmail() {
            shouldGetEmail = true;
            builder.requestEmail();
            return this;
        }

        public GoogleHelper build() {
            return new GoogleHelper(socialMediaListener,
                    builder.requestProfile().build(),
                    shouldGetEmail);
        }
    }
}
