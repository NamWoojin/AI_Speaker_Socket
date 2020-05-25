package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CareMembersListActivity extends AppCompatActivity implements View.OnClickListener{
    String Worker_Name;
    TextView NonInfoTextView;
    ListView CareMembersListView;
    CareMembersListViewAdapter CMListViewAdapter = new CareMembersListViewAdapter();

    private FloatingActionButton fab_main, fab_add, fab_delete;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_members_list);

        Intent intent = getIntent();
        Worker_Name = intent.getStringExtra("Worker_Name");

        NonInfoTextView = (TextView)findViewById(R.id.NonInfoTextView_id) ;
        CareMembersListView = (ListView) findViewById(R.id.CareMembersListView_id);
        CareMembersListView.setAdapter(CMListViewAdapter);

        TextView WorkerNameTextView= (TextView)findViewById(R.id.socialWorkerNameTextView_id);
        WorkerNameTextView.setText("사회복지사 "+Worker_Name+"님");

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);


        fab_main = (FloatingActionButton) findViewById(R.id.ShowOther_FloatingActionButton);
        fab_add = (FloatingActionButton) findViewById(R.id.Add_CareMember_FloatingActionButton);
        fab_delete = (FloatingActionButton) findViewById(R.id.Delete_CareMember_FloatingActionButton);

        fab_main.setOnClickListener(this);
        fab_add.setOnClickListener(this);
        fab_delete.setOnClickListener(this);

        ListView CareMembersListView = (ListView) findViewById(R.id.CareMembersListView_id);
        CareMembersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CareMembersListActivity.this,HealthInfoActivity.class);
                CareMembersData cmData = (CareMembersData)parent.getAdapter().getItem(position);
                intent.putExtra("MemberName",cmData.getName());
                intent.putExtra("DeviceId",cmData.getDeviceId());
                startActivity(intent);
            }
        });

    }





    @Override
    protected void onStart() {
        super.onStart();
        CareMembersListView.setVisibility(View.GONE);
        NonInfoTextView.setVisibility(View.VISIBLE);
        NonInfoTextView.setText("정보 가져오는 중...");

        Socket_GetInfo socket_getInfo = new Socket_GetInfo(this,"GetMember","/{'worker_name':'"+Worker_Name+"'}");
        while (true){
            if(socket_getInfo.getAnswer){
                SetUI(socket_getInfo.input);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ShowOther_FloatingActionButton:
                toggleFab();
                break;

            case R.id.Add_CareMember_FloatingActionButton:
                intent = new Intent(CareMembersListActivity.this,AddCareMemberActivity.class);
                intent.putExtra("Worker_Name",Worker_Name);
                startActivity(intent);
                break;

            case R.id.Delete_CareMember_FloatingActionButton:
                intent = new Intent(CareMembersListActivity.this,DeleteCareMemberActivity.class);
                intent.putExtra("Worker_Name",Worker_Name);
                startActivity(intent);
                break;

        }

    }

    private void toggleFab() {

        if (isFabOpen) {

            fab_main.setImageResource(R.drawable.ic_menu_white_24dp);
            fab_add.startAnimation(fab_close);
            fab_delete.startAnimation(fab_close);
            fab_add.setClickable(false);
            fab_delete.setClickable(false);
            isFabOpen = false;

        } else {
            fab_main.setImageResource(R.drawable.ic_close_white_24dp);
            fab_add.startAnimation(fab_open);
            fab_delete.startAnimation(fab_open);
            fab_add.setClickable(true);
            fab_delete.setClickable(true);
            isFabOpen = true;
        }
    }

    void SetUI(String input) {
        if (!input.equals("")) {
            ArrayList<PublicFunctions.MemberTag> arrayList = PublicFunctions.getMemberListFromJSONString(input);
            CareMembersListView.setVisibility(View.VISIBLE);
            NonInfoTextView.setVisibility(View.GONE);

            if (CMListViewAdapter.getCount() > 0)
                CMListViewAdapter.removeALL();
            CMListViewAdapter.notifyDataSetChanged();

            for (int i = 0; i < arrayList.size(); i++)
                CMListViewAdapter.addItem(arrayList.get(i).MemberName, arrayList.get(i).MemberGender, arrayList.get(i).MemberAge, arrayList.get(i).Device_Id,arrayList.get(i).Photo);

        }
        else{
            CareMembersListView.setVisibility(View.GONE);
            NonInfoTextView.setVisibility(View.VISIBLE);
            NonInfoTextView.setText("정보가 없습니다.");
        }
    }




}
