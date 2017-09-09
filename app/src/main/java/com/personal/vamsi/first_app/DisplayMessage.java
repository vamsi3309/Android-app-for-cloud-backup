package com.personal.vamsi.first_app;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String name = intent.getStringExtra("NAME");
        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("File Uploaded "+name);
        //new DataUpload(path);
    }

}

