package com.example.recyclerview2.lists;

/* lista elemre kattintás:
    röviden: célja az adott lista megmutatása
    hosszan: célja az adott lista kijelölése
*/

import com.example.recyclerview2.appDataBase.ListClass;

public interface OnListItemCL {
    void onListClick(ListClass list);
    void onListLongClick(ListClass list);
}
