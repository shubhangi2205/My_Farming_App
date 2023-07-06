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


import com.example.farmingapp.databinding.ActivityExpenseBinding;
import com.example.farmingapp.databinding.CropdetailsBinding;
import com.example.farmingapp.databinding.PlotdetailsBinding;
import com.example.farmingapp.models.CropModel;
import com.example.farmingapp.models.ExpenseModel;
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

public class ExpenseActivity extends AppCompatActivity {
   ActivityExpenseBinding binding;
   FirebaseFirestore db;
    final String TAG = "firestore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String phoneNum = getIntent().getStringExtra("phoneNum");
        binding = DataBindingUtil.setContentView(this,R.layout.activity_expense);
        db = FirebaseFirestore.getInstance();
        ArrayAdapter<String> plotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, R.id.txtSpinnerItemPlot);
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_crop, R.id.txtSpinnerItemCrop);
        binding.spinnerPlot.setAdapter(plotAdapter);
        binding.spinnerCrop.setAdapter(cropAdapter);
        setPlotAdapter(plotAdapter);
        setCropAdapter(cropAdapter);
        String[] items= new String[]{"Labour","Seeds","Fertilizers", "Electricity", "Water", "Transportation"};
        Spinner spinner = (Spinner) findViewById(R.id.spinnerTypeOfExpense);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, items);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        binding.tvPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseActivity.this, PlotDetails.class);
                //intent.putExtra("phoneNum", "+91"+phoneNum);
                startActivity(intent);
            }
        });
        binding.tvcrop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseActivity.this, CropDetails.class);
                //intent.putExtra("phoneNum", "+91"+phoneNum);
                startActivity(intent);
            }
        });

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
                        ExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                int month = (monthOfYear + 1);
                                String monthStr = String.format("%02d", month);
                                String dayStr = String.format("%02d", dayOfMonth);
                                binding.etDateOfExpense.setText(dayStr + "-" + monthStr + "-" + year);

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



        binding.saveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseModel expenseModel = new ExpenseModel(
                        binding.spinnerTypeOfExpense.getSelectedItem().toString(),
                        binding.etDateOfExpense.getText().toString(),
                        binding.description1.getText().toString(),
                        Integer.parseInt(binding.amt.getText().toString())
                );
                String DateOfExpense = binding.etDateOfExpense.getText().toString();
                String Desc = binding.description1.getText().toString();
                String Amount  = binding.amt.getText().toString();
                if(DateOfExpense.isEmpty()){
                    //Name.setError("Required");
                    //Name.requestFocus();
                    binding.etDateOfExpense.setError("Required");
                    return;
                }
                if(Desc.isEmpty()){
                    binding.description1.setError("Required");
                    return;
                }
                if(Amount.isEmpty()){
                    binding.amt.setError("Required");
                    return;
                }
                saveExpenseDetails(expenseModel);
                //getExpenseDetails();
                Intent intent = new Intent(ExpenseActivity.this, HomePage.class);
                startActivity(intent);

            }
        });



    }

    private void getExpenseDetails() {
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
                                if (Objects.equals(phoneNumber,phoneNum)) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("expense_details")
                                   // .whereEqualTo("ExpenseAmount", 600)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                                Log.d(TAG, "Data at expense_details retrieved successfully");
                                                Log.d(TAG, String.valueOf(expenseDetailsList));
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    }

    private void saveExpenseDetails(ExpenseModel expenseModel) {
        Map<String, Object> expenseDetailMap = expenseModel.mapAllTheData();
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
                                if(Objects.equals(phoneNumber,Utils.phoneNum)) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("expense_details")
                                    .add(expenseDetailMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Data Saved at expense_details successfully");
//                                        Intent intent = new Intent(ExpenseActivity.this, CropDetails.class);
//                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {

                                            Log.d(TAG, "Failed to Save data at expense_details");


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
                                                        cropAdapter.add("No Crop Found!");
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
    };
}