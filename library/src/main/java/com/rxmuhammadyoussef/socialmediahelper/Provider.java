package com.rxmuhammadyoussef.socialmediahelper;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.rxmuhammadyoussef.socialmediahelper.Provider.FACEBOOK;
import static com.rxmuhammadyoussef.socialmediahelper.Provider.GOOGLE;
import static com.rxmuhammadyoussef.socialmediahelper.Provider.TWITTER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({FACEBOOK, GOOGLE, TWITTER})
public @interface Provider {

    int FACEBOOK = 0;
    int GOOGLE = 1;
    int TWITTER = 2;
}