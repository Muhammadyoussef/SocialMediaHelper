package com.rxmuhammadyoussef.socialmediahelper.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.model.User;
import com.rxmuhammadyoussef.socialmediahelper.util.Preconditions;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class TwitterHelper {

    private final boolean shouldRequestEmail;
    private final TwitterAuthClient authClient;
    private final SocialMediaListener socialMediaListener;

    private TwitterHelper(SocialMediaListener socialMediaListener, boolean shouldGetEmail) {
        this.socialMediaListener = socialMediaListener;
        this.shouldRequestEmail = shouldGetEmail;
        this.authClient = new TwitterAuthClient();
    }

    public void login(Activity activity) {
        Preconditions.checkNotNull(activity);
        authClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession twitterSession = result.data;
                if (shouldRequestEmail) {
                    requestEmail(twitterSession);
                } else {
                    socialMediaListener.onLoggedIn(
                            new User(
                                    String.valueOf(twitterSession.getUserId()),
                                    twitterSession.getUserName(),
                                    ""));
                }
            }

            @Override
            public void failure(TwitterException exception) {
                socialMediaListener.onError(exception);
            }
        });
    }

    public boolean isSessionActive() {
        return TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    }

    public void logout() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        socialMediaListener.onLoggedOut();
    }

    private void requestEmail(TwitterSession twitterSession) {
        authClient.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                socialMediaListener.onLoggedIn(
                        new User(
                                String.valueOf(twitterSession.getUserId()),
                                result.data,
                                twitterSession.getUserName()));
            }

            @Override
            public void failure(TwitterException exception) {
                socialMediaListener.onError(exception);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        authClient.onActivityResult(requestCode, resultCode, data);
    }

    public static class Builder {

        private SocialMediaListener socialMediaListener;
        private boolean shouldRequestEmail;

        public Builder(Context context, String consumerKey, String consumerSecret) {
            TwitterConfig twitterConfig = new TwitterConfig.Builder(context)
                    .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                    .build();
            Twitter.initialize(twitterConfig);
        }

        public Builder registerCallback(SocialMediaListener socialMediaListener) {
            this.socialMediaListener = socialMediaListener;
            return this;
        }

        public Builder requestEmail() {
            shouldRequestEmail = true;
            return this;
        }

        public TwitterHelper build() {
            return new TwitterHelper(socialMediaListener, shouldRequestEmail);
        }
    }
}
