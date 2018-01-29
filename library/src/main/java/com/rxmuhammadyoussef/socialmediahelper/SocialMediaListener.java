package com.rxmuhammadyoussef.socialmediahelper;

import com.rxmuhammadyoussef.socialmediahelper.model.User;

public interface SocialMediaListener {

    void onLoggedIn(@Provider int provider, User userInfo);

    void onLoggedOut(@Provider int provider);

    void onError(@Provider int provider, Exception e);
}
