package com.example.farmingapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button b2  ;
    TextView h1;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.login);

        b2 = findViewById(R.id.getotp);
        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        Intent i2 = new Intent(Login.this,PlotDetails.class);
                        startActivity(i2);
                        //finish();
                    }
                }
        );

        h1 = findViewById(R.id.register_link);
        h1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        Intent i2 = new Intent(Login.this,Register.class);
                        startActivity(i2);
                        //finish();
                    }
                }
        );

    }
}
