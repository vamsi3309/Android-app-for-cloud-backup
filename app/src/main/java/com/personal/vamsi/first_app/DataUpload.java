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
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

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
        final Intent temp_path = new Intent(this, DisplayMessage.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String path=null;
                    Uri uri = data.getData();
                    path = uri.getPath();
                    Log.d("android_home",System.getProperty("user.home"));
                    Log.v("Path",path);
                    File file = new File(uri.toString());
                    Log.v("File value", file.getName());
                    File file2 = new File(file.getAbsolutePath());
                    if(!file2.exists()) {
                        throw new FileNotFoundException();
                    }
//                    if(!new File(path).exists()) throw new FileNotFoundException();
//                    Log.d("PATH2",path);
                    // if (new File(filePath).canRead()) {
                    Cursor returnCursor =
                            getContentResolver().query(uri, null, null, null, null);
                    returnCursor.moveToFirst();
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    data.putExtra("path",path);
                    data.putExtra("name",returnCursor.getString(nameIndex));
                    Log.d("Location",path);
                    Intent print_path = temp_path;
                    print_path.putExtra(EXTRA_MESSAGE, path);
                    print_path.putExtra("NAME",returnCursor.getString(nameIndex));
                    startActivity(print_path);
                    startDataUpload(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }//onActivityResult

    public void startDataUpload(Intent data){
        try {
            Socket sercom = new Socket("172.21.187.24",8080);
            Intent print_path = data;
            String path = print_path.getStringExtra("path");
            String file_name = print_path.getStringExtra("name");
            Log.d("path  ",path);
            File upload_file = new File(path);
            if(!upload_file.exists()){throw new FileNotFoundException();}
            TextView textView = (TextView) findViewById(R.id.uploadView);
            //textView.setText(path+"   File "+upload_file.exists());
            startActivity(print_path);
            DataOutputStream out = new DataOutputStream(sercom.getOutputStream());
            out.writeUTF(file_name);
            FileInputStream file = new FileInputStream(upload_file);
            byte[] bytes = new byte[124];
            int limit;
            while((limit = file.read(bytes))>0){
                Log.d("Inloop","      ");
                out.write(bytes,0,limit);
            }
            file.close();
            out.close();
            sercom.close();
            Log.d("Connection","Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        Log.d("coloumn"," "+column_index);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
