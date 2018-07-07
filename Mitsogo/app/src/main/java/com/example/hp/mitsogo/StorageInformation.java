package com.example.hp.mitsogo;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class StorageInformation extends AppCompatActivity {

    private String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Storage");

        ListView memoryList = (ListView)findViewById(R.id.list_of_memory);

        List<Memory> memoryDataSource = new ArrayList<Memory>();


        String internalMemoryTitle = "Internal Memory Information";
        long totalInternalValue = getTotalInternalMemorySize();
        long freeInternalValue = getAvailableInternalMemorySize();
        long usedInternalValue = totalInternalValue - freeInternalValue;
        int internalIcon = R.drawable.piechartone;

        Memory internalMemory = new Memory(internalMemoryTitle, formatSize(usedInternalValue), formatSize(freeInternalValue), formatSize(totalInternalValue), internalIcon);
        memoryDataSource.add(internalMemory);

        String externalMemoryTitle = "External Memory Information";
        long totalExternalValue = getTotalExternalMemorySize();
        long freeExternalValue = getAvailableExternalMemorySize();
        long usedExternalValue = totalExternalValue - freeExternalValue;
        int externalIcon = R.drawable.piecharttwo;

        Memory externalMemory = new Memory(externalMemoryTitle, formatSize(usedExternalValue), formatSize(freeExternalValue), formatSize(totalExternalValue), externalIcon);
        memoryDataSource.add(externalMemory);

        MemoryAdapter memoryAdapter = new MemoryAdapter(StorageInformation.this, memoryDataSource);
        memoryList.setAdapter(memoryAdapter);

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

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return 0;
        }
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

    private String returnToDecimalPlaces(long values){
        DecimalFormat df = new DecimalFormat("#.00");
        String angleFormated = df.format(values);
        return angleFormated;
    }

}