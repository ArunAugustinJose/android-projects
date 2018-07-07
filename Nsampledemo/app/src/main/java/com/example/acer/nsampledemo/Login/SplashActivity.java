package com.example.acer.nsampledemo.Login;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.nsampledemo.MapsActivity;

import com.example.acer.nsampledemo.Profile.ProfileEditActivity;
import com.example.acer.nsampledemo.R;
import com.example.acer.nsampledemo.ServerConnection.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.acer.nsampledemo.Login.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class SplashActivity  extends AppCompatActivity {
    String networkStatus;
    FirebaseAuth fbAuth;
    SessionManager session;
    boolean f = false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        //session value
        session = new SessionManager(getApplicationContext());
        final String uid1 = session.getValue(SplashActivity.this, "uid");
        fbAuth = FirebaseAuth.getInstance();

        //firebase chekking for any user is there..

        new CountDownTimer(1000, 200) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
            if (fbAuth.getCurrentUser() != null) {
                ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            try {
                                if (snapshot.hasChild(uid1)) {
                                    if (fbAuth.getCurrentUser() != null) {
                                        startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(SplashActivity.this, PhoneNumberActivity.class));
                                        finish();
                                    }

                                } else {
                                    if (fbAuth.getCurrentUser() != null) {
                                        startActivity(new Intent(SplashActivity.this, ProfileEditActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(SplashActivity.this, PhoneNumberActivity.class));
                                        finish();
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else{

                    final Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content), "You're Offline", Snackbar.LENGTH_INDEFINITE);
                    final View view = mSnackBar.getView();
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    view.setBackgroundResource(R.color.snak_red);
                    mSnackBar.show();



                    IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
                    LocalBroadcastManager.getInstance(SplashActivity.this).registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                            networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                            if(networkStatus == "connected")
                            {

                                view.setBackgroundResource(R.color.snak_green);

                                mSnackBar.setText("You're Online");
                                DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
                                rootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot1) {

                                        try {
                                            if (snapshot1.hasChild(uid1)) {
//                                                if (fbAuth.getCurrentUser() != null) {
                                                    startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                                                    finish();
//                                                } else {
//                                                    startActivity(new Intent(SplashActivity.this, PhoneNumberActivity.class));
//                                                    finish();
//                                                }

                                            } else {
                                                if (fbAuth.getCurrentUser() != null) {
                                                    startActivity(new Intent(SplashActivity.this, ProfileEditActivity.class));
                                                    finish();
                                                } else {
                                                    startActivity(new Intent(SplashActivity.this, PhoneNumberActivity.class));
                                                    finish();
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }


                        }
                    }, intentFilter);
                }
            }

            }
        }.start();


    }
}