package com.example.acer.nsample2.Login;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.acer.nsample2.MapsActivity;
import com.example.acer.nsample2.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.acer.nsample2.Login.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class SplashActivity  extends AppCompatActivity {
    String networkStatus;
    FirebaseAuth fbAuth;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();


        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            // Toast.makeText(SplashActivity.this, "Network Available", Toast.LENGTH_LONG).show();
            fbAuth = FirebaseAuth.getInstance();
            new CountDownTimer(1000, 200) {
                public void onFinish() {

                    if (fbAuth.getCurrentUser() != null) {

                        startActivity(new Intent(com.example.acer.nsample2.Login.SplashActivity.this, MapsActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(com.example.acer.nsample2.Login.SplashActivity.this, PhoneNumberActivity.class));

                    }
                    finish();
                }

                public void onTick(long millisUntilFinished) {
                }

            }.start();

        }
        else
        {


            final Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content), "You're Offline", Snackbar.LENGTH_INDEFINITE);
            final View view = mSnackBar.getView();
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.color.snak_red);
            TextView mainTextView = (TextView) (view).findViewById(android.support.design.R.id.snackbar_text);
            mainTextView.setTextColor(Color.WHITE);
            mSnackBar.show();



            IntentFilter intentFilter = new IntentFilter(com.example.acer.nsample2.Login.NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
            LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                    if(networkStatus == "connected")
                    {

                        view.setBackgroundResource(R.color.snak_green);

                        mSnackBar.setText("You're Online");
                        fbAuth = FirebaseAuth.getInstance();
                        new CountDownTimer(1000, 200) {
                            public void onFinish() {

                                if (fbAuth.getCurrentUser() != null) {

                                    startActivity(new Intent(com.example.acer.nsample2.Login.SplashActivity.this, MapsActivity.class));
                                }
                                else
                                {
                                    startActivity(new Intent(com.example.acer.nsample2.Login.SplashActivity.this, PhoneNumberActivity.class));

                                }
                                finish();
                            }

                            public void onTick(long millisUntilFinished) {
                            }

                        }.start();


                    }
                    // Toast.makeText(SplashActivity.this, "Network Status::::"+networkStatus, Toast.LENGTH_LONG).show();
                    // Snackbar.make(findViewById(R.id.activity_main), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
                }
            }, intentFilter);
        }

    }
}