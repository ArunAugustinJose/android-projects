package com.example.hp.mitsogo.Device;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hp.mitsogo.R;

public class DeviceInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Device");

        // Get device information
        String osVersion = System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")\n";
        String apiLevel = android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")\n";
        String device = android.os.Build.DEVICE + "\n";
        String model = android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")\n";

        TextView tdevice = (TextView) findViewById(R.id.TxtDevice);
        TextView tname = (TextView) findViewById(R.id.txtName);
        TextView tos = (TextView) findViewById(R.id.txtOS);
        TextView tapi = (TextView) findViewById(R.id.txtAPI);

        tdevice.setText("Device: "+device);
        tname.setText("Model: "+model);
        tos.setText("OS version: "+osVersion);
        tapi.setText("API Level: "+apiLevel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
