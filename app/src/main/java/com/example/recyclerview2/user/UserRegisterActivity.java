package com.example.recyclerview2.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.UserRegister;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

public class UserRegisterActivity extends AppCompatActivity {
    private TextInputLayout realName;
    private TextInputLayout userName;
    private TextInputLayout userMail;
    private TextInputLayout userMailConfirm;
    private TextInputLayout userPassword;
    private TextInputLayout userPasswordConfirm;
    private Button registerButton;
    private UserRegister userRegister;
    private ProgressDialog regDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        realName = findViewById(R.id.realName);
        userName = findViewById(R.id.userName);
        userMail = findViewById(R.id.userMail);
        userMailConfirm = findViewById(R.id.userMailConfirm);
        userPassword = findViewById(R.id.userPassword);
        userPasswordConfirm = findViewById(R.id.userPasswordConfirm);
        registerButton = findViewById(R.id.sendRegisterButton);
        userRegister = new UserRegister();

        List<String> fruitAndVegetables = Arrays.asList(getResources().getStringArray(R.array.fruitandvegetables));
        List<String> bakery = Arrays.asList(getResources().getStringArray(R.array.bakery));
        List<String> beverage = Arrays.asList(getResources().getStringArray(R.array.beverage));
        List<String> dairy = Arrays.asList(getResources().getStringArray(R.array.dairy));
        List<String> meat = Arrays.asList(getResources().getStringArray(R.array.meat));

        userRegister.getIsRegSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean regSuccessful) {
                regDialog.dismiss();
                if(regSuccessful) {
                    Snackbar.make(registerButton, "Felhasználó sikeresen regisztrálva!", Snackbar.LENGTH_LONG).show();
                    userRegister.createProductCategory(fruitAndVegetables, bakery, beverage, dairy, meat);
                } else {
                    Snackbar.make(registerButton, "Hiba történt, próbálja újra!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRegDialog();
                regDialog.show();
                String email = userMail.getEditText().getText().toString().trim();
                String password = userPassword.getEditText().getText().toString().trim();
                String fName = realName.getEditText().getText().toString().trim();
                String uName = userName.getEditText().getText().toString().trim();

                if(!validateName() | !validateUserName() | !validateMail() | !validateMailConfirmation() |!validatePassword() | !validatePasswordConfirm()) {
                    regDialog.dismiss();
                }
                else {
                    userRegister.regUser(fName,email,uName,password);

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Regisztráció");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancelActivity) {
            finish();
        }
        return true;
    }

    private void createRegDialog() {
        regDialog = new ProgressDialog(UserRegisterActivity.this);
        regDialog.setCancelable(false);
        regDialog.setCanceledOnTouchOutside(false);
        regDialog.setMessage("Regisztráció folyamatban!");
        regDialog.show();
    }


    private boolean validateName(){
        String inputRealName = realName.getEditText().getText().toString().trim();
        if (inputRealName.isEmpty()) {
            realName.setError("Kötelező mező!");
            return false;
        } else {
            realName.setError(null);
            return true;
        }
    }

    private boolean validateUserName(){
        String inputUserName = userName.getEditText().getText().toString().trim();
        if (inputUserName.isEmpty()) {
            userName.setError("Kötelező mező!");
            return false;
        } else {
            userName.setError(null);
            return true;
        }
    }

    private boolean validateMail() {
        String inputMail = userMail.getEditText().getText().toString().trim();
        if(inputMail.isEmpty()) {
            userMail.setError("Kötelező mező!");
            return false;
        } else if (!inputMail.contains("@")) {
            userMail.setError("Érvényes e-mail-címet adjon meg!");
            return false;
        }
        else {
            userMail.setError(null);
            return true;
        }
    }

    private boolean validateMailConfirmation() {
        String inputMailConf = userMailConfirm.getEditText().getText().toString().trim();
        if(inputMailConf.isEmpty()) {
            userMailConfirm.setError("Kötelező mező!");
            return false;
        } else if (!inputMailConf.equals(userMail.getEditText().getText().toString().trim())) {
            userMailConfirm.setError("A megadott e-mail-címek nem egyeznek meg!");
            return false;
        } else {
            userMailConfirm.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String inputPassword = userPassword.getEditText().getText().toString().trim();
        if(inputPassword.isEmpty()) {
            userPassword.setError("Kötelező mező!");
            return false;
        } else if (inputPassword.length() < 6) {
            userPassword.setError("A jelszó legalább 6 karakter hosszú kell legyen!");
            return false;
        } else {
            userPassword.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String inputPasswordConf = userPasswordConfirm.getEditText().getText().toString().trim();
        if(inputPasswordConf.isEmpty()) {
            userPasswordConfirm.setError("Kötelező mező!");
            return false;
        } else if (!inputPasswordConf.equals(userPassword.getEditText().getText().toString().trim())) {
            userPasswordConfirm.setError("A megadott jelszavak nem egyeznek meg!");
            return false;
        } else {
            userPasswordConfirm.setError(null);
            return true;
        }
    }
}
