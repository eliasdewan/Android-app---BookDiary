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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {

    myDbAdapter helper;

    Button addEntry;
    Button reloadButton;
    Button getData;
    TextView textView;
    EditText entryNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new myDbAdapter(this);
        addEntry = (Button) findViewById(R.id.add_entry);
        getData = (Button) findViewById(R.id.getData);
        textView = (TextView) findViewById(R.id.dataView);
        entryNumber = (EditText) findViewById(R.id.selectNumber);
        reloadButton = (Button) findViewById(R.id.reloadButton);

        loadData();
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createScreen = new Intent(MainActivity.this,create.class);
                startActivity(createScreen);
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

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
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