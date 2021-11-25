package com.example.recyclerview2.appDataBase;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDB {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private List<ListClass> shoppingLists;
    private MutableLiveData<List<ListClass>> listMutableLiveData;
    private String ownerMail;

    private static final String LIST_NAME = "listName";
    private static final String LIST_ID = "listID";
    private static final String OWNER = "owner";
    private static final String OWNER_NAME = "ownerName";
    private static final String LIST_IS_SHARED = "shared";
    private static final String LIST_SHARED_WITH = "sharedWith";

    private static final String USERS = "users";
    private static final String LISTS = "lists";

    private static final String PRODUCTS = "products";
    private static final String PRODUCTS_OF_LIST = "productsOfList";

    //construktor
    public ListDB(String ownerMail) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        shoppingLists = new ArrayList<>();
        listMutableLiveData = new MutableLiveData<>();
        this.ownerMail = ownerMail;
    }

    //A felhasználó összes listájának lekérése
    public void getListOfsUser() {
        CollectionReference listsRef = fStore.collection(USERS).document(ownerMail).collection(LISTS);
        listsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot listOfUser : task.getResult().getDocuments()) {
                        List<ContactClass> sharedWith = new ArrayList<>();
                        if (listOfUser.getBoolean(LIST_IS_SHARED)) {
                            List<Map<String, String>> sharedWithUsers = (List<Map<String, String>>) listOfUser.get(LIST_SHARED_WITH);
                            for (Map<String, String> sharedWithUser : sharedWithUsers) {
                                sharedWith.add(new ContactClass(sharedWithUser.get("contactEmail"), sharedWithUser.get("contactFullName"), sharedWithUser.get("contactUserName")));
                            }
                        }
                        ListClass listInstance = new ListClass(
                                String.valueOf(listOfUser.get(LIST_NAME)),
                                String.valueOf(listOfUser.get(LIST_ID)),
                                String.valueOf(listOfUser.get(OWNER)),
                                String.valueOf(listOfUser.get(OWNER_NAME)),
                                listOfUser.getBoolean(LIST_IS_SHARED),
                                sharedWith);
                        shoppingLists.add(listInstance);
                    }
                    listMutableLiveData.postValue(shoppingLists);
                }
            }
        });
        DocumentReference sharedListsRef = fStore.collection(USERS).document(ownerMail);
        sharedListsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().get("externalLists") != null) {
                        List<Map<String, String>> externalLists = (List<Map<String, String>>) task.getResult().get("externalLists");
                        for (Map<String, String> externalList : externalLists) {
                            String listID = externalList.get("ListID");
                            String ownerMail = externalList.get("OwnerMail");
                            DocumentReference sharedListRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID);
                            sharedListRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        List<Map<String, String>> sharedWithUsers = (List<Map<String, String>>) task.getResult().get(LIST_SHARED_WITH);
                                        List<ContactClass> sharedWith = new ArrayList<>();
                                        for (Map<String, String> sharedWithUser : sharedWithUsers) {
                                            sharedWith.add(new ContactClass(sharedWithUser.get("contactEmail"), sharedWithUser.get("contactFullName"), sharedWithUser.get("contactUserName")));
                                        }
                                        ListClass listInstance = new ListClass(
                                                String.valueOf(task.getResult().get(LIST_NAME)),
                                                String.valueOf(task.getResult().get(LIST_ID)),
                                                String.valueOf(task.getResult().get(OWNER)),
                                                String.valueOf(task.getResult().get(OWNER_NAME)),
                                                task.getResult().getBoolean(LIST_IS_SHARED),
                                                sharedWith);
                                        shoppingLists.add(listInstance);
                                        Collections.sort(shoppingLists);
                                    }
                                    listMutableLiveData.postValue(shoppingLists);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    //LiveData a felhasználólista frissítésének követésére
    public MutableLiveData<List<ListClass>> getListMutableLiveData() { return listMutableLiveData; }

    //Új lista létrehozása az adatbázisban
    @SuppressLint("SimpleDateFormat")
    public void createList(ListClass newList) {
        String  listID = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        DocumentReference newListRef = fStore.collection("users").document(ownerMail).collection(LISTS ).document(listID);
        newList.setListID(listID);
        newListRef.set(newList);
        postChanges(newList);
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        Map<String, Object> products = new HashMap<>();
        listRef.set(products);
    }

    //Lista törlése az adatbázisból
    public void deleteList(ListClass deleteList) {
        String listID = deleteList.getListID();
        DocumentReference deleteListRef = fStore.collection(USERS).document(deleteList.getOwner()).collection(LISTS).document(listID);
        DocumentReference deleteProdRef = fStore.collection(USERS).document(deleteList.getOwner()).collection(LISTS).document(listID).collection(PRODUCTS).document(PRODUCTS_OF_LIST);
        if (fAuth.getCurrentUser().getEmail().equals(deleteList.getOwner())) {
            if (deleteList.isShared()) {
                for (ContactClass deleteShares : deleteList.getSharedWith()) {
                    deleteSharing(listID,deleteShares.getContactEmail());
                }
            }
            deleteProdRef.delete();
            deleteListRef.delete();
        } else {
            List<ContactClass> sharedWithList = deleteList.getSharedWith();
            for (ContactClass sharedWith : sharedWithList) {
                if (fAuth.getCurrentUser().getEmail().equals(sharedWith.getContactEmail())) {
                    sharedWithList.remove(sharedWith);
                    break;
                }
            }

            if (sharedWithList.size() == 0) {
                deleteListRef.update(LIST_IS_SHARED, false);
                deleteListRef.update(LIST_SHARED_WITH, null);
            } else {
                deleteListRef.update(LIST_SHARED_WITH, sharedWithList);
            }
            DocumentReference externalListsRef = fStore.collection(USERS).document(fAuth.getCurrentUser().getEmail());
            Map<String, String> deleteSharedList = new HashMap<>();
            deleteSharedList.put("ListID", deleteList.getListID());
            deleteSharedList.put("OwnerMail", deleteList.getOwner());
            externalListsRef.update("externalLists", FieldValue.arrayRemove(deleteSharedList));
        }
        shoppingLists.remove(deleteList);
        listMutableLiveData.postValue(shoppingLists);
    }

    //Lista adatainak frissítése (átnevezés)
    public void updateList(ListClass updateList, String newName) {
        if (fAuth.getCurrentUser().getEmail().equals(updateList.getOwner())) {
            DocumentReference updateListRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(updateList.getListID());
            shoppingLists.remove(updateList);
            updateList.setListName(newName);
            updateListRef.update("listName", newName);
            postChanges(updateList);
        }
    }

    //Lista megosztása
    public void shareList(ListClass shareList, List<ContactClass> sharedWith) {
        DocumentReference shareListRef = fStore.collection(USERS).document(shareList.getOwner()).collection(LISTS).document(shareList.getListID());
        shoppingLists.remove(shareList);
        for (ContactClass shared : shareList.getSharedWith()) {
            if (!sharedWith.contains(shared)) {
                deleteSharing(shareList.getListID(),shared.getContactEmail());
            }
        }
        if (sharedWith.size() == 0) {
            shareList.setShared(false);
            shareList.setSharedWith(sharedWith);
            shareListRef.update(LIST_IS_SHARED, false);
            shareListRef.update(LIST_SHARED_WITH, null);
        } else {
            shareList.setShared(true);
            shareList.setSharedWith(sharedWith);
            shareListRef.update(LIST_IS_SHARED, true);
            shareListRef.update(LIST_SHARED_WITH, sharedWith);
            for (ContactClass shareWith : sharedWith) {
                postSharedList(shareList.getListID(),shareWith.getContactEmail());
            }
        }
        postChanges(shareList);
    }

    //Lista megjelnítése a másik félnél:
    private void postSharedList(String ListID, String contactEmail) {
        DocumentReference listOfListsRef = fStore.collection(USERS).document(contactEmail);
        Map<String, String> listDate = new HashMap<>();
        listDate.put("ListID", ListID);
        listDate.put("OwnerMail", ownerMail);
        listOfListsRef.update("externalLists", FieldValue.arrayUnion(listDate));
    }

    //Lista megosztásának visszavonása:
    private void deleteSharing(String listID, String contactEmail) {
        DocumentReference listOfListsRef = fStore.collection(USERS).document(contactEmail);
        Map<String, String> listDate = new HashMap<>();
        listDate.put("ListID", listID);
        listDate.put("OwnerMail", ownerMail);
        listOfListsRef.update("externalLists", FieldValue.arrayRemove(listDate));
    }

    //HasMap készítése a share, update és delete funkciók számára
    private Map<String, Object> createHashMap(ListClass listClass) {
        Map<String, Object> hashMapOfList = new HashMap<>();
        hashMapOfList.put("ListID", listClass.getListID());
        hashMapOfList.put("ListName", listClass.getListName());
        hashMapOfList.put("isShared", listClass.isShared());
        if (listClass.isShared()) {
            hashMapOfList.put("sharedWith", listClass.getSharedWith());
            Log.d("TAG", "createHashMap: " + listClass.getSharedWith().get(0).getContactUserName());
        } else hashMapOfList.put("sharedWith", null);
        return hashMapOfList;
    }

    //A változások posztolásának metódusa
    private void postChanges(ListClass listClass) {
        shoppingLists.add(listClass);
        Collections.sort(shoppingLists);
        listMutableLiveData.postValue(shoppingLists);
    }

    //kijelentkezés
    public void singOut() { fAuth.signOut(); }
}
