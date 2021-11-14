package com.example.recyclerview2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.Lists.ListClass;
import com.example.recyclerview2.appDataBase.ListDB;
import com.example.recyclerview2.appDataBase.ProductDB;
import com.example.recyclerview2.appDataBase.User;
import com.example.recyclerview2.charts.ChartActivity;
import com.example.recyclerview2.contacts.ContactsActivity;
import com.example.recyclerview2.interfaces.OnProductItemCL;
import com.example.recyclerview2.user.UserEditDataActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements OnProductItemCL {
    private Button addProduct;
    private AutoCompleteTextView productName;
    private EditText productQuantity;
    private Spinner productQuantityUnit;
    private RecyclerView shoppingListView;
    //private List<ProductClass> shoppingList = new ArrayList<ProductClass>();
    private ProductAdapter adapter;
    private ProductDB productDB;
    private User currentUser;
    private String currentList;
    private String ownerMail;

    //private ViewModel ViewModel;
    //A bevásárlólista címe
    private String title;
    private int listID;

    //létrehozás, nézet elemeinek és funkcióinak beállítása
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        //Intent intent = getIntent();

        Intent getCurrentList = getIntent();
        //currentUser = getCurrentUser.getParcelableExtra("currentUser");
        currentList = getCurrentList.getStringExtra("name");
        ownerMail = getCurrentList.getStringExtra("mail");

        //title = intent.getStringExtra("name");
        //listID = intent.getIntExtra("ID",0);

        productDB = new ProductDB();

        addProduct = findViewById(R.id.modifyButton);
        productName = findViewById(R.id.modifyName);
        productQuantity = findViewById(R.id.quantityProduct);
        productQuantityUnit = findViewById(R.id.unitSpinner);
        shoppingListView = findViewById(R.id.shoppingLists);

        adapter= new ProductAdapter(productDB.getListOfProducts(ownerMail, currentList),  this);
        shoppingListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        shoppingListView.setLayoutManager(layoutManager);
        shoppingListView.setItemAnimator(new DefaultItemAnimator());
        shoppingListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //ViewModel = new ViewModelProvider(this).get(ViewModel.class);
        //ViewModel.getAllProductsOfList(listID).observe(this, new Observer<List<ProductClass>>() {
        //    @Override
        //    public void onChanged(List<ProductClass> ProductClasses) {
        //        adapter.setProducts(ProductClasses);
        //    }
        //});


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
                //int quantity = Integer.getInteger(productQuantity.getText().toString());
                int quantity = 2;
                String unit;
                if (quantity > 0) {
                    unit = productQuantityUnit.getSelectedItem().toString();
                }
                else {
                    unit = "NN";
                }
                Boolean checked = false;
                if (listItem.isEmpty())
                    Snackbar.make(shoppingListView, "Adj nevet a terméknek!", Snackbar.LENGTH_LONG).show();
                else {
                    productName.getText().clear();
                    productQuantity.getText().clear();
                    productName.requestFocus();
                    //ViewModel.insertProduct(new ProductClass(listItem, quantity, unit, checked, listID));
                    productDB.createProduct(ownerMail, currentList, listItem, 0, quantity,unit, checked, false );
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    //menü megjelenítés és a cím átállítása
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        setTitle(title);
        return true;
    }

    //menü kattintás funkciók beállítása
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                confirmationAndDelete();
                break;
            case R.id.edit:
                editProduct();
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
                finish();
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
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
                //ViewModel.deleteProduct(adapter.getItem(i));
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

    private void createActivity(Context context, Class cls){
        Intent startActivity = new Intent(context, cls);
        startActivity(startActivity);
    }

    //elem kijelölése a listában
    @Override
    public void onProductClick(ProductClass product) {
        product.setSelected(!product.isSelected());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveCheckedStatus(ProductClass product) {
        //ViewModel.updateProduct(product);
        adapter.notifyDataSetChanged();
    }

    //elem módosítása
    public void editProduct(String name, int quantity, int quantityType, int position) {
        adapter.getItem(position).setName(name);
        adapter.getItem(position).setQuantity(quantity);
        if (adapter.getItem(position).getQuantity() > 0) {
            adapter.getItem(position).setQuantityType(productQuantityUnit.getItemAtPosition(quantityType).toString());
        }
        else {
            adapter.getItem(position).setQuantityType("NN");
        }
        //ViewModel.updateProduct(adapter.getItem(position));
        adapter.notifyDataSetChanged();
    }
}