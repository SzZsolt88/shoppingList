package com.example.recyclerview2.appDataBase;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class ListClass implements Comparable<ListClass> {
    //változók
    private String listID;
    private String listName;
    private final String owner;
    private final String ownerName;
    boolean isShared;
    private List<ContactClass> sharedWith;
    private boolean selected;

    //metódus-ok, constructor és getter/setter
    public ListClass(String listName, String owner, String ownerName) {
        this.owner = owner;
        this.listName = listName;
        this.ownerName = ownerName;
        isShared = false;
        selected = false;
        sharedWith = new ArrayList<>();
    }

    //updateList funkció miatt, a tulajdonos nem változtatható!
    public ListClass(String listName, String listID, String owner, String ownerName, boolean isShared, List<ContactClass> sharedWith) {
        this.listID = listID;
        this.listName = listName;
        this.owner = owner;
        this.ownerName = ownerName;
        this.isShared = isShared;
        this.sharedWith = sharedWith;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }
    @Exclude
    public boolean isSelected() {
        return selected;
    }
    @Exclude
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ContactClass> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<ContactClass> sharedWith) {
        this.sharedWith = sharedWith;
    }

    @Override
    public int compareTo(ListClass o) {
        return this.getListName().compareToIgnoreCase(o.getListName());
    }
}
