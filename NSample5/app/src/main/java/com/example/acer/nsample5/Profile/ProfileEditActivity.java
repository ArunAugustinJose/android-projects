package com.example.acer.nsample5.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.acer.nsample5.Map.MapsActivity;
import com.example.acer.nsample5.R;
import com.example.acer.nsample5.ServerConnection.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class ProfileEditActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private ImageView imgNext, imgPrevious, imgProfile;
    private TextView tvTitle;
    private LinearLayout li;
    private EditText etEmail, etName, etCountry, etState;
    SessionManager session;
    String uid, etEmail1, etName1, etCountry1, etState1,phno;
    Context context;

    int StringIndex = 0;
    String[] Row = {
            "Email",
            "Name",
            "Country",
            "State",
    };
    Animation animFadeIn, animFadeOut;
    private static int RESULT_LOAD_IMAGE = 1;

    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String path;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().hide();

        //get uid from session
        session = new SessionManager(getApplicationContext());
        uid = session.getValue(ProfileEditActivity.this, "uid");
        phno = session.getValue(ProfileEditActivity.this, "phno");

        //firebase chekking for any user is there..


        //transition animation
        overridePendingTransition(R.transition.slide_in, R.transition.slide_out);

        //firebase storage ref
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        database = FirebaseDatabase.getInstance();
        myRef =database.getReference(uid).child("Profile");
        viewFlipper = findViewById(R.id.view_flipper);
        imgNext = findViewById(R.id.imgNext);
        tvTitle = findViewById(R.id.tvTitle);
        imgPrevious = findViewById(R.id.imgPrevious);
        imgProfile = findViewById(R.id.imgProfile);
        li = findViewById(R.id.layoutProfile);

        etEmail = findViewById(R.id.EtEmail);
        etName = findViewById(R.id.EtName);
        etCountry = findViewById(R.id.EtCountry);
        etState = findViewById(R.id.EtState);

        li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        tvTitle.setText(Row[0]);
        if (tvTitle.getText().toString().equals("Email")) {
            imgPrevious.setVisibility(View.INVISIBLE);
        }


        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);


        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringIndex == Row.length - 1) {
                    if (etState.getText().toString().isEmpty()) {
                        etState.setError("State is required");
                        etState.requestFocus();
                        return;
                    } else {

                        uploadImage();

                        startActivity(new Intent(ProfileEditActivity.this, MapsActivity.class));
                        ProfileEditActivity.this.finish();
                    }
                } else {
                    //email validations
                    if (tvTitle.getText().equals("Email")) {
                        imgPrevious.setVisibility(View.INVISIBLE);
                        if (etEmail.getText().toString().isEmpty()) {
                            etEmail.setError("Email is required");
                            etEmail.requestFocus();
                            return;
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                            etEmail.setError("Please enter a valid email");
                            etEmail.requestFocus();
                            return;
                        } else {
                            nextView(v);
                            imgPrevious.setVisibility(View.VISIBLE);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.startAnimation(animFadeOut);
                            tvTitle.setText(Row[++StringIndex]);
                            tvTitle.startAnimation(animFadeIn);
                        }
                    }
                    //name validations
                    else if (tvTitle.getText().toString().equals("Name")) {
                        if (etName.getText().toString().isEmpty()) {
                            etName.setError("Name is required");
                            etName.requestFocus();
                            return;
                        }
                        if (etName.getText().toString().length() < 3) {
                            etName.setError("Please enter a valid name");
                            etName.requestFocus();
                            return;
                        } else {
                            nextView(v);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.startAnimation(animFadeOut);
                            tvTitle.setText(Row[++StringIndex]);
                            tvTitle.startAnimation(animFadeIn);
                        }
                    }
                    //country validations
                    else if (tvTitle.getText().toString().equals("Country")) {
                        if (etCountry.getText().toString().isEmpty()) {
                            etCountry.setError("Country is required");
                            etCountry.requestFocus();
                            return;
                        } else {
                            nextView(v);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.startAnimation(animFadeOut);
                            tvTitle.setText(Row[++StringIndex]);
                            tvTitle.startAnimation(animFadeIn);
                        }
                    } else {
                        nextView(v);
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.startAnimation(animFadeOut);

                        tvTitle.setText(Row[++StringIndex]);
                        if (tvTitle.getText().toString().equals("Email")) {
                            imgPrevious.setVisibility(View.INVISIBLE);
                        }
                        if (!tvTitle.getText().toString().equals("Email")) {
                            imgPrevious.setVisibility(View.VISIBLE);
                        }

                        tvTitle.startAnimation(animFadeIn);
                    }
                }
            }
        });

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousView(v);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.startAnimation(animFadeOut);

                if (StringIndex == Row.length + 1) {

                    StringIndex = 0;

                    tvTitle.setText(Row[StringIndex]);
                    if (tvTitle.getText().toString().equals("Email")) {
                        imgPrevious.setVisibility(View.INVISIBLE);
                    }
                    if (!tvTitle.getText().toString().equals("Email")) {
                        imgPrevious.setVisibility(View.VISIBLE);
                    }
                    tvTitle.startAnimation(animFadeIn);
                } else {

                    tvTitle.setText(Row[--StringIndex]);
                    if (tvTitle.getText().toString().equals("Email")) {
                        imgPrevious.setVisibility(View.INVISIBLE);
                    }
                    if (!tvTitle.getText().toString().equals("Email")) {
                        imgPrevious.setVisibility(View.VISIBLE);
                    }
                    tvTitle.startAnimation(animFadeIn);
                }
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  new PhotoFullPopupWindow(ProfileEditActivity.this, R.layout.popup_photo_full, v, URL, null);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fltPhoto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to show image in full screen:
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
                chooseImage();
            }
        });

    }


    public void previousView(View v) {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right_tv);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left_tv);
        viewFlipper.showPrevious();
    }

    public void nextView(View v) {
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.showNext();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
//                            final ProgressDialog progressDialog = new ProgressDialog(this);
//                            progressDialog.setTitle("Uploading...");
//                            progressDialog.show();
            path="images/" + UUID.randomUUID().toString();
            final StorageReference ref = storageReference.child(path);

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           // path =taskSnapshot.getMetadata().getDownloadUrl();

                            //progressDialog.dismiss();
                            //Toast.makeText(ProfileEditActivity.this, "Uploaded"+path, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // progressDialog.dismiss();
                            // Toast.makeText(ProfileEditActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }

                    });
        }
        etEmail1 = etEmail.getText().toString();
        etName1 = etName.getText().toString();
        etCountry1 = etCountry.getText().toString();
        etState1 = etState.getText().toString();
        addProfile user= new addProfile(etName1,etEmail1,path,etState1,etCountry1,phno);
        myRef.setValue(user);
    }
}