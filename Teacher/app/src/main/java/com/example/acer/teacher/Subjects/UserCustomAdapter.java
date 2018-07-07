package com.example.acer.teacher.Subjects;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acer.teacher.R;

import java.util.List;

public class UserCustomAdapter extends ArrayAdapter<Subjects> {

    int resource;
    String response;
    Context context;
    private List<Subjects> items;
    private UserCustomAdapter adapter;
    private ProgressDialog pDialog;
    private static final String TAG = "AdminHome";


    public UserCustomAdapter(Context context, int resource, List<Subjects> items) {
        super(context, resource, items);
        this.resource=resource;
        this.context = context;
        this.items=items;
        this.adapter = this;
        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout contactsView;
        final Subjects contact = getItem(position);
        if(convertView==null) {
            contactsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, contactsView, true);
        } else {
            contactsView = (LinearLayout) convertView;
        }
        final TextView class_name =contactsView.findViewById(R.id.tvClassName);
        final TextView subject = contactsView.findViewById(R.id.tvSubjectName);

        if (class_name != null) {
            class_name.setText((contact.getClass_name()));
        }
        if (subject != null) {
            subject.setText((contact.getSubject()));
        }

        return contactsView;
    }

}
