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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductItemCL, OnProductEL {
    private Button addProduct;
    private AutoCompleteTextView productName;
    private EditText productQuantity;
    private Spinner productQuantityUnit;
    private RecyclerView shoppingListView;
    private List<ProductClass> shoppingList = new ArrayList<ProductClass>();
    private ProductAdapter adapter;
    private listsViewModel listsViewModel;
    //A bevásárlólista címe
    private String title;
    private int listID;


    //létrehozás, nézet elemeinek és funkcióinak beállítása
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);
        Intent intent = getIntent();
        title = intent.getStringExtra("name");
        listID = intent.getIntExtra("ID",0);

        addProduct = findViewById(R.id.modifyButton);
        productName = findViewById(R.id.modifyName);
        productQuantity = findViewById(R.id.quantityProduct);
        productQuantityUnit = findViewById(R.id.unitSpinner);
        shoppingListView = findViewById(R.id.shoppingLists);

        adapter= new ProductAdapter(shoppingList, this);
        shoppingListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        shoppingListView.setLayoutManager(layoutManager);
        shoppingListView.setItemAnimator(new DefaultItemAnimator());
        shoppingListView.setAdapter(adapter);

        listsViewModel = new ViewModelProvider(this).get(listsViewModel.class);
        listsViewModel.getAllProductsOfList(listID).observe(this, new Observer<List<ProductClass>>() {
            @Override
            public void onChanged(List<ProductClass> ProductClasses) {
                adapter.setProducts(ProductClasses);
            }
        });


        //A termék neve mezőhőz kapcsolása a javasolt termékek listájának erőforrások -> product_variants
        String[] productVariants = getResources().getStringArray(R.array.product_variants);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productVariants);
        productName.setAdapter(adapterProducts);

        //A mennyisége egység spinner-hez értékek társítása a "unit_variants" erőforássból
        String[] units = getResources().getStringArray(R.array.unit_variants);
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,units);
        productQuantityUnit.setAdapter(adapterUnits);

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
                    productName.getText().clear();
                    productQuantity.getText().clear();
                    productName.requestFocus();
                    listsViewModel.insertProduct(new ProductClass(listItem, quantity, unit, checked, listID));
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
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).isSelected()) {
                    listsViewModel.deleteProduct(adapter.getItem(i));
                }
            }
        }
        if (item.getItemId() == R.id.edit){
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).isSelected()) {
                    ProductEditFragment editDialog = new ProductEditFragment(this,
                            adapter.getItem(i).getName(),
                            adapter.getItem(i).getQuantity(),
                            adapter.getItem(i).getQuantityType(),
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
    public void onProductClick(ProductClass product) {
        product.setSelected(!product.isSelected());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveCheckedStatus(ProductClass product) {
        listsViewModel.updateProduct(product);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editProduct(String name, String quantity, int quantityType, int position) {
        adapter.getItem(position).setName(name);
        adapter.getItem(position).setQuantity(quantity);
        adapter.getItem(position).setQuantityType(productQuantityUnit.getItemAtPosition(quantityType).toString());
        listsViewModel.updateProduct(adapter.getItem(position));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.getItem(i).setChecked(adapter.getItem(i).isChecked());
            listsViewModel.updateProduct(adapter.getItem(i));
        }
    }
}