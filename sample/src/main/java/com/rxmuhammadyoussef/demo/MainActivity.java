package com.rxmuhammadyoussef.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.facebook.FacebookRequest;
import com.rxmuhammadyoussef.socialmediahelper.model.User;

public class MainActivity extends AppCompatActivity implements SocialMediaListener {

    private FacebookRequest facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookLogin = new FacebookRequest.RequestBuilder(this)
                .getPublicProfile()
                .getEmail()
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookLogin.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loginByFacebook(View view) {
        facebookLogin.login(this);
    }

    @Override
    public void onLoggedIn(User userInfo) {
        Log.d("SocialMediaHelper", "User Info: " + userInfo.toString());
    }

    @Override
    public void onCancel() {
        Log.d("SocialMediaHelper", "Canceled");
    }

    @Override
    public void onError(Exception e) {
        Log.e("SocialMediaHelper", "Error: " + e.getMessage());
    }

    @Override
    public void onLoggedOut() {
        Log.d("SocialMediaHelper", "LoggedOut");
    }
}
