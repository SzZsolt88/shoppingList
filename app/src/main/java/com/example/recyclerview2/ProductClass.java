package com.example.recyclerview2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ProductClass {
    //adatbázisba
    @PrimaryKey(autoGenerate = true)
    private int ProductID;
    @ColumnInfo(name = "Termek neve")
    private String name;
    @ColumnInfo(name = "Mennyiseg")
    private String quantity;
    @ColumnInfo(name = "Egyseg")
    private String quantityType;
    @ColumnInfo(name = "Statusz")
    private boolean checked = false;

    @ColumnInfo(name = "listID")
    private int listID;


    //segédváltozó a törlés és módosítás funkciókhoz
    @Ignore
    private boolean selected = false;

    //Constructor
    @Ignore
    public ProductClass(String name, String quantity, String quantityType) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public ProductClass(String name, String quantity, String quantityType, boolean checked, int listID) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.checked = checked;
        this.listID = listID;
    }

    //Getter&Setter
    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean setSelected(boolean selected) {
        this.selected = selected;
        return selected;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }


    @Override
    public String toString() {
        return "ProductClass{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", quantityType='" + quantityType + '\'' +
                ", checked=" + checked +
                '}';
    }
}
