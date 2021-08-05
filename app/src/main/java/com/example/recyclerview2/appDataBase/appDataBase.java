package com.example.recyclerview2.appDataBase;

import android.content.Context;
import com.example.recyclerview2.listsShoppingListClass;
import com.example.recyclerview2.shoppingListProductClass;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {listsShoppingListClass.class, shoppingListProductClass.class}, version = 1, exportSchema = false)
public abstract class appDataBase extends RoomDatabase {
    public abstract listsShoppingListDao shoppingListDAO();
    public abstract shoppingListProductDao productDao();
    private static appDataBase INSTANCE;

    public static appDataBase getDbInstance(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), appDataBase.class,"Products")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
