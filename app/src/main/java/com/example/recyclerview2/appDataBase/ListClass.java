package com.example.recyclerview2.appDataBase;


import com.google.firebase.firestore.Exclude;

import java.util.List;
import java.util.Map;

public class ListClass implements Comparable<ListClass> {
    //változók
    private String listID;
    private String name;
    private String owner;
    boolean isShared = false;
    private List<Map<String,String>> products;
    @Exclude
    private boolean selected = false;

    //metódus-ok, constructor és getter/setter
    public ListClass(String name, String owner) {
        this.owner = owner;
        this.name = name;
    }

    //updateList funkció miatt, a tulajdonos nem változtatható!
    public ListClass(String name, String listID, String owner) {
        this.listID = listID;
        this.name = name;
        this.owner = owner;
    }

    public List<Map<String, String>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, String>> products) {
        this.products = products;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
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

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(ListClass o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
}
