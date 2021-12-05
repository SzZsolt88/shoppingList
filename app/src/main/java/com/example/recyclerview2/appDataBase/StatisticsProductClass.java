package com.example.recyclerview2.appDataBase;

import com.google.firebase.firestore.Exclude;

import java.util.Comparator;

public class StatisticsProductClass{
    private String name;
    private String lastBuyDate;
    private int average;

    public StatisticsProductClass(String name, String lastBuyDate, int average) {
        this.name = name;
        this.lastBuyDate = lastBuyDate;
        this.average = average;
    }

    //Getter&Setter

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getlastBuyDate() {
        return lastBuyDate;
    }

    public void setlastBuyDate(String lastBuyDate) {
        this.lastBuyDate = lastBuyDate;
    }

    public int getaverage() {
        return average;
    }

    public void setaverage(int average) {
        this.average = average;
    }

}
