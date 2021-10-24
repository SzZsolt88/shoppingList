package com.example.recyclerview2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.recyclerview2.repository.repository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private repository repository;
    private LiveData<List<ListClass>> allLists;
    private LiveData<List<ProductClass>> allProducts;
    private LiveData<List<ProductClass>> allProductsOfList;

    public ViewModel(@NonNull Application application){
        super(application);
        repository = new repository(application);
        allLists = repository.getAllLists();
        allProducts = repository.getAllProducts();
    }

    public void insertList(ListClass list) {
        repository.insertList(list);
    }

    public void updateList(ListClass list) {
        repository.updateList(list);
    }

    public void deleteList(ListClass list, int ID){
        repository.deleteList(list);
        repository.deleteAllProductOfList(ID);
    }

    public LiveData<List<ListClass>> getAllLists() {
        return allLists;
    }

    public void insertProduct(ProductClass product) {
        repository.insertProduct(product);
    }

    public void deleteProduct(ProductClass product) {
        repository.deleteProduct(product);
    }

    public void updateProduct(ProductClass product) {
        repository.updateProduct(product);
    }

    public LiveData<List<ProductClass>> getAllProducts() {
        return  allProducts;
    }

    public LiveData<List<ProductClass>> getAllProductsOfList(int ID) {
        allProductsOfList = repository.getAllProductsOfList(ID);
        return allProductsOfList;
    }
}
