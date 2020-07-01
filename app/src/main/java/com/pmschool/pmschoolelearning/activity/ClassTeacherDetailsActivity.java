package com.pmschool.pmschoolelearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pmschool.pmschoolelearning.R;
import com.pmschool.pmschoolelearning.adapter.SubjectAdapter;
import com.pmschool.pmschoolelearning.model.SubjectModel;

import java.util.ArrayList;


public class ClassTeacherDetailsActivity extends AppCompatActivity {

    EditText first_name, last_name, class_name, section_name;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<SubjectModel> list;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher_details);
        firestore = FirebaseFirestore.getInstance();
        initComponent();
        initializeRecyclerView();
    }

    private void initComponent() {
        first_name = findViewById(R.id.et_first_name);
        last_name = findViewById(R.id.et_last_name);
        class_name = findViewById(R.id.et_class);
        section_name = findViewById(R.id.et_section);
        recyclerView = findViewById(R.id.recycler_view_subject);
        getSupportActionBar().setTitle("Teacher Details");

        findViewById(R.id.et_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClassDialog(view);
            }
        });

        findViewById(R.id.et_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSectionDialog(view);
            }
        });

        findViewById(R.id.et_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectDialog(view);
            }
        });

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void initializeRecyclerView() {
        list  = new ArrayList<>();

        list.add(new SubjectModel("Chemistry", "Class IX", "B"));
        list.add(new SubjectModel("Biology", "Class X", "A"));
        list.add(new SubjectModel("Chemistry", "Class IX", "A"));
        list.add(new SubjectModel( "Mathematics", "Class IX", "D"));
        list.add(new SubjectModel("Geography", "Class X", "C"));
        SubjectAdapter adapter = new SubjectAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void checkForm(){
        if(TextUtils.isEmpty(first_name.getText())){
            first_name.setError("Please enter field");
        }else if(TextUtils.isEmpty(last_name.getText())){
            last_name.setError("Please enter field");
        }else if(TextUtils.isEmpty(class_name.getText())){
            class_name.setError("Please enter field");
        }else if(TextUtils.isEmpty(section_name.getText())){
            section_name.setError("Please enter field");
        }else{
            Intent intent = new Intent(ClassTeacherDetailsActivity.this, MainActivity.class);
            intent.putExtra("first_name", first_name.getText().toString());
            intent.putExtra("last_name", last_name.getText().toString());
            intent.putExtra("class_name", class_name.getText().toString());
            intent.putExtra("section_name", section_name.getText().toString());

            startActivity(intent);
        }

    }

    private void showClassDialog(final View v) {
        final String[] classList = new String[]{"Class I","Class II","Class III", "Class IV",
                "Class V", "Class VI", "Class VII",
                "Class VIII", "Class IX", "Class X"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Class");
        builder.setSingleChoiceItems(classList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(classList[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showSectionDialog(final View v) {
        final String[] sectionList = new String[]{"A", "B", "C", "D"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Section");
        builder.setSingleChoiceItems(sectionList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(sectionList[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showSubjectDialog(final View v) {
        final String[] subjectList = new String[]{"Physics", "Maths", "Chemistry", "Geography", "Biology"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subject");
        builder.setSingleChoiceItems(subjectList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(subjectList[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_done){
            checkForm();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_subject);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.findViewById(R.id.et_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClassDialog(view);
            }
        });

        dialog.findViewById(R.id.et_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSectionDialog(view);
            }
        });
        dialog.findViewById(R.id.et_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectDialog(view);
            }
        });

    }
}