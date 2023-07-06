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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.example.farmingapp.databinding.CropdetailsBinding;
import com.example.farmingapp.models.CropModel;
import com.example.farmingapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    final String TAG = "crop_details";
    CropdetailsBinding binding;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("chkPNumAtCrop", Utils.phoneNum);

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
//        ArrayAdapter<String> cropAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_crop, R.id.txtSpinnerItemCrop);
//        binding.spinnerCrop.setAdapter(cropAdapter);
//        setCropAdapter(cropAdapter);
//        setPlotAdapter(plotAdapter);
        String[] years= new String[]{"2018","2019","2020", "2021", "2022", "2023","2024"};
        Spinner spinner1 = (Spinner) findViewById(R.id.spYear);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, years);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
// Apply the adapter to the spinner
        spinner1.setAdapter(adapter);

        String[] season= new String[]{"Summer","Winter","Monsoon", "Spring", "Autumn"};
        Spinner spinner2 = (Spinner) findViewById(R.id.spSeason);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, season);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);


        ArrayAdapter<String> plotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, R.id.txtSpinnerItemPlot);
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_crop, R.id.txtSpinnerItemCrop);
        binding.spPlot.setAdapter(plotAdapter);

        setPlotAdapter(plotAdapter);
        //setCropAdapter(cropAdapter);

        binding.saveCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropModel cropModel = new CropModel(
                        binding.etCrop.getText().toString(),
                        binding.spYear.getSelectedItem().toString(),
                        //binding.etYear.getText().toString(),
                       binding.spSeason.getSelectedItem().toString(),
                        //binding.etSeason.getText().toString(),
                        Integer.parseInt(binding.etSowingArea.getText().toString()),
                      //  binding.spinnerCrop.getSelectedItem().toString()
                        binding.spPlot.getSelectedItem().toString()
                );
                String CropName = binding.etCrop.getText().toString();
                String ShowingArea = binding.etSowingArea.getText().toString();
                if(CropName.isEmpty()){
                    //Name.setError("Required");
                    //Name.requestFocus();
                    binding.etCrop.setError("Required");
                    return;
                }
                if(ShowingArea.isEmpty()){
                    binding.etSowingArea.setError("Required");
                    return;
                }

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
                        //String phoneNum = getIntent().getStringExtra("phoneNum");
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            QuerySnapshot result = task.getResult();
                            for (QueryDocumentSnapshot document : result) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNumber = (String) document.get("Phone Number");
                                Log.d("chkPhnNum1", phoneNumber);
                                Log.d("chkPhnNum2", Utils.phoneNum);
                                if(Objects.equals(phoneNumber,Utils.phoneNum)) {
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
                                if (Objects.equals(phoneNum, "\"7355303625\"")) {
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
                                if (Objects.equals(phoneNum, "7355303625")) {
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

                });}

    void setPlotAdapter(ArrayAdapter<String> plotAdapter){
        // Map<String, Object> plotDetailMap = plotModel.mapAllTheData();
        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //String phoneNum = getIntent().getStringExtra("phoneNum");
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
    }


}