package com.example.resfingdiaryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    myDbAdapter helper;

    Button addEntry;
    ListView listDataBox;
    SearchView searchBox;
    String queryData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new myDbAdapter(this);
        addEntry = (Button) findViewById(R.id.add_entry);

                //textView = (TextView) findViewById(R.id.dataView);
        listDataBox = (ListView) findViewById(R.id.dataList);
        searchBox = (SearchView) findViewById(R.id.searchBox);
        loadData(); // LOADS DATA
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createScreen = new Intent(MainActivity.this,create.class);
                activityResultLauncher.launch(createScreen);
            }
        });

        searchBox.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadData();
                return false;
            }
        });
        searchBox.setSubmitButtonEnabled(true);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                             @Override
                                             public boolean onQueryTextSubmit(String s) {
                                                 searchData();
                                                 Log.w("this",">PRessed on the submit button<");
                                                 return false;
                                             }

                                             @Override
                                             public boolean onQueryTextChange(String s) {
                                                 return false;
                                             }
                                         });
                searchBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.w("this",">Pressed on the search boc<");
                        searchBox.setIconified(false);
                        searchBox.setIconified(false);
                    }
                });
    }

    public void loadData(){
        queryData = helper.getData();
        Message.message(this,queryData);
        String[] listData = queryData.split("\n");
        setAdapter(listData);
           //  textView.setText(data);
    }

    public void searchData(){
        // Get and filter test
        String searchEntry = searchBox.getQuery().toString();
        Log.w("this",">"+searchEntry+"<");
        if (searchEntry.equals("")){Log.w("this","NO DATA HERE EMPTY STRING");};
        // Dismiss keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

       // queryData = helper.getData(); // Maybe not needed
        String[] dataArray = queryData.split("\n");
        StringBuffer buffer = new StringBuffer();
        for (String sr: dataArray){
            if(sr.toLowerCase().contains(searchEntry.toLowerCase())){
                buffer.append(sr+"\n");
                Log.w("this","Found match>"+sr);
            }
        }
        String[] listData = buffer.toString().split("\n");
        setAdapter(listData);
        Log.w("this","OUTPUT>"+buffer.toString());
    }

    public void setAdapter(String[] data){
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_layout,R.id.textView, data);
        listDataBox.setAdapter(adapter);
        listDataBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] select = adapterView.getItemAtPosition(i).toString().split(" ");
                Log.w("this",adapterView.getItemAtPosition(i).toString());
                Log.w("this",Integer.toString(i)+"< long:"+l+"View:"+view+"Adapter view:"+adapterView);
                try {
                    int idNumber = Integer.parseInt(select[0]);
                    helper.getDataByID(idNumber);
                    Intent viewScreen = new Intent(MainActivity.this,view.class);
                    viewScreen.putExtra("rowId",idNumber);
                    activityResultLauncher.launch(viewScreen);
                }
                catch (Exception e){
                    Message.message(getApplicationContext(),e+"Number error");
                }


            }
        });
    }

    //Currently unused user for getting row number in main screen
    public void loadDataById(int idn){
        String data = helper.getDataByID(idn);
        Message.message(this,data);
        //textView.setText(data);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadData();
                    }
                }
            });
}