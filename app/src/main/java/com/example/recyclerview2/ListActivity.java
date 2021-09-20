package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.recyclerview2.user.EditUserDataActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ListAdapter.OnListItemCL, ListAdapter.OnListItemLongCL, OnShoppingListEL {
    private EditText listName;
    private Button addList;
    private RecyclerView shoppingListsView;
    private List<ListClass> shoppingList = new ArrayList<>();
    private ListAdapter adapter;
    private listsViewModel listsViewModel;

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
        adapter = new ListAdapter(shoppingList,this, this);
        shoppingListsView.setAdapter(adapter);

        listsViewModel = new ViewModelProvider(this).get(listsViewModel.class);
        listsViewModel.getAllLists().observe(this, new Observer<List<ListClass>>() {
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
                    listsViewModel.insertList(new ListClass(name));
                    listName.getText().clear();
                    listName.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        setTitle("Bevásárlólisták");
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            for (int i = 0; i < listsViewModel.getAllLists().getValue().size(); i++) {
                if (listsViewModel.getAllLists().getValue().get(i).isSelected()) {
                    listsViewModel.deleteList(listsViewModel.getAllLists().getValue().get(i));
                }
            }
        }
        if (item.getItemId() == R.id.edit) {
            for (int i = 0; i < listsViewModel.getAllLists().getValue().size(); i++) {
                if (listsViewModel.getAllLists().getValue().get(i).isSelected()) {
                    ListEditFragment editDialog = new ListEditFragment(this, i, listsViewModel.getAllLists().getValue().get(i).getName());
                    editDialog.show(getSupportFragmentManager(), "listNameEdit");
                }
            }
        }
        if(item.getItemId() == R.id.userEditActivity){
            Intent intent = new Intent(ListActivity.this, EditUserDataActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.logOutMenu) finish();
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void editShoppingList(String input, int position) {
        int ID = listsViewModel.getAllLists().getValue().get(position).getListID();
        ListClass updateList = new ListClass(input);
        updateList.setListID(ID);
        listsViewModel.updateList(updateList);
        adapter.notifyDataSetChanged();
    }

    private boolean alreadyExits(String name) {
        boolean exits = false;
        for (int i = 0; i < listsViewModel.getAllLists().getValue().size(); i++) {
            if (listsViewModel.getAllLists().getValue().get(i).getName().equals(name)) {
                exits = true;
            }
        }
        return exits;
    }


    @Override
    public void onListClick(ListClass list) {
        Intent intent = new Intent(ListActivity.this, ProductActivity.class);
        intent.putExtra("name", list.getName());
        intent.putExtra("ID", list.getListID());
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(ListClass list) {
        list.setSelected(!list.isSelected());
        adapter.notifyDataSetChanged();
    }
}



