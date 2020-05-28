package com.hansung.android.ai_speaker_socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessagePopUpActivity extends Activity {
    Button sendButton, cancelButton;
    String MemberName,DeviceId,ip;
    int port;

    EditText messageEditTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send_message_pop_up);

        Intent intent = getIntent();
        MemberName = intent.getStringExtra("MemberName");
        DeviceId = intent.getStringExtra("device_id");
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port",-1);

        sendButton = (Button)findViewById(R.id.sendButton_id);
        cancelButton = (Button)findViewById(R.id.cancelButton_id);
        messageEditTextView = (EditText)findViewById(R.id.MessageEditView_id);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageEditTextView.getText().toString().length()>0)
                    SendInformation(DeviceId, MemberName, messageEditTextView.getText().toString());

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


    void SendInformation(String deviceid,String name,String message){
        String send_msg = "{"+PublicFunctions.MakeMsg("device_id",deviceid)+","+
                PublicFunctions.MakeMsg("member_name",name)+","+
                PublicFunctions.MakeMsg("message",message)+"}";

        Socket_SendInfo socket_sendInfo=new Socket_SendInfo("SendMessage",send_msg);
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
}
