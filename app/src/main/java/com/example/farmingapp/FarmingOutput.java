package com.example.farmingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.farmingapp.databinding.*;
import com.example.farmingapp.models.CropModel;
import com.example.farmingapp.models.ExpenseModel;
import com.example.farmingapp.models.FarmingOutputModel;
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

public class FarmingOutput extends AppCompatActivity {
    FirebaseFirestore db;
    final String TAG = "firestore";


    FarmingOutputBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.farming_output);
        db = FirebaseFirestore.getInstance();

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
        binding.spCrop.setAdapter(cropAdapter);
        setPlotAdapter(plotAdapter);
        setCropAdapter(cropAdapter);

        binding.saveFarmingData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        FarmingOutputModel farmingOutputModel = new FarmingOutputModel(
                binding.spYear.getSelectedItem().toString(),
                binding.spSeason.getSelectedItem().toString(),
                binding.spPlot.getSelectedItem().toString(),
                Integer.parseInt(binding.etYield.getText().toString()),
                binding.spCrop.getSelectedItem().toString()
        );
                String Yield = binding.etYield.getText().toString();
                if(Yield.isEmpty()){
                    //Name.setError("Required");
                    //Name.requestFocus();
                    binding.etYield.setError("Required");
                    return;
                }
        saveFarmingOutputDetails(farmingOutputModel);
        //getFarmingOutputDetails();
//                Intent intent = new Intent(FarmingOutput.this, HomePage.class);
//                startActivity(intent);

            }
        });


    }

    void saveFarmingOutputDetails(FarmingOutputModel farmingOutputModel){
        Map<String, Object> farmingOutputMap = farmingOutputModel.mapAllTheData();
        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            QuerySnapshot result = task.getResult();
                            for (QueryDocumentSnapshot document : result) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNum = (String) document.get("Phone Number");
                                Log.d("chkPhnNum2", Utils.phoneNum);
                                if(Objects.equals(phoneNum, Utils.phoneNum)

                                ) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("farming_output")
                                    .add(farmingOutputMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Data Saved at farming_output successfully");
                                            Intent intent = new Intent(FarmingOutput.this, HomePage.class);
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

    void getFarmingOutputDetails(){

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
                                    .collection("farming_output")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ArrayList<String> cropNameList = new ArrayList<>();
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());


                                                }

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    };

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

    private void setCropAdapter(ArrayAdapter<String> cropAdapter) {

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


}