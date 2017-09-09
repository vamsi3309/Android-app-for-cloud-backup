package com.personal.vamsi.first_app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DataUpload extends AppCompatActivity implements Runnable{
    public void run(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_upload);
        dataUpload();
    }

    public void dataUpload(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Select file "), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final Intent data = intent;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Uri uri = data.getData();
                    Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                    returnCursor.moveToFirst();
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    data.putExtra("name",returnCursor.getString(nameIndex));
                    startDataUpload(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }//onActivityResult

    public void startDataUpload(Intent data){
        try {
            Socket sercom = new Socket("10.2.58.250",8080);
            Intent print_path = data;
            String file_name = print_path.getStringExtra("name");
            TextView textView = (TextView) findViewById(R.id.uploadView);
            startActivity(print_path);
            DataOutputStream out = new DataOutputStream(sercom.getOutputStream());
            out.writeUTF(file_name);
            InputStream file = getContentResolver().openInputStream(print_path.getData());
            byte[] bytes = new byte[1024];
            int limit;
            while((limit = file.read(bytes))>0){
                out.write(bytes,0,limit);
            }
            Intent disp_success = new Intent(this, DisplayMessage.class);
            disp_success.putExtra("NAME",file_name);
            startActivity(disp_success);
            file.close();
            out.close();
            sercom.close();
            Log.d("Connection","Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
