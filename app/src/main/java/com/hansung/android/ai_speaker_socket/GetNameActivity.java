package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetNameActivity extends AppCompatActivity {

    private String ip = "192.168.0.28"; // IP
    private int port = 8888; // PORT번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        Button getNameButton = (Button)findViewById(R.id.GetNameButton_id);
        final EditText editText = (EditText)findViewById(R.id.EditName_id) ;
        getNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GetNameActivity.this,CareMembersListActivity.class);
                intent.putExtra("Worker_Name",editText.getText().toString());
                intent.putExtra("ip",ip);
                intent.putExtra("port",port);
                startActivity(intent);

            }
        });
    }
}
