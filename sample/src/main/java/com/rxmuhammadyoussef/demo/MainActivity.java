package com.rxmuhammadyoussef.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.facebook.FacebookHelper;
import com.rxmuhammadyoussef.socialmediahelper.google.GoogleHelper;
import com.rxmuhammadyoussef.socialmediahelper.model.User;
import com.rxmuhammadyoussef.socialmediahelper.twitter.TwitterHelper;

public class MainActivity extends AppCompatActivity {

    private FacebookHelper facebookHelper;
    private TwitterHelper twitterHelper;
    private GoogleHelper googleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookHelper = new FacebookHelper.Builder(this)
                .registerCallback(new SocialMediaListener() {
                    @Override
                    public void onLoggedIn(User userInfo) {
                        Log.d("Muhammad", userInfo.toString());
                        ((Button)findViewById(R.id.btn_facebook)).setText("Logout from Facebook");
                    }

                    @Override
                    public void onLoggedOut() {
                        Log.d("Muhammad", "Logged out");
                        ((Button)findViewById(R.id.btn_facebook)).setText("Login by Facebook");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Muhammad", e.getMessage());
                    }
                })
                .requestPublicProfile()
                .requestEmail()
                .build();
        twitterHelper = new TwitterHelper.Builder(this, getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
                .registerCallback(new SocialMediaListener() {
                    @Override
                    public void onLoggedIn(User userInfo) {
                        Log.d("Muhammad", userInfo.toString());
                        ((Button)findViewById(R.id.btn_twitter)).setText("Logout from Twitter");
                    }

                    @Override
                    public void onLoggedOut() {
                        Log.d("Muhammad", "Logged out");
                        ((Button)findViewById(R.id.btn_twitter)).setText("Login by Twitter");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Muhammad", e.getMessage());
                    }
                })
                .requestEmail()
                .build();
        googleHelper = new GoogleHelper.Builder()
                .registerCallback(new SocialMediaListener() {
                    @Override
                    public void onLoggedIn(User userInfo) {
                        Log.d("Muhammad", userInfo.toString());
                        ((Button)findViewById(R.id.btn_google)).setText("Logout from Google");
                    }

                    @Override
                    public void onLoggedOut() {
                        Log.d("Muhammad", "Logged out");
                        ((Button)findViewById(R.id.btn_google)).setText("Login by Google");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Muhammad", e.getMessage());
                    }
                })
                .requestPublicProfile()
                .requestEmail()
                .build();
    }

    public void onFacebookClick(View view) {
        if (facebookHelper.isSessionActive()) {
            facebookHelper.logout();
        } else {
            facebookHelper.login(this);
        }
    }

    public void onTwitterClick(View view) {
        if (twitterHelper.isSessionActive()) {
            twitterHelper.logout();
        } else {
            twitterHelper.login(this);
        }
    }

    public void onGoogleClick(View view) {
        if (googleHelper.isSessionActive(this)) {
            googleHelper.logout();
        } else {
            googleHelper.login(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookHelper.onActivityResult(requestCode, resultCode, data);
        twitterHelper.onActivityResult(requestCode, resultCode, data);
        googleHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
