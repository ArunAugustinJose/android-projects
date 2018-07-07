package com.example.acer.teacher.Students;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acer.teacher.R;
import com.example.acer.teacher.Students.Student;

import java.util.List;

public class StudentCustomAdapter extends ArrayAdapter<Student> {

    int resource;
    String response;
    Context context;
    private List<Student> items;
    private ProgressDialog pDialog;
    private static final String TAG = "AdminHome";


    public StudentCustomAdapter(Context context, int resource, List<Student> items) {
        super(context, resource, items);
        this.resource=resource;
        this.context = context;
        this.items=items;
        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout contactsView;
        final Student contact = getItem(position);
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
            class_name.setText((contact.getSname()));
        }
        if (subject != null) {
            subject.setText((contact.getPhno()));
        }

        return contactsView;
    }

}