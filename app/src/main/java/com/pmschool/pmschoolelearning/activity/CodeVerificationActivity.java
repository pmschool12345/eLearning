package com.pmschool.pmschoolelearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pmschool.pmschoolelearning.R;

import java.util.concurrent.TimeUnit;

public class CodeVerificationActivity extends AppCompatActivity {

    private static final String TAG = "CodeVerificationActivit";
    private String verificationId;
    private FirebaseAuth mAuth;
    private TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.editText_code);
        final String number = getIntent().getStringExtra("phone_number");
        sendVerificationCode(number);
        findViewById(R.id.button_verify).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                String code = editText.getText().toString().trim();
                if(code.isEmpty() || code.length() < 6){
                    editText.setError("Enter Code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
           }
       });

        findViewById(R.id.resend_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(number);
            }
        });

    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                20,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(CodeVerificationActivity.this,RoleActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(CodeVerificationActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
          if(code != null){
              editText.setText(code);
              verifyCode(code);
          }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(CodeVerificationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}