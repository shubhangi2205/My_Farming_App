package com.example.farmingapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class PlotDetails extends AppCompatActivity {

    Button b3  ;
    EditText area, area_name, location;
    CheckBox checkbox;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.plotdetails);

        db = FirebaseFirestore.getInstance();
        area_name = findViewById(R.id.editAreaName);
        area = findViewById(R.id.editArea);
        location = findViewById(R.id.editLocation);
        checkbox= findViewById(R.id.checkBox);
        b3 = findViewById(R.id.savebutton1);
        b3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v3) {

                        String Area_name = area_name.getText().toString();
                        String Area = area.getText().toString();
                        String Location = location.getText().toString();
                        String Landcheckbox = checkbox.getText().toString();
                        Map<String,Object> plot = new HashMap<>();
                        plot.put("Area_name",area_name);
                        plot.put("Area",area);
                        plot.put("Location",location);
                        plot.put("Landcheckbox",checkbox);

                        db.collection("plot")
                                .add(plot)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(PlotDetails.this,"Details saved",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {

                                        Toast.makeText(PlotDetails.this,"Failed",Toast.LENGTH_SHORT).show();


                                    }
                                });
                        Intent i3 = new Intent(PlotDetails.this,CropDetails.class);
                        startActivity(i3);
                        //finish();
                    }
                }
        );

    }
}
