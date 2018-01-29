package com.rxmuhammadyoussef.socialmediahelper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.rxmuhammadyoussef.socialmediahelper.util.Preconditions;

public class SocialMediaHelper {

    public static Builder init(Context appContext) {
        Preconditions.checkNotNull(appContext);
        return new Builder(appContext);
    }

    public static class Builder {

        private final Context appContext;
        private boolean facebookEnabled;

        Builder(@NonNull Context appContext) {this.appContext = appContext;}

        public Builder withFacebookProvider() {
            this.facebookEnabled = true;
            return this;
        }

        public void create() {
            if (facebookEnabled) {
                FacebookSdk.sdkInitialize(appContext);
            }
        }
    }
}
