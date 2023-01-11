package com.example.resfingdiaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        String rowData = getIntent().getStringExtra("rowData");
        String[] PRD = rowData.split("<>");

        title = (TextView) findViewById(R.id.titleBook);
        dateText = (TextView) findViewById(R.id.timeStamp);
        pages = (TextView) findViewById(R.id.pagesBox);
        readerComment = (TextView) findViewById(R.id.commentBook);
        supportComment = (TextView) findViewById(R.id.supportComment);
        email = (Button) findViewById(R.id.emailButton);
        edit = (Button) findViewById(R.id.editButton);
        remove = (Button) findViewById(R.id.removeButton);


        supportComment.setText(rowData);
        title.setText(PRD[1]);
        pages.setText("From page : " +PRD[2] +" to page: "+PRD[3]);
        dateText.setText(PRD[4]);
        readerComment.setText(PRD[5]);
        supportComment.setText(PRD[6]);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mailUri = Uri.parse("mailto:test@mail.com");
                String subject = "This is a test email";
                String body = " This is a test email body.\n\n";
                body+=" This is a next line to check.\n\n";
                body+=" This is last line body.\n\n";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,mailUri);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(emailIntent); // Works so ignoring later part
                if (emailIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(emailIntent);
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
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editScreen = new Intent(view.this,edit.class);
                editScreen.putExtra("rowData",PRD);
                startActivity(editScreen);
            }
        });
    }
}