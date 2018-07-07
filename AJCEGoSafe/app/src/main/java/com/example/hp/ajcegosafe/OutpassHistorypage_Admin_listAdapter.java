package com.example.hp.ajcegosafe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hp on 18-11-2017.
 */

public class OutpassHistorypage_Admin_listAdapter extends ArrayAdapter<OutpassHistoryAdmin> {
    private final Context context;
    private LayoutInflater mInflater;
    private ArrayList<OutpassHistoryAdmin> outpass;
    private int mViewResourceId;

    public OutpassHistorypage_Admin_listAdapter(Context context, int textViewResourceId, ArrayList<OutpassHistoryAdmin> outpass){
        super(context,textViewResourceId,outpass);
        this.outpass = outpass;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {

        convertView = mInflater.inflate(mViewResourceId,null);

        OutpassHistoryAdmin outpassHistory = outpass.get(position);

        if(outpassHistory != null){
            TextView txt_sno = (TextView) convertView.findViewById(R.id.txtSno);
            TextView txt_purpose = (TextView) convertView.findViewById(R.id.txtPurpose);
            final TextView txt_date = (TextView) convertView.findViewById(R.id.Txt_date);
            final TextView txt_adno = (TextView) convertView.findViewById(R.id.txtAdno);
            TextView txt_status = (TextView) convertView.findViewById(R.id.Txt_status);
            final TextView txt_oid = (TextView) convertView.findViewById(R.id.Txt_Hiddenoid);
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
            if (txt_adno != null) {
                txt_adno.setText((outpassHistory.getAdmno()));
            }

            if (txt_status != null) {
                String st = outpassHistory.getStatus();
                if (st.equals("0")){
                    txt_status.setText(("Requested"));
                    txt_sno.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                    txt_date.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                    txt_adno.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                    txt_purpose.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                    txt_status.setBackgroundColor(Color.parseColor("#FFDAC5FC"));
                }
                else if (st.equals("1")){
                    txt_status.setText(("Approved"));
                    txt_sno.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                    txt_date.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                    txt_adno.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                    txt_purpose.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                    txt_status.setBackgroundColor(Color.parseColor("#FFC2FEBD"));
                }else if (st.equals("2")){
                    txt_status.setText(("Rejected"));
                    txt_sno.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                    txt_date.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                    txt_adno.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                    txt_purpose.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                    txt_status.setBackgroundColor(Color.parseColor("#FFFDC4C4"));
                }

                else if (st.equals("3")){
                    txt_status.setText(("Went out"));
                    txt_sno.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                    txt_date.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                    txt_adno.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                    txt_purpose.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                    txt_status.setBackgroundColor(Color.parseColor("#FFC5FDDE"));
                }
                else if (st.equals("4")){
                    txt_status.setText(("Returned"));
                    txt_sno.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                    txt_date.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                    txt_adno.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                    txt_purpose.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                    txt_status.setBackgroundColor(Color.parseColor("#FFB2B9FB"));
                }
                else if (st.equals("5")){
                    txt_status.setText(("Late"));
                    txt_sno.setBackgroundColor(Color.parseColor("#ffcc0000"));
                    txt_sno.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_adno.setBackgroundColor(Color.parseColor("#ffcc0000"));
                    txt_adno.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_date.setBackgroundColor(Color.parseColor("#ffcc0000"));
                    txt_date.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_purpose.setBackgroundColor(Color.parseColor("#ffcc0000"));
                    txt_purpose.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_status.setBackgroundColor(Color.parseColor("#ffcc0000"));
                    txt_status.setTextColor(Color.parseColor("#FFFFFF"));
                }


            }
            if (txt_oid != null) {
                txt_oid.setText(String.valueOf(outpassHistory.getOid()));
            }

            txt_purpose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String adno = txt_adno.getText().toString();
                    String oid = txt_oid.getText().toString();

                    Intent intent = new Intent(getContext(), AdminOutpassDetails.class);
                    intent.putExtra("adno", adno);
                    intent.putExtra("oid", oid);
                    context.startActivity(intent);

//                    Toast.makeText(getContext(), " Clicked "+adno+"  "+date, Toast.LENGTH_SHORT).show();
                }
            });

            txt_sno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String adno = txt_adno.getText().toString();
                    String oid = txt_oid.getText().toString();

                    Intent intent = new Intent(getContext(), AdminOutpassDetails.class);
                    intent.putExtra("adno", adno);
                    intent.putExtra("oid",oid );
                    context.startActivity(intent);

//                    Toast.makeText(getContext(), " Clicked "+adno+"  "+date, Toast.LENGTH_SHORT).show();
                }
            });

            txt_adno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String adno = txt_adno.getText().toString();
                    String oid = txt_oid.getText().toString();

                    Intent intent = new Intent(getContext(), AdminOutpassDetails.class);
                    intent.putExtra("adno", adno);
                    intent.putExtra("oid",oid );
                    context.startActivity(intent);

//                    Toast.makeText(getContext(), " Clicked "+adno+"  "+date, Toast.LENGTH_SHORT).show();
                }
            });

            txt_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String adno = txt_adno.getText().toString();
                    String oid = txt_oid.getText().toString();

                    Intent intent = new Intent(getContext(), AdminOutpassDetails.class);
                    intent.putExtra("adno", adno);
                    intent.putExtra("oid",oid );
                    context.startActivity(intent);

//                    Toast.makeText(getContext(), " Clicked "+adno+"  "+date, Toast.LENGTH_SHORT).show();
                }
            });

            txt_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String adno = txt_adno.getText().toString();
                    String oid = txt_oid.getText().toString();

                    Intent intent = new Intent(getContext(), AdminOutpassDetails.class);
                    intent.putExtra("adno", adno);
                    intent.putExtra("oid",oid );
                    context.startActivity(intent);

//                    Toast.makeText(getContext(), " Clicked "+adno+"  "+date, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }
}
