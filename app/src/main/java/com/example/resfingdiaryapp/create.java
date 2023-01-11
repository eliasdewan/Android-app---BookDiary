package com.example.resfingdiaryapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class create extends AppCompatActivity {

    myDbAdapter helper;
    Button addEntry;
    //Attributes
    EditText title;
    EditText fromPage;
    EditText toPage;
    TextView dateText;
    EditText readerComment;
    EditText supportComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        addEntry = (Button) findViewById(R.id.editButton);

        title = (EditText) findViewById(R.id.titleBook);
        fromPage = (EditText) findViewById(R.id.editFromPage);
        toPage = (EditText) findViewById(R.id.editToPage);
        dateText = (TextView) findViewById(R.id.timeStamp);
        readerComment = (EditText) findViewById(R.id.commentBook);
        supportComment = (EditText) findViewById(R.id.supportComment);

         SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yy HH:mm");
         String date = sdf.format(new Date());
         dateText.setText(date);
         helper = new myDbAdapter(this);

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry(view); // This needs changing;
            }
        });


    }




    public void addEntry(View view){
        Log.w("this","in the add entry after button");
        String titleString = title.getText().toString();//"Book title";
        int fromInt = -1;
        int toInt = -1;
        try{
             fromInt = Integer.parseInt(fromPage.getText().toString());
             toInt = Integer.parseInt(toPage.getText().toString());}
        catch (Exception e){
           // Message.message(getApplicationContext(),e+"Number error");
        }
            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yy HH:mm");
        String timeString = sdf.format(new Date());
        String commentString = readerComment.getText().toString();
        String supportCommentString = supportComment.getText().toString();

        if(titleString.isEmpty()||commentString.isEmpty()){
            Message.message(getApplicationContext(),"Fill all the fields");
        }else if ((fromInt<0)||(fromInt >= toInt)) {
            Message.message(getApplicationContext(), "Not valid pages");
        }else{
            helper.insertData(titleString,fromInt,toInt,timeString,commentString,supportCommentString);
            Message.message(getApplicationContext(),"Insertion successful");
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            //name.setText("");
        }
    };

}