package com.example.recyclerview2.lists;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ContactDB;
import com.example.recyclerview2.appDataBase.ListClass;
import com.example.recyclerview2.appDataBase.UserClass;

public class ListShareFragment extends DialogFragment {

    private UserClass currentUser;
    private ListClass shareList;
    private int shareListPosition;
    private TextView listName;
    private TextView contactListTitle;
    private ContactDB contactDB;


    public ListShareFragment(ListClass shareList, int shareListPosition, UserClass currentUser) {
        this.shareList = shareList;
        this.shareListPosition = shareListPosition;
        this.currentUser = currentUser;
        contactDB = new ContactDB(currentUser);
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_share, container, false);
        Log.d("TAG", "onCreateView: " + shareList.getName());

        listName = v.findViewById(R.id.shareListNameView);
        contactListTitle = v.findViewById(R.id.shareListContactListTitle);

        listName.setTextColor(R.color.yellow);
        listName.setText(shareList.getName() + " lista megosztása:");

        contactListTitle.setText("Ismerősők:");

        contactDB.getAllConfirmedUser();

        return v;
    }
}
