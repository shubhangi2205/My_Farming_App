package com.example.farmingapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.farmingapp.databinding.LoginBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

//    Button b2  ;
//    TextView h1;
    EditText etPhoneNum;
    LoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        binding = DataBindingUtil.setContentView(this,R.layout.login);

//
//        b2 = findViewById(R.id.getotp);
//        etPhoneNum = findViewById(R.id.et_phone_num);
        binding.getotp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        if(binding.etPhoneNum.getText().toString().isEmpty() || binding.etPhoneNum.getText().toString().length() < 10){
                            etPhoneNum.setError("Enter a valid mobile");
                            etPhoneNum.requestFocus();
                            return;
                        }
                        Intent intent = new Intent(Login.this, OtpVerificationActivity.class);
                        intent.putExtra("phoneNum", "+91"+binding.etPhoneNum.getText().toString());
                        startActivity(intent);
                        //finish();
                    }
                }
        );

    //    h1 = findViewById(R.id.register_link);
        binding.registerLink.setOnClickListener(
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
