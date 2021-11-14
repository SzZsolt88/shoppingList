/*
package com.example.recyclerview2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.recyclerview2.Lists.ListClass;
import com.example.recyclerview2.appDataBase.LocalDB;
import com.example.recyclerview2.appDataBase.LocalDBDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class repository {
    private final LocalDBDao localDBDao;

    public repository(Application application){
        LocalDB appDatabase = LocalDB.getDbInstance(application);
        localDBDao = appDatabase.localDBDao();
    }

    ////
    //// Termékekkel kapcsolatos műveletek!
    ////
    public LiveData<List<ProductClass>> getAllProducts() {
        return localDBDao.getAllProducts();
    }
    public LiveData<List<ProductClass>> getAllProductsOfList(int ID) {
        return localDBDao.getAllProductsOfList(ID);
    }

    public void insertProduct(ProductClass ProductClass){
        ExecutorService insertProduct = Executors.newSingleThreadExecutor();
        insertProduct.execute(new Runnable() {
            //doInBackground
            @Override
            public void run() {
                localDBDao.insertProduct(ProductClass);
            }
        });
    }
    public void updateProduct(ProductClass ProductClass){
        ExecutorService updateProduct = Executors.newSingleThreadExecutor();
        updateProduct.execute(new Runnable() {
            //doInBackground
            @Override
            public void run() {
                localDBDao.updateProduct(ProductClass);
            }
        });
    }
    public void deleteProduct(ProductClass ProductClass){
        ExecutorService deleteProduct = Executors.newSingleThreadExecutor();
        deleteProduct.execute(new Runnable() {
            //doInBackground
            @Override
            public void run() {
                localDBDao.deleteProduct(ProductClass);
            }
        });
    }
    public void deleteAllProductOfList(int ID) {
        ExecutorService deleteAllProductOfList = Executors.newSingleThreadExecutor();
        deleteAllProductOfList.execute(new Runnable() {
            //doInBackground
            @Override
            public void run() {
                localDBDao.deleteAllProductsOfList(ID);
            }
        });
    }

    ////
    //// Listával kapcsolatos műveletek!
    ////

    public LiveData<List<ListClass>> getAllLists() {
        return localDBDao.getAllList();
    }
    public LiveData<List<ListClass>> getAllListOfUser(String ID) {
        return localDBDao.getAllListOfUser(ID);
    }

    public void insertList(ListClass ListClass){
       ExecutorService insertList = Executors.newSingleThreadExecutor();
       insertList.execute(new Runnable() {
           //doInBackground
           @Override
           public void run() {
               localDBDao.insertList(ListClass);
           }
       });
    }
    public void updateList(ListClass ListClass){
        ExecutorService updateList = Executors.newSingleThreadExecutor();
        updateList.execute(new Runnable() {
            //doInBackground
            @Override
            public void run() {
                localDBDao.updateList(ListClass);
            }
        });
    }
    public void deleteList(ListClass ListClass){
        ExecutorService deleteList = Executors.newSingleThreadExecutor();
        deleteList.execute(new Runnable() {
            //doInBackground
            @Override
            public void run() {
                localDBDao.deleteList(ListClass);
            }
        });
    }
}

 */