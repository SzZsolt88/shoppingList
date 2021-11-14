package com.example.recyclerview2.appDataBase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDB {
    private FirebaseFirestore fStore;
    private List<ProductClass> productsList;
    private MutableLiveData<List<ProductClass>> productsMutableLiveData;

    public ProductDB() {
        fStore = FirebaseFirestore.getInstance();
        productsList = new ArrayList<>();
        productsMutableLiveData = new MutableLiveData<>();
    }

    public void getAllProductsOfList(String ownerMail, String listID) {
        DocumentReference listRef = fStore.collection("users").document(ownerMail).collection("lists").document(listID);
        listRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().get("products") != null) {
                        List<Map<String, String>> products = (List<Map<String, String>>) task.getResult().get("products");
                        for (int i = 0; i < products.size(); i++) {
                            String productName = products.get(i).get("name");
                            String quantity = products.get(i).get("quantity");
                            String quantityUnit = products.get(i).get("quantityType");
                            ProductClass getProduct = new ProductClass(productName, quantity, quantityUnit);
                            productsList.add(getProduct);
                        }
                        productsMutableLiveData.postValue(productsList);
                    }
                }
            }
        });
    }

    public void registerNewProduct(String ownerMail, String listID, ProductClass productClass) {
        DocumentReference listRef = fStore.collection("users").document(ownerMail).collection("lists").document(listID);
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("name", productClass.getName());
        newProduct.put("quantity", String.valueOf(productClass.getQuantity()));
        newProduct.put("quantityType", productClass.getQuantityType());
        //newProduct.put("checked", String.valueOf(productChecked));
        //newProduct.put("selected", String.valueOf(productSelected));
        listRef.update("products", FieldValue.arrayUnion(newProduct));
        productsList.add(productClass);
        productsMutableLiveData.postValue(productsList);
    }

    public void deleteProduct(String ownerMail, String listID, ProductClass deleteProduct) {
        DocumentReference listRef = fStore.collection("users").document(ownerMail).collection("lists").document(listID);
        Map<String, String> deleteProductMap = new HashMap<>();
        deleteProductMap.put("name", deleteProduct.getName());
        deleteProductMap.put("quantity", String.valueOf(deleteProduct.getQuantity()));
        deleteProductMap.put("quantityType", deleteProduct.getQuantityType());
        listRef.update("products", FieldValue.arrayRemove(deleteProductMap));
        productsList.remove(deleteProduct);
        productsMutableLiveData.postValue(productsList);
    }

    public void updateProduct(String ownerMail, String listID, ProductClass originalProduct, ProductClass alreadyUpdatedProduct) {
        registerNewProduct(ownerMail,listID,alreadyUpdatedProduct);
        deleteProduct(ownerMail,listID,originalProduct);
    }

    public MutableLiveData<List<ProductClass>> getProductsMutableLiveData() {
        return productsMutableLiveData;
    }

}
