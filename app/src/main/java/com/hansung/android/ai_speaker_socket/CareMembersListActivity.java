package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CareMembersListActivity extends AppCompatActivity {
    String Worker_Name;
    String ip;
    int port;
    TextView NonInfoTextView;
    ListView CareMembersListView;
    CareMembersListViewAdapter CMListViewAdapter = new CareMembersListViewAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_members_list);

        Intent intent = getIntent();
        Worker_Name = intent.getStringExtra("Worker_Name");
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port",-1);

        NonInfoTextView = (TextView)findViewById(R.id.NonInfoTextView_id) ;
        CareMembersListView = (ListView) findViewById(R.id.CareMembersListView_id);
        CareMembersListView.setAdapter(CMListViewAdapter);

        TextView WorkerNameTextView= (TextView)findViewById(R.id.socialWorkerNameTextView_id);
        WorkerNameTextView.setText("사회복지사 "+Worker_Name+"님");

        ListView CareMembersListView = (ListView) findViewById(R.id.CareMembersListView_id);
        CareMembersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CareMembersListActivity.this,HealthInfoActivity.class);
                CareMembersData cmData = (CareMembersData)parent.getAdapter().getItem(position);
                intent.putExtra("MemberName",cmData.getName());
                intent.putExtra("DeviceId",cmData.getDeviceId());
                intent.putExtra("ip",ip);
                intent.putExtra("port",port);
                startActivity(intent);
            }
        });

        FloatingActionButton addMemberButton = (FloatingActionButton)findViewById(R.id.AddMemberButton_id);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CareMembersListActivity.this,AddCareMemberActivity.class);
                intent.putExtra("Worker_Name",Worker_Name);
                intent.putExtra("ip",ip);
                intent.putExtra("port",port);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(port > 0){
            CareMembersListView.setVisibility(View.GONE);
            NonInfoTextView.setVisibility(View.VISIBLE);
            NonInfoTextView.setText("정보 가져오는 중...");


            new Socket_GetInfo(this,ip,port,"GetMember","/{'worker_name':'"+Worker_Name+"'}");
        }
    }

    void SetUI(String input){
        ArrayList<Tag> arrayList = getArrayListFromJSONString(input);
        CareMembersListView.setVisibility(View.VISIBLE);
        NonInfoTextView.setVisibility(View.GONE);

        if(CMListViewAdapter.getCount()>0)
                CMListViewAdapter.removeALL();
        CMListViewAdapter.notifyDataSetChanged();

        for (int i = 0; i < arrayList.size(); i++)
            CMListViewAdapter.addItem(arrayList.get(i).MemberName, arrayList.get(i).MemberGender, arrayList.get(i).MemberAge, arrayList.get(i).Device_Id);

    }

    void NonInfo(){
        CareMembersListView.setVisibility(View.GONE);
        NonInfoTextView.setVisibility(View.VISIBLE);
        NonInfoTextView.setText("정보가 없습니다.");
    }


    protected ArrayList<Tag> getArrayListFromJSONString(String jsonString) {
        ArrayList<Tag> output = new ArrayList();
        try {
            //jsonString = jsonString.substring(1,jsonString.length());
            jsonString = jsonString.replace("\\\"","\"");


            //Log.i("TAG", "jsonString="+jsonString);

            JSONObject root = new JSONObject(jsonString);

            JSONArray jsonArray = root.getJSONArray("data");
            if(jsonArray.length() >0){

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                    Tag thing = new Tag(jsonObject.getString("RegisterTime"),
                            jsonObject.getString("MemberGender"),
                            jsonObject.getString("SocialWorkerName"),
                            jsonObject.getString("Device_Id"),
                            jsonObject.getString("MemberAge"),
                            jsonObject.getString("MemberName")

                    );
                    output.add(thing);
                }
            }
            else{
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;
    }


    class Tag {
        String SocialWorkerName;
        String MemberName;
        String MemberAge;
        String Device_Id;
        String MemberGender;
        String RegisterTime;

        public Tag(String time,String member_Gender,String worker_Name,String Device_Id, String member_Age, String member_Name) {


            this.SocialWorkerName = worker_Name;
            this.MemberName = member_Name;
            this.Device_Id = Device_Id;
            this.MemberAge = member_Age;
            this.MemberGender = member_Gender;
            this.RegisterTime = time;

        }

        public String toString() {
            return String.format("WorkerName : %s, MemberName: %s, MemberAge: %s,  MemberGender: %s, RegisterTime: %s", SocialWorkerName, MemberName,MemberAge,MemberGender,RegisterTime);
        }
    }

}
