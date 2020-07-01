package com.pmschool.pmschoolelearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.pmschool.pmschoolelearning.R;

public class PhoneVerificationActivity extends AppCompatActivity {

    private TextInputEditText phoneNumberText;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        phoneNumberText = findViewById(R.id.phone_number_text);
        findViewById(R.id.phone_button_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumber();
            }
        });
    }

    private void verifyPhoneNumber(){
        if(TextUtils.isEmpty(phoneNumberText.getText()) || phoneNumberText.getText().length() != 10){
            Toast.makeText(this, "Please enter a 10 digits phone number", Toast.LENGTH_SHORT).show();
        }else{
            number = "+91"+phoneNumberText.getText().toString();
            Intent intent = new Intent(PhoneVerificationActivity.this, CodeVerificationActivity.class);
            intent.putExtra("phone_number", number);
            startActivity(intent);
        }
    }
}