package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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
import com.google.firebase.firestore.auth.User;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDB {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private List<ListClass> shoppingLists;
    private MutableLiveData<List<ListClass>> listMutableLiveData;

    public ListDB() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        shoppingLists = new ArrayList<>();
        listMutableLiveData = new MutableLiveData<>();
    }

    public void getListOfsUser(String ownerEmail) {
        CollectionReference listsRef = fStore.collection("users").document(ownerEmail).collection("lists");
        listsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ListClass listOfUser = new ListClass(document.getString("name"), document.getString("listID"), document.getString("owner"));
                        shoppingLists.add(listOfUser);
                    }
                    Collections.sort(shoppingLists);
                    listMutableLiveData.postValue(shoppingLists);
                }
            }
        });
    }

    public MutableLiveData<List<ListClass>> getListMutableLiveData() { return listMutableLiveData; }

    private DocumentReference getDocRef(ListClass list) {
        String ownerMail = list.getOwner();
        String listID = list.getListID();
        return fStore.collection("users").document(ownerMail).collection("lists").document(listID);
    }

    public void createList(ListClass newList) {
        String ownerMail = newList.getOwner();
        DocumentReference newListRef = fStore.collection("users").document(ownerMail).collection("lists").document();
        newListRef.set(newList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                newListRef.update("listID", newListRef.getId());
                newList.setListID(newListRef.getId());
                shoppingLists.add(0, newList);
                Collections.sort(shoppingLists);
                listMutableLiveData.postValue(shoppingLists);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error writing document", e);
            }
        });
    }

    public void deleteList(ListClass deleteList) {
        DocumentReference deleteListRef = getDocRef(deleteList);
        deleteListRef.delete();
        shoppingLists.remove(deleteList);
        listMutableLiveData.postValue(shoppingLists);
    }

    public void updateList(ListClass updateList, int position, String newName) {
        DocumentReference updateListRef = getDocRef(updateList);
        updateListRef.update("name", newName);
        shoppingLists.get(position).setName(newName);
        Collections.sort(shoppingLists);
        listMutableLiveData.postValue(shoppingLists);
    }

    public void singOut() { fAuth.signOut(); }

}
