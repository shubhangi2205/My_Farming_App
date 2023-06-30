package com.example.farmingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    LinearLayout t1;
    TextView t2;
    TextView t3;
    TextView t4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        t1 = findViewById(R.id.expense);
        t2 = findViewById(R.id.harvest);
        t3 = findViewById(R.id.dashboard);
        t4 = findViewById(R.id.suggestion);

        t1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomePage.this,ExpenseActivity.class);
                        startActivity(i);
                    }
                }
        );


        t2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomePage.this,CropDetails.class);
                        startActivity(i);
                    }
                }
        );


        t3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomePage.this,CropDetails.class);
                        startActivity(i);
                    }
                }
        );

        t4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomePage.this,CropDetails.class);
                        startActivity(i);
                    }
                }
        );

    }
}