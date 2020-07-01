package com.pmschool.pmschoolelearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pmschool.pmschoolelearning.R;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                    Intent intent = new Intent(MainActivity.this, PhoneVerificationActivity.class);
                    startActivity(intent);
                    finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
             db.collection("subject_teacher")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                   Log.d(TAG, "onComplete: " + snapshot.toString());
                                }
                            }else{
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
//            Intent intent = new Intent(MainActivity.this, PhoneVerificationActivity.class);
//            startActivity(intent);
        }else{
            Intent intent = new Intent(MainActivity.this, PhoneVerificationActivity.class);
            startActivity(intent);
            finish();
        }

    }
}