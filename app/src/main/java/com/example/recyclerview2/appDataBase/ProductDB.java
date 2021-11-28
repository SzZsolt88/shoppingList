package com.example.recyclerview2.appDataBase;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.recyclerview2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDB {
    private static final String TAG = "ProductDB";
    private FirebaseFirestore fStore;
    private List<ProductClass> productsList;
    private MutableLiveData<List<ProductClass>> productsMutableLiveData;
    private String ownerMail;
    private String listID;
    private int quantity;

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
        quantity = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public void saveCheckedStatus(ProductClass boughtProduct, String category) throws InterruptedException {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update("products", FieldValue.arrayRemove(boughtProduct));
        productsList.remove(boughtProduct);
        boughtProduct.setChecked(!boughtProduct.isChecked());
        listRef.update("products", FieldValue.arrayUnion(boughtProduct));
        productsList.add(boughtProduct);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
        String  statisticsID = new SimpleDateFormat("yyyyMM").format(new Date());
        DocumentReference statisticsRef = fStore.collection(USERS).document(ownerMail).collection("statistics").document(statisticsID);
        statisticsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int quantity_buffer = 0;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        quantity_buffer = document.get(category+".quantity",Integer.TYPE);
                        if(boughtProduct.isChecked()) {
                            setQuantity(quantity_buffer + 1);
                            quantity_buffer++;
                        } else {
                            if(quantity_buffer > 0) {
                                setQuantity(quantity_buffer - 1);
                                quantity_buffer--;
                            }
                        }
                        //Thread.sleep(5000);
                        statisticsRef
                                .update(category+".quantity", quantity_buffer)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                        Log.d(TAG, "quantity_buffer: " + quantity_buffer);
                        Log.d(TAG, "quantity: " + getQuantity());
                        Log.d(TAG, "category: " + category);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public MutableLiveData<List<ProductClass>> getProductsMutableLiveData() {
        return productsMutableLiveData;
    }

}