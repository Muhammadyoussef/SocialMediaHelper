package com.rxmuhammadyoussef.socialmediahelper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class User implements Parcelable {

    private final String accessToken;
    private final String userId;
    private final String email;
    private final String userName;

    public User(
            String accessToken,
            String userId,
            String userName,
            String email) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
    }

    @NonNull
    public String getAccessToken() {
        return accessToken;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "AccessToken: " + accessToken
                .concat("\nUser ID: " + userId)
                .concat("\nEmail: " + email)
                .concat("\nUsername: " + userName);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.userId);
        dest.writeString(this.email);
        dest.writeString(this.userName);
    }

    protected User(Parcel in) {
        this.accessToken = in.readString();
        this.userId = in.readString();
        this.email = in.readString();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {return new User(source);}

        @Override
        public User[] newArray(int size) {return new User[size];}
    };
}
