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

public class edit extends AppCompatActivity {

    String rowId;
    myDbAdapter helper;
    Button confirmButton;
    //Attributes
    TextView dateText;
    EditText title;
    EditText fromPage;
    EditText toPage;
    EditText readerComment;
    EditText supportComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String[] rowData = getIntent().getStringArrayExtra("rowData");

        rowId = rowData[0];
        helper = new myDbAdapter(this);
        confirmButton = (Button) findViewById(R.id.confirmEditButton);
        dateText = (TextView) findViewById(R.id.newDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yy HH:mm");
        String date = sdf.format(new Date());
        dateText.setText(date);
        title = (EditText) findViewById(R.id.editTitle);
        title.setText(rowData[1]);
        fromPage = (EditText) findViewById(R.id.editNewFromPage);
        fromPage.setText(rowData[2]);
        toPage = (EditText) findViewById(R.id.editNewToPage);
        toPage.setText(rowData[3]);
        readerComment = (EditText) findViewById(R.id.editReaderComment);
        readerComment.setText(rowData[5]);
        supportComment = (EditText) findViewById(R.id.editSupportComment);
        supportComment.setText(rowData[6]);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message.message(getApplicationContext(),"All good until now");
                editEntry();

            }
        });
    }

    public void editEntry(){
        Log.w("this","in the edit entry after button");
        String titleString = title.getText().toString();//"Book title";
        int fromInt = -1;
        int toInt = -1;
        try{
            fromInt = Integer.parseInt(fromPage.getText().toString());
            toInt = Integer.parseInt(toPage.getText().toString());}
        catch (Exception e){
             Message.message(getApplicationContext(),e+"Number error");
        }
        String timeString = dateText.getText().toString();
        String commentString = readerComment.getText().toString();
        String supportCommentString = supportComment.getText().toString();

        if(titleString.isEmpty()||commentString.isEmpty()){
            Message.message(getApplicationContext(),"Fill all the fields");
        }else if ((fromInt<0)||(fromInt >= toInt)) {
            Message.message(getApplicationContext(), "Not valid pages");
        }else{

            helper.update(rowId,titleString,fromInt,toInt,timeString,commentString,supportCommentString);
            Message.message(getApplicationContext(),"Edit successful");
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            //name.setText("");
        }
    };

}