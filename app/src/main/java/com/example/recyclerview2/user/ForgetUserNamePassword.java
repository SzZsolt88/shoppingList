package com.example.recyclerview2.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.recyclerview2.OnShoppingListEL;
import com.example.recyclerview2.R;

public class ForgetUserNamePassword extends DialogFragment {

    //widgets
    private AutoCompleteTextView email;
    private Button sendEmail;

    public ForgetUserNamePassword() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forget_user_name_password, container, false);
        email = v.findViewById(R.id.emailForgetUP);
        sendEmail = v.findViewById(R.id.sendEmailForgetUPButton);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}