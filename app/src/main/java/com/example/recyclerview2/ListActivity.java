package com.example.recyclerview2;

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

import com.example.recyclerview2.charts.ChartActivity;
import com.example.recyclerview2.interfaces.OnListItemCL;
import com.example.recyclerview2.user.EditUserDataActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements OnListItemCL {
    private EditText listName;
    private Button addList;
    private RecyclerView shoppingListsView;
    private List<ListClass> shoppingList = new ArrayList<>();
    private ListAdapter adapter;
    private ViewModel ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);
        //deleteDatabase("shoppingList_Database");

        listName = findViewById(R.id.shoppingListName);
        addList = findViewById(R.id.createListButton);
        shoppingListsView = findViewById(R.id.shoppingLists);

        shoppingListsView.setHasFixedSize(true);
        shoppingListsView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListsView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(shoppingList,this);
        shoppingListsView.setAdapter(adapter);

        ViewModel = new ViewModelProvider(this).get(ViewModel.class);
        ViewModel.getAllLists().observe(this, new Observer<List<ListClass>>() {
            @Override
            public void onChanged(List<ListClass> ListClasses) {
                adapter.setLists(ListClasses);
            }
        });
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = listName.getText().toString();
                if (name.isEmpty())
                    Snackbar.make(shoppingListsView, "Adj nevet a listának!", Snackbar.LENGTH_LONG).show();
                else if (alreadyExits(name)) {
                    Snackbar.make(shoppingListsView, "Van már ilyen lista, adj másik nevet!", Snackbar.LENGTH_LONG).show();
                    listName.getText().clear();
                    listName.requestFocus();
                }
                else {
                    ViewModel.insertList(new ListClass(name));
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
        setTitle("Bevásárlólisták");
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
                createActivity(this,EditUserDataActivity.class);
                break;
            case R.id.chartsMenuButton:
                createActivity(this,ChartActivity.class);
                break;
            case R.id.logOutMenu:
                finish();
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
    // lista szerkesztése fragment indítása
    private void editLists() {
        for (int i = 0; i < ViewModel.getAllLists().getValue().size(); i++) {
            if (ViewModel.getAllLists().getValue().get(i).isSelected()) {
                ListEditFragment editDialog = new ListEditFragment(i, ViewModel.getAllLists().getValue().get(i).getName());
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
            if (ViewModel.getAllLists().getValue().get(i).isSelected()) {
                ViewModel.deleteList(ViewModel.getAllLists().getValue().get(i), ViewModel.getAllLists().getValue().get(i).getListID());
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
        if (input.isEmpty())
            Snackbar.make(shoppingListsView, "Adj nevet a listának!", Snackbar.LENGTH_LONG).show();
        else if (alreadyExits(input)) {
            Snackbar.make(shoppingListsView, "Van már ilyen nevü lista, adj másik nevet!", Snackbar.LENGTH_LONG).show();
        }
        else {
            int ID = ViewModel.getAllLists().getValue().get(position).getListID();
            ListClass updateList = new ListClass(input);
            updateList.setListID(ID);
            ViewModel.updateList(updateList);
        }
    }


    // Megvizsgálja, hogy van-e hasonló nevű lista
    private boolean alreadyExits(String name) {
        boolean exits = false;
        for (int i = 0; i < ViewModel.getAllLists().getValue().size(); i++) {
            if (ViewModel.getAllLists().getValue().get(i).getName().equals(name)) {
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
        startActivity(openList);
    }
    // lista elemre hosszan kattintás
    @Override
    public void onListLongClick(ListClass list) {
        list.setSelected(!list.isSelected());
        adapter.notifyDataSetChanged();
    }
}