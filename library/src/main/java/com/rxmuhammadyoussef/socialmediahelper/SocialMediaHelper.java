package com.rxmuhammadyoussef.socialmediahelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.rxmuhammadyoussef.socialmediahelper.util.Preconditions;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class SocialMediaHelper {

    public static Builder init(Context appContext) {
        Preconditions.checkNotNull(appContext);
        return new Builder(appContext);
    }

    public static class Builder {

        private final Context appContext;
        private boolean facebookEnabled;
        private boolean twitterEnabled;
        private String consumerKey;
        private String consumerSecret;

        Builder(@NonNull Context appContext) {this.appContext = appContext;}

        public Builder withFacebookProvider() {
            this.facebookEnabled = true;
            return this;
        }

        public Builder withTwitterProvider(String consumerKey, String consumerSecret) {
            Preconditions.checkNotNull(consumerKey, consumerKey);
            this.twitterEnabled = true;
            this.consumerKey = consumerKey;
            this.consumerSecret = consumerSecret;
            return this;
        }

        public void create() {
            if (facebookEnabled) {
                FacebookSdk.sdkInitialize(appContext);
            }
            if (twitterEnabled) {
                TwitterConfig twitterConfig = new TwitterConfig.Builder(appContext)
                        .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                        .build();
                Twitter.initialize(twitterConfig);
            }
        }
    }
}
