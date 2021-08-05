package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class listsActivity extends AppCompatActivity implements listsAdapter.OnListItemCL, listsAdapter.OnListItemLongCL, OnShoppingListEL {
    private EditText listName;
    private Button addList;
    private RecyclerView shoppingListsView;
    private List<listsShoppingListClass> shoppingList = new ArrayList<>();
    private listsAdapter adapter;
    private listsViewModel listsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);


        listName = findViewById(R.id.shoppingListName);
        addList = findViewById(R.id.createListButton);
        shoppingListsView = findViewById(R.id.shoppingLists);

        shoppingListsView.setHasFixedSize(true);
        shoppingListsView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListsView.setItemAnimator(new DefaultItemAnimator());
        adapter = new listsAdapter(shoppingList,this, this);
        shoppingListsView.setAdapter(adapter);

        listsViewModel = new ViewModelProvider(this).get(listsViewModel.class);
        listsViewModel.getAllLists().observe(this, new Observer<List<listsShoppingListClass>>() {
            @Override
            public void onChanged(List<listsShoppingListClass> listsShoppingListClasses) {
                adapter.setLists(listsShoppingListClasses);
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
                    listsViewModel.insert(new listsShoppingListClass(name));
                    listName.getText().clear();
                    listName.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        setTitle("Bevásárló listák");
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            for (int i = 0; i < listsViewModel.getAllLists().getValue().size(); i++) {
                if (listsViewModel.getAllLists().getValue().get(i).isSelected()) {
                    listsViewModel.delete(listsViewModel.getAllLists().getValue().get(i));
                    Log.d("TAG", "onOptionsItemSelected: torolve");
                }
            }
        }
        if (item.getItemId() == R.id.edit) {
            for (int i = 0; i < listsViewModel.getAllLists().getValue().size(); i++) {
                if (listsViewModel.getAllLists().getValue().get(i).isSelected()) {
                    listsEditFragment editDialog = new listsEditFragment(this, i, listsViewModel.getAllLists().getValue().get(i).getName());
                    editDialog.show(getSupportFragmentManager(), "listNameEdit");
                }
            }
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void editShoppingList(String input, int position) {
        int ID = listsViewModel.getAllLists().getValue().get(position).getListID();
        listsShoppingListClass updateList = new listsShoppingListClass(input);
        updateList.setListID(ID);
        listsViewModel.update(updateList);
        adapter.notifyDataSetChanged();
    }

    public boolean alreadyExits(String name) {
        boolean exits = false;
        for (int i = 0; i < listsViewModel.getAllLists().getValue().size(); i++) {
            if (listsViewModel.getAllLists().getValue().get(i).getName().equals(name)) {
                exits = true;
            }
        }
        return exits;
    }

    @Override
    public void onListClick(listsShoppingListClass list) {
        Intent intent = new Intent(listsActivity.this, shoppingListActivity.class);
        intent.putExtra("name", list.getName());
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(listsShoppingListClass list) {
        if(!list.isSelected()){
            list.setSelected(true);
        }
        else list.setSelected(false);
        adapter.notifyDataSetChanged();
    }
}



