package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegister extends AbstractFireStoreInstance {

    private final FirebaseAuth fAuth;
    private final FirebaseFirestore fStore;
    private final MutableLiveData<Boolean> isRegSuccess;

    public UserRegister() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        isRegSuccess = new MutableLiveData<>();
    }

    public void regUser(String fName, String email, String uName, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserData(fName,uName);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isRegSuccess.postValue(false);
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    private void saveUserData(String fullName, String userName) {
        String userMailAddress = fAuth.getCurrentUser().getEmail();
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userMailAddress);

        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("userName", userName);
        user.put("activeStatus", true);


        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                isRegSuccess.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isRegSuccess.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getIsRegSuccess() {return isRegSuccess;}

    public void createProductCategory(List<String> fruitAndVegetables, List<String> bakery, List<String> beverage, List<String> dairy, List<String> meat){
        List<String> other = new ArrayList<>();
        DocumentReference productCategory = fStore.collection("users").document(fAuth.getCurrentUser().getEmail()).collection("productCategory").document("productCategory");
        productCategory.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Map<String, Object> category = new HashMap<>();
                        category.put(CATEGORY_FRUIT_AND_VEG, fruitAndVegetables);
                        category.put(CATEGORY_BAKERY, bakery);
                        category.put(CATEGORY_DAIRY, dairy);
                        category.put(CATEGORY_BEVERAGE, beverage);
                        category.put(CATEGORY_MEAT, meat);
                        category.put(CATEGORY_OTHER, other);
                        productCategory.set(category);
                    }
                }
            }
        });

    }
}
