package com.rxmuhammadyoussef.demo;

import android.app.Application;

import com.rxmuhammadyoussef.socialmediahelper.SocialMediaHelper;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SocialMediaHelper.init(this)
                .withFacebookProvider()
                .withTwitterProvider(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
                .create();
    }
}
