package com.example.farmingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.example.farmingapp.databinding.ActivityHarvestBinding;
import com.example.farmingapp.databinding.CropdetailsBinding;
import com.example.farmingapp.databinding.PlotdetailsBinding;
import com.example.farmingapp.models.CropModel;
import com.example.farmingapp.models.ExpenseModel;
import com.example.farmingapp.models.HarvestModel;
import com.example.farmingapp.models.PlotModel;
import com.example.farmingapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class HarvestActivity extends AppCompatActivity {
    ActivityHarvestBinding binding;
    FirebaseFirestore db;
    final String TAG = "firestore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_harvest);
        db = FirebaseFirestore.getInstance();
        ArrayAdapter<String> plotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, R.id.txtSpinnerItemPlot);
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_crop, R.id.txtSpinnerItemCrop);
        binding.spinnerPlot.setAdapter(plotAdapter);
        binding.spinnerCrop.setAdapter(cropAdapter);
        setPlotAdapter(plotAdapter);
        setCropAdapter(cropAdapter);

        binding.savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HarvestModel harvestModel = new HarvestModel(

                        binding.etdateofsell.getText().toString(),
                        Integer.parseInt(binding.etQuantity.getText().toString()),
                        Integer.parseInt(binding.etSellAmt.getText().toString())
                );
                saveHarvestDetails(harvestModel);
                getHarvestDetails(harvestModel);
                Intent intent = new Intent(HarvestActivity.this, HomePage.class);
                startActivity(intent);

            }
        });

        // on below line we are adding click listener for our pick date button
        binding.calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        HarvestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.etdateofsell.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    private void getHarvestDetails(HarvestModel harvestModel) {

        Map<String, Object> harvestDetailMap = harvestModel.mapAllTheData();
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
                                if (Objects.equals(phoneNum, "123568898")) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("harvest_details")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ArrayList<Integer> sellingAmountList = new ArrayList<>();
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                                    Log.d(TAG, document.getId() + " => " + document.get("SellingAmount"));
                                                    int sellingAmount = (int) document.get("SellingAmount");

                                                    sellingAmountList.add(sellingAmount);
                                                    //expenseDetailsList.add((String) document.get("ExpenseAmount"));
                                                    Log.d("sellingAmountList", String.valueOf(sellingAmountList));


                                                }
                                                Log.d(TAG, "Data at harvest_details retrieved successfully");
                                                Log.d(
                                                        TAG, String.valueOf(sellingAmountList)
                                                );
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    }

    private void saveHarvestDetails(HarvestModel harvestModel) {

        Map<String, Object> harvestDetailMap = harvestModel.mapAllTheData();
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
                                if(Objects.equals(phoneNum, "123568898")) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("harvest_details")
                                    .add(harvestDetailMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Data Saved at harvest_details successfully");
                                            Intent intent = new Intent(HarvestActivity.this, CropDetails.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {

                                            Log.d(TAG, "Failed to Save data at harvest_details");


                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setCropAdapter(ArrayAdapter<String> cropAdapter) {

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
                                if (Objects.equals(phoneNum, "123568898")) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("crop_details")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ArrayList<String> cropNameList = new ArrayList<>();
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(Utils.DB_TAG, document.getId() + " => " + document.get("cropName"));
                                                    //String listOfCrops = (String) document.get("cropName");
                                                    //Log.d(TAG, "Data at crop_details retrieved successfully");

                                                    cropNameList.add((String) document.get("cropName"));
                                                    if(cropNameList.isEmpty()){
                                                        cropAdapter.add("No Plot Found!");
                                                    } else {
                                                        cropAdapter.clear();
                                                        cropAdapter.addAll(cropNameList);
                                                    }
                                                    cropAdapter.notifyDataSetChanged();
                                                    //Log.d("List of crops", String.valueOf(cropNameList));


                                                }
                                                Log.d(
                                                        Utils.DB_TAG, String.valueOf(cropNameList)
                                                );
                                            } else {
                                                Log.d(Utils.DB_TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });

    }

    void setPlotAdapter(ArrayAdapter<String> plotAdapter){
        // Map<String, Object> plotDetailMap = plotModel.mapAllTheData();
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
                                if (Objects.equals(phoneNum, "123568898")) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("plot_details")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ArrayList<String> plotNameList = new ArrayList<>();
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(Utils.DB_TAG, document.getId() + " => " + document.get("plotName"));
                                                    //String listOfCrops = (String) document.get("cropName");
                                                    //Log.d(TAG, "Data at crop_details retrieved successfully");

                                                    plotNameList.add((String) document.get("plotName"));
                                                    if(plotNameList.isEmpty()){
                                                        plotAdapter.add("No Plot Found!");
                                                    } else {
                                                        plotAdapter.clear();
                                                        plotAdapter.addAll(plotNameList);
                                                    }
                                                    plotAdapter.notifyDataSetChanged();
                                                    //Log.d("List of crops", String.valueOf(cropNameList));


                                                }
                                                Log.d(
                                                        Utils.DB_TAG, String.valueOf(plotNameList)
                                                );
                                            } else {
                                                Log.d(Utils.DB_TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    };
}