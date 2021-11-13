package com.example.recyclerview2.Lists;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ListClass {
    //változók
    @PrimaryKey(autoGenerate = true)
    int listID;
    private String name;
    String owner;
    boolean isShared = false;
    int syncStatus = 0;

    //kijelölés állapota törlésre és módosításra
    @Ignore
    private boolean selected = false;

    //metódus-ok, constructor és getter/setter
    public ListClass(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
