package com.example.recyclerview2.contacts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements OnContactItemCL {
    private EditText contactMailAddress;
    private Button addContact;
    private RecyclerView contactListView;
    private List<ContactsClass> contactList = new ArrayList<>();
    private ContactsAdapter contactsAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        contactMailAddress = findViewById(R.id.contactMailAddressET);
        addContact = findViewById(R.id.addContactBtn);
        contactListView = findViewById(R.id.contactsLists);

        contactListView.setHasFixedSize(true);
        contactListView.setLayoutManager(new LinearLayoutManager(this));
        contactListView.setItemAnimator(new DefaultItemAnimator());


        ContactsClass contact = new ContactsClass("1");
        contact.setFullName("Szandai Zsolt");
        contact.setUserName("Zsolt");
        contact.setContactStatus("Megerősített");
        contactList.add(contact);

        ContactsClass contact2 = new ContactsClass("1");
        contact2.setFullName("Pellek Fanni");
        contact2.setUserName("Fanni");
        contact2.setContactStatus("Megerősítettlen");
        contactList.add(contact2);

        ContactsClass contact3 = new ContactsClass("1");
        contact3.setFullName("Minta Pista");
        contact3.setUserName("Pityu");
        contact3.setContactStatus("Megerősítendő");
        contactList.add(contact3);
        contactsAdapter = new ContactsAdapter(contactList, this);
        contactsAdapter.setContacts(contactList);
        contactListView.setAdapter(contactsAdapter);


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
    public void OnContactClick(ContactsClass contactsClass) {
        final AlertDialog.Builder editContactDialog = new AlertDialog.Builder(ContactsActivity.this);
        if (contactsClass.getContactStatus().contains("Megerősítendő")) {
            editContactDialog.setTitle("Felkérés visszaigozolása");
            editContactDialog.setMessage(contactsClass.getUserName() + " (" + contactsClass.getFullName() + ")" +
                    " fel szeretné venni veled a kapcsolatot, elfogadod a felkérést?");
            editContactDialog.setCancelable(false);
            editContactDialog.setPositiveButton("Megerősítés", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            editContactDialog.setNegativeButton("Elutasítás", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else if (contactsClass.getContactStatus().trim().equals("Megerősített")) {
            editContactDialog.setTitle("Ismerős törlése");
            editContactDialog.setMessage(contactsClass.getUserName() + " (" + contactsClass.getFullName() + ")" +
                    " szeretnéd törölni az ismerőseid közül?");
            editContactDialog.setCancelable(false);
            editContactDialog.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            editContactDialog.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        else if (contactsClass.getContactStatus().trim().equals("Megerősítettlen")) {
            editContactDialog.setTitle("Felkérés visszavonása");
            editContactDialog.setMessage(contactsClass.getUserName() + " (" + contactsClass.getFullName() + ")" +
                    " még nem igazolta vissza a felkérést, szeretnéd visszavonni?");
            editContactDialog.setCancelable(false);
            editContactDialog.setPositiveButton("Visszavonás", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            editContactDialog.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        editContactDialog.show();
    }
}
