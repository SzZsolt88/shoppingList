package com.example.recyclerview2.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ContactClass;
import com.example.recyclerview2.appDataBase.ContactDB;
import com.example.recyclerview2.appDataBase.ListClass;
import com.example.recyclerview2.appDataBase.UserClass;

import java.util.ArrayList;
import java.util.List;

public class ListShareFragment extends DialogFragment implements ListShareInterface {

    private ListClass shareList;
    private List<ContactClass> sharedWith;

    private ContactDB contactDB;
    private ListShareContactAdapter listShareContactAdapter;

    private TextView listName;
    private RecyclerView contactsView;
    private Button share;
    private Button cancelShare;


    public ListShareFragment(ListClass shareList, UserClass currentUser) {
        this.shareList = shareList;
        contactDB = new ContactDB(currentUser);
        sharedWith = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_share, container, false);
        getDialog().setCanceledOnTouchOutside(false);

        listName = v.findViewById(R.id.shareListNameView);
        contactsView = v.findViewById(R.id.confirmedContactsList);
        share = v.findViewById(R.id.shareListBtn);
        cancelShare = v.findViewById(R.id.cancelShareListBtn);

        //Cím létrehozása
        listName.setText(shareList.getListName() + " lista megosztása:");

        //visszaigazolt felhasználók listájának lekérdezése.
        contactDB.getAllConfirmedUser();
        contactDB.getConfirmedContactsLiveData().observe(this, new Observer<List<ContactClass>>() {
            @Override
            public void onChanged(List<ContactClass> contactClasses) {
                listShareContactAdapter.setContacts(contactClasses, shareList.getSharedWith());
            }
        });

        if (shareList.getSharedWith() != null) {
            for (ContactClass contact : shareList.getSharedWith()) {
                sharedWith.add(contact);
            }
        }


        //visszaigazolt felhasználók listájának megjelenítése.
        contactsView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        contactsView.setItemAnimator(new DefaultItemAnimator());
        listShareContactAdapter = new ListShareContactAdapter(this);
        contactsView.setAdapter(listShareContactAdapter);

        //gombok beállítása
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity) getActivity()).shareList(shareList,sharedWith);
                getDialog().dismiss();
            }
        });
        cancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }

    @Override
    public void saveSharedStatus(ContactClass contactClass, boolean isChecked) {
        if(isChecked) {
            sharedWith.add(contactClass);
        } else {
            sharedWith.removeIf(contactClass1 -> contactClass1.getContactEmail().equals(contactClass.getContactEmail()));
        }
    }
}
