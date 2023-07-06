package com.example.farmingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.math.MathUtils;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.farmingapp.databinding.ActivityDashboardBinding;
import com.example.farmingapp.models.CropModel;
import com.example.farmingapp.models.HarvestModel;
import com.example.farmingapp.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    //TODO: Show profit and loss on a graph
    FirebaseFirestore db;
    final String TAG = "firestore";
    Button b1;
    ActivityDashboardBinding binding;

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        int btdb;
        b1=findViewById(R.id.btdb);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getExpenseDetails();
//                getHarvestDetails();
//                barChart.setOnChartValueSelectedListener(new barChartOnChartValueSelectedListener());
                  calculateProfitOrLoss();
               // calculateLoss();
            }
        });
    }

    Map<String, Long> getProfitLossMap(Map<String, Long> expenseMap, Map<String, Long> harvestMap){
        HashMap<String, Long> profitLossMap = new HashMap<>();
        Map<String, Long> iterativeMap;
        if(expenseMap.size() > harvestMap.size()){
            iterativeMap = expenseMap;
        } else {
            iterativeMap = harvestMap;
        }

        for (Map.Entry<String,Long> yearWiseEntry : iterativeMap.entrySet()){
            String key = yearWiseEntry.getKey();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                long profitOrLoss = harvestMap.getOrDefault(key,0l) - expenseMap.getOrDefault(key, 0l);
                profitLossMap.put(key,profitOrLoss);
            }
        }
        return profitLossMap;
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());

    }

    String getYearFromDate(String date){
        // date format is => 28-02-2023
        String year = date.substring(6);
        return year;
    }

    private void calculateProfitOrLoss() {
        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNum = (String) document.get("Phone Number");
                                if (Objects.equals(phoneNum, Utils.phoneNum)) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            // Will get all the available year
//                            ArrayList<String> yearList = new ArrayList<>();
                            // Expense Details refernce
                           CollectionReference expenseDetails =  db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("expense_details");

                            // harvest details reference
                            CollectionReference harvestDetails =  db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("harvest_details");


                                    expenseDetails.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Map<String,Long> expenseMap = new HashMap<>();
                                                Map<String,Long> harvestMap = new HashMap<>();
                                                QuerySnapshot querySnapshot = task.getResult();
                                                for (QueryDocumentSnapshot document : querySnapshot) {
                                                    String date = document.getString("DateOfExpense");
                                                    String year = getYearFromDate(date);
                                                    long expenseAmount = document.getLong("ExpenseAmount");
                                                    if(expenseMap.containsKey(year)){
                                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                            long totalExpense = expenseMap.getOrDefault(year,0l)+expenseAmount;
                                                            expenseMap.put(year, totalExpense);
                                                        }
                                                    } else {
                                                        expenseMap.put(year, expenseAmount);
                                                    }

                                                }
                                              //  Log.d(TAG, "expenseMap is" + " => " + expenseMap.toString());
                                                // calculating the harvest details
                                                harvestDetails.get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if(task.isSuccessful()){
                                                                    QuerySnapshot querySnapshot = task.getResult();
                                                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                                                        String date = document.getString("DateOfSell");
                                                                        String year = getYearFromDate(date);
                                                                        long sellingAmount = document.getLong("SellingAmount");
                                                                        if(harvestMap.containsKey(year)){
                                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                                                long totalExpense = harvestMap.getOrDefault(year,0l)+sellingAmount;
                                                                                harvestMap.put(year, totalExpense);
                                                                            }
                                                                        } else {
                                                                            harvestMap.put(year, sellingAmount);
                                                                        }
                                                                    }
                                                                    Log.d(TAG, "expenseMap is" + " => " + expenseMap.toString());
                                                                    Log.d(TAG, "harvestMap is" + " => " + harvestMap.toString());
                                                                    Map<String, Long> profitLossMap = getProfitLossMap(expenseMap,harvestMap);
                                                                    Log.d(TAG, "profitLossMap is" + " => " + profitLossMap.toString());
//                                                                    ArrayList<DataPoint> dataPoints = new ArrayList<>();
//                                                                    for (Map.Entry<String,Long> yearWiseEntry : profitLossMap.entrySet()){
//                                                                        dataPoints.add(new DataPoint(Integer.parseInt(yearWiseEntry.getKey()), yearWiseEntry.getValue()));
//                                                                    }
                                                                    ArrayList<BarEntry> dataPoints = new ArrayList<>();
                                                                    for (Map.Entry<String,Long> yearWiseEntry : profitLossMap.entrySet()){
                                                                        dataPoints.add(new BarEntry(Integer.parseInt(yearWiseEntry.getKey()), yearWiseEntry.getValue()));

                                                                    }
//                                                                    dataPoints.add(new BarEntry(2020, 14));
//                                                                    dataPoints.add(new BarEntry(2022, 23));
//                                                                    dataPoints.add(new BarEntry(2023, -45));

                                                                    barChart = findViewById(R.id.barChart_view);
                                                                    barChart.setFitBars(true);
                                                                    //Log.d(TAG, "data ", dataPoints.toString());


//                                                                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>((DataPoint[]) dataPoints.toArray());
                                                                    BarDataSet barDataSet = new BarDataSet(dataPoints,"Datapoints");
                                                                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                                                    barDataSet.setValueTextColor(Color.BLACK);
                                                                    barDataSet.setValueTextSize(16f);
                                                                    BarData barData = new BarData(barDataSet);
                                                                   // barChart.setFitBars(true);
                                                                    barChart.setData(barData);
                                                                    barChart.getDescription().setText("Bar Chart");
                                                                    //barChart.animateY(2000);
                                                                    barChart.animateXY(2000,2000);
//                                                                    binding.graph.addSeries(series);
//                                                                    initBarChart();
//                                                                    showBarChart();

                                                                } else {
                                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                            //Log.d(TAG, "Year list is" + " => " + yearList.toString());
                          //  yearList.forEach();
//                            //db.collection("crop_details")
//                            db.collection("farmer")
//                                    .document(farmerDocId)
//                                    .collection("expense_details")
//                                    //.whereEqualTo("date_of_expense".substring("date_of_expense".length()-4), "ense")
//                                   // .whereEqualTo("date_of_expense", "2023")
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            int sum_expense = 0;
//                                            ArrayList<Integer> profitLossList = new ArrayList<>();
//                                            if (task.isSuccessful()) {
//
//                                                QuerySnapshot querySnapshot = task.getResult();
//                                                for (QueryDocumentSnapshot document : querySnapshot) {
//                                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                                    Log.d(TAG, document.getId() + " => " + document.get("ExpenseAmount"));
//                                                    int expenseAmount = document.getLong("ExpenseAmount").intValue();
//                                                    sum_expense += expenseAmount;
////                                                    expenseDetailsList.add(expenseAmountList);
////                                                    Log.d("expenseAmountList", String.valueOf(expenseAmountList));
//
//                                                }
//                                                for (int amt : expenseDetailsList){
//                                                    Log.d("Item", String.valueOf(amt));
//                                                    sum_expense += amt;
//                                                }
//                                                Log.d(TAG, "Data at expense_details retrieved successfully");
//                                                Log.d(TAG, String.valueOf(expenseDetailsList));
//                                                Log.d("Expense_amt_2023", String.valueOf(sum_expense_2023));
//                                            } else {
//                                                Log.d(TAG, "Error getting documents: ", task.getException());
//                                            }
//                                        }
//                                    });
                        }
                    }

                });
    }

//    private void showBarChart(){
//        ArrayList<Double> valueList = new ArrayList<Double>();
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        String title = "Title";
//
//        //input data
//        for(int i = 0; i < 6; i++){
//            valueList.add(i * 100.1);
//        }
//
//        //fit the data into a bar
//        for (int i = 0; i < valueList.size(); i++) {
//            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
//            entries.add(barEntry);
//        }
//
//        BarDataSet barDataSet = new BarDataSet(entries, title);
//
//        BarData data = new BarData(barDataSet);
//        barChart.setData(data);
//        barChart.invalidate();
//        initBarDataSet(barDataSet);
//    }
//
//    private void initBarDataSet(BarDataSet barDataSet){
//        //Changing the color of the bar
//        barDataSet.setColor(Color.parseColor("#304567"));
//        //Setting the size of the form in the legend
//        barDataSet.setFormSize(15f);
//        //showing the value of the bar, default true if not set
//        barDataSet.setDrawValues(false);
//        //setting the text size of the value of the bar
//        barDataSet.setValueTextSize(12f);
//    }
//
//    private void initBarChart(){
//        //hiding the grey background of the chart, default false if not set
//        barChart.setDrawGridBackground(false);
//        //remove the bar shadow, default false if not set
//        barChart.setDrawBarShadow(false);
//        //remove border of the chart, default false if not set
//        barChart.setDrawBorders(false);
//
//        //remove the description label text located at the lower right corner
//        Description description = new Description();
//        description.setEnabled(false);
//        barChart.setDescription(description);
//
//        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
//        barChart.animateY(1000);
//        //setting animation for x-axis, the bar will pop up separately within the time we set
//        barChart.animateX(1000);
//
//        XAxis xAxis = barChart.getXAxis();
//        //change the position of x-axis to the bottom
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //set the horizontal distance of the grid line
//        xAxis.setGranularity(1f);
//        //hiding the x-axis line, default true if not set
//        xAxis.setDrawAxisLine(false);
//        //hiding the vertical grid lines, default true if not set
//        xAxis.setDrawGridLines(false);
//
//        YAxis leftAxis = barChart.getAxisLeft();
//        //hiding the left y-axis line, default true if not set
//        leftAxis.setDrawAxisLine(false);
//
//        YAxis rightAxis = barChart.getAxisRight();
//        //hiding the right y-axis line, default true if not set
//        rightAxis.setDrawAxisLine(false);
//
//        Legend legend = barChart.getLegend();
//        //setting the shape of the legend form to line, default square shape
//        legend.setForm(Legend.LegendForm.LINE);
//        //setting the text size of the legend
//        legend.setTextSize(11f);
//        //setting the alignment of legend toward the chart
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        //setting the stacking direction of legend
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        //setting the location of legend outside the chart, default false if not set
//        legend.setDrawInside(false);
//
//    }
//
//
//


    private void getExpenseDetails(String farmerDocID) {
        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNum = (String) document.get("Phone Number");
                                if (Objects.equals(phoneNum, Utils.phoneNum)) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("expense_details")
                                    //.whereEqualTo("date_of_expense".substring("date_of_expense".length()-4), "ense")
                                    .whereEqualTo("date_of_expense", "2023")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            int sum_expense_2023 = 0;
                                            ArrayList<Integer> expenseDetailsList = new ArrayList<>();
                                            if (task.isSuccessful()) {

                                                QuerySnapshot querySnapshot = task.getResult();
                                                for (QueryDocumentSnapshot document : querySnapshot) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    Log.d(TAG, document.getId() + " => " + document.get("ExpenseAmount"));
                                                    int expenseAmountList = document.getLong("ExpenseAmount").intValue();
                                                    expenseDetailsList.add(expenseAmountList);
                                                    Log.d("expenseAmountList", String.valueOf(expenseAmountList));

                                                }
                                                for (int amt : expenseDetailsList){
                                                    Log.d("Item", String.valueOf(amt));
                                                    sum_expense_2023 += amt;
                                                }
                                                Log.d(TAG, "Data at expense_details retrieved successfully");
                                                Log.d(TAG, String.valueOf(expenseDetailsList));
                                                Log.d("Expense_amt_2023", String.valueOf(sum_expense_2023));
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    }

    private void getHarvestDetails() {


        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String phoneNum = getIntent().getStringExtra("phoneNum");
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNumber = (String) document.get("Phone Number");
                                if (Objects.equals(phoneNumber,Utils.phoneNum)) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("harvest_details")
                                    .whereEqualTo("date_of_sell", "2023")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            int sum_harvest_2023 = 0;
                                            ArrayList<Integer> sellingAmountList = new ArrayList<>();
                                            if (task.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                for (QueryDocumentSnapshot document : querySnapshot) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    Log.d(TAG, document.getId() + " => " + document.get("SellingAmount"));
                                                    int sellingAmount = document.getLong("SellingAmount").intValue();
                                                    sellingAmountList.add(sellingAmount);
                                                    Log.d("sellingAmountList", String.valueOf(sellingAmountList));
                                                }
                                                for (int amt : sellingAmountList){
                                                    Log.d("Item", String.valueOf(amt));
                                                    sum_harvest_2023 += amt;
                                                }



                                                Log.d(TAG, "Data at harvest_details retrieved successfully");
                                                Log.d(TAG, String.valueOf(sellingAmountList));
                                                Log.d("Harvest_amt_2023", String.valueOf(sum_harvest_2023));
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    }
}