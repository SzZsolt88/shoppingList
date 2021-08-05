package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class shoppingListProductClass {
    //adatbazisba
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

    @ColumnInfo(name = "lista")
    private String productToList;

    //segédváltozó a törlés és módosítás funkciókhoz
    @Ignore
    private boolean selected = false;

    //Constructor
    @Ignore
    public shoppingListProductClass(String name, String quantity, String quantityType) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public shoppingListProductClass(int ID) {
        this.ProductID = ID;
    }

    public shoppingListProductClass(String name, String quantity, String quantityType, boolean checked, String productToList) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.checked = checked;
        this.productToList = productToList;
    }

    public shoppingListProductClass(int productID, String name, String quantity, String quantityType, boolean checked, String productToList) {
        this.ProductID = productID;
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.checked = checked;
        this.productToList = productToList;
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

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getProductToList() {
        return productToList;
    }

    public void setProductToList(String productToList) {
        this.productToList = productToList;
    }


    @Override
    public String toString() {
        return "shoppingListProductClass{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", quantityType='" + quantityType + '\'' +
                ", checked=" + checked +
                '}';
    }
}
