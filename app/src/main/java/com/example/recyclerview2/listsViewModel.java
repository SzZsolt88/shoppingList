package com.example.recyclerview2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.recyclerview2.repository.repository;

import java.util.List;

public class listsViewModel extends AndroidViewModel {
    private repository listReposirtory;
    private LiveData<List<listsShoppingListClass>> allLists;
    private LiveData<List<shoppingListProductClass>> allProducts;

    public listsViewModel(@NonNull Application application){
        super(application);
        listReposirtory = new repository(application);
        allLists = listReposirtory.getAllLists();
        allProducts = listReposirtory.getAllProducts();
    }

    public void insert(listsShoppingListClass list){
        listReposirtory.insertList(list);
    }

    public void update(listsShoppingListClass list) {
        listReposirtory.updateList(list);
    }

    public void delete(listsShoppingListClass list){
        listReposirtory.deleteList(list);
    }

    public LiveData<List<listsShoppingListClass>> getAllLists() {
        return allLists;
    }

    public void insertProduct(shoppingListProductClass product) {
        listReposirtory.insertProduct(product);
    }

    public void deleteProduct(shoppingListProductClass product) {
        listReposirtory.deleteProduct(product);
    }

    public void updateProduct(shoppingListProductClass product) {
        listReposirtory.updateProduct(product);
    }

    public LiveData<List<shoppingListProductClass>> getAllProducts() {
        return  allProducts;
    }

}
