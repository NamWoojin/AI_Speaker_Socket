package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCareMemberActivity extends AppCompatActivity {
    final static String TAG = "AndroidAPITest";
    String Gender = "남";
    String Worker_Name;
    String ip = "";
    int port = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_care_member);

        Intent intent = getIntent();
        Worker_Name = intent.getStringExtra("Worker_Name");
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port",-1);

        Button addButton = (Button)findViewById(R.id.addCareMemberButton_id);
        final Button maleButton = (Button)findViewById(R.id.MaleButton_id);
        final Button femaleButton = (Button)findViewById(R.id.FemaleButton_id);



        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText NameEditText = (EditText)findViewById(R.id.CMNameEditText_id) ;
                String CMName = NameEditText.getText().toString();
                EditText AgeEditText = (EditText)findViewById(R.id.CMAgeEditText_id);
                String CMAge = AgeEditText.getText().toString();
                EditText DeviceIdEditText = (EditText)findViewById(R.id.CMDeviceEditText_id);
                String CMDeviceId = DeviceIdEditText.getText().toString();
                if(!CMName.equals("")&&!CMAge.equals("")&&!CMDeviceId.equals("")) {
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


        maleButton.callOnClick();
    }

    void SendInformation(String name,String gender, String age,String deviceid){
        String send_msg = "{"+PublicFunctions.MakeMsg("device_id",deviceid)+","+
                PublicFunctions.MakeMsg("worker_name",Worker_Name)+","+
                PublicFunctions.MakeMsg("member_name",name)+","+
                PublicFunctions.MakeMsg("member_age",age)+","+
                PublicFunctions.MakeMsg("member_gender",gender)+"}";

        new Socket_SendInfo("AddMember",send_msg);

    }
}
