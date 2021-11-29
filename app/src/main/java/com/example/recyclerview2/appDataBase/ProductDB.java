package com.example.recyclerview2.appDataBase;

import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductDB extends FireStoreInstance {
    private static final String TAG = "TAG";
    private FirebaseFirestore fStore;
    private List<ProductClass> productsList;
    private MutableLiveData<List<ProductClass>> productsMutableLiveData;
    private String ownerMail;
    private String listID;
    private int quantity;
    private Source source;

    public ProductDB(String ownerMail, String listID) {
        this.ownerMail = ownerMail;
        this.listID = listID;
        fStore = FirebaseFirestore.getInstance();
        productsList = new ArrayList<>();
        productsMutableLiveData = new MutableLiveData<>();
        quantity = 0;
        source = Source.CACHE;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void getAllProductsOfList(boolean connected) {
        if (connected) {
            source = Source.SERVER;
        } else {
            source = Source.CACHE;
        }
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().get(PRODUCT_ARRAY) != null) {
                        List<Map<String, ProductClass>> products = (List<Map<String, ProductClass>>) task.getResult().get(PRODUCT_ARRAY);
                        for (int i = 0; i<products.size(); i++) {
                            ProductClass productClass = new ProductClass(
                                    String.valueOf(products.get(i).get(PRODUCT_NAME)),
                                    String.valueOf(products.get(i).get(PRODUCT_QUANTITY)),
                                    String.valueOf(products.get(i).get(PRODUCT_QUANTITY_TYPE)),
                                    Boolean.valueOf(String.valueOf(products.get(i).get(PRODUCT_CHECKED_STATUS))));
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
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayUnion(productClass));
        productsList.add(productClass);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
    }
    public void deleteProduct(ProductClass deleteProduct) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayRemove(deleteProduct));
        productsList.remove(deleteProduct);
        productsMutableLiveData.postValue(productsList);
    }
    public void updateProduct(ProductClass originalProduct, ProductClass alreadyUpdatedProduct) {
        deleteProduct(originalProduct);
        registerNewProduct(alreadyUpdatedProduct);
    }
    public void saveCheckedStatus(ProductClass boughtProduct, String category) throws InterruptedException {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayRemove(boughtProduct));
        productsList.remove(boughtProduct);
        boughtProduct.setChecked(!boughtProduct.isChecked());
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayUnion(boughtProduct));
        productsList.add(boughtProduct);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
        String  statisticsID = new SimpleDateFormat("yyyyMM").format(new Date());
        DocumentReference statisticsRef = fStore.collection(USERS).document(ownerMail).collection(COLLECTION_OF_STATISTICS).document(statisticsID);
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
