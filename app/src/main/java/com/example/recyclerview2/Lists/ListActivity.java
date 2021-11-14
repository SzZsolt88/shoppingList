package com.example.recyclerview2.Lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.recyclerview2.ProductActivity;
import com.example.recyclerview2.R;
//import com.example.recyclerview2.ViewModel;
import com.example.recyclerview2.appDataBase.ListDB;
import com.example.recyclerview2.appDataBase.User;
import com.example.recyclerview2.charts.ChartActivity;
import com.example.recyclerview2.contacts.ContactsActivity;
import com.example.recyclerview2.interfaces.OnListItemCL;
import com.example.recyclerview2.user.UserEditDataActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements OnListItemCL {
    private EditText listName;
    private Button addList;
    private RecyclerView shoppingListsView;
    private ListAdapter adapter;
    //private com.example.recyclerview2.ViewModel ViewModel;
    private ListDB listDB;
    private User currentUser;
    private String ownerMail;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);

        Intent getCurrentUser = getIntent();
        currentUser = getCurrentUser.getParcelableExtra("currentUser");
        ownerMail = currentUser.getuMail();
        userId = currentUser.getUserID();

        listDB = new ListDB();

        listName = findViewById(R.id.shoppingListName);
        addList = findViewById(R.id.createListButton);
        shoppingListsView = findViewById(R.id.shoppingLists);

        shoppingListsView.setHasFixedSize(true);
        shoppingListsView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListsView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(listDB.getListOfUser(ownerMail),this);
        shoppingListsView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //ViewModel = new ViewModelProvider(this).get(ViewModel.class);
        //ViewModel.getAllListsOfUser(ownerMail).observe(this, new Observer<List<ListClass>>() {
        //    @Override
        //    public void onChanged(List<ListClass> ListClasses) {

        //        adapter.setLists(ListClasses);
        //    }
        //});

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = listName.getText().toString();
                if (name.isEmpty())
                    Snackbar.make(shoppingListsView, "Adj nevet a listának!", Snackbar.LENGTH_LONG).show();
/*                else if (alreadyExits(name)) {
                    Snackbar.make(shoppingListsView, "Van már ilyen lista, adj másik nevet!", Snackbar.LENGTH_LONG).show();
                    listName.getText().clear();
                    listName.requestFocus();
                }*/
                else {
                    listDB.createList(ownerMail,name);
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

    // lista szerkesztése fragment indítása
    private void editLists() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                ListEditFragment editDialog = new ListEditFragment(i, adapter.getItem(i).getName());
                editDialog.show(getSupportFragmentManager(), "listNameEdit");
            }
        }
    }
    // lista törlése előtt megerősítés kérése a felhasználótól
    private void confirmationAndDelete() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage("Szeretné törölni a kijelölt elemeket?")
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
        //        ViewModel.deleteList(adapter.getItem(i), adapter.getItem(i).getListID());
            }
        }
    }
    // felhasználói adatainak módosítása vagy a kimutatás elindítás
    private void createActivity(Context context, Class cls){
        Intent startActivity = new Intent(context, cls);
        startActivity(startActivity);
    }
    // lista szerkesztése, adatbázis frissítése
    public void editShoppingList(String input, int position) {
            //int ID = adapter.getItem(position).getListID();
      //      ListClass updateList = new ListClass(input, ownerMail);
      //      updateList.setListID(ID);
      //      ViewModel.updateList(updateList);
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
        openList.putExtra("mail", ownerMail);
        //openList.putExtra("ID", list.getListID());
        startActivity(openList);
    }
    // lista elemre hosszan kattintás
    @Override
    public void onListLongClick(ListClass list) {
        list.setSelected(!list.isSelected());
        adapter.notifyDataSetChanged();
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

    // kilépéskor a változások feltölése a firestoreba
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}