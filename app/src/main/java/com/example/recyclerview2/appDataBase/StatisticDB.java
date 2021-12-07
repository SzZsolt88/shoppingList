package com.example.recyclerview2.appDataBase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticDB extends AbstractFireStoreInstance {
    private final FirebaseFirestore fStore;
    private final FirebaseAuth fAuth;
    private final CollectionReference statisticCollectionRef;

    private final List<Map<String,Long>> productCategoryQuantity = new ArrayList<>();
    private final MutableLiveData<List<Map<String,Long>>> productCategoryQuantityMutableLiveDate;

    private final List<String> listOfRecommendedProducts = new ArrayList<>();
    private final MutableLiveData<List<String>> lisOfRecommendedProductsLiveData = new MutableLiveData<>();

    public StatisticDB() {
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        statisticCollectionRef = fStore.collection(COLLECTION_OF_USERS).document(fAuth.getCurrentUser().getEmail()).collection(COLLECTION_OF_STATISTICS);
        productCategoryQuantityMutableLiveDate = new MutableLiveData<>();
    }

    public void getProductCategoryQuantity(String yearAndMonth) {
        DocumentReference statisticRef = statisticCollectionRef.document(yearAndMonth);
        statisticRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot statisticData = task.getResult();
                    productCategoryQuantity.clear();
                    if (statisticData.exists()) {
                        Map<String, Long> other = new HashMap<>();
                        other.put(CATEGORY_OTHER, (Long) statisticData.get(CATEGORY_OTHER + ".quantity"));

                        Map<String, Long> meat = new HashMap<>();
                        meat.put(CATEGORY_MEAT, (Long) statisticData.get(CATEGORY_MEAT + ".quantity"));

                        Map<String, Long> bakery = new HashMap<>();
                        bakery.put(CATEGORY_BAKERY, (Long) statisticData.get(CATEGORY_BAKERY + ".quantity"));

                        Map<String, Long> beverage = new HashMap<>();
                        beverage.put(CATEGORY_BEVERAGE, (Long) statisticData.get(CATEGORY_BEVERAGE + ".quantity"));

                        Map<String, Long> dairy = new HashMap<>();
                        dairy.put(CATEGORY_DAIRY, (Long) statisticData.get(CATEGORY_DAIRY + ".quantity"));

                        Map<String, Long> fruitAndVeg = new HashMap<>();
                        fruitAndVeg.put(CATEGORY_FRUIT_AND_VEG, (Long) statisticData.get(CATEGORY_FRUIT_AND_VEG + ".quantity"));

                        productCategoryQuantity.add(other);
                        productCategoryQuantity.add(fruitAndVeg);
                        productCategoryQuantity.add(meat);
                        productCategoryQuantity.add(beverage);
                        productCategoryQuantity.add(bakery);
                        productCategoryQuantity.add(dairy);

                        productCategoryQuantityMutableLiveDate.postValue(productCategoryQuantity);
                    }
                    else {
                        productCategoryQuantityMutableLiveDate.postValue(null);
                    }
                }
            }
        });
    }

    public MutableLiveData<List<Map<String,Long>>> getProductCategoryQuantityMutableLiveDate() {return productCategoryQuantityMutableLiveDate; }

    public void getRecommendedProducts() {
        DocumentReference getBuyStatistic = statisticCollectionRef.document(PRODUCT_STATISTICS);
        getBuyStatistic.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        Date today = ProductDB.getTodayDate();
                        DocumentSnapshot buyStat = task.getResult();
                        Map<String, Object> listOfProducts = buyStat.getData();
                        for (Map.Entry<String, Object> product : listOfProducts.entrySet()) {
                            HashMap<String, Object> productHash = (HashMap<String, Object>) product.getValue();
                            String lastBuyDate = String.valueOf(productHash.get(PRODUCT_LAST_BUY_DATE));
                            Long frequency = Long.valueOf(String.valueOf(productHash.get(PRODUCT_BUY_FREQUENCY)));
                            Long dateDiff = ProductDB.dateDiff(lastBuyDate, today);
                            if (frequency != 0 && frequency < dateDiff) {
                                listOfRecommendedProducts.add(String.valueOf(productHash.get(PRODUCT_NAME)));
                            }
                        }
                        lisOfRecommendedProductsLiveData.postValue(listOfRecommendedProducts);
                    }
                }
            }
        });

    }

    public MutableLiveData<List<String>> getLisOfRecommendedProductsLiveData() { return lisOfRecommendedProductsLiveData; }
}
