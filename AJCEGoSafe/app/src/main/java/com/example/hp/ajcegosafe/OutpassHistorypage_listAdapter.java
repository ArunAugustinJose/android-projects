package com.example.hp.ajcegosafe;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 30-09-2017.
 */

public class OutpassHistorypage_listAdapter extends ArrayAdapter<OutpassHistory> {
    private LayoutInflater mInflater;
    private ArrayList<OutpassHistory> outpass;
    private int mViewResourceId;

    public OutpassHistorypage_listAdapter(Context context, int textViewResourceId,ArrayList<OutpassHistory> outpass){
        super(context,textViewResourceId,outpass);
        this.outpass = outpass;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {

        convertView = mInflater.inflate(mViewResourceId,null);

        OutpassHistory outpassHistory = outpass.get(position);

        if(outpassHistory != null){
            TextView txt_sno = (TextView) convertView.findViewById(R.id.txtSno);
            TextView txt_purpose = (TextView) convertView.findViewById(R.id.txtPurpose);
            TextView txt_date = (TextView) convertView.findViewById(R.id.Txt_date);
            TextView txt_status = (TextView) convertView.findViewById(R.id.Txt_status);
               int count = outpass.size();
                    if (txt_sno != null) {
                        int pos = position+1;
                        String posi = String.valueOf(pos);
                        txt_sno.setText(posi);
                        pos++;
                    }

                    if (txt_purpose != null) {
                        txt_purpose.setText((outpassHistory.getPurpose()));
                    }
                    if (txt_date != null) {
                        txt_date.setText((outpassHistory.getDate()));
                    }
                    if (txt_status != null) {
                        String st = outpassHistory.getStatus();
                        if (st.equals("0")){
                            txt_status.setText(("Requested"));
                            txt_sno.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                            txt_date.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                            txt_purpose.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                            txt_status.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                        }
                        else if (st.equals("1")){
                            txt_status.setText(("Approved"));
                            txt_sno.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                            txt_date.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                            txt_purpose.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                            txt_status.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                        }else if (st.equals("2")){
                            txt_status.setText(("Rejected"));
                            txt_sno.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                            txt_date.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                            txt_purpose.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                            txt_status.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                        }

                        else if (st.equals("3")){
                            txt_status.setText(("Went out"));
                            txt_sno.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                            txt_date.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                            txt_purpose.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                            txt_status.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                        }
                        else if (st.equals("4")){
                            txt_status.setText(("Returned"));
                            txt_sno.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                            txt_date.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                            txt_purpose.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                            txt_status.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                        }
                        else if (st.equals("5")){
                            txt_status.setText(("Late"));
                            txt_sno.setBackgroundColor(Color.parseColor("#ffcc0000"));
                            txt_sno.setTextColor(Color.parseColor("#FFFFFF"));
                            txt_date.setBackgroundColor(Color.parseColor("#ffcc0000"));
                            txt_date.setTextColor(Color.parseColor("#FFFFFF"));
                            txt_purpose.setBackgroundColor(Color.parseColor("#ffcc0000"));
                            txt_purpose.setTextColor(Color.parseColor("#FFFFFF"));
                            txt_status.setBackgroundColor(Color.parseColor("#ffcc0000"));
                            txt_status.setTextColor(Color.parseColor("#FFFFFF"));
                        }

                    }
        }
        return convertView;
    }
}
