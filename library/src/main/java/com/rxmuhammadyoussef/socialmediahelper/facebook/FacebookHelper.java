package com.rxmuhammadyoussef.socialmediahelper.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.rxmuhammadyoussef.socialmediahelper.Provider;
import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.model.User;
import com.rxmuhammadyoussef.socialmediahelper.util.Preconditions;

import java.util.ArrayList;
import java.util.HashSet;

/**
 TODO: Add class header
 */

public class FacebookHelper {

    private final SocialMediaListener socialMediaListener;
    private final ArrayList<String> readPermissions;
    private final CallbackManager callbackManager = CallbackManager.Factory.create();

    private FacebookHelper(@NonNull SocialMediaListener facebookListener, ArrayList<String> readPermissions) {
        this.socialMediaListener = facebookListener;
        this.readPermissions = readPermissions;
        registerCallBack(callbackManager);
    }

    private void registerCallBack(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                parseUserInfo(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                socialMediaListener.onError(Provider.FACEBOOK, error);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void parseUserInfo(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (jsonObject, response) -> {
            try {
                User user;
                if (loginResult.getRecentlyGrantedPermissions().contains("email")) {
                    user = new User(
                            jsonObject.getString("id"),
                            jsonObject.getString("first_name").concat(" ").concat(jsonObject.getString("last_name")),
                            jsonObject.getString("email"));
                } else {
                    user = new User(
                            jsonObject.getString("id"),
                            jsonObject.getString("first_name").concat(" ").concat(jsonObject.getString("last_name")),
                            "");
                }
                socialMediaListener.onLoggedIn(Provider.FACEBOOK, user);
            } catch (Exception e) {
                socialMediaListener.onError(Provider.FACEBOOK, e);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void login(Activity activity) {
        Preconditions.checkNotNull(activity);
        LoginManager.getInstance().logInWithReadPermissions(activity, readPermissions);
    }

    public boolean isSessionActive() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        socialMediaListener.onLoggedOut(Provider.FACEBOOK);
    }

    public static class Builder {

        private SocialMediaListener socialMediaListener;

        private final HashSet<String> readPermissions;

        public Builder(SocialMediaListener socialMediaListener) {
            Preconditions.checkNotNull(socialMediaListener);
            this.socialMediaListener = socialMediaListener;
            this.readPermissions = new HashSet<>();
            this.readPermissions.add("public_profile");
        }

        public Builder getEmail() {
            readPermissions.add("email");
            return this;
        }

        public FacebookHelper build() {
            return new FacebookHelper(socialMediaListener, new ArrayList<>(readPermissions));
        }
    }
}
