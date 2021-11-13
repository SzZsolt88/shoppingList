package com.example.recyclerview2.appDataBase;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userID;
    private String fullName;
    private String uName;
    private String uMail;

    public User() {};

    public User(String userID, String fullName, String uName, String uMail) {
        this.userID = userID;
        this.fullName = fullName;
        this.uName = uName;
        this.uMail = uMail;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuMail() {
        return uMail;
    }

    public void setuMail(String uMail) {
        this.uMail = uMail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(fullName);
        dest.writeString(uName);
        dest.writeString(uMail);
    }

    protected User(Parcel in) {
        userID = in.readString();
        fullName = in.readString();
        uName = in.readString();
        uMail = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}


