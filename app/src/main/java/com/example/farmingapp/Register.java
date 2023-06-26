package com.example.farmingapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    Button b1  ;
    EditText name;
    EditText village;
    TextView phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        b1 = findViewById(R.id.savebutton1);
        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        village = findViewById(R.id.editvillage);
        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Register.this,Login.class);
                        startActivity(i);
                    }
                }
        );

    }
}