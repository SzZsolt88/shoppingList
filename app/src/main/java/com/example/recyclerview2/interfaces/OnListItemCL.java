package com.example.recyclerview2.interfaces;

import com.example.recyclerview2.Lists.ListClass;

/* lista elemre kattintás:
    röviden: célja az adott lista megmutatása
    hosszan: célja az adott lista kijelölése
*/

public interface OnListItemCL {
    void onListClick(ListClass list);
    void onListLongClick(ListClass list);
}
