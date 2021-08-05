package com.example.recyclerview2.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

import com.example.recyclerview2.appDataBase.shoppingListProductDao;
import com.example.recyclerview2.appDataBase.appDataBase;
import com.example.recyclerview2.appDataBase.listsShoppingListDao;
import com.example.recyclerview2.shoppingListProductClass;
import com.example.recyclerview2.listsShoppingListClass;

import java.util.List;

public class repository {
    private shoppingListProductDao productDao;
    private listsShoppingListDao listDao;
    private LiveData<List<shoppingListProductClass>> allProducts;
    private LiveData<List<listsShoppingListClass>> allLists;

    public repository(Application application){
        appDataBase appDatabase = appDataBase.getDbInstance(application);
        productDao = appDatabase.productDao();
        listDao = appDatabase.shoppingListDAO();
        allProducts = productDao.getAllProducts();
        allLists = listDao.getAllList();
    }

    public void insertProduct(shoppingListProductClass shoppingListProductClass){
        new InsertProductAsyncTask(productDao).execute(shoppingListProductClass);
    }

    public void updateProduct(shoppingListProductClass shoppingListProductClass){
        new  UpdateProductAsyncTask(productDao).execute(shoppingListProductClass);
    }

    public void deleteProduct(shoppingListProductClass shoppingListProductClass){
        new DeleteProductAsyncTask(productDao).execute(shoppingListProductClass);
    }

    public LiveData<List<shoppingListProductClass>> getAllProducts() {
        return allProducts;
    }

    private static class InsertProductAsyncTask extends AsyncTask<shoppingListProductClass, Void, Void> {
        private  shoppingListProductDao shoppingListProductDao;

        private InsertProductAsyncTask(shoppingListProductDao shoppingListProductDao){
            this.shoppingListProductDao = shoppingListProductDao;
        }

        @Override
        protected Void doInBackground(shoppingListProductClass... shoppingListProductClasses) {
            shoppingListProductDao.insertProduct(shoppingListProductClasses[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<shoppingListProductClass, Void, Void> {
        private  shoppingListProductDao shoppingListProductDao;

        private DeleteProductAsyncTask(shoppingListProductDao shoppingListProductDao){
            this.shoppingListProductDao = shoppingListProductDao;
        }

        @Override
        protected Void doInBackground(shoppingListProductClass... shoppingListProductClasses) {
            shoppingListProductDao.delete(shoppingListProductClasses[0]);
            return null;
        }
    }

    private static class UpdateProductAsyncTask extends AsyncTask<shoppingListProductClass, Void, Void> {
        private  shoppingListProductDao shoppingListProductDao;

        private UpdateProductAsyncTask(shoppingListProductDao shoppingListProductDao){
            this.shoppingListProductDao = shoppingListProductDao;
        }

        @Override
        protected Void doInBackground(shoppingListProductClass... shoppingListProductClasses) {
            shoppingListProductDao.update(shoppingListProductClasses[0]);
            return null;
        }
    }

    ////
    //// Listával kapcsolatos műveletek!
    ////

    public void insertList(listsShoppingListClass listsShoppingListClass){
        new InsertListAsyncTask(listDao).execute(listsShoppingListClass) ;

    }
    public void updateList(listsShoppingListClass listsShoppingListClass){
        new UpdateListAsyncTask(listDao).execute(listsShoppingListClass);
    }
    public void deleteList(listsShoppingListClass listsShoppingListClass){
        new DeleteListAsyncTask(listDao).execute(listsShoppingListClass);
    }

    public LiveData<List<listsShoppingListClass>> getAllLists() {
        return allLists;
    }

    private static class InsertListAsyncTask extends AsyncTask<listsShoppingListClass, Void, Void> {
        private  listsShoppingListDao listsShoppingListDao;

        private InsertListAsyncTask(listsShoppingListDao listsShoppingListDao){
            this.listsShoppingListDao = listsShoppingListDao;
        }

        @Override
        protected Void doInBackground(listsShoppingListClass... listsShoppingListClasses) {
            listsShoppingListDao.insertList(listsShoppingListClasses[0]);
            return null;
        }
    }

    private static class DeleteListAsyncTask extends AsyncTask<listsShoppingListClass, Void, Void> {
        private  listsShoppingListDao listsShoppingListDao;

        private DeleteListAsyncTask(listsShoppingListDao listsShoppingListDao){
            this.listsShoppingListDao = listsShoppingListDao;
        }

        @Override
        protected Void doInBackground(listsShoppingListClass... listsShoppingListClasses) {
            listsShoppingListDao.deleteList(listsShoppingListClasses[0]);
            return null;
        }
    }

    private static class UpdateListAsyncTask extends AsyncTask<listsShoppingListClass, Void, Void> {
        private  listsShoppingListDao listsShoppingListDao;

        private UpdateListAsyncTask(listsShoppingListDao listsShoppingListDao){
            this.listsShoppingListDao = listsShoppingListDao;
        }

        @Override
        protected Void doInBackground(listsShoppingListClass... listsShoppingListClasses) {
            listsShoppingListDao.updateList(listsShoppingListClasses[0]);
            return null;
        }
    }



}
