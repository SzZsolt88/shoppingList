package com.example.recyclerview2.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyclerview2.R;
import com.google.android.material.textfield.TextInputLayout;

public class EditUserDataActivity extends AppCompatActivity {
    private TextInputLayout realName;
    private TextInputLayout userName;
    private TextInputLayout userMail;
    private TextInputLayout userActualPW;
    private TextInputLayout userNewPW;
    private TextInputLayout userNewPWConfirm;
    private Button modifyPWButton;
    private Button deleteUserButton;

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
        modifyPWButton = findViewById(R.id.modifyPWButton);
        deleteUserButton = findViewById(R.id.deleteUser);


        realName.getEditText().setText("Józska Miska2");
        userName.getEditText().setText("vasarloMiska");
        userMail.getEditText().setText("miska77@freemail.hu");
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
}
