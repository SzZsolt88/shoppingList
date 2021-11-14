package com.example.recyclerview2.Lists;

import android.os.Parcel;
import android.os.Parcelable;

public class ListClass implements Parcelable {
    //változók
    //int listID;
    private String name;


    //kijelölés állapota törlésre és módosításra
    private boolean selected = false;

    //metódus-ok, constructor és getter/setter
    public ListClass(String name) {
        this.name = name;
    }

    //metódus-ok, constructor és getter/setter
    public ListClass() {
        this.name = "Unnamed";
    }

    //public int getListID() {
      //  return listID;
    //}

    //public void setListID(int listID) {
      //  this.listID = listID;
    //}

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(listID);
        dest.writeString(name);

    }

    protected ListClass(Parcel in) {
        //listID = in.readInt();
        name = in.readString();
    }

    public static final Creator<ListClass> CREATOR = new Creator<ListClass>() {
        @Override
        public ListClass createFromParcel(Parcel in) {
            return new ListClass(in);
        }

        @Override
        public ListClass[] newArray(int size) {
            return new ListClass[size];
        }
    };

}
