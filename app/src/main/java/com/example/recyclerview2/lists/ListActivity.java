package com.example.recyclerview2.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ListClass;
import com.example.recyclerview2.appDataBase.ListDB;
import com.example.recyclerview2.appDataBase.UserClass;
import com.example.recyclerview2.charts.ChartActivity;
import com.example.recyclerview2.contacts.ContactsActivity;
import com.example.recyclerview2.products.ProductActivity;
import com.example.recyclerview2.user.UserEditDataActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements OnListItemCL {
    private EditText listName;
    private Button addList;
    private RecyclerView shoppingListsView;
    private List<ListClass> shoppingList = new ArrayList<>();
    private ListAdapter adapter;
    private ListDB listDB;
    private UserClass currentUser;
    private String ownerMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);
        deleteDatabase("shoppingList_Database");

        Intent getCurrentUser = getIntent();
        currentUser = getCurrentUser.getParcelableExtra("currentUser");
        ownerMail = currentUser.getuMail();

        listDB = new ListDB();
        listDB.getListOfsUser(ownerMail);

        listDB.getListMutableLiveData().observe(this, new Observer<List<ListClass>>() {
            @Override
            public void onChanged(List<ListClass> strings) {
                adapter.setLists(strings);
            }
        });

        listName = findViewById(R.id.shoppingListName);
        addList = findViewById(R.id.createListButton);
        shoppingListsView = findViewById(R.id.shoppingLists);

        shoppingListsView.setHasFixedSize(true);
        shoppingListsView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListsView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(shoppingList,this, currentUser.getuName());
        shoppingListsView.setAdapter(adapter);

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = listName.getText().toString();
                String owner = currentUser.getuMail();
                if (name.isEmpty())
                    Snackbar.make(shoppingListsView, "Adj nevet a listának!", Snackbar.LENGTH_LONG).show();
                else if (alreadyExits(name)) {
                    Snackbar.make(shoppingListsView, "Van már ilyen lista, adj másik nevet!", Snackbar.LENGTH_LONG).show();
                    listName.getText().clear();
                    listName.requestFocus();
                }
                else {
                    ListClass newList = new ListClass(name,owner);
                    listDB.createList(newList);
                    adapter.notifyDataSetChanged();
                    listName.getText().clear();
                    listName.requestFocus();
                }
            }
        });
    }

    // Menü létrehozása
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        String userName = currentUser.getuName();
        setTitle(userName + " Listái");
        return true;
    }

    // Menü elemek programozása
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                confirmationAndDelete();
                break;
            case R.id.edit:
                editLists();
                break;
            case R.id.userEditActivity:
                createActivity(this, UserEditDataActivity.class);
                break;
            case R.id.contacts:
                createActivity(this, ContactsActivity.class);
                break;
            case R.id.chartsMenuButton:
                createActivity(this,ChartActivity.class);
                break;
            case R.id.logOutMenu:
                logOut();
                finish();
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    // felhasználói adatainak módosítása vagy a kimutatás elindítás
    private void createActivity(Context context, Class cls){
        Intent startActivity = new Intent(context, cls);
        startActivity(startActivity);
    }

    // Megvizsgálja, hogy van-e hasonló nevű lista
    public boolean alreadyExits(String name) {
        boolean exits = false;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).getName().equals(name)) {
                exits = true;
            }
        }
        return exits;
    }

    // lista elemre kattintás
    @Override
    public void onListClick(ListClass list) {
        Intent openList = new Intent(ListActivity.this, ProductActivity.class);
        openList.putExtra("name", list.getName());
        openList.putExtra("ID", list.getListID());
        openList.putExtra("ownerMail", ownerMail);
        startActivity(openList);
    }
    // lista elemre hosszan kattintás
    @Override
    public void onListLongClick(ListClass list) {
        list.setSelected(!list.isSelected());
        adapter.notifyDataSetChanged();
    }

    // lista törlése előtt megerősítés kérése a felhasználótól
    private void confirmationAndDelete() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage("Biztosan törölni szeretné a kijelölt elemeket?")
                .setCancelable(false)
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLists();
                    }
                }).setNegativeButton("Nem", null);
        deleteDialog.show();
    }

    // lista tényleges törlése
    private void deleteLists() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                listDB.deleteList(adapter.getItem(i));
            }
        }
    }

    // lista szerkesztése fragment indítása
    private void editLists() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                ListEditFragment editDialog = new ListEditFragment(adapter.getItem(i), i);
                editDialog.show(getSupportFragmentManager(), "listNameEdit");
            }
        }
    }

    // lista szerkesztése, adatbázis frissítése
    public void editShoppingList(ListClass originalList, int position, String newName) {
        listDB.updateList(originalList, position, newName);
    }

    // felhasználó kijelentkeztetése
    private void logOut() {
        listDB.singOut();
    }

    // kijelentkezés a visszagomb megnyomására is!
    @Override
    public void onBackPressed() {
        logOut();
        super.onBackPressed();
    }
}