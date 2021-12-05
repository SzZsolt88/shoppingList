package com.example.recyclerview2.appDataBase;

import com.google.firebase.firestore.Exclude;

import java.util.Comparator;

public class StatisticsProductClass{
    private String name;
    private String lastBuyDate;
    private Long average;

    public StatisticsProductClass(String name, String lastBuyDate, Long average) {
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

    public Long getaverage() {
        return average;
    }

    public void setaverage(Long average) {
        this.average = average;
    }

}
