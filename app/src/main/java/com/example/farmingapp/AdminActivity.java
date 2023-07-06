package com.example.farmingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        b1 = findViewById(R.id.button_admin);
        b2= findViewById(R.id.button_farmer);

        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        Intent intent = new Intent(AdminActivity.this, Login.class);
                        startActivity(intent);
                        //finish();
                    }
                }
        );

        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        Intent intent = new Intent(AdminActivity.this, Login.class);
                        startActivity(intent);
                        //finish();
                    }
                }
        );
    }
}