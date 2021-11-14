package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recyclerview2.Lists.ListClass;
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

public class ListDB {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private static final String TAG = "ListDB/";

    public ListDB() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public void singOut() { fAuth.signOut(); }

    public List<ListClass> getListOfUser(String ownerID) {
        List<ListClass> lists = new ArrayList<ListClass>();
        CollectionReference listsRef = fStore.collection("users").document(ownerID).collection("lists");
        listsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        ListClass list = document.toObject(ListClass.class);
                        lists.add(list);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());

                }
            }
        });
        return lists;
    }

    public void createList(String ownerMail, String listID) {
        Map<String, String> newList = new HashMap<>();
        newList.put("name", listID);
        DocumentReference newListRef = fStore.collection("users").document(ownerMail).collection("lists").document(listID);
        newListRef.set(newList).addOnSuccessListener(new OnSuccessListener<Void>() {
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
