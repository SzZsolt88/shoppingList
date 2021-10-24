package com.example.recyclerview2.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyclerview2.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.charts_pie_chart);
        PieChart pieChart = findViewById(R.id.pieChart);
        super.onCreate(savedInstanceState);

        ArrayList<PieEntry> productGroups = new ArrayList<>();
        productGroups.add(new PieEntry(400, "Gyümölcs"));
        productGroups.add(new PieEntry(300, "Tejtermék"));
        productGroups.add(new PieEntry(200, "Ital"));
        productGroups.add(new PieEntry(200, "Üditő"));
        productGroups.add(new PieEntry(300, "Pékárú"));
        productGroups.add(new PieEntry(100, "Háztartási"));
        productGroups.add(new PieEntry(500, "Egyéb"));

        int[] colorList = new int[]{Color.parseColor("#C570C5"),
                Color.parseColor("#E0A2E0"),
                Color.parseColor("#DDBFDD"),
                Color.parseColor("#DDBF23"),
                Color.parseColor("#E7D7E7"),
                Color.parseColor("#DDBF75"),
                Color.parseColor("#E7D712")};


        PieDataSet pieDataSet = new PieDataSet(productGroups, "Termékcsoportok");
        pieDataSet.setColors(colorList);
        pieDataSet.getValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setCenterText("Termékcsoportok");
        pieChart.animate();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Torta Diagram");
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
