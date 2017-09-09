package com.personal.vamsi.first_app;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessage extends AppCompatActivity implements Runnable {


    public void run(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final String name = intent.getStringExtra("NAME");
        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(path);
        //new DataUpload(path);
        final Intent temp_path = new Intent(this,DataUpload.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
        Intent Upload_path = temp_path;
                Upload_path.putExtra("key",path);
                Upload_path.putExtra("name",name);
        startActivity(Upload_path);
        }
    }).start();
}
}
