package com.example.recyclerview2.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.UserClass;
import com.example.recyclerview2.appDataBase.UserEditData;
import com.example.recyclerview2.appDataBase.UserEditDataInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class UserEditDataActivity extends AppCompatActivity implements UserEditDataInterface {
    private UserEditData userEditData;
    private TextInputLayout realName;
    private TextInputLayout userName;
    private TextInputLayout userMail;
    private TextInputLayout userActualPW;
    private TextInputLayout userNewPW;
    private TextInputLayout userNewPWConfirm;
    private ProgressDialog UEDDialog;

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
                UEDDialog.dismiss();
                if (isSuccess) {
                    Snackbar.make(deleteUserButton, "A felhaszn??l?? jelszava megv??ltozott!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(deleteUserButton, "Hiba t??rt??nt, pr??b??ld ??jra!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        userEditData.getDeleteSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                UEDDialog.dismiss();
                if (isSuccess) {
                    Snackbar.make(deleteUserButton, "A felhaszn??l??t t??r??lt??k!", Snackbar.LENGTH_LONG).show();
                    userEditData.signOut();
                } else {
                    Snackbar.make(deleteUserButton, "Hiba t??rt??nt, pr??b??ld ??jra!", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        userEditData.getUserData();

        modifyPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePassword() | !validatePasswordConfirm() | !actualPassword()) {

                } else {
                    createDialog("Jelsz?? v??ltoztat??s folyamatban!");
                    String uActualPW = userActualPW.getEditText().getText().toString();
                    String uNewPW = userNewPW.getEditText().getText().toString();
                    userEditData.changePW(uMail, uActualPW, uNewPW);
                }
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText uActualPW = new EditText(UserEditDataActivity.this);
                uActualPW.setTransformationMethod(PasswordTransformationMethod.getInstance());

                final AlertDialog.Builder userDeleteDialog = new AlertDialog.Builder(UserEditDataActivity.this);

                userDeleteDialog.setTitle("Felhaszn??l?? T??rl??s");
                userDeleteDialog.setMessage("Add meg jelszavad, amennyiben t??nyleg t??r??lni szeretn??d a fi??kodat.");
                userDeleteDialog.setView(uActualPW);
                userDeleteDialog.setCancelable(false);

                userDeleteDialog.setPositiveButton("T??rl??s", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String actualPW = uActualPW.getText().toString().trim();
                        if (actualPW.length() > 0) {
                            createDialog("Felhaszn??l?? t??rl??se folyamatban!");
                            userEditData.deleteUser(uMail, actualPW);
                        }
                        else {
                            Toast.makeText(UserEditDataActivity.this, "Nem adtad meg a jelszavad!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("M??gse", null);
                userDeleteDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Felhaszn??l??i adatok");
        return super.onCreateOptionsMenu(menu);
    }

    private void createDialog(String text) {
        UEDDialog = new ProgressDialog(UserEditDataActivity.this);
        UEDDialog.setCancelable(false);
        UEDDialog.setCanceledOnTouchOutside(false);
        UEDDialog.setMessage(text);
        UEDDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancelActivity) {
            finish();
        }
        return true;
    }

    @Override
    public void getUser(UserClass currentUser) {
        realName.getEditText().setText(currentUser.getFullName());
        userName.getEditText().setText(currentUser.getuName());
        userMail.getEditText().setText(currentUser.getuMail());
        uMail = userMail.getEditText().getText().toString();
    }

    private boolean actualPassword() {
        String inputPassword = userActualPW.getEditText().getText().toString().trim();
        if(inputPassword.isEmpty()) {
            userActualPW.setError("K??telez?? mez??!");
            return false;
        }else {
            userActualPW.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String inputPassword = userNewPW.getEditText().getText().toString().trim();
        if(inputPassword.isEmpty()) {
            userNewPW.setError("K??telez?? mez??!");
            return false;
        } else if (inputPassword.length() < 6) {
            userNewPW.setError("A jelsz?? legal??bb 6 karakter hossz?? kell legyen!");
            return false;
        } else {
            userNewPW.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String inputPasswordConf = userNewPWConfirm.getEditText().getText().toString().trim();
        if(inputPasswordConf.isEmpty()) {
            userNewPWConfirm.setError("K??telez?? mez??!");
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
