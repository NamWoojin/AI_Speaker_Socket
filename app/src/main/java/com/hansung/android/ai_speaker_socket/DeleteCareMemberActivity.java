package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteCareMemberActivity extends AppCompatActivity {

    ArrayList<CareMembersData> careMembersDataArrayList = new ArrayList<>();
    DeleteCareMembersListViewAdapter DeleteCMListAdapter = new DeleteCareMembersListViewAdapter();
    RecyclerView mRecyclerView;
    String WorkerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_care_member);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this,"옆으로 밀어서 삭제하세요.", Toast.LENGTH_SHORT).show();
        setTitle("사용자 삭제");

        Intent intent = getIntent();
        WorkerName=intent.getStringExtra("WorkerName");

        mRecyclerView = (RecyclerView) findViewById(R.id.deleteCareMembers_RecyclerView_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DeleteCMListAdapter.addItem(careMembersDataArrayList);
        mRecyclerView.setAdapter(DeleteCMListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCareMemberList();
    }

    public void GetCareMemberList(){
        Socket_GetInfo socket_getInfo = new Socket_GetInfo(this,"GetMember","/{'worker_name':'"+WorkerName+"'}");
        while (true){
            if(socket_getInfo.getAnswer){
                SetUI(socket_getInfo.input);
                break;
            }
        }
    }

    public void SetUI(String input){
        if (!input.equals("")) {
            ArrayList<PublicFunctions.MemberTag> arrayList = PublicFunctions.getMemberListFromJSONString(input);
            ArrayList<CareMembersData> CMdataArrayList = new ArrayList<>();
            CareMembersData CMdata;
            if (DeleteCMListAdapter.getItemCount() > 0)
                DeleteCMListAdapter.removeALL();
            DeleteCMListAdapter.notifyDataSetChanged();

            for (int i = 0; i < arrayList.size(); i++){
                CMdata = new CareMembersData();
                CMdata.setDeviceId(arrayList.get(i).Device_Id);
                CMdata.setAge(arrayList.get(i).MemberAge);
                CMdata.setGender(arrayList.get(i).MemberGender);
                CMdata.setName(arrayList.get(i).MemberName);

                CMdataArrayList.add(CMdata);
            }

            DeleteCMListAdapter.addItem(CMdataArrayList);
        }
        else{

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            String send_msg = "{"+PublicFunctions.MakeMsg("device_id",DeleteCMListAdapter.getItem(position).getDeviceId())+","+
                    PublicFunctions.MakeMsg("woreker_name", WorkerName)+","+
                    PublicFunctions.MakeMsg("member_name", DeleteCMListAdapter.getItem(position).getName())+","+
                    PublicFunctions.MakeMsg("member_age", DeleteCMListAdapter.getItem(position).getAge())+","+
                    PublicFunctions.MakeMsg("member_gender", DeleteCMListAdapter.getItem(position).getGender())+"}";
            Socket_SendInfo socket_sendInfo = new Socket_SendInfo("DeleteMember",send_msg);

            while (true){
                if(socket_sendInfo.sendMsg){
                    if(socket_sendInfo.sendSuccess){
                        careMembersDataArrayList.remove(position);
                        DeleteCMListAdapter.notifyItemRemoved(position);
                        Toast.makeText(DeleteCareMemberActivity.this,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        GetCareMemberList();
                        Toast.makeText(DeleteCareMemberActivity.this,"다시 시도하세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            }
        }
    };
}
