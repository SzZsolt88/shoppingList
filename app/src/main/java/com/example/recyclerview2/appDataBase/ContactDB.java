package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactDB {
    private FirebaseFirestore fStore;
    private UserClass currentUser;
    private List<ContactClass> contactList;
    private MutableLiveData<List<ContactClass>> contactListLiveData;


    private static final String COLLECTION_OF_USERS = "users";
    private static final String COLLECTION_OF_CONTACTS = "contactList";
    private static final String EMAIL = "contactEmail";
    private static final String FULL_NAME = "contactFullName";
    private static final String USER_NAME = "contactUserName";
    private static final String CONTACT_CONFIRMED = "contactStatus";


    public ContactDB(UserClass currentUser) {
        fStore = FirebaseFirestore.getInstance();
        contactList = new ArrayList<>();
        contactListLiveData = new MutableLiveData<>();
        this.currentUser = currentUser;
    }

    public void getAllContactsOfUser() {
        CollectionReference contactsRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS);
        contactsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ContactClass getContact = new ContactClass(document.get(EMAIL).toString(),document.get(FULL_NAME).toString(),document.get(USER_NAME).toString(),document.get(CONTACT_CONFIRMED).toString());
                        contactList.add(getContact);
                    }
                    contactListLiveData.postValue(contactList);
                }
            }
        });
    }

    public void contactExists(String email) {
        fStore.collection(COLLECTION_OF_USERS).document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            ContactClass getContact = new ContactClass(documentSnapshot.getId(),documentSnapshot.get("fullName").toString(), documentSnapshot.get("userName").toString(), "Megerősítettlen");
                            addNewContact(getContact);
                        }
                        else {Log.d("TAG", "contactExists: false");}
                    }
                });
    }

    public void addNewContact(ContactClass newContact) {
        DocumentReference contactRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS).document(newContact.getContactEmail());
        contactRef.set(newContact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                }
            }
        });
        DocumentReference otherUserContactList = fStore.collection(COLLECTION_OF_USERS).document(newContact.getContactEmail()).collection(COLLECTION_OF_CONTACTS).document(currentUser.getuMail());
        ContactClass mySelf = new ContactClass(currentUser.getuMail(),currentUser.getFullName(),currentUser.getuName(),"Megerősítendő");
        otherUserContactList.set(mySelf).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });
    }

    public void updateContact(ContactClass updateContact) {
        DocumentReference contactRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS).document(updateContact.getContactEmail());
        contactRef.update(CONTACT_CONFIRMED, "Megerősített").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "contactAdded: true");
                contactList.add(updateContact);
                contactListLiveData.postValue(contactList);
            }
        });

        DocumentReference otherUserContactList = fStore.collection(COLLECTION_OF_USERS).document(updateContact.getContactEmail()).collection(COLLECTION_OF_CONTACTS).document(currentUser.getuMail());

    }

    public void deleteContact(ContactClass updateContact) {
        DocumentReference contactRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS).document(updateContact.getContactEmail());

        DocumentReference otherUserContactList = fStore.collection(COLLECTION_OF_USERS).document(updateContact.getContactEmail()).collection(COLLECTION_OF_CONTACTS).document(currentUser.getuMail());

    }


    public MutableLiveData<List<ContactClass>> getContactListLiveData() { return contactListLiveData; }

}
