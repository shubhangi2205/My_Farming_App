package com.example.farmingapp;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmingapp.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button savebutton  ;
    EditText name, village, phone;
    FirebaseFirestore db;
    TextView clicklogin;

    boolean isNameEntered = false;
    boolean isPhoneEntered = false;
    boolean isVillageEntered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        db = FirebaseFirestore.getInstance();
        savebutton = findViewById(R.id.savebutton1);
        name = findViewById(R.id.date);
        phone = findViewById(R.id.description);
        village = findViewById(R.id.description1);
        clicklogin = findViewById(R.id.clicklogin);

        LiveData<Boolean> shouldEnable = new MutableLiveData<>();


        name.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        isNameEntered = false;
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                               if(charSequence.toString().isEmpty()){

                               } else {

                               }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        isNameEntered = true;
                    }
                }
        );

        phone.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        isPhoneEntered = false;
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.toString().isEmpty()){

                        } else {

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        village.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        isVillageEntered = false;
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.toString().isEmpty()){

                        } else {

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        isVillageEntered = true;
                    }
                }
        );

        clicklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                String Village = village.getText().toString();

                if(Name.isEmpty()){
                    //Name.setError("Required");
                    //Name.requestFocus();
                    name.setError("Required");
                    return;
                }
                if(Phone.isEmpty()){
                    phone.setError("Required");
                    return;
                }
                if(Village.isEmpty()){
                    village.setError("Required");
                    return;
                }
               // b1.setEnabled(!Name.isEmpty() && !Phone.isEmpty() && !Village.isEmpty());
                Utils.name = Name;
                Utils.phoneNum = Phone;
                Utils.village = Village;

                Intent i = new Intent(Register.this,Login.class);
//                i.putExtra("phoneNum", phone.getText().toString());
//                Utils.phoneNum = phone.getText().toString();
                startActivity(i);
            }
        });




    }
}