package com.example.farmingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CropDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button b4  ;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.cropdetails);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        b4 = findViewById(R.id.savebutton1);
        b4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v4) {
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
