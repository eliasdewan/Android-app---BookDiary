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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    myDbAdapter helper;

    Button addEntry;
    Button searchButton;
    Button getData;
    TextView textView;
    EditText entryNumber;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new myDbAdapter(this);
        addEntry = (Button) findViewById(R.id.add_entry);
        getData = (Button) findViewById(R.id.getData);
        textView = (TextView) findViewById(R.id.dataView);
        entryNumber = (EditText) findViewById(R.id.selectNumber);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchBox = (EditText) findViewById(R.id.searchBox);

        loadData();
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createScreen = new Intent(MainActivity.this,create.class);
                activityResultLauncher.launch(createScreen);
            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int idNumber = Integer.parseInt(entryNumber.getText().toString());
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchEntry = searchBox.getText().toString();
                Log.w("this",">"+searchEntry+"<");
                if (searchEntry.equals("")){Log.w("this","NO DATA HERE EMPTY STRING");};
                searchData(searchEntry);

                //Pull keyboard down
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(entryNumber.getWindowToken(), 0);
                entryNumber.setText("");
            }
        });
    }


    public void loadData(){
        String data = helper.getData();
        Message.message(this,data);
        textView.setText(data);
    }

    public void searchData(String searchString){
        String data = helper.getData();
        String[] dataArray = data.split("\n");
        StringBuffer buffer = new StringBuffer();
        for (String sr: dataArray){
            if(sr.toLowerCase().contains(searchString.toLowerCase())){
                buffer.append(sr+"\n");
                Log.w("this","Found match>"+sr);
            }
        }
        textView.setText(buffer.toString());
        Log.w("this","OUTPUT>"+buffer.toString());
    }

    //Currently unused user for getting row number in main screen
    public void loadDataById(int idn){
        String data = helper.getDataByID(idn);
        Message.message(this,data);
        textView.setText(data);
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