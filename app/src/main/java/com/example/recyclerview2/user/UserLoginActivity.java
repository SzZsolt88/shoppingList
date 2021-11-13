package com.example.recyclerview2.user ;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.recyclerview2.Lists.ListActivity;
import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.User;
import com.example.recyclerview2.appDataBase.UserLoginInterface;
import com.example.recyclerview2.appDataBase.UserLogin;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;

public class UserLoginActivity extends AppCompatActivity implements UserLoginInterface {
    private Button loginButton;
    private Button registerButton;
    private EditText userName;
    private EditText userPassword;
    private TextView forgetNP;
    private ProgressBar progressBar;

    private UserLogin userLoginDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //deleteDatabase("shoppingList_Database");

        setContentView(R.layout.user_login);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        forgetNP = findViewById(R.id.forgetPasswordUsername);
        progressBar = findViewById(R.id.loginProgressBar);

        userLoginDB = new UserLogin(this);

        userLoginDB.getIsPWResetSuccess().observe(this, new Observer<Boolean>() {
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
        if (userLoginDB.getCurrentUser() != null ) {
            String userID = userLoginDB.getCurrentUser();
            userLoginDB.startListActivity(userID);
        } else {
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.loginImageContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.loginContainer).setVisibility(View.VISIBLE);
        }
        createLoginActivity();
        super.onResume();
    }

    private void createLoginActivity() {
        loginButton.setOnClickListener(v -> {
            String email = userName.getText().toString().trim();
            String password = userPassword.getText().toString().trim();
            if (email.length() > 0 && password.length() > 0) {
                progressBar.setVisibility(View.VISIBLE);
                userLoginDB.authentication(email,password);
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
                        userLoginDB.pwReset(mail);
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
    public void createListActivity(User currentUser) {
        if (currentUser != null) {
            Intent login = new Intent(UserLoginActivity.this, ListActivity.class);
            login.putExtra("currentUser", currentUser);
            startActivity(login);
        }
        else {
            Snackbar.make(forgetNP, "Rossz felhasználónév vagy email cím!", Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }
}
