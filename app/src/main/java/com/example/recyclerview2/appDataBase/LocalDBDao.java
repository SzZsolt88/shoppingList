package com.example.recyclerview2.appDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.recyclerview2.ListClass;
import com.example.recyclerview2.ProductClass;

import java.util.List;

@Dao
public interface LocalDBDao {

    //--------------------------------//
    //Listával kapcsolatos műveletek://
    //------------------------------//

    //Listák lekérdezése:
    @Query("SELECT * FROM ListClass")
    LiveData<List<ListClass>> getAllList();

    //Lista beillesztése:
    @Insert
    void insertList(ListClass listClass);

    //Lista törlése:
    @Delete
    void deleteList(ListClass listClass);

    //Lista adatainak frissítése:
    @Update
    void updateList(ListClass listClass);

    //-----------------------------------//
    //Termékekkel kapcsolatos műveletek://
    //---------------------------------//

    //Termékek listájának lekérdezése:
    @Query("SELECT * FROM ProductClass")
    LiveData<List<ProductClass>> getAllProducts();

    //Listához tartozó termékek lekérdezése
    @Transaction
    @Query("SELECT * FROM ProductClass WHERE listID = :ID")
    LiveData<List<ProductClass>> getAllProductsOfList(int ID);

    //Termék beillesztése:
    @Insert
    void insertProduct(ProductClass productClass);

    //Termék törlése:
    @Delete
    void deleteProduct(ProductClass productClass);

    //Termék adatainak frissítése:
    @Update
    void updateProduct(ProductClass productClass);
}
