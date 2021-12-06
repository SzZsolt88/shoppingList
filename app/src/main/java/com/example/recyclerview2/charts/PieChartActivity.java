package com.example.recyclerview2.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.StatisticDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PieChartActivity extends AppCompatActivity {
    private EditText yearInputEditText;
    private Spinner monthSpinner;
    private Button evaluationBtn;
    private StatisticDB statisticDB;
    private PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.charts_pie_chart);
        yearInputEditText = findViewById(R.id.yearInputEditText);
        monthSpinner = findViewById(R.id.monthSpinner);
        evaluationBtn = findViewById(R.id.evaluationBtn);
        pieChart = findViewById(R.id.pieChart);
        statisticDB = new StatisticDB();

        String  year = new SimpleDateFormat("yyyy").format(new Date());
        yearInputEditText.setText(year);
        pieChart.setNoDataText("A diagram megjelenítéséhez add meg a kívánt hónapot és évet!");

        String[] months = getResources().getStringArray(R.array.monthsList);
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,months);
        monthSpinner.setAdapter(adapterMonths);
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        monthSpinner.setSelection(month-1);

        evaluationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(yearInputEditText.getText().toString(),monthSpinner.getSelectedItemPosition()+1);
            }
        });

        statisticDB.getProductCategoryQuantityMutableLiveDate().observe(this, new Observer<List<Map<String, Long>>>() {
            @Override
            public void onChanged(List<Map<String, Long>> maps) {
                List<Map<String, Long>> statistic = new ArrayList<>();
                statistic = maps;
                createChart(statistic);
            }
        });


        super.onCreate(savedInstanceState);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        setTitle("Kördiagram");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancelActivity) {
            finish();
        }
        return true;
    }

    private void getData(String Year, int monthAdapterPosition) {
        String yearAndMonth = Year + monthAdapterPosition;
        statisticDB.getProductCategoryQuantity(yearAndMonth);
    }

    private void createChart(List<Map<String, Long>> statistic) {
        if (statistic != null) {
            ArrayList<PieEntry> productGroups = new ArrayList<>();
            int sumOfProducts = 0;
            for (Map<String, Long> category : statistic) {
                for (Map.Entry<String, Long> data : category.entrySet()) {
                    if (data.getValue() != 0) {
                        productGroups.add(new PieEntry(data.getValue().intValue(), data.getKey()));
                        sumOfProducts += data.getValue();
                    }
                }
            }

            int[] colorList = new int[]{
                    Color.parseColor("#C570C5"),
                    Color.parseColor("#E0A2E0"),
                    Color.parseColor("#C0BFDD"),
                    Color.parseColor("#DDBF23"),
                    Color.parseColor("#E7D7E7"),
                    Color.parseColor("#DDBF75"),
                    Color.parseColor("#E7D712")};

            PieDataSet pieDataSet = new PieDataSet(productGroups, "Termékcsoportok");
            pieDataSet.setColors(colorList);
            pieDataSet.getValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(16f);

            PieData pieData = new PieData(pieDataSet);

            pieData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) Math.floor(value));
                }
            });

            //set the data
            pieChart.setData(pieData);
            //refresh the chart
            pieChart.invalidate();
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            //set center text
            pieChart.setCenterText("Vásárolt termékek: " + sumOfProducts);
            pieChart.animate();
        } else {
            pieChart.setData(null);
            pieChart.setNoDataText("A megadott hónapra nincs adat!");
            pieChart.invalidate();
        }

    }
}
