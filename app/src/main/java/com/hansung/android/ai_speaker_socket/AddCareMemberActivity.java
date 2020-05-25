package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class AddCareMemberActivity extends AppCompatActivity {
    final static String TAG = "AndroidAPITest";
    String Gender = "남";
    String Worker_Name;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_care_member);

        Intent intent = getIntent();
        Worker_Name = intent.getStringExtra("Worker_Name");

        Button addButton = (Button)findViewById(R.id.addCareMemberButton_id);
        final Button maleButton = (Button)findViewById(R.id.MaleButton_id);
        final Button femaleButton = (Button)findViewById(R.id.FemaleButton_id);
        final ImageButton photoButton = (ImageButton)findViewById(R.id.add_photo_button);



        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText NameEditText = (EditText)findViewById(R.id.CMNameEditText_id) ;
                String CMName = NameEditText.getText().toString();
                EditText AgeEditText = (EditText)findViewById(R.id.CMAgeEditText_id);
                String CMAge = AgeEditText.getText().toString();
                EditText DeviceIdEditText = (EditText)findViewById(R.id.CMDeviceEditText_id);
                String CMDeviceId = DeviceIdEditText.getText().toString();
                if(!CMName.equals("")&&!CMAge.equals("")&&!CMDeviceId.equals("")&& bitmap != null) {
                    SendInformation(CMName, Gender, CMAge, CMDeviceId);
                    finish();
                }
                else
                    Toast.makeText(AddCareMemberActivity.this,"모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = "Male";
                maleButton.setBackgroundColor(Color.rgb(15,76,129));
                maleButton.setTextColor(Color.WHITE);
                femaleButton.setBackgroundColor(Color.TRANSPARENT);
                femaleButton.setTextColor(Color.BLACK);

            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = "Female";
                maleButton.setBackgroundColor(Color.TRANSPARENT);
                maleButton.setTextColor(Color.BLACK);
                femaleButton.setBackgroundColor(Color.rgb(15,76,129));
                femaleButton.setTextColor(Color.WHITE);
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });


        maleButton.callOnClick();
    }

    void SendInformation(String name,String gender, String age,String deviceid){
        String send_msg = "{"+PublicFunctions.MakeMsg("device_id",deviceid)+","+
                PublicFunctions.MakeMsg("worker_name",Worker_Name)+","+
                PublicFunctions.MakeMsg("member_name",name)+","+
                PublicFunctions.MakeMsg("member_age",age)+","+
                PublicFunctions.MakeMsg("member_gender",gender)+","+
                PublicFunctions.MakeMsg("photo",PublicFunctions.BitmapToByteArray(bitmap).toString())+"}";


        Socket_SendInfo socket_sendInfo = new Socket_SendInfo("AddMember",send_msg);
        while(true){
            if(socket_sendInfo.sendMsg){
                if(socket_sendInfo.sendSuccess){
                    Toast.makeText(this,"전송 성공.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(this,"전송 실패. 다시 시도하세요.",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap)bundle.get("data");
            Log.i("TAG","++++++"+PublicFunctions.BitmapToByteArray(bitmap));
            ImageButton button1 = (ImageButton)findViewById(R.id.add_photo_button);
            button1.setImageBitmap(bitmap);
        }
    }


    public void SaveBitmapToFile(Bitmap bitmap, String strFilePath ,String filename) {
        File file = new File(strFilePath);
        OutputStream out = null;
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileCacheItem = new File(strFilePath + filename);
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
