package com.example.acer.logindesignwithoutsocial;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

public class MobileNoEntry extends AppCompatActivity {

    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no_entry);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        EditText editText = (EditText) findViewById(R.id.etMobile);
        MyKeyboard keyboard = (MyKeyboard) findViewById(R.id.keyboard);

        // prevent system keyboard from appearing when EditText is tapped
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);

        // prevent system keyboard from appearing
//        if (android.os.Build.VERSION.SDK_INT >= 11) {
//            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
//            editText.setTextIsSelectable(false);
//        } else {
//            editText.setRawInputType(InputType.TYPE_NULL);
//            editText.setFocusable(false);
//        }

        editText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });


        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(MobileNoEntry.this, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
