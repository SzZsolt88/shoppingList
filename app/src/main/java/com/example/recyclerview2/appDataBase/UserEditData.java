package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserEditData {
    private final FirebaseAuth fAuth;
    private final FirebaseFirestore fStore;
    private final UserClass currentUser;
    private final UserEditDataInterface userEditDataInterface;
    private final MutableLiveData<Boolean> isChangePWSuccess;
    private final MutableLiveData<Boolean> isDeleteSuccess;

    private static final String fullNameField = "fullName";
    private static final String uNameField = "userName";
    private static final String uActiveStatus = "activeStatus";

    public UserEditData(UserEditDataInterface userEditDataInterface) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUser = new UserClass();
        this.userEditDataInterface = userEditDataInterface;
        isChangePWSuccess = new MutableLiveData<>();
        isDeleteSuccess = new MutableLiveData<>();
    }

    public void getUserData() {
        currentUser.setUserID(fAuth.getCurrentUser().getUid());
        currentUser.setuMail(fAuth.getCurrentUser().getEmail());
        fStore.collection("users").document(currentUser.getuMail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currentUser.setFullName(task.getResult().getString(fullNameField));
                    currentUser.setuName(task.getResult().getString(uNameField));
                    userEditDataInterface.getUser(currentUser);
                }
            }
        });
    }

    public void changePW(String uMail, String uActualPW, String uNewPW) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(uMail,uActualPW);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(uNewPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        isChangePWSuccess.postValue(true);
                                        Log.d("TAG", "Password updated");
                                    } else {
                                        isChangePWSuccess.postValue(false);
                                        Log.d("TAG", "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            isChangePWSuccess.postValue(false);
                            Log.d("TAG", "Error auth failed");
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> getChangePWSuccess() {return isChangePWSuccess;}

    public void deleteUser(String uMail, String uActualPW) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(uMail,uActualPW);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            fStore.collection("users").document(uMail).update(uActiveStatus, false);
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        isDeleteSuccess.postValue(true);
                                    } else {
                                        fStore.collection("users").document(uMail).update(uActiveStatus, true);
                                        isDeleteSuccess.postValue(false);
                                    }
                                }
                            });
                        } else {
                            isDeleteSuccess.postValue(false);
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> getDeleteSuccess() { return isDeleteSuccess; }

    public void signOut() { fAuth.signOut();}
}
