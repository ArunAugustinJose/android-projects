package com.example.hp.ajcegosafe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 16-10-2017.
 */

public class LogDetaild_listAdapter extends ArrayAdapter<LogDetailsContent> {
    private LayoutInflater mInflater;
    private ArrayList<LogDetailsContent> log;
    private int mViewResourceId;

    public LogDetaild_listAdapter(Context context, int textViewResourceId, ArrayList<LogDetailsContent> outpass) {
        super(context, textViewResourceId, outpass);
        this.log = outpass;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {

        convertView = mInflater.inflate(mViewResourceId, null);

        LogDetailsContent logd = log.get(position);

        if (logd != null) {
            TextView txt_sno = (TextView) convertView.findViewById(R.id.txtSno);
            TextView txt_purpose = (TextView) convertView.findViewById(R.id.txtPurpose);
            TextView txt_date = (TextView) convertView.findViewById(R.id.Txt_date);
            TextView txt_status = (TextView) convertView.findViewById(R.id.Txt_status);
            if (txt_sno != null) {
                int pos = position + 1;
                String posi = String.valueOf(pos);
                txt_sno.setText(posi);
                pos++;
            }

            if (txt_date != null) {
                txt_date.setText((logd.getAdno()));
            }

            if (txt_purpose != null) {

                txt_purpose.setText(logd.getName());
            }
            if (txt_status != null) {
                txt_status.setText(logd.getPassword());
            }
        }
        return convertView;
    }
}
