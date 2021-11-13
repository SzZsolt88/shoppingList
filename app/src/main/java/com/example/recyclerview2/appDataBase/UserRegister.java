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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRegister {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private MutableLiveData<Boolean> isRegSuccess;

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

    private void saveUserData(String fName, String uName) {
        String userMailAddress = fAuth.getCurrentUser().getEmail();
        DocumentReference documentReference = fStore.collection("users").document(userMailAddress);
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fName);
        user.put("userName", uName);
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

}
