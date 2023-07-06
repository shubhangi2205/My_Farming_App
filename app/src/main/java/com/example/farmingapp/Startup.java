package com.example.farmingapp;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Startup extends AppCompatActivity {

    Button b1  ;
    Button b2  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        Intent i = new Intent(Startup.this,AdminActivity.class);
        long delayMillis;
        //int i1 = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
            }
        }, delayMillis=1000);

//        b1 = findViewById(R.id.savebutton1);
//        b1.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(Startup.this,Register.class);
//                        startActivity(i);
//                    }
//                }
//        );
//
//        b2 = findViewById(R.id.getotp);
//        b2.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(Startup.this,Login.class);
//                        startActivity(i);
//                    }
//                }
//        );

    }
}