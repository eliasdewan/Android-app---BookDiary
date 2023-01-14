package com.example.readingdiaryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class view extends AppCompatActivity {

    myDbAdapter helper;
    //Button addEntry;

    //Attributes
    TextView title;
    TextView dateText;
    TextView pages;

    TextView readerComment;
    TextView supportComment;
    Button email;
    Button edit;
    Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        helper = new myDbAdapter(this);

        int rowNumber = getIntent().getIntExtra("rowId",0);
        String rowData = helper.getDataByID(rowNumber);
        String[] PRD = rowData.split("<>");

        title = (TextView) findViewById(R.id.titleBook);
        dateText = (TextView) findViewById(R.id.timeStamp);
        pages = (TextView) findViewById(R.id.pagesBox);
        readerComment = (TextView) findViewById(R.id.commentBook);
        supportComment = (TextView) findViewById(R.id.supportComment);
        email = (Button) findViewById(R.id.emailButton);
        edit = (Button) findViewById(R.id.editButton);
        remove = (Button) findViewById(R.id.removeButton);


        //supportComment.setText(rowData);
        title.setText(PRD[1]);
        pages.setText("From page : " +PRD[2] +" to page: "+PRD[3]);
        dateText.setText(PRD[4]);
        readerComment.setText(PRD[5]);
        supportComment.setText(PRD[6]);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] address = {"test@mail.com"};
                String subject = PRD[1] + "on From page : " +PRD[2] +" to page: "+PRD[3];
                String body = "Title:"+"\n";
                body+= subject +".\n\n";
                body+="Reader Comment:\n";
                body+=PRD[5] +".\n\n";
                body+="Teacher Comment/ Parent comment:\n";
                body+=PRD[6] +".\n\n";

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL,address);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT,body);

                startActivity(emailIntent); // Works so ignoring later part
                if (emailIntent.resolveActivity(getPackageManager()) != null){
                    //startActivity(emailIntent);
                }else {
                    Toast errorMessage = Toast.makeText(getApplicationContext(),"No email app installed",Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.delete(PRD[0]);
                Message.message(getApplicationContext(),"Deleted");
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editScreen = new Intent(view.this,edit.class);
                editScreen.putExtra("rowData",PRD);
                //startActivity(editScreen);
                activityResultLauncher.launch(editScreen);

            }
        });
    }

    public void refreshData(){
        int rowNumber = getIntent().getIntExtra("rowId",0);
        String rowData = helper.getDataByID(rowNumber);
        String[] PRD = rowData.split("<>");

        title.setText(PRD[1]);
        pages.setText("From page : " +PRD[2] +" to page: "+PRD[3]);
        dateText.setText(PRD[4]);
        readerComment.setText(PRD[5]);
        supportComment.setText(PRD[6]);

    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Message.message(getApplicationContext(),"Got result EDITED time to update");
                        refreshData();
                        // There are no request codes
                        //Intent data = result.getData();
                       // doSomeOperations();
                    }
                }
            });
}