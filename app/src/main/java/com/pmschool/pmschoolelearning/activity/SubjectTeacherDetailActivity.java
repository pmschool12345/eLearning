package com.pmschool.pmschoolelearning.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pmschool.pmschoolelearning.R;
import com.pmschool.pmschoolelearning.adapter.SubjectAdapter;
import com.pmschool.pmschoolelearning.model.SubjectModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectTeacherDetailActivity extends AppCompatActivity {

    private final String TAG = "This is Testing";
    CircleImageView imageViewLoad;
    ImageButton imageButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    SubjectAdapter adapter;
    ArrayList<SubjectModel> subject_list;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextInputEditText first_name, last_name;
    FirebaseAuth auth;
    String subject,classes, section;
    FirebaseUser user;
    Uri uri;
    String downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_teacher_detail);
        imageViewLoad = findViewById(R.id.profile_image);
        imageButton = findViewById(R.id.imageButton);
        recyclerView = findViewById(R.id.recycler_view_subject);

        //Firebase reference
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();
        user = auth.getCurrentUser();

        first_name = findViewById(R.id.et_first_name);
        last_name = findViewById(R.id.et_last_name);

        getSupportActionBar().setTitle("Teacher Details");
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGalleryIntent();
            }
        });
        imageViewLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGalleryIntent();
            }
        });
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        subject_list = new ArrayList<>();
        adapter = new SubjectAdapter(subject_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void launchGalleryIntent() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                uri = result.getUri();
                imageViewLoad.setImageURI(uri);
            }

        } catch (Exception e) {
            Log.d(TAG, "onActivityResult: " + e.getMessage());
        }

    }

    private void uploadImage(){

        final StorageReference ref = storageReference.child("teachers").child(user.getUid()).child("profile_image").child("profile_image");
        if(uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri.toString();
                                    addNewTeacher(downloadUri);
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SubjectTeacherDetailActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    })

                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(SubjectTeacherDetailActivity.this, "Cannot Upload Photo", Toast.LENGTH_LONG).show();
                       }
                   })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot
                            .getTotalByteCount();
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void showDialog() {
        final TextInputEditText class_name, section_name, subject_name;
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_subject);

        class_name = dialog.findViewById(R.id.et_class);
        section_name = dialog.findViewById(R.id.et_section);
        subject_name = dialog.findViewById(R.id.et_subject);

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

        dialog.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try{
                  subject = subject_name.getText().toString();
                  classes = class_name.getText().toString();
                  section = section_name.getText().toString();
                  if(!subject.isEmpty() && !classes.isEmpty() && !section.isEmpty()){
                      subject_list.add(new SubjectModel(subject,classes, section));
                      adapter.notifyItemInserted(adapter.getItemCount());
                      recyclerView.scrollToPosition(adapter.getItemCount());
                      dialog.dismiss();
                  }
              }catch(Exception e){
                  Log.d(TAG, "onClick: Exception "+e.toString());
              }
                Log.d(TAG, "onClick: "+subject_list.size());
            }
        });

    }

    private void addNewTeacher(String imageUrl) {
        String fname = first_name.getText().toString();
        String lname = last_name.getText().toString();

        Map<String, Object> teacher_details = new HashMap<>();
        teacher_details.put("first_name", fname);
        teacher_details.put("last_name", lname);
        teacher_details.put("profile_url", imageUrl);
        teacher_details.put("phone_number", user.getPhoneNumber());



        firestore.collection("subject_teacher").document(user.getUid())
                .set(teacher_details)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for(int i=0;i<subject_list.size();i++){
                            subject = subject_list.get(i).getSubject();
                            section = subject_list.get(i).getSection();
                            classes = subject_list.get(i).getClass_name();
                            addNewSubject(classes, section, subject);
                        }
                        Intent intent = new Intent(SubjectTeacherDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SubjectTeacherDetailActivity.this,"Error occurred", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addNewSubject(String class_name, String section_name, String subject){
        Map<String, Object> subject_list = new HashMap<>();
        subject_list.put("class", class_name);
        subject_list.put("section", section_name);
        subject_list.put("subject", subject);

        firestore.collection("subject_teacher").document(user.getUid()).collection("subject_list")
                .document().set(subject_list)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SubjectTeacherDetailActivity.this, "Record Added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SubjectTeacherDetailActivity.this, "Record not Added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showClassDialog(final View v) {
        final String[] classList = new String[]{"Class I", "Class II", "Class III", "Class IV",
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
        if(item.getItemId() == R.id.action_done) {
           if(checkName()){
              uploadImage();
           }
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean checkName(){
        if(TextUtils.isEmpty(first_name.getText())){
            first_name.setError("Please enter field");
        }else if(TextUtils.isEmpty(last_name.getText())){
            last_name.setError("Please enter field");
        }else{
            return true;
        }
        return false;
    }
}