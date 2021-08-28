package com.example.recyclerview2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.recyclerview2.repository.repository;

import java.util.List;

public class listsViewModel extends AndroidViewModel {
    private repository listReposirtory;
    private LiveData<List<ListClass>> allLists;
    private LiveData<List<ProductClass>> allProducts;
    private LiveData<List<ProductClass>> allProductsOfList;

    public listsViewModel(@NonNull Application application){
        super(application);
        listReposirtory = new repository(application);
        allLists = listReposirtory.getAllLists();
        allProducts = listReposirtory.getAllProducts();
    }

    public void insertList(ListClass list){
        listReposirtory.insertList(list);
    }

    public void updateList(ListClass list) {
        listReposirtory.updateList(list);
    }

    public void deleteList(ListClass list){
        listReposirtory.deleteList(list);
    }

    public LiveData<List<ListClass>> getAllLists() {
        return allLists;
    }

    public void insertProduct(ProductClass product) {
        listReposirtory.insertProduct(product);
    }

    public void deleteProduct(ProductClass product) {
        listReposirtory.deleteProduct(product);
    }

    public void updateProduct(ProductClass product) {
        listReposirtory.updateProduct(product);
    }

    public LiveData<List<ProductClass>> getAllProducts() {
        return  allProducts;
    }

    public LiveData<List<ProductClass>> getAllProductsOfList(int ID) {
        allProductsOfList = listReposirtory.getAllProductsOfList(ID);
        return allProductsOfList;
    }
}
