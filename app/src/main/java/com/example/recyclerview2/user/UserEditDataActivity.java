package com.example.recyclerview2.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.recyclerview2.Lists.ListActivity;
import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.User;
import com.example.recyclerview2.appDataBase.UserEditData;
import com.example.recyclerview2.appDataBase.UserEditDataInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserEditDataActivity extends AppCompatActivity implements UserEditDataInterface {
    private UserEditData userEditData;
    private TextInputLayout realName;
    private TextInputLayout userName;
    private TextInputLayout userMail;
    private TextInputLayout userActualPW;
    private TextInputLayout userNewPW;
    private TextInputLayout userNewPWConfirm;

    private String uMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_date);

        realName = findViewById(R.id.realName);
        userName = findViewById(R.id.userName);
        userMail = findViewById(R.id.userMail);
        userActualPW = findViewById(R.id.userPasswordActual);
        userNewPW = findViewById(R.id.userNewPassword);
        userNewPWConfirm = findViewById(R.id.userNewPasswordConfirm);
        Button modifyPWButton = findViewById(R.id.modifyPWButton);
        Button deleteUserButton = findViewById(R.id.deleteUser);

        userEditData = new UserEditData(this);

        userEditData.getChangePWSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Snackbar.make(deleteUserButton, "A felhasználó jelszava megváltozott!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(deleteUserButton, "Hiba történt, próbáld újra!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        userEditData.getDeleteSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Snackbar.make(deleteUserButton, "A felhasználót töröltük!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(deleteUserButton, "Hiba történt, próbáld újra!", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        userEditData.getUserData();

        modifyPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePassword() && validatePasswordConfirm()) {
                    String uActualPW = userActualPW.getEditText().getText().toString();
                    String uNewPW = userNewPW.getEditText().getText().toString();
                    userEditData.changePW(uMail, uActualPW, uNewPW);
                }
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {

            EditText uActualPW = new EditText(UserEditDataActivity.this);
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder userDeleteDialog = new AlertDialog.Builder(UserEditDataActivity.this);
                userDeleteDialog.setTitle("Felhasználó Törlés");
                userDeleteDialog.setMessage("Add meg jelszavad, amennyiben tényleg törölni szeretnéd a fiókodat.");
                userDeleteDialog.setView(uActualPW);
                userDeleteDialog.setCancelable(false);

                userDeleteDialog.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String actualPW = uActualPW.getText().toString().trim();
                        if (actualPW.length() > 0) {
                            userEditData.deleteUser(uMail, actualPW);
                        }
                        else {
                            Toast.makeText(UserEditDataActivity.this, "Nem adtad meg a jelszavad!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("Mégse", null);
                userDeleteDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Felhasználói adatok");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancelActivity) {
            finish();
        }
        return true;
    }

    @Override
    public void getUser(User currentUser) {
        realName.getEditText().setText(currentUser.getFullName());
        userName.getEditText().setText(currentUser.getuName());
        userMail.getEditText().setText(currentUser.getuMail());
        uMail = userMail.getEditText().getText().toString();
    }

    private boolean validatePassword() {
        String inputPassword = userNewPW.getEditText().getText().toString().trim();
        if(inputPassword.isEmpty()) {
            userNewPW.setError("Kötelező mező!");
            return false;
        } else if (inputPassword.length() < 6) {
            userNewPW.setError("A jelszó legalább 6 karakter hosszú kell legyen!");
            return false;
        } else {
            userNewPW.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String inputPasswordConf = userNewPWConfirm.getEditText().getText().toString().trim();
        if(inputPasswordConf.isEmpty()) {
            userNewPWConfirm.setError("Kötelező mező!");
            return false;
        } else if (!inputPasswordConf.equals(userNewPW.getEditText().getText().toString().trim())) {
            userNewPWConfirm.setError("A megadott jelszavak nem egyeznek meg!");
            return false;
        } else {
            userNewPWConfirm.setError(null);
            return true;
        }
    }
}
