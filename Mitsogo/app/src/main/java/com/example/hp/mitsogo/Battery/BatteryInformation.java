package com.example.hp.mitsogo.Battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hp.mitsogo.R;


public class BatteryInformation extends AppCompatActivity {
    private Context mContext;

    private TextView mTextViewInfo;
    private TextView mTextViewPercentage;
    private ProgressBar mProgressBar;

    private int mProgressStatus = 0;

    // Initialize a new BroadcastReceiver instance
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the battery scale
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            // get the battery level
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

            // Calculate the battery charged percentage
            float percentage = level/ (float) scale;
            // Update the progress bar to display current battery charged percentage
            mProgressStatus = (int)((percentage)*100);

            // Show the battery charged percentage text inside progress bar
            mTextViewPercentage.setText("" + mProgressStatus + "%");

            // Display the battery charged percentage in progress bar
            mProgressBar.setProgress(mProgressStatus);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Battery");

        // Get the application context
        mContext = getApplicationContext();

        // Initialize a new IntentFilter instance
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        // Register the broadcast receiver
        mContext.registerReceiver(mBroadcastReceiver,iFilter);

        // Get the widgets reference from XML layout
        mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
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
