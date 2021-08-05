package com.example.recyclerview2.appDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.recyclerview2.listsShoppingListClass;

import java.util.List;

@Dao
public interface listsShoppingListDao {

    @Query("SELECT * FROM listsShoppingListClass")
    LiveData<List<listsShoppingListClass>> getAllList();

    @Insert
    void insertList(listsShoppingListClass listsShoppingListClass);

    @Delete
    void deleteList(listsShoppingListClass listsShoppingListClass);

    @Update
    void updateList(listsShoppingListClass listsShoppingListClass);
}
