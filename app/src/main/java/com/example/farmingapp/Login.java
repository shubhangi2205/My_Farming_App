package com.example.farmingapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Login extends AppCompatActivity {

    Button b2  ;

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

    }
}
