package com.example.recyclerview2.user ;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.recyclerview2.lists.ListActivity;
import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.UserClass;
import com.example.recyclerview2.appDataBase.UserLoginInterface;
import com.example.recyclerview2.appDataBase.UserLogin;

import com.google.android.material.snackbar.Snackbar;

public class UserLoginActivity extends AppCompatActivity implements UserLoginInterface {
    private Button loginButton;
    private Button registerButton;
    private EditText userName;
    private EditText userPassword;
    private TextView forgetNP;
    private ProgressDialog loginDialog;

    private UserLogin userLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteDatabase("shoppingList_Database");

        setContentView(R.layout.user_login);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        forgetNP = findViewById(R.id.forgetPasswordUsername);

        createAuthDialog();

        userLogin = new UserLogin(this);

        userLogin.getIsPWResetSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Snackbar.make(forgetNP, "Helyreállító emailt kiküldtük!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(forgetNP, "Hiba történt, próbáld újra!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userLogin.getCurrentUser() != null ) {
            userLogin.startListActivity();
        } else {
            loginDialog.dismiss();
            findViewById(R.id.loginImageContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.loginContainer).setVisibility(View.VISIBLE);
        }
        createLoginActivity();
    }

    private void createAuthDialog() {
        loginDialog = new ProgressDialog(UserLoginActivity.this);
        loginDialog.setCancelable(false);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.setMessage("Azonosítás folyamatban!");
        loginDialog.show();
    }

    private void createLoginActivity() {
        loginButton.setOnClickListener(v -> {
            String email = userName.getText().toString().trim();
            String password = userPassword.getText().toString().trim();
            if (email.length() > 0 && password.length() > 0) {
                createAuthDialog();
                userLogin.authentication(email,password);
            } else {
                Snackbar.make(forgetNP, "Add meg az email címed és jelszavad!", Snackbar.LENGTH_LONG).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent register = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
            startActivity(register);
        });

        forgetNP.setOnClickListener(v -> {
            EditText resetMail = new EditText(UserLoginActivity.this);

            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(UserLoginActivity.this);
            passwordResetDialog.setTitle("Elfelejtetted a jelszavad?");
            passwordResetDialog.setMessage("Add meg az Email címed, amire elküldhetjük a jelszó helyreállító linket!");
            passwordResetDialog.setView(resetMail);
            passwordResetDialog.setCancelable(false);

            passwordResetDialog.setPositiveButton("Küldés", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mail = resetMail.getText().toString().trim();
                    if (mail.length() > 0) {
                        userLogin.pwReset(mail);
                    }
                    else {
                        Toast.makeText(UserLoginActivity.this, "Nem adtad meg az email címedet!", Toast.LENGTH_LONG).show();
                    }
                }
            }).setNegativeButton("Mégse", null);
            passwordResetDialog.show();

        });
    }

    @Override
    public void createListActivity(UserClass currentUser) {
        if (currentUser != null) {
            Intent login = new Intent(UserLoginActivity.this, ListActivity.class);
            login.putExtra("currentUser", currentUser);
            loginDialog.dismiss();
            startActivity(login);
        }
        else {
            Snackbar.make(forgetNP, "Rossz felhasználónév vagy email cím!", Snackbar.LENGTH_LONG).show();
            loginDialog.dismiss();
        }
    }
}
