package com.example.farmingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.example.farmingapp.databinding.CropdetailsBinding;
import com.example.farmingapp.databinding.PlotdetailsBinding;
import com.example.farmingapp.models.CropModel;
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
import java.util.Map;
import java.util.Objects;

public class CropDetails extends AppCompatActivity {
    FirebaseFirestore db;
    final String TAG = "firestore";
    CropdetailsBinding binding;

//    Button b4  ;
//    EditText crop;
//    EditText sowing_area;
//    Spinner year,season,plot;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.cropdetails);
//
//        Spinner spinner = findViewById(R.id.spYear);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.years, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
//        db = FirebaseFirestore.getInstance();
//        season = findViewById(R.id.spSeason);
//        crop = findViewById(R.id.etCrop);
//        plot = findViewById(R.id.spinnerPlot);
//        sowing_area = findViewById(R.id.etSowingArea);
//        year = findViewById(R.id.spYear);
//        b4 = findViewById(R.id.saveCrop);
//
//        setContentView(R.layout.cropdetails);

        binding = DataBindingUtil.setContentView(this,R.layout.cropdetails);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.years, android.R.layout.simple_spinner_item);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        binding.spSeason.setAdapter(arrayAdapter);
//        binding.spSeason.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        db = FirebaseFirestore.getInstance();
        ArrayAdapter<String> plotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, R.id.txtSpinnerItemPlot);
        binding.spinnerPlot.setAdapter(plotAdapter);
        setPlotAdapter(plotAdapter);

        binding.saveCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropModel cropModel = new CropModel(
                        binding.etCrop.getText().toString(),
                        binding.spYear.getAdapter(),
                        binding.spSeason.getAdapter(),
                        Integer.parseInt(binding.etSowingArea.getText().toString()),
                        binding.spinnerPlot.getAdapter()
                );
                saveCropDetails(cropModel);
            }
        });

        //  getPlotDetails(plotModel);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }

    //saveCropDetails(cropModel);
        //getCropDetails(cropModel);


    void saveCropDetails(CropModel cropModel){
        Map<String, Object> cropDetailMap = cropModel.mapAllTheData();
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
                                    .collection("crop_details")
                                    .add(cropDetailMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Data Saved at crop_details successfully");
                                            Intent intent = new Intent(CropDetails.this, ExpenseActivity.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {

                                            Log.d(TAG, "Failed to Save data at crop_details");


                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void getCropDetails(CropModel cropModel){
        Map<String, Object> cropDetailMap = cropModel.mapAllTheData();
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
                                                    Log.d(TAG, document.getId() + " => " + document.get("cropName"));
                                                    //String listOfCrops = (String) document.get("cropName");
                                                    //Log.d(TAG, "Data at crop_details retrieved successfully");

                                                    cropNameList.add((String) document.get("cropName"));
                                                    //Log.d("List of crops", String.valueOf(cropNameList));


                                                }
                                                Log.d(
                                                        TAG, String.valueOf(cropNameList)
                                                );
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    };

    void updateCropDetails(){

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