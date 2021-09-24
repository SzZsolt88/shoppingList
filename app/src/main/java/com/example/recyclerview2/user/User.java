package com.example.recyclerview2.user;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("e_mail")
    private String e_mail;

    @SerializedName("password")
    private String password;

    public User(String first_name, String last_name, String e_mail, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.e_mail = e_mail;
        this.password = password;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEMail() {
        return e_mail;
    }

    public String getPassword() {
        return password;
    }
}
