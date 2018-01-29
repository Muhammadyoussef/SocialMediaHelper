package com.rxmuhammadyoussef.socialmediahelper;

import com.rxmuhammadyoussef.socialmediahelper.model.User;

public interface SocialMediaListener {

    void onLoggedIn(User userInfo);

    void onLoggedOut();

    void onError(Exception e);
}
