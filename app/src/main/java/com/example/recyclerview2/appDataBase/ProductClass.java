package com.example.recyclerview2.appDataBase;

import com.google.firebase.firestore.Exclude;

import java.util.Comparator;

public class ProductClass implements Comparable<ProductClass>{
    private String name;
    private String quantity;
    private String quantityType;
    private String productCategory;
    private boolean checked = false;

    private boolean selected = false;

    public ProductClass(String name, String quantity, String quantityType) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public ProductClass(String name, String quantity, String quantityType, String productCategory, boolean checked) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.productCategory = productCategory;
        this.checked = checked;
    }

    //Getter&Setter

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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    @Exclude
    public boolean isSelected() {
        return selected;
    }
    @Exclude
    public void setSelected(boolean selected) {
        this.selected = selected;
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

    @Override
    public int compareTo(ProductClass o) {
        return Comparator
                .comparing(ProductClass::isChecked)
                .thenComparing(ProductClass::getName).compare(this,o);
    }
}
