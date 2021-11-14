package com.example.recyclerview2.appDataBase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLogin {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private UserLoginInterface userLoginInterface;
    private MutableLiveData<Boolean> isPWResetSuccess;

    public UserLogin(UserLoginInterface userLoginInterface) {
        this.userLoginInterface = userLoginInterface;
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        isPWResetSuccess = new MutableLiveData<>();
    }

    public String getCurrentUser() {
        if (fAuth.getCurrentUser() != null) {
            return fAuth.getCurrentUser().getUid();
        } else return null;
    }

    public void authentication(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startListActivity();
                } else {
                    userLoginInterface.createListActivity(null);
                }
            }
        });
    }

    public void startListActivity() {
        String userMailAddress = fAuth.getCurrentUser().getEmail();
        fStore.collection("users").document(userMailAddress).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    UserClass currentUser = new UserClass();
                    currentUser.setUserID(fAuth.getCurrentUser().getUid());
                    currentUser.setuMail(userMailAddress);
                    currentUser.setFullName(task.getResult().getString("fullName"));
                    currentUser.setuName(task.getResult().getString("userName"));
                    userLoginInterface.createListActivity(currentUser);
                }
            }
        });
    }

    public void pwReset(String mail) {
        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isPWResetSuccess.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isPWResetSuccess.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getIsPWResetSuccess() {return isPWResetSuccess;}

}



