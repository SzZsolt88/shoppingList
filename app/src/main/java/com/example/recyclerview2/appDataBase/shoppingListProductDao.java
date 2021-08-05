package com.example.recyclerview2.appDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recyclerview2.shoppingListProductClass;

import java.util.List;

@Dao
public interface shoppingListProductDao {

    @Query("SELECT * FROM shoppingListProductClass WHERE lista = :listName")
    List<shoppingListProductClass> getAllProductsOfList(String listName);

    @Query("SELECT * FROM shoppingListProductClass")
    LiveData<List<shoppingListProductClass>> getAllProducts();

    @Insert
    void insertProduct(shoppingListProductClass... shoppingListProductClasses);

    @Delete
    void delete(shoppingListProductClass shoppingListProductClass);

    @Query("DELETE FROM shoppingListProductClass WHERE ProductID = :productId")
    int delete(final int productId);

    @Update
    void update(shoppingListProductClass... shoppingListProductClasses);
}
