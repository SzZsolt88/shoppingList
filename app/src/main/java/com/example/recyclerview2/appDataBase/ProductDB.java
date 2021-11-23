package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDB {
    private FirebaseFirestore fStore;
    private List<ProductClass> productsList;
    private MutableLiveData<List<ProductClass>> productsMutableLiveData;
    private String ownerMail;
    private String listID;

    private static final String USERS = "users";
    private static final String LISTS = "lists";
    private static final String PRODUCTS = "products";
    private static final String PRODUCTS_OF_LIST = "productsOfList";

    public ProductDB(String ownerMail, String listID) {
        this.ownerMail = ownerMail;
        this.listID = listID;
        fStore = FirebaseFirestore.getInstance();
        productsList = new ArrayList<>();
        productsMutableLiveData = new MutableLiveData<>();
    }

    public void getAllProductsOfList() {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().get("products") != null) {
                        List<Map<String, ProductClass>> products = (List<Map<String, ProductClass>>) task.getResult().get("products");
                        for (int i = 0; i<products.size(); i++) {
                            ProductClass productClass = new ProductClass(
                                   String.valueOf(products.get(i).get("name")),
                                   String.valueOf(products.get(i).get("quantity")),
                                   String.valueOf(products.get(i).get("quantityType")),
                                   Boolean.valueOf(String.valueOf(products.get(i).get("checked"))));
                           productsList.add(productClass);
                        }
                        Collections.sort(productsList);
                        productsMutableLiveData.postValue(productsList);
                    }
                }
            }
        });
    }

    public void registerNewProduct(ProductClass productClass) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update("products", FieldValue.arrayUnion(productClass));
        productsList.add(productClass);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
    }

    public void deleteProduct(ProductClass deleteProduct) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update("products", FieldValue.arrayRemove(deleteProduct));
        productsList.remove(deleteProduct);
        productsMutableLiveData.postValue(productsList);
    }

    public void updateProduct(ProductClass originalProduct, ProductClass alreadyUpdatedProduct) {
        deleteProduct(originalProduct);
        registerNewProduct(alreadyUpdatedProduct);
    }

    public void saveCheckedStatus(ProductClass boughtProduct) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update("products", FieldValue.arrayRemove(boughtProduct));
        productsList.remove(boughtProduct);
        boughtProduct.setChecked(!boughtProduct.isChecked());
        listRef.update("products", FieldValue.arrayUnion(boughtProduct));
        productsList.add(boughtProduct);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
    }

    public MutableLiveData<List<ProductClass>> getProductsMutableLiveData() {
        return productsMutableLiveData;
    }

}