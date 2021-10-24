package com.example.recyclerview2.interfaces;

import com.example.recyclerview2.ProductClass;

//Termékre kattintás, kijelölés és a "vásárlás" rögzítése
public interface OnProductItemCL {
    void onProductClick(ProductClass list);
    void saveCheckedStatus(ProductClass product);
}
