package com.hansung.android.ai_speaker_socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineAddPopUpActivity extends Activity {
    String DeviceId = "";
    Boolean breakfast = false;
    Boolean lunch = false;
    Boolean dinner = false;
    Boolean afterMeal = true;
    EditText medicineNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_medicine_add_pop_up);

        Intent intent = getIntent();
        DeviceId = intent.getStringExtra("DeviceId");

        Button sendButton = (Button)findViewById(R.id.sendButton_MedicineAddButton_id);
        Button cancelButton = (Button)findViewById(R.id.cancelButton_MedicineAddButton_id);
        medicineNameEditText = (EditText)findViewById(R.id.MedicineName_MedicineAddTextView_id);
        Switch breakfastSwitch = (Switch)findViewById(R.id.Breakfast_MedicineAddSwitch_id);
        Switch lunchSwich = (Switch)findViewById(R.id.Lunch_MedicineAddSwitch_id);
        Switch dinnerSwitch= (Switch)findViewById(R.id.Dinner_MedicineAddSwitch_id);


        breakfastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                breakfast = b;
            }
        });
        lunchSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lunch = b;
            }
        });
        dinnerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dinner = b;
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGetAllInfos()) {
                    String medicine_name = medicineNameEditText.getText().toString();
                    String cycle ="";
                    String type = "";

                    if(medicine_name.contains("약"))
                        medicine_name=medicine_name.replace("약","");

                    if(breakfast && lunch&& dinner)
                        cycle = "세끼/";
                    else {
                        if (breakfast)
                            cycle += "아침/";
                        if (lunch)
                            cycle += "점심/";
                        if (dinner)
                            cycle += "저녁/";
                    }

                    if(afterMeal)
                        type = "식후";
                    else
                        type = "식전";



                    SendInformation(DeviceId, cycle.substring(0,cycle.length()-1),type,medicine_name);
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onToggleClicked(View v){
        afterMeal = ((ToggleButton)v).isChecked();
    }

    public Boolean isGetAllInfos(){
        if(!medicineNameEditText.getText().equals("")&&(breakfast||lunch||dinner))
            return true;

        else {
            Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void SendInformation(String deviceid,String cycle,String type,String medicine_name){
        String send_msg = "{"+PublicFunctions.MakeMsg("device_id",deviceid)+","+
                PublicFunctions.MakeMsg("cycle",cycle)+","+
                PublicFunctions.MakeMsg("type",type)+","+
                PublicFunctions.MakeMsg("medicine_name",medicine_name)+"}";

        Socket_SendInfo socket_sendInfo=new Socket_SendInfo("AddMedicine",send_msg);

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


    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
