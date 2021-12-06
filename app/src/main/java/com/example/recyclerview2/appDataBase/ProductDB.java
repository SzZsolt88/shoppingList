package com.example.recyclerview2.appDataBase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;

import com.example.recyclerview2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProductDB extends AbstractFireStoreInstance {
    private final FirebaseAuth fAuth;
    private final FirebaseFirestore fStore;
    private final List<ProductClass> productsList;
    private final MutableLiveData<List<ProductClass>> productsMutableLiveData;
    private final String ownerMail;
    private final String listID;
    private Source source;
    private List<String> fruitAndVegetables = new ArrayList<>();
    private List<String> bakery = new ArrayList<>();
    private List<String> beverage = new ArrayList<>();
    private List<String> dairy = new ArrayList<>();
    private List<String> meat = new ArrayList<>();
    private List<String> other = new ArrayList<>();
    private final List<String> listOfAvailableProducts = new ArrayList<>();
    private final MutableLiveData<String[]> listOfAvailableProductsMutableLiveData;

    public ProductDB(String ownerMail, String listID) {
        this.ownerMail = ownerMail;
        this.listID = listID;
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        productsList = new ArrayList<>();
        productsMutableLiveData = new MutableLiveData<>();
        listOfAvailableProductsMutableLiveData = new MutableLiveData<>();
        source = Source.CACHE;
        getProductClasses();
    }

    public void getAllProductsOfList(boolean connected) {
        if (connected) {
            source = Source.SERVER;
        } else {
            source = Source.CACHE;
        }
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().get(PRODUCT_ARRAY) != null) {
                        List<Map<String, ProductClass>> products = (List<Map<String, ProductClass>>) task.getResult().get(PRODUCT_ARRAY);
                        for (int i = 0; i<products.size(); i++) {
                            ProductClass productClass = new ProductClass(
                                    String.valueOf(products.get(i).get(PRODUCT_NAME)),
                                    String.valueOf(products.get(i).get(PRODUCT_QUANTITY)),
                                    String.valueOf(products.get(i).get(PRODUCT_QUANTITY_TYPE)),
                                    String.valueOf(products.get(i).get(PRODUCT_CATEGORY)),
                                    Boolean.parseBoolean(String.valueOf(products.get(i).get(PRODUCT_CHECKED_STATUS))));
                            productsList.add(productClass);
                        }
                        Collections.sort(productsList);
                        productsMutableLiveData.postValue(productsList);
                    }
                }
            }
        });
    }

    public void registerNewProduct(ProductClass productClass) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        DocumentReference categoryRef = fStore.collection(USERS).document(fAuth.getCurrentUser().getEmail()).collection(COLLECTION_OF_CATEGORY).document(COLLECTION_OF_CATEGORY);
        if (dairy.contains(productClass.getName())) {
            productClass.setProductCategory(CATEGORY_DAIRY);
        }
        else if (fruitAndVegetables.contains(productClass.getName())) {
            productClass.setProductCategory(CATEGORY_FRUIT_AND_VEG);
        }
        else if (bakery.contains(productClass.getName())) {
            productClass.setProductCategory(CATEGORY_BAKERY);
        }
        else if (beverage.contains(productClass.getName())) {
            productClass.setProductCategory(CATEGORY_BEVERAGE);
        }
        else if (meat.contains(productClass.getName())) {
            productClass.setProductCategory(CATEGORY_MEAT);
        }
        else {
            productClass.setProductCategory(CATEGORY_OTHER);
            categoryRef.update(CATEGORY_OTHER,FieldValue.arrayUnion(productClass.getName()));
        }
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayUnion(productClass));
        productsList.add(productClass);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
    }
    public void deleteProduct(ProductClass deleteProduct) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayRemove(deleteProduct));
        productsList.remove(deleteProduct);
        productsMutableLiveData.postValue(productsList);
    }
    public void updateProduct(ProductClass originalProduct, ProductClass alreadyUpdatedProduct, boolean isProductCategoryModified) {
        deleteProduct(originalProduct);
        if (!isProductCategoryModified) {
            registerNewProduct(alreadyUpdatedProduct);
        } else {
            DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
            listRef.update(PRODUCT_ARRAY, FieldValue.arrayUnion(alreadyUpdatedProduct));
            productsList.add(alreadyUpdatedProduct);
            Collections.sort(productsList);
            productsMutableLiveData.postValue(productsList);
            updateProductClasses(originalProduct.getProductCategory(), alreadyUpdatedProduct.getProductCategory(), alreadyUpdatedProduct.getName());
        }
    }
    public void saveCheckedStatus(ProductClass boughtProduct) {
        DocumentReference listRef = fStore.collection(USERS).document(ownerMail).collection(LISTS).document(listID).collection(COLLECTION_PRODUCTS).document(PRODUCTS_OF_LIST);
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayRemove(boughtProduct));
        productsList.remove(boughtProduct);
        boughtProduct.setChecked(!boughtProduct.isChecked());
        listRef.update(PRODUCT_ARRAY, FieldValue.arrayUnion(boughtProduct));
        productsList.add(boughtProduct);
        Collections.sort(productsList);
        productsMutableLiveData.postValue(productsList);
        saveToStatistic(boughtProduct);
    }

    public MutableLiveData<List<ProductClass>> getProductsMutableLiveData() {
        return productsMutableLiveData;
    }

    private void saveToStatistic(ProductClass boughtProduct) {
        String  statisticsID = new SimpleDateFormat("yyyyMM").format(new Date());
        DocumentReference statisticsRef = fStore.collection(USERS).document(ownerMail).collection(COLLECTION_OF_STATISTICS).document(statisticsID);
        statisticsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        createStatisticDoc(boughtProduct);
                    } else {
                        statisticsRef.update(boughtProduct.getProductCategory() + ".quantity", registerBuyIntoStatistic(document, boughtProduct));
                    }
                }
            }
        });

        if(boughtProduct.isChecked()) {
            ArrayList<Integer> addedindexes = new ArrayList<Integer>();
            ArrayList<StatisticsProductClass> productslist = new ArrayList<StatisticsProductClass>();
            DocumentReference StatisticsRef2 = fStore.collection(USERS).document(ownerMail).collection(COLLECTION_OF_STATISTICS).document(PRODUCT_STATISTICS);
            StatisticsRef2.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                    if(task2.isSuccessful()) {
                        if (task2.getResult().getData() != null) {
                            HashMap<String, Object> productslistdb = (HashMap<String, Object>) task2.getResult().getData();
                            int i = 0;
                            for (Map.Entry<String, Object> entry : productslistdb.entrySet()) {
                                HashMap<String, Object> producthashmap = (HashMap<String, Object>) entry.getValue();
                                StatisticsProductClass StatisticsProductClassIns = new StatisticsProductClass(
                                        String.valueOf(producthashmap.get("name")),
                                        String.valueOf(producthashmap.get("lastBuyDate")),
                                        Long.valueOf(String.valueOf(producthashmap.get("average"))));
                                System.out.println(entry.getKey() + ":" + entry.getValue());
                                i++;
                                if(StatisticsProductClassIns.getname().contentEquals(boughtProduct.getName())) {
                                    Date today = getTodayDate();
                                    String latestBuyDate = StatisticsProductClassIns.getlastBuyDate();
                                    Long dateDiff = dateDiff(latestBuyDate, today);
                                    String strToday = new SimpleDateFormat("yyyyMMdd").format(today);

                                    StatisticsProductClassIns.setlastBuyDate(strToday);
                                    StatisticsProductClassIns.setaverage(dateDiff);
                                    addedindexes.add(i);
                                }
                                productslist.add(StatisticsProductClassIns);
                            }
                        }
                    }
                }
            });

            DocumentReference StatisticsRef3 = fStore.collection(USERS).document(ownerMail).collection(COLLECTION_OF_STATISTICS).document(PRODUCT_STATISTICS);
            StatisticsRef3.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                    if (addedindexes.size() == 0) {
                        String buyDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
                        StatisticsProductClass product = new StatisticsProductClass(boughtProduct.getName(), buyDate, (long) 0);
                        productslist.add(product);
                    }
                    Map<String, Object> productsmap = new HashMap<>();
                    for (StatisticsProductClass productIns : productslist) {
                        productsmap.put(productIns.getname(), productIns);
                    }
                    StatisticsRef3.set(productsmap);
                }
        });
        }
    }

    private void createStatisticDoc(ProductClass boughtProduct) {
        String  statisticsID = new SimpleDateFormat("yyyyMM").format(new Date());
        DocumentReference StatisticsRef = fStore.collection(USERS).document(ownerMail).collection(COLLECTION_OF_STATISTICS).document(statisticsID);
        Map<String, Object> fruitandvegetables = new HashMap<>();
        fruitandvegetables.put(PRODUCT_QUANTITY, 0);

        Map<String, Object> bakery = new HashMap<>();
        bakery.put(PRODUCT_QUANTITY, 0);

        Map<String, Object> dairy = new HashMap<>();
        dairy.put(PRODUCT_QUANTITY, 0);

        Map<String, Object> beverage = new HashMap<>();
        beverage.put(PRODUCT_QUANTITY, 0);

        Map<String, Object> meat = new HashMap<>();
        meat.put(PRODUCT_QUANTITY, 0);

        Map<String, Object> other = new HashMap<>();
        other.put(PRODUCT_QUANTITY, 0);

        Map<String, Object> category = new HashMap<>();
        category.put(CATEGORY_FRUIT_AND_VEG, fruitandvegetables);
        category.put(CATEGORY_BAKERY, bakery);
        category.put(CATEGORY_DAIRY, dairy);
        category.put(CATEGORY_BEVERAGE, beverage);
        category.put(CATEGORY_MEAT, meat);
        category.put(CATEGORY_OTHER, other);

        StatisticsRef.set(category);
        if(boughtProduct.isChecked()) {
            StatisticsRef.update(boughtProduct.getProductCategory() + ".quantity", 1);
        }

    }

    private int registerBuyIntoStatistic(DocumentSnapshot document,ProductClass boughtProduct) {
        int quantity_buffer = document.get(boughtProduct.getProductCategory()+".quantity",Integer.TYPE);
        if(boughtProduct.isChecked()) {
            quantity_buffer++;
        } else {
            if(quantity_buffer > 0) {
                quantity_buffer--;
            }
        }
        return quantity_buffer;
    }

    private void getProductClasses() {
        DocumentReference categoryRef = fStore.collection(USERS).document(fAuth.getCurrentUser().getEmail()).collection(COLLECTION_OF_CATEGORY).document(COLLECTION_OF_CATEGORY);
        categoryRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot categoryDocRef = task.getResult();
                    fruitAndVegetables = (List<String>) categoryDocRef.get(CATEGORY_FRUIT_AND_VEG);
                    bakery = (List<String>) categoryDocRef.get(CATEGORY_BAKERY);
                    beverage = (List<String>) categoryDocRef.get(CATEGORY_BEVERAGE);
                    dairy = (List<String>) categoryDocRef.get(CATEGORY_DAIRY);
                    meat = (List<String>) categoryDocRef.get(CATEGORY_MEAT);
                    other = (List<String>) categoryDocRef.get(CATEGORY_OTHER);
                    listOfAvailableProductsMutableLiveData.postValue(setListOfAvailableProducts());
                }
            }
        });
    }

    private void updateProductClasses(String originalCategory, String newCategory, String productName) {
        DocumentReference categoryRef = fStore.collection(USERS).document(fAuth.getCurrentUser().getEmail()).collection(COLLECTION_OF_CATEGORY).document(COLLECTION_OF_CATEGORY);
        categoryRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    switch (originalCategory) {
                        case "Pékáru":
                            bakery.remove(productName);
                            break;
                        case "Gyümölcs és Zöldség":
                            fruitAndVegetables.remove(productName);
                            break;
                        case "Ital":
                            beverage.remove(productName);
                            break;
                        case "Tejtermék":
                            dairy.remove(productName);
                            break;
                        case "Hús":
                            meat.remove(productName);
                            break;
                        case "Egyéb":
                            other.remove(productName);
                            break;
                    }

                    switch (newCategory) {
                        case "Pékáru":
                            bakery.add(productName);
                            break;
                        case "Gyümölcs és Zöldség":
                            fruitAndVegetables.add(productName);
                            break;
                        case "Ital":
                            beverage.add(productName);
                            break;
                        case "Tejtermék":
                            dairy.add(productName);
                            break;
                        case "Hús":
                            meat.add(productName);
                            break;
                        case "Egyéb":
                            other.add(productName);

                            break;
                    }
                    categoryRef.update(originalCategory,FieldValue.arrayRemove(productName));
                    categoryRef.update(newCategory,FieldValue.arrayUnion(productName));

                }
            }
        });
    }

    private String[] setListOfAvailableProducts() {
        listOfAvailableProducts.addAll(fruitAndVegetables);
        listOfAvailableProducts.addAll(bakery);
        listOfAvailableProducts.addAll(beverage);
        listOfAvailableProducts.addAll(dairy);
        listOfAvailableProducts.addAll(meat);
        listOfAvailableProducts.addAll(other);
        return listOfAvailableProducts.toArray(new String[0]);
    }

    public MutableLiveData<String[]> getListOfAvailableProductsMutableLiveData() { return listOfAvailableProductsMutableLiveData; }

    public static Date getTodayDate() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        return now.getTime();
    }

    public static Long dateDiff(String strLatestBuyDate, Date today) {
        Date latestBuyDate = null;
        try {
            latestBuyDate = new SimpleDateFormat("yyyyMMdd").parse(strLatestBuyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return TimeUnit.DAYS.convert((today.getTime() - latestBuyDate.getTime()), TimeUnit.MILLISECONDS);
    }
}
