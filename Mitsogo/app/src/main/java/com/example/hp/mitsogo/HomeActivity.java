package com.example.hp.mitsogo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hp.mitsogo.Battery.BatteryInformation;
import com.example.hp.mitsogo.Device.DeviceInformation;
import com.example.hp.mitsogo.Scan.MainActivity;
import com.example.hp.mitsogo.weather.Function;
import com.example.hp.mitsogo.weather.WeatherActivity;

import java.util.ArrayList;

import static com.example.hp.mitsogo.StorageInformation.getAvailableExternalMemorySize;
import static com.example.hp.mitsogo.StorageInformation.getAvailableInternalMemorySize;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private Context mContext;

    private TextView mTextViewPercentage, mTextViewPercent;
    TextView mTextViewname, mTextViewOS, mTextViewApi;
    TextView mTextViewInternal, mTextViewExternal;
    TextView mTextViewcityField, mTextViewupdatedField, mTextViewcurrentTemperatureField;
    TextView mTextViewNetType,mTextInfoNetInfo;
    ImageView mImageViewNetType;
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;

    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;

    View v;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the battery scale
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            // get the battery level
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

            // Display the battery level in TextView
//            mTextViewInfo.setText(mTextViewInfo.getText() + "\nBattery Level : " + level);

            // Calculate the battery charged percentage
            float percentage = level/ (float) scale;
            // Update the progress bar to display current battery charged percentage
            mProgressStatus = (int)((percentage)*100);

            // Show the battery charged percentage text inside progress bar
            mTextViewPercentage.setText("" + mProgressStatus + "%");

            // Show the battery charged percentage in TextView
            mTextViewPercent.setText(mTextViewPercent.getText()+ ""+mProgressStatus + "%");

            // Display the battery charged percentage in progress bar
            mProgressBar.setProgress(mProgressStatus);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View parentLayout = findViewById(android.R.id.content);

        try {

            // Get the application context
            mContext = getApplicationContext();

            // Initialize a new IntentFilter instance
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

            // Register the broadcast receiver
            mContext.registerReceiver(mBroadcastReceiver, iFilter);

            // Get the widgets reference from XML layout
            mTextViewPercent = (TextView) findViewById(R.id.tv_percentage);
            mTextViewPercentage = (TextView) findViewById(R.id.txt_btry_info);
            mProgressBar = (ProgressBar) findViewById(R.id.pb);

            // Get device information
            String osVersion = System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")\n";
            String apiLevel = android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")\n";
            String device = android.os.Build.DEVICE + "\n";
            String model = android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")\n";

            mTextViewname = (TextView) findViewById(R.id.txt_device_name);
            mTextViewOS = (TextView) findViewById(R.id.txt_device_os);
            mTextViewApi = (TextView) findViewById(R.id.txt_apiLevel);

            mTextViewname.setText("Mode:" + model);
            mTextViewApi.setText("API Level:" + apiLevel);
            mTextViewOS.setText("OS:" + osVersion);

            //get Storage information
            long freeInternalValue = getAvailableInternalMemorySize();
            long freeExternalValue = getAvailableExternalMemorySize();

            mTextViewInternal = (TextView) findViewById(R.id.txt_str_internal);
            mTextViewExternal = (TextView) findViewById(R.id.txt_str_external);

            mTextViewInternal.setText(String.valueOf(formatSize(freeInternalValue)));
            mTextViewExternal.setText(String.valueOf(formatSize(freeExternalValue)));

            //network type(mobile or wifi)
            mTextViewNetType = (TextView) findViewById(R.id.txt_net_name);
            mTextInfoNetInfo = (TextView) findViewById(R.id.txt_net_info);
            mImageViewNetType = (ImageView) findViewById(R.id.img_network_type);

            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            String type = activeNetwork.getSubtypeName();
            String name = activeNetwork.getTypeName();
            String extra = activeNetwork.getExtraInfo();

            mTextViewNetType.setText(name + " " + type);
            mTextInfoNetInfo.setText(extra);

            //get current location
            locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
            isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionsToRequest = findUnAskedPermissions(permissions);

            if (!isGPS && !isNetwork) {
                Log.d(TAG, "Connection off");
                showSettingsAlert();
                getLastLocation();
            } else {
                Log.d(TAG, "Connection on");
                // check permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                                ALL_PERMISSIONS_RESULT);
                        Log.d(TAG, "Permission requests");
                        canGetLocation = false;
                    }
                }

                // get location
                getLocation();
            }

            //get weather information using public weather API
            mTextViewcityField = (TextView) findViewById(R.id.txt_location);
            mTextViewcurrentTemperatureField = (TextView) findViewById(R.id.txt_heat);

        }
        catch (Exception e){
            Snackbar.make(parentLayout, "No internet Connection.!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");

        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {


                mTextViewcityField.setText(weather_city);
//                mTextViewupdatedField.setText(weather_updatedOn);
                mTextViewcurrentTemperatureField.setText(weather_temperature);
//                weatherIcon.setText(Html.fromHtml(weather_iconText));


            }
        });
        asyncTask.execute(Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude())); //  asyncTask.execute("Latitude", "Longitude")
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_properties) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_battery) {
            Intent intent = new Intent(HomeActivity.this, BatteryInformation.class);
            startActivity(intent);
        } else if (id == R.id.nav_device) {
            Intent intent = new Intent(HomeActivity.this, DeviceInformation.class);
            startActivity(intent);
        } else if (id == R.id.nav_network) {

        } else if (id == R.id.nav_storage) {
            Intent intent = new Intent(HomeActivity.this, StorageInformation.class);
            startActivity(intent);

        } else if (id == R.id.nav_whether) {
            Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(HomeActivity.this, AboutApp.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


}
