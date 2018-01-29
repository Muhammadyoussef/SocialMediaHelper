package com.rxmuhammadyoussef.socialmediahelper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class User implements Parcelable {

    private final String accessToken;
    private final String userId;
    private final String firstName;
    private final String lastName;
    private final String email;

    public User(
            String accessToken,
            String userId,
            String firstName,
            String lastName,
            String email) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "AccessToken: " + accessToken
                .concat("\nUser ID: " + userId)
                .concat("\nFirst name: " + firstName)
                .concat("\nLast name: " + lastName)
                .concat("\nEmail: " + email);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.userId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
    }

    protected User(Parcel in) {
        this.accessToken = in.readString();
        this.userId = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {return new User(source);}

        @Override
        public User[] newArray(int size) {return new User[size];}
    };
}
