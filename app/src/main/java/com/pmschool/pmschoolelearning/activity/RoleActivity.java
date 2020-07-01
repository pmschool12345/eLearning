package com.pmschool.pmschoolelearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pmschool.pmschoolelearning.R;

public class RoleActivity extends AppCompatActivity {

    Button button_yes, button_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        button_yes = findViewById(R.id.button_role_yes);
        button_no = findViewById(R.id.button_role_no);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(RoleActivity.this, ClassTeacherDetailsActivity.class);
                startActivity(intent);
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoleActivity.this, SubjectTeacherDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
