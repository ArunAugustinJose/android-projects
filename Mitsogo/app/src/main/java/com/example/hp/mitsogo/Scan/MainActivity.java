package com.example.hp.mitsogo.Scan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hp.mitsogo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ActionMode.Callback {
    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    //i created List of int type to store id of data, you can create custom class type data according to your need.
    private List<Integer> selectedIds = new ArrayList<>();
    private MyAdapter adapter;

    AlertDialog alertDialog1;
    CharSequence[] values = {" 5 Minutes "," 15 Minutes "," 30 Minutes "," 1 Hour"," None "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.widget_list);
        adapter = new MyAdapter(this,getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Properties");

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null){
                        actionMode = startActionMode(MainActivity.this); //show ActionMode.
                    }
                }

                multiSelect(position);
            }
        }));
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

    private void multiSelect(int position) {
        MyData data = adapter.getItem(position);
        if (data != null){
            if (actionMode != null) {
                if (selectedIds.contains(data.getId()))
                    selectedIds.remove(Integer.valueOf(data.getId()));
                else
                    selectedIds.add(data.getId());

                if (selectedIds.size() > 0)
                    actionMode.setTitle(String.valueOf(selectedIds.size())); //show selected item count on action mode.
                else{
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish(); //hide action mode.
                }
                adapter.setSelectedIds(selectedIds);

            }
        }
    }

    /**
     * @see MyData Create dummy List of type MyData.
     * @return list
     */
    private List<MyData> getList(){
        List<MyData> list = new ArrayList<>();
        list.add(new MyData(1,"Battery"));
        list.add(new MyData(2,"Storage"));
        list.add(new MyData(3,"Network"));
        list.add(new MyData(4,"Whether"));

        return list;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_multi_select, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_delete:
                //just to show selected items.
                StringBuilder stringBuilder = new StringBuilder();
                for (MyData data : getList()) {
                    if (selectedIds.contains(data.getId()))
                        stringBuilder.append("\n").append(data.getTitle());
                }
                CreateAlertDialogWithRadioButtonGroup() ;
                Toast.makeText(this, "Selected items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<Integer>());
    }

    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Select Scanning Period");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        Toast.makeText(MainActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:

                        Toast.makeText(MainActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:

                        Toast.makeText(MainActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 3:

                        Toast.makeText(MainActivity.this, "4tm Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 4:

                        Toast.makeText(MainActivity.this, "5th Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 5:

                        Toast.makeText(MainActivity.this, "6th Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
}
