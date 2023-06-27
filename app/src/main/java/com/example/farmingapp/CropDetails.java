package com.example.farmingapp;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CropDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button b4  ;
    EditText season, plot, crop, sowing_area;
    Spinner year;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.cropdetails);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        db = FirebaseFirestore.getInstance();
        season = findViewById(R.id.editAreaName);
        crop = findViewById(R.id.editArea);
        plot = findViewById(R.id.editLocation);
        sowing_area = findViewById(R.id.editSowing);
        year = findViewById(R.id.spinner);
        b4 = findViewById(R.id.savebutton1);
        
        b4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v4) {

                        String Year = year.getSelectedItem().toString();
                        String Season = season.getText().toString();
                        String Plot = plot.getText().toString();
                        String Crop = crop.getText().toString();
                        String Showing_area = sowing_area.getText().toString();
                        Map<String,Object> crop = new HashMap<>();
                        crop.put("Year",year);
                        crop.put("Season",season);
                        crop.put("Plot",plot);
                        crop.put("Crop",crop);
                        crop.put("Showing_area",sowing_area);

                        db.collection("crop")
                                .add(crop)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(CropDetails.this,"Details saved",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {

                                        Toast.makeText(CropDetails.this,"Failed",Toast.LENGTH_SHORT).show();


                                    }
                                });
                        Intent i4 = new Intent(CropDetails.this,PlotDetails.class);
                        startActivity(i4);
                        //finish();
                    }
                }
        );

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
