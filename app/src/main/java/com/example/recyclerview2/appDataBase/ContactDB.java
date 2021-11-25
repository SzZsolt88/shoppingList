package com.example.recyclerview2.appDataBase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactDB {
    private FirebaseFirestore fStore;
    private UserClass currentUser;
    private List<ContactClass> contactList;
    private MutableLiveData<List<ContactClass>> contactListLiveData;
    private MutableLiveData<List<ContactClass>> confirmedContactsLiveData;
    private MutableLiveData<String> errorMessage;

    private static final String COLLECTION_OF_USERS = "users";
    private static final String COLLECTION_OF_CONTACTS = "contactList";

    private static final String EMAIL = "contactEmail";
    private static final String FULL_NAME = "contactFullName";
    private static final String USER_NAME = "contactUserName";
    private static final String CONTACT_STATUS = "contactStatus";

    private static final String CONTACT_CONFIRMED = "0";
    private static final String CONTACT_NOT_CONFIRMED = "1";
    private static final String CONTACT_NEED_CONFIRM = "2";

    public ContactDB(UserClass currentUser) {
        fStore = FirebaseFirestore.getInstance();
        contactList = new ArrayList<>();
        contactListLiveData = new MutableLiveData<>();
        confirmedContactsLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        this.currentUser = currentUser;
    }

    public void getAllContactsOfUser() {
        CollectionReference contactsRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS);
        contactsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ContactClass getContact =
                                new ContactClass(document.get(EMAIL).toString(),
                                        document.get(FULL_NAME).toString(),
                                        document.get(USER_NAME).toString(),
                                        document.get(CONTACT_STATUS).toString());
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
                            ContactClass getContact = new ContactClass(documentSnapshot.getId(),documentSnapshot.get("fullName").toString(), documentSnapshot.get("userName").toString(), CONTACT_NOT_CONFIRMED);
                            addNewContact(getContact);
                        }
                        else {
                            errorMessage.postValue("Nincs ilyen e-mail címmel regisztrált tagunk!");
                        }
                    }
                });
    }

    public void addNewContact(ContactClass newContact) {
        if(!isInContact(newContact)) {
            DocumentReference contactRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS).document(newContact.getContactEmail());
            contactRef.set(newContact).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        contactList.add(newContact);
                        contactListLiveData.postValue(contactList);
                    }
                }
            });
            DocumentReference otherUserContactList = fStore.collection(COLLECTION_OF_USERS).document(newContact.getContactEmail()).collection(COLLECTION_OF_CONTACTS).document(currentUser.getuMail());
            ContactClass mySelf = new ContactClass(currentUser.getuMail(),currentUser.getFullName(),currentUser.getuName(),CONTACT_NEED_CONFIRM);
            otherUserContactList.set(mySelf).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
        } else errorMessage.postValue("Már ismerősnek jelölted!");
    }

    private boolean isInContact(ContactClass contactClass) {
        for (ContactClass contacts : contactList) {
            if (contacts.getContactEmail().equals(contactClass.getContactEmail())) {
                return true;
            }
        }
        return false;
    }

    public void updateContact(ContactClass updateContact, int position) {
        DocumentReference contactRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS).document(updateContact.getContactEmail());
        contactRef.update(CONTACT_STATUS, CONTACT_CONFIRMED).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                contactList.get(position).setContactStatus(CONTACT_CONFIRMED);
                contactListLiveData.postValue(contactList);
            }
        });
        DocumentReference otherUserContactList = fStore.collection(COLLECTION_OF_USERS).document(updateContact.getContactEmail()).collection(COLLECTION_OF_CONTACTS).document(currentUser.getuMail());
        otherUserContactList.update(CONTACT_STATUS, CONTACT_CONFIRMED);
    }

    public void deleteContact(ContactClass updateContact, int position) {
        DocumentReference contactRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS).document(updateContact.getContactEmail());
        contactRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                contactList.remove(position);
                contactListLiveData.postValue(contactList);
            }
        });
        DocumentReference otherUserContactList = fStore.collection(COLLECTION_OF_USERS).document(updateContact.getContactEmail()).collection(COLLECTION_OF_CONTACTS).document(currentUser.getuMail());
        otherUserContactList.delete();
    }

    public MutableLiveData<List<ContactClass>> getContactListLiveData() { return contactListLiveData; }

    public void getAllConfirmedUser() {
        CollectionReference contactsRef = fStore.collection(COLLECTION_OF_USERS).document(currentUser.getuMail()).collection(COLLECTION_OF_CONTACTS);
        contactsRef.whereEqualTo(CONTACT_STATUS, CONTACT_CONFIRMED).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    List<ContactClass> contactClasses = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        ContactClass confirmedContact = new ContactClass(
                                queryDocumentSnapshot.get(EMAIL).toString(),
                                queryDocumentSnapshot.get(FULL_NAME).toString(),
                                queryDocumentSnapshot.get(USER_NAME).toString());
                        contactClasses.add(confirmedContact);
                    }
                    confirmedContactsLiveData.postValue(contactClasses);
                }
            }
        });
    }

    public MutableLiveData<List<ContactClass>> getConfirmedContactsLiveData() { return confirmedContactsLiveData; }

    public MutableLiveData<String> getErrorMessage() { return errorMessage; }
}
