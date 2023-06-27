package com.example.farmingapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button b1  ;
    EditText name, village, phone;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        db = FirebaseFirestore.getInstance();
        b1 = findViewById(R.id.savebutton1);
        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        village = findViewById(R.id.editvillage);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                String Village = village.getText().toString();
                Map<String,Object> farmer = new HashMap<>();
                farmer.put("Name",Name);
                farmer.put("Phone Number",Phone);
                farmer.put("Village",Village);

                db.collection("farmer")
                        .add(farmer)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Register.this,"Details saved",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                                Toast.makeText(Register.this,"Failed",Toast.LENGTH_SHORT).show();


                            }
                        });
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });




    }
}