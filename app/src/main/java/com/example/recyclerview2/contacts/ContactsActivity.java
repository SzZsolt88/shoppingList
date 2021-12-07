package com.example.recyclerview2.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ContactClass;
import com.example.recyclerview2.appDataBase.ContactDB;
import com.example.recyclerview2.appDataBase.UserClass;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactsActivity extends AppCompatActivity implements OnContactItemCL {
    private EditText contactMailAddress;
    private Button addContact;
    private RecyclerView contactListView;
    private ContactsAdapter contactAdapter;
    private ContactDB contactDB;
    private UserClass currentUser;

    private static final String CONTACT_CONFIRMED = "0";
    private static final String CONTACT_NOT_CONFIRMED = "1";
    private static final String CONTACT_NEED_CONFIRM = "2";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        Intent getCurrentUser = getIntent();
        currentUser = getCurrentUser.getParcelableExtra("currentUser");

        contactMailAddress = findViewById(R.id.contactMailAddressET);
        addContact = findViewById(R.id.addContactBtn);
        contactListView = findViewById(R.id.contactsLists);

        contactListView.setHasFixedSize(true);
        contactListView.setLayoutManager(new LinearLayoutManager(this));
        contactListView.setItemAnimator(new DefaultItemAnimator());

        contactDB = new ContactDB(currentUser);
        contactDB.getAllContactsOfUser();

        contactDB.getContactListLiveData().observe(this, new Observer<List<ContactClass>>() {
            @Override
            public void onChanged(List<ContactClass> contactClasses) {
                contactAdapter.setContacts(contactClasses);
            }
        });

        contactDB.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Snackbar.make(contactListView, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });

        contactAdapter = new ContactsAdapter(this);
        contactListView.setAdapter(contactAdapter);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactMail = contactMailAddress.getText().toString();
                if (contactMail.isEmpty()) {
                    Snackbar.make(contactListView, "Nem adtál még meg mail címet", Snackbar.LENGTH_LONG).show();
                }
                else {
                    contactDB.contactExists(contactMailAddress.getText().toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Ismerősök");
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
    public void OnContactClick(ContactClass contactsClass, int position) {
        final AlertDialog.Builder editContactDialog = new AlertDialog.Builder(ContactsActivity.this);
        switch (contactsClass.getContactStatus()) {
            case CONTACT_NEED_CONFIRM:
                editContactDialog.setTitle("Felkérés visszaigozolása");
                editContactDialog.setMessage(contactsClass.getContactUserName() + " (" + contactsClass.getContactFullName() + ")" +
                        " fel szeretné venni veled a kapcsolatot, elfogadod a felkérést?");
                editContactDialog.setCancelable(false);
                editContactDialog.setNeutralButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                editContactDialog.setPositiveButton("Megerősítés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactDB.updateContact(contactsClass, position);
                    }
                });
                editContactDialog.setNegativeButton("Elutasítás", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactDB.deleteContact(contactsClass, position);
                    }
                });
                break;
            case CONTACT_CONFIRMED:
                editContactDialog.setTitle("Ismerős törlése");
                editContactDialog.setMessage(contactsClass.getContactUserName() + " (" + contactsClass.getContactFullName() + ")" +
                        " szeretnéd törölni az ismerőseid közül?");
                editContactDialog.setCancelable(false);
                editContactDialog.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactDB.deleteContact(contactsClass, position);
                    }
                });
                editContactDialog.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case CONTACT_NOT_CONFIRMED:
                editContactDialog.setTitle("Felkérés visszavonása");
                editContactDialog.setMessage(contactsClass.getContactUserName() + " (" + contactsClass.getContactFullName() + ")" +
                        " még nem igazolta vissza a felkérést, szeretnéd visszavonni?");
                editContactDialog.setCancelable(false);
                editContactDialog.setPositiveButton("Visszavonás", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactDB.deleteContact(contactsClass, position);
                    }
                });
                editContactDialog.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
        editContactDialog.show();
    }
}
