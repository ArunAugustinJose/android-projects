package com.example.acer.nsample2.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.example.acer.nsample2.MapsActivity;
import com.example.acer.nsample2.R;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ProfileEditActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private ImageView imgNext, imgPrevious, imgProfile;
    private TextView tvTitle;
    private LinearLayout li;
    private EditText etEmail, etName, etCountry, etState;

    Context context;

    int StringIndex = 0 ;
    String[] Row = {
            "Email",
            "Name",
            "Country",
            "State",
    };
    Animation animFadeIn,animFadeOut;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().hide();

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
        if (tvTitle.getText().toString().equals("Email")){
            imgPrevious.setVisibility(View.INVISIBLE);
        }


        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);


        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StringIndex == Row.length-1){
                    if (etState.getText().toString().isEmpty()) {
                        etState.setError("State is required");
                        etState.requestFocus();
                        return;
                    }
                    else{
                        startActivity(new Intent(ProfileEditActivity.this, MapsActivity.class));
                        ProfileEditActivity.this.finish();
                    }
                }
                else{
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
                        }
                        else {
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
                        }
                        else {
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
                        }
                        else {
                            nextView(v);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.startAnimation(animFadeOut);
                            tvTitle.setText(Row[++StringIndex]);
                            tvTitle.startAnimation(animFadeIn);
                        }
                    }
                    else {
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

                if(StringIndex == Row.length+1){

                    StringIndex = 0;

                    tvTitle.setText(Row[StringIndex]);
                    if (tvTitle.getText().toString().equals("Email")){
                        imgPrevious.setVisibility(View.INVISIBLE);
                    }
                    if (!tvTitle.getText().toString().equals("Email")){
                        imgPrevious.setVisibility(View.VISIBLE);
                    }
                    tvTitle.startAnimation(animFadeIn);
                }
                else{

                    tvTitle.setText(Row[--StringIndex]);
                    if (tvTitle.getText().toString().equals("Email")){
                        imgPrevious.setVisibility(View.INVISIBLE);
                    }
                    if (!tvTitle.getText().toString().equals("Email")){
                        imgPrevious.setVisibility(View.VISIBLE);
                    }
                    tvTitle.startAnimation(animFadeIn);
                }
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(ProfileEditActivity.this, R.layout.popup_photo_full, v, URL, null);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fltPhoto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to show image in full screen:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


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
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}