package com.hansung.android.ai_speaker_socket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MedicineEditActivity extends AppCompatActivity {
    ArrayList<medicineListViewItem> mLVIArray = new ArrayList<>();
    medicineRecyclerViewAdapter mRVAdapter = new medicineRecyclerViewAdapter();
    String DeviceId="";
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this,"옆으로 밀어서 삭제하세요.", Toast.LENGTH_SHORT).show();
        setTitle("복약 주기 수정");

        Intent intent = getIntent();
        DeviceId = intent.getStringExtra("DeviceId");

        mRecyclerView = (RecyclerView) findViewById(R.id.medicine_edit_RecyclerView_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRVAdapter.addItem(mLVIArray);
        mRecyclerView.setAdapter(mRVAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton addMedicineButton = (FloatingActionButton)findViewById(R.id.Add_Medicine_Button_id);
        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MedicineEditActivity.this,MedicineAddPopUpActivity.class);
                intent.putExtra("DeviceId",DeviceId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMedicine();
    }


    void GetMedicine(){
        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+"}";
        new Socket_GetInfo(this,"GetMedicine",params,0);
    }

    public void SetMedicineUI(String input){
        if(!input.equals("")) {
            ArrayList<PublicFunctions.MedicineTag> arrayList = PublicFunctions.getMedicineFromJSONString(input);
            mLVIArray = new ArrayList<>();
            medicineListViewItem mLVI;
            for (int i = 0; i < arrayList.size(); i++) {
                mLVI = new medicineListViewItem();
                mLVI.setMedicineName(arrayList.get(i).Name);
                mLVI.setMedicineType(arrayList.get(i).Type);
                mLVI.setMedicineCycle(arrayList.get(i).Cycle);
                mLVIArray.add(mLVI);
            }
        }

        mRVAdapter.addItem(mLVIArray);
        mRecyclerView.setAdapter(mRVAdapter);
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
            String send_msg = "{"+PublicFunctions.MakeMsg("device_id",DeviceId)+","+
                    PublicFunctions.MakeMsg("cycle",mRVAdapter.getItem(position).getMedicineCycle())+","+
                    PublicFunctions.MakeMsg("type",mRVAdapter.getItem(position).getMedicineType())+","+
                    PublicFunctions.MakeMsg("medicine_name",mRVAdapter.getItem(position).getMedicineName())+"}";
            Socket_SendInfo socket_sendInfo = new Socket_SendInfo("DeleteMedicine",send_msg);

            while (true){
                if(socket_sendInfo.sendMsg){
                    if(socket_sendInfo.sendSuccess){
                        mLVIArray.remove(position);
                        mRVAdapter.notifyItemRemoved(position);
                        Toast.makeText(MedicineEditActivity.this,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MedicineEditActivity.this,"옆으로 밀어서 삭제하세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            }
        }
    };


}
