package com.example.recyclerview2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.appDataBase.appDataBase;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class shoppingListActivity extends AppCompatActivity implements OnListCL, OnProductEL {
    private Button addProduct;
    private AutoCompleteTextView productName;
    private EditText productQuantity;
    private Spinner productQuantityUnit;
    private RecyclerView shoppingListView;
    private ArrayList<shoppingListProductClass> shoppingList = new ArrayList<shoppingListProductClass>();
    private shoppingListAdapter adapter;
    private listsViewModel listsViewModel;
    private String title; //A bevásárlólista címe


    //létrehozás, nézet elemeinek és funkcióinak beállítása
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);
        Intent intent = getIntent();
        title = intent.getStringExtra("name");

        addProduct = findViewById(R.id.modifyButton);
        productName = findViewById(R.id.modifyName);
        productQuantity = findViewById(R.id.quantityProduct);
        productQuantityUnit = findViewById(R.id.unitSpinner);
        shoppingListView = findViewById(R.id.shoppingLists);

        listsViewModel = new ViewModelProvider(this).get(listsViewModel.class);
        listsViewModel.getAllProducts().observe(this, new Observer<List<shoppingListProductClass>>() {
            @Override
            public void onChanged(List<shoppingListProductClass> shoppingListProductClasses) {
                adapter.setProducts(shoppingListProductClasses);
            }
        });

        String[] productVariants = getResources().getStringArray(R.array.product_variants);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productVariants);
        productName.setAdapter(adapterProducts);

        String[] units = getResources().getStringArray(R.array.unit_variants);
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,units);
        productQuantityUnit.setAdapter(adapterUnits);

        adapter= new shoppingListAdapter(shoppingList, this);
        shoppingListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        shoppingListView.setLayoutManager(layoutManager);
        shoppingListView.setItemAnimator(new DefaultItemAnimator());
        shoppingListView.setAdapter(adapter);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listItem = productName.getText().toString();
                String quantity = productQuantity.getText().toString();
                String unit = productQuantityUnit.getSelectedItem().toString();
                Boolean checked = false;
                if (listItem.isEmpty())
                    Snackbar.make(shoppingListView, "Adj nevet a terméknek!", Snackbar.LENGTH_LONG).show();
                else {
                    shoppingList.add(new shoppingListProductClass(listItem, quantity, unit));
                    productName.getText().clear();
                    productQuantity.getText().clear();
                    productName.requestFocus();
                    listsViewModel.insertProduct(new shoppingListProductClass(listItem, quantity, unit, checked, title));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    //menü megjelenítés és címállítás
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        setTitle(title);
        return true;
    }

    //menü kattintás funkciók beállítása
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            for (int i = 0; i < listsViewModel.getAllProducts().getValue().size(); i++) {
                if (listsViewModel.getAllProducts().getValue().get(i).isSelected()) {
                    listsViewModel.deleteProduct(listsViewModel.getAllProducts().getValue().get(i));
                }
            }
        }
        if (item.getItemId() == R.id.edit){
            for (int i = 0; i < listsViewModel.getAllProducts().getValue().size(); i++) {
                if (listsViewModel.getAllProducts().getValue().get(i).isSelected()) {
                    shoppingListEditFragment editDialog = new shoppingListEditFragment(this,
                            listsViewModel.getAllProducts().getValue().get(i).getName(),
                            listsViewModel.getAllProducts().getValue().get(i).getQuantity(),
                            listsViewModel.getAllProducts().getValue().get(i).getQuantityType(),
                            i);
                    editDialog.show(getSupportFragmentManager(), "listNameEdit");
                }
            }
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    //elem kijelölése a listában
    @Override
    public void onClick(int position) {
        boolean selected = listsViewModel.getAllProducts().getValue().get(position).isSelected();
        listsViewModel.getAllProducts().getValue().get(position).setSelected(!selected);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editProduct(String name, String quantity, int quantityType, int position) {
        int productID = listsViewModel.getAllProducts().getValue().get(position).getProductID();
        shoppingListProductClass updateProduct = new shoppingListProductClass(name,quantity,productQuantityUnit.getItemAtPosition(quantityType).toString(), false, title);
        updateProduct.setProductID(productID);
        listsViewModel.updateProduct(updateProduct);
        adapter.notifyDataSetChanged();
    }
}