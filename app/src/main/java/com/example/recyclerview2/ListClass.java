package com.example.recyclerview2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ListClass {
    //változók
    @PrimaryKey(autoGenerate = true)
    int listID;

    @ColumnInfo(name = "name")
    private String name;

    //kijelölés állapota törlésre és módosításra
    @Ignore
    private boolean selected = false;

    //metódus-ok, constructor és getter/setter
    public ListClass(String name) {
        this.name = name;
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
}
