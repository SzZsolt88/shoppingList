package com.example.recyclerview2.appDataBase;

import android.content.Context;

import com.example.recyclerview2.ListClass;
import com.example.recyclerview2.ProductClass;
import com.example.recyclerview2.user.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ListClass.class, ProductClass.class}, version = 1, exportSchema = false)
public abstract class LocalDB extends RoomDatabase {
    public abstract LocalDBDao localDBDao();
    private static LocalDB INSTANCE;

    public static LocalDB getDbInstance(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LocalDB.class,"shoppingList_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
