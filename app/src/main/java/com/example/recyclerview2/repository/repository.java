package com.example.recyclerview2.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.recyclerview2.ListClass;
import com.example.recyclerview2.ProductClass;
import com.example.recyclerview2.appDataBase.LocalDB;
import com.example.recyclerview2.appDataBase.LocalDBDao;

import java.util.List;

public class repository {
    private LocalDBDao localDBDao;
    private LiveData<List<ProductClass>> allProducts;
    private LiveData<List<ProductClass>> allProductsOfList;
    private LiveData<List<ListClass>> allLists;

    public repository(Application application){
        LocalDB appDatabase = LocalDB.getDbInstance(application);
        localDBDao = appDatabase.localDBDao();
        allProducts = localDBDao.getAllProducts();
        allLists = localDBDao.getAllList();
    }

    ////
    //// Termékekkel kapcsolatos műveletek!
    ////
    public LiveData<List<ProductClass>> getAllProducts() {
        return allProducts;
    }
    public LiveData<List<ProductClass>> getAllProductsOfList(int ID) {
        allProductsOfList = localDBDao.getAllProductsOfList(ID);
        return allProductsOfList;
    }

    public void insertProduct(ProductClass ProductClass){
        new InsertProductAsyncTask(localDBDao).execute(ProductClass);
    }
    public void updateProduct(ProductClass ProductClass){
        new  UpdateProductAsyncTask(localDBDao).execute(ProductClass);
    }
    public void deleteProduct(ProductClass ProductClass){
        new DeleteProductAsyncTask(localDBDao).execute(ProductClass);
    }

    private static class InsertProductAsyncTask extends AsyncTask<ProductClass, Void, Void> {
        private LocalDBDao localDBDao;

        private InsertProductAsyncTask(LocalDBDao shoppingListProductDao){
            this.localDBDao = shoppingListProductDao;
        }

        @Override
        protected Void doInBackground(ProductClass... ProductClasses) {
            localDBDao.insertProduct(ProductClasses[0]);
            return null;
        }
    }
    private static class UpdateProductAsyncTask extends AsyncTask<ProductClass, Void, Void> {
        private LocalDBDao shoppingListProductDao;

        private UpdateProductAsyncTask(LocalDBDao shoppingListDao){
            this.shoppingListProductDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ProductClass... ProductClasses) {
            shoppingListProductDao.updateProduct(ProductClasses[0]);
            return null;
        }
    }
    private static class DeleteProductAsyncTask extends AsyncTask<ProductClass, Void, Void> {
        private LocalDBDao shoppingListDao;

        private DeleteProductAsyncTask(LocalDBDao localDBDao){
            this.shoppingListDao = localDBDao;
        }

        @Override
        protected Void doInBackground(ProductClass... ProductClasses) {
            shoppingListDao.deleteProduct(ProductClasses[0]);
            return null;
        }
    }

    ////
    //// Listával kapcsolatos műveletek!
    ////

    public LiveData<List<ListClass>> getAllLists() {
        return allLists;
    }

    public void insertList(ListClass ListClass){
        new InsertListAsyncTask(localDBDao).execute(ListClass) ;

    }
    public void updateList(ListClass ListClass){
        new UpdateListAsyncTask(localDBDao).execute(ListClass);
    }
    public void deleteList(ListClass ListClass){
        new DeleteListAsyncTask(localDBDao).execute(ListClass);
    }

    private static class InsertListAsyncTask extends AsyncTask<ListClass, Void, Void> {
        private LocalDBDao localDBDao;

        private InsertListAsyncTask(LocalDBDao localDBDao){
            this.localDBDao = localDBDao;
        }

        @Override
        protected Void doInBackground(ListClass... ListClasses) {
            localDBDao.insertList(ListClasses[0]);
            return null;
        }
    }
    private static class UpdateListAsyncTask extends AsyncTask<ListClass, Void, Void> {
        private LocalDBDao localDBDao;

        private UpdateListAsyncTask(LocalDBDao localDBDao){
            this.localDBDao = localDBDao;
        }

        @Override
        protected Void doInBackground(ListClass... ListClasses) {
            localDBDao.updateList(ListClasses[0]);
            return null;
        }
    }
    private static class DeleteListAsyncTask extends AsyncTask<ListClass, Void, Void> {
        private LocalDBDao localDBDao;

        private DeleteListAsyncTask(LocalDBDao localDBDao){
            this.localDBDao = localDBDao;
        }

        @Override
        protected Void doInBackground(ListClass... ListClasses) {
            localDBDao.deleteList(ListClasses[0]);
            return null;
        }
    }
}
