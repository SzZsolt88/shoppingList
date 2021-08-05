package com.example.recyclerview2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class listsShoppingListClass {

    //változók
    @PrimaryKey(autoGenerate = true)
    int listID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "Status")
    private boolean checked = false;

    //kijelölés állapota törlésre és módosításra
    @Ignore
    private boolean selected = false;

    //metódusok, constructor és getter/setter
    public listsShoppingListClass(String name) {
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
