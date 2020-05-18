package com.hansung.android.ai_speaker_socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendMessagePopUpActivity extends Activity {
    Button sendButton, cancelButton;
    String SocialWorker,MemberName;

    EditText messageEditTextView;
    final static String TAG = "AndroidAPITest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send_message_pop_up);

        Intent intent = getIntent();
        MemberName = intent.getStringExtra("MemberName");

        sendButton = (Button)findViewById(R.id.sendButton_id);
        cancelButton = (Button)findViewById(R.id.cancelButton_id);
        messageEditTextView = (EditText)findViewById(R.id.MessageEditView_id);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageEditTextView.getText().toString().length()>0)
                    SendInformation(messageEditTextView.getText().toString());
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

    void SendInformation(String message){
        JSONObject payload = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject tag1 = new JSONObject();
            tag1.put("tagName", "Member_Name");
            tag1.put("tagValue", MemberName);
            jsonArray.put(tag1);

            JSONObject tag2 = new JSONObject();
            tag2.put("tagName", "message");
            tag2.put("tagValue", message);
            jsonArray.put(tag2);

            if (jsonArray.length() > 0)
                payload.put("tags", jsonArray);
        } catch (JSONException e) {
            Log.e(TAG, "JSONEXception");
        }
        Log.i(TAG,"payload="+payload);
        if (payload.length() >0 ) {
            //new UpdateShadow(SendMessagePopUpActivity.this, urlbase).execute(payload);
        }
        else
            Toast.makeText(SendMessagePopUpActivity.this,"변경할 상태 정보 입력이 필요합니다", Toast.LENGTH_SHORT).show();

    }
}
