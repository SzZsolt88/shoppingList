package com.example.recyclerview2.charts;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.StatisticDB;

import java.util.ArrayList;
import java.util.List;


public class RecommendedProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.recommended_products_activity);

        RecyclerView recyclerViewRecommendedProducts = findViewById(R.id.recommendedProductsRV);
        recyclerViewRecommendedProducts.setHasFixedSize(true);
        recyclerViewRecommendedProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecommendedProducts.setItemAnimator(new DefaultItemAnimator());

        RecommendedProductAdapter rPAdapter = new RecommendedProductAdapter();
        recyclerViewRecommendedProducts.setAdapter(rPAdapter);

        StatisticDB statisticDB = new StatisticDB();
        statisticDB.getLisOfRecommendedProductsLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> recommendedProductsList) {
                rPAdapter.setRecommendedProductsList(recommendedProductsList);
            }
        });

        statisticDB.getRecommendedProducts();

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Ajánlott termékek");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancelActivity) {
            finish();
        }
        return true;
    }

}
