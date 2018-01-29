package com.rxmuhammadyoussef.socialmediahelper.facebook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.model.User;
import com.rxmuhammadyoussef.socialmediahelper.util.Preconditions;

import java.util.ArrayList;
import java.util.HashSet;

/**
 TODO: Add class header
 */

public class FacebookRequest {

    private final SocialMediaListener socialMediaListener;
    private final ArrayList<String> readPermissions;
    private final CallbackManager callbackManager = CallbackManager.Factory.create();

    private FacebookRequest(@NonNull SocialMediaListener facebookListener, ArrayList<String> readPermissions) {
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
                socialMediaListener.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                socialMediaListener.onError(error);
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
                            loginResult.getAccessToken().getToken(),
                            jsonObject.getString("id"),
                            jsonObject.getString("first_name"),
                            jsonObject.getString("last_name"),
                            jsonObject.getString("email"));
                } else {
                    user = new User(
                            loginResult.getAccessToken().getToken(),
                            jsonObject.getString("id"),
                            jsonObject.getString("first_name"),
                            jsonObject.getString("last_name"),
                            "");
                }
                socialMediaListener.onLoggedIn(user);
            } catch (Exception e) {
                socialMediaListener.onError(e);
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

    public void login(Fragment fragment) {
        Preconditions.checkNotNull(fragment);
        LoginManager.getInstance().logInWithReadPermissions(fragment, readPermissions);
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        socialMediaListener.onLoggedOut();
    }

    public static class RequestBuilder {

        private SocialMediaListener socialMediaListener;

        private final HashSet<String> readPermissions;

        public RequestBuilder(SocialMediaListener socialMediaListener) {
            Preconditions.checkNotNull(socialMediaListener);
            this.socialMediaListener = socialMediaListener;
            readPermissions = new HashSet<>();
        }

        public RequestBuilder getPublicProfile() {
            readPermissions.add("public_profile");
            return this;
        }

        public RequestBuilder getEmail() {
            readPermissions.add("email");
            return this;
        }

        public FacebookRequest build() {
            return new FacebookRequest(socialMediaListener, new ArrayList<>(readPermissions));
        }
    }
}
