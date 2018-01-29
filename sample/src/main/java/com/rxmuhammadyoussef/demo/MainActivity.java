package com.rxmuhammadyoussef.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rxmuhammadyoussef.socialmediahelper.Provider;
import com.rxmuhammadyoussef.socialmediahelper.SocialMediaListener;
import com.rxmuhammadyoussef.socialmediahelper.facebook.FacebookHelper;
import com.rxmuhammadyoussef.socialmediahelper.google.GoogleHelper;
import com.rxmuhammadyoussef.socialmediahelper.model.User;
import com.rxmuhammadyoussef.socialmediahelper.twitter.TwitterHelper;

public class MainActivity extends AppCompatActivity implements SocialMediaListener {

    private FacebookHelper facebookLogin;
    private TwitterHelper twitterLogin;
    private GoogleHelper googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookLogin = new FacebookHelper.Builder(this)
                .getEmail()
                .build();
        twitterLogin = new TwitterHelper.Builder(this)
                .getEmail()
                .build();
        googleLogin = new GoogleHelper.Builder(this)
                .getEmail()
                .build();
    }

    public void onTwitterClick(View view) {
        if (twitterLogin.isSessionActive()) {
            twitterLogin.logout();
        } else {
            twitterLogin.login(this);
        }
    }

    public void onFacebookClick(View view) {
        if (facebookLogin.isSessionActive()) {
            facebookLogin.logout();
        } else {
            facebookLogin.login(this);
        }
    }

    public void onGoogleClick(View view) {
        if (googleLogin.isSessionActive(this)) {
            googleLogin.logout();
        } else {
            googleLogin.login(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookLogin.onActivityResult(requestCode, resultCode, data);
        twitterLogin.onActivityResult(requestCode, resultCode, data);
        googleLogin.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoggedIn(int provider, User userInfo) {
        Log.d("Muhammad", "User: " + userInfo.toString());
        if (provider == Provider.FACEBOOK) {
            ((Button) findViewById(R.id.btn_facebook)).setText("Logout form Facebook");
        } else if (provider == Provider.TWITTER) {
            ((Button) findViewById(R.id.btn_twitter)).setText("Logout form Twitter");
        }else if (provider == Provider.GOOGLE) {
            ((Button) findViewById(R.id.btn_google)).setText("Logout form Google");
        }
    }

    @Override
    public void onError(int provider, Exception e) {
        Log.d("Muhammad", "error: " + e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoggedOut(int provider) {
        Log.d("Muhammad", "logged out");
        if (provider == Provider.FACEBOOK) {
            ((Button) findViewById(R.id.btn_facebook)).setText("Login by Facebook");
        } else if (provider == Provider.TWITTER) {
            ((Button) findViewById(R.id.btn_twitter)).setText("Login by Twitter");
        }else if (provider == Provider.GOOGLE) {
            ((Button) findViewById(R.id.btn_google)).setText("Login by Google");
        }
    }
}
