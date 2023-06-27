package com.example.farmingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PlotDetails extends AppCompatActivity {

    Button b3  ;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.plotdetails);

        b3 = findViewById(R.id.savebutton1);
        b3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v3) {
                        Intent i3 = new Intent(PlotDetails.this,CropDetails.class);
                        startActivity(i3);
                        //finish();
                    }
                }
        );

    }
}
