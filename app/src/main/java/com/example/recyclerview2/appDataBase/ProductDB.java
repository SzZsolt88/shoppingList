package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recyclerview2.ProductClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDB {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private static final String TAG = "ProductDB/";

    public ProductDB() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public void singOut() { fAuth.signOut(); }

    public List<ProductClass> getListOfProducts(String ownerMail, String listName) {
        Log.d(TAG, ownerMail + " => ");
        Log.d(TAG, listName + " => ");
        List<ProductClass> products = new ArrayList<ProductClass>();
        CollectionReference listsRef = fStore.collection("users").document(ownerMail).collection("lists").document(listName).collection("products");
        listsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        ProductClass product = document.toObject(ProductClass.class);
                        products.add(product);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());

                }
            }
        });
        return products;
    }

    public void createProduct(String ownerMail, String listName, String productName, int productID, int productQuantity, String productQuantityType, boolean productChecked, boolean productSelected) {
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("ProductID", String.valueOf(productID));
        newProduct.put("name", productName);
        newProduct.put("quantity", String.valueOf(productQuantity));
        newProduct.put("quantityType", productQuantityType);
        newProduct.put("checked", String.valueOf(productChecked));
        newProduct.put("selected", String.valueOf(productSelected));
        DocumentReference newProductRef = fStore.collection("users").document(ownerMail).collection("lists").document(listName).collection("products").document(productName);;
        newProductRef.set(newProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error writing document", e);
            }
        });
    }
}
