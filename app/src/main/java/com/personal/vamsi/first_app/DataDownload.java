package com.personal.vamsi.first_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DataDownload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_download);
        startdownload();
    }

    public void startdownload(){
        Intent dataupload = new Intent(Intent.ACTION_GET_CONTENT);
        dataupload.setType("*/*");
        dataupload.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(dataupload, 1);
    }
}
