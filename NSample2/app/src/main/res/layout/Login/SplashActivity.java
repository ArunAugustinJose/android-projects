package com.example.acer.nsample.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.example.acer.nsample.MapsActivity;
import com.example.acer.nsample.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        fbAuth = FirebaseAuth.getInstance();
        new CountDownTimer(1000, 200) {
            public void onFinish() {

                if (fbAuth.getCurrentUser() != null) {

                   startActivity(new Intent(com.example.acer.nsample.Login.SplashActivity.this, MapsActivity.class));
                }
                else
                {
                     startActivity(new Intent(com.example.acer.nsample.Login.SplashActivity.this, PhoneNumberActivity.class));

                }
                finish();
            }

            public void onTick(long millisUntilFinished) {
            }

        }.start();
    }

}
