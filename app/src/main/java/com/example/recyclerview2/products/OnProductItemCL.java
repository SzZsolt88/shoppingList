package com.example.recyclerview2.products;

import com.example.recyclerview2.appDataBase.ProductClass;

//Termékre kattintás, kijelölés és a "vásárlás" rögzítése
public interface OnProductItemCL {
    void onProductClick(ProductClass list);
    void saveCheckedStatus(ProductClass product);
}
