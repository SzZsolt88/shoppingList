package com.example.recyclerview2;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductClass implements Parcelable {
    //adatbázisba

    private int ProductID;
    private String name;
    private int quantity;
    private String quantityType;
    private boolean checked = false;

    //segédváltozó a törlés és módosítás funkciókhoz

    private boolean selected = false;

    //Constructor
    public ProductClass() {
    }

    //Constructor
    public ProductClass(String name, int quantity, String quantityType) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public ProductClass(String name, int quantity, String quantityType, boolean checked, int listID) {
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.checked = checked;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
    protected ProductClass(Parcel in) {
        ProductID = in.readInt();
        name = in.readString();
        quantity = in.readInt();
        quantityType = in.readString();
        checked = in.readByte() != 0;
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ProductID);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeString(quantityType);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductClass> CREATOR = new Creator<ProductClass>() {
        @Override
        public ProductClass createFromParcel(Parcel in) {
            return new ProductClass(in);
        }

        @Override
        public ProductClass[] newArray(int size) {
            return new ProductClass[size];
        }
    };

    }

