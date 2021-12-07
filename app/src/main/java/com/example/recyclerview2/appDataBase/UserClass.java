package com.example.recyclerview2.appDataBase;

import android.os.Parcel;
import android.os.Parcelable;

public class UserClass implements Parcelable {
    private String userID;
    private String fullName;
    private String userName;
    private String uMail;

    public UserClass() {};

    public UserClass(String userID, String fullName, String uName, String uMail) {
        this.userID = userID;
        this.fullName = fullName;
        this.userName = uName;
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
        return userName;
    }

    public void setuName(String uName) {
        this.userName = uName;
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
        dest.writeString(userName);
        dest.writeString(uMail);
    }

    protected UserClass(Parcel in) {
        userID = in.readString();
        fullName = in.readString();
        userName = in.readString();
        uMail = in.readString();
    }

    public static final Creator<UserClass> CREATOR = new Creator<UserClass>() {
        @Override
        public UserClass createFromParcel(Parcel in) {
            return new UserClass(in);
        }

        @Override
        public UserClass[] newArray(int size) {
            return new UserClass[size];
        }
    };
}


