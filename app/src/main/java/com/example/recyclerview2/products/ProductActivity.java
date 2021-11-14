package com.example.recyclerview2.products;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.appDataBase.ProductClass;
import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ProductDB;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements OnProductItemCL {
    private Button addProduct;
    private AutoCompleteTextView productName;
    private EditText productQuantity;
    private Spinner productQuantityUnit;
    private RecyclerView productsListView;
    private ProductAdapter adapter;
    private ProductDB productDB;
    //A bevásárlólista tulajdonosa,cím és ID
    private String ownerMail;
    private String title;
    private String listID;

    //létrehozás, nézet elemeinek és funkcióinak beállítása
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        Intent intent = getIntent();
        ownerMail = intent.getStringExtra("ownerMail");
        title = intent.getStringExtra("name");
        listID = intent.getStringExtra("ID");

        productDB = new ProductDB();
        productDB.getAllProductsOfList(ownerMail, listID);

        productDB.getProductsMutableLiveData().observe(this, new Observer<List<ProductClass>>() {
            @Override
            public void onChanged(List<ProductClass> productClasses) {
                adapter.setProducts(productClasses);
            }
        });

        addProduct = findViewById(R.id.addProductBtn);
        productName = findViewById(R.id.productNameTextView);
        productQuantity = findViewById(R.id.quantityProduct);
        productQuantityUnit = findViewById(R.id.unitSpinner);
        productsListView = findViewById(R.id.productsLists);


        productsListView.setHasFixedSize(true);
        productsListView.setLayoutManager(new LinearLayoutManager(this));
        productsListView.setItemAnimator(new DefaultItemAnimator());
        adapter= new ProductAdapter(this);
        productsListView.setAdapter(adapter);

        //A termék neve mező kapcsolása a javasolt termékek listájának erőforrások -> product_variants
        String[] productVariants = getResources().getStringArray(R.array.product_variants);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productVariants);
        productName.setAdapter(adapterProducts);

        //A mennyiségi egység spinner-hez értékek társítása a "unit_variants" erőforrásból
        String[] units = getResources().getStringArray(R.array.unit_variants);
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,units);
        productQuantityUnit.setAdapter(adapterUnits);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listItem = productName.getText().toString();
                String quantity = productQuantity.getText().toString();
                int unit;
                String unitString;
                if (quantity.length() > 0) {
                    unit = productQuantityUnit.getSelectedItemPosition();
                    unitString = productQuantityUnit.getItemAtPosition(unit).toString();
                }
                else {
                    unitString = "NN";
                }
                Boolean checked = false;
                if (listItem.isEmpty())
                    Snackbar.make(productsListView, "Adj nevet a terméknek!", Snackbar.LENGTH_LONG).show();
                else if (alreadyExits(listItem)) {
                    Snackbar.make(productsListView, "Van már ilyen terméked a listán!", Snackbar.LENGTH_LONG).show();
                    productName.getText().clear();
                    productQuantity.getText().clear();
                    productName.requestFocus();
                }
                else {
                    Log.d("TAG", "onClick: " + unitString);
                    ProductClass newProduct = new ProductClass(listItem,quantity,unitString);
                    Log.d("TAG", "onClick: " + newProduct.getQuantityType());
                    productDB.registerNewProduct(ownerMail,listID, newProduct);
                    productName.getText().clear();
                    productQuantity.getText().clear();
                    productName.requestFocus();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    //Megvizsgálja, hogy van-e hasonló nevű lista
    public boolean alreadyExits(String name) {
        boolean exits = false;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).getName().equals(name)) {
                exits = true;
            }
        }
        return exits;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_menu, menu);
        setTitle(title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancelActivity) {
            finish();
        } else if (item.getItemId() == R.id.delete) {
            confirmationAndDelete();
        } else if (item.getItemId() == R.id.edit) {
            editProduct();
        }

        return true;
    }

    private void confirmationAndDelete() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage("Szeretné törölni a kijelölt elemeket?")
                .setCancelable(false)
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  deleteProduct(); }
                }).setNegativeButton("Nem", null);
        deleteDialog.show();
    }

    private void deleteProduct() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                productDB.deleteProduct(ownerMail, listID, adapter.getItem(i));
                i--;
            }
        }
    }

    private void editProduct(){
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                ProductEditFragment editDialog = new ProductEditFragment(
                        adapter.getItem(i).getName(),
                        adapter.getItem(i).getQuantity(),
                        adapter.getItem(i).getQuantityType(),
                        i);
                editDialog.show(getSupportFragmentManager(), "listNameEdit");
            }
        }
    }

    //elem módosítása
    public void updateProduct(String updatedName, String updatedQuantity, int quantityType, ProductClass originalProduct) {
        String updatedQuantityType = "";
        if (updatedQuantity.length() > 0) {
            updatedQuantityType = productQuantityUnit.getItemAtPosition(quantityType).toString();
        }
        ProductClass alreadyUpdatedProduct = new ProductClass(updatedName,updatedQuantity,updatedQuantityType);
        productDB.updateProduct(ownerMail, listID, originalProduct, alreadyUpdatedProduct);
    }

    //elem kijelölése a listában
    @Override
    public void onProductClick(ProductClass product) {
        product.setSelected(!product.isSelected());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveCheckedStatus(ProductClass product) {
        adapter.notifyDataSetChanged();
    }
}