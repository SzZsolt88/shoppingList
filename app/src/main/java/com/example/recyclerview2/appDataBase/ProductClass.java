package com.example.recyclerview2.appDataBase;

public class ProductClass implements Comparable<ProductClass>{
    private int ProductID;
    private String name;
    private String quantity;
    private String quantityType;
    private boolean checked = false;
    private String listID;


    private boolean selected = false;

    public ProductClass(String name, String quantity, String quantityType) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public ProductClass(String name, String quantity, String quantityType, boolean checked, String listID) {
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

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
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

    @Override
    public int compareTo(ProductClass o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
}
