package com.example.recyclerview2.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyclerview2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.charts_bar_chart);
        BarChart barChart = findViewById(R.id.barChart);
        super.onCreate(savedInstanceState);

        ArrayList<BarEntry> products = new ArrayList<>();
        products.add(new BarEntry(40, 50));
        products.add(new BarEntry(60, 70));
        products.add(new BarEntry(80, 90));
        products.add(new BarEntry(90, 100));


        BarDataSet barDataSet = new BarDataSet(products, "Products");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        //barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Termékek Oszlop Diagram");
        barChart.animateY(2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Oszlop Diagram");
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
