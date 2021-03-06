package com.example.recyclerview2.charts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyclerview2.R;

public class ChartActivity extends AppCompatActivity {
    Button recommendedProducts;
    Button pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.charts);
            recommendedProducts = findViewById(R.id.recommendedProductsBtn);
            pieChart = findViewById(R.id.pieChartButton);

            recommendedProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startBarChart = new Intent(ChartActivity.this, RecommendedProductsActivity.class);
                    startActivity(startBarChart);
                }
            });
            pieChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startPieChart = new Intent(ChartActivity.this, PieChartActivity.class);
                    startActivity(startPieChart);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Vásárlási szokások");
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
