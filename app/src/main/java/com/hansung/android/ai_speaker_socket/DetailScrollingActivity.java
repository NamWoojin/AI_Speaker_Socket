package com.hansung.android.ai_speaker_socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailScrollingActivity extends AppCompatActivity implements DetailItemAdapter.OnLoadMoreListener{

    String urlbase = "";
    String DeviceId = "";
    int position = 0;
    int loadNum = 0;


    private DetailItemAdapter mAdapter;
    private ArrayList<DetailItemData> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrolling);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        position = intent.getIntExtra("position",-1);
        DeviceId = intent.getStringExtra("DeviceId");
        urlbase = intent.getStringExtra("urlbase");

        itemList = new ArrayList<DetailItemData>();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.Detail_Recycler_View);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DetailItemAdapter(this,position);
        mAdapter.setLinearLayoutManager(mLayoutManager);
        mAdapter.getDrawables(getResources().getDrawable(R.drawable.o),getResources().getDrawable(R.drawable.x));
        mAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayout mealBar = (LinearLayout)findViewById(R.id.averageMealBarLinearLayout_id);
        LinearLayout sleepBar = (LinearLayout)findViewById(R.id.averageSleepBarLinearLayout_id);
        LinearLayout mealMenuBar = (LinearLayout)findViewById(R.id.detail_meal_menu_linear_layout);
        LinearLayout sleepMenuBar = (LinearLayout)findViewById(R.id.detail_sleep_menu_linear_layout);

        LinearLayout[] linearLayouts = {mealBar,sleepBar,mealMenuBar,sleepMenuBar};

        for(int i = 0;i < linearLayouts.length/2;i++){
            if(i == position) {
                linearLayouts[i].setVisibility(LinearLayout.VISIBLE);
                linearLayouts[i+linearLayouts.length/2].setVisibility(LinearLayout.VISIBLE);
            }
            else {
                linearLayouts[i].setVisibility(LinearLayout.GONE);
                linearLayouts[i+linearLayouts.length/2].setVisibility(LinearLayout.GONE);
            }
        }

        if(position == 0){
            setTitle("식사 패턴");
        }
        else if(position == 1){
            setTitle("수면 패턴");
        }
        else if(position ==2){
            setTitle("심박수");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNum = 0;
        if(position == 0)
            GetMeal();

        else if(position == 1)
            GetSleepTime();


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

    void GetMeal(){
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, loadNum*(-14));
//        String curDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//
//        Calendar twoWeekBefore = Calendar.getInstance();
//        twoWeekBefore.add(Calendar.DATE, (loadNum+1)*(-14));
//        String yesterDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(twoWeekBefore.getTime());
//
//        String params = "?device_id="+DeviceId + "&from="+yesterDate+"%2000:00:00&to="+curDate+"%2023:59:59";
//        String mealUrl = urlbase+params;
//        new GetLogMeal(DetailScrollingActivity.this,mealUrl,"detail",this,loadNum).execute();
    }

    void GetSleepTime(){
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, loadNum*(-14));
//        String curDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//
//        Calendar twoWeekBefore = Calendar.getInstance();
//        twoWeekBefore.add(Calendar.DATE, (loadNum+1)*(-14));
//        String yesterDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(twoWeekBefore.getTime());
//
//        String params = "?device_id="+DeviceId + "&from="+yesterDate+"%2000:00:00&to="+curDate+"%2023:59:59";  //전날 00시부터 오늘 현재시간까지의 기록 조회
//        String sleepUrl = urlbase+params;
//        new GetLogSleepTime(DetailScrollingActivity.this,sleepUrl,"detail",this,loadNum).execute();
    }

    @Override
    public void onLoadMore() {
        mAdapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemList.clear();
                mAdapter.setProgressMore(false);

                if(position == 0){
                    loadNum ++;
                    GetMeal();
                }
                else if(position == 1){
                    loadNum ++;
                    GetSleepTime();
                }


            }
        }, 2000);
    }

    public void AddData(ArrayList<DetailItemData> did){
        mAdapter.addAll(did);
    }
    public void AddMoreData (ArrayList<DetailItemData> did){
        mAdapter.addItemMore(did);
        mAdapter.setMoreLoading(false);
    }

    public void SetAverageSleepTime(String sleepTime,String wakeUpTime,String goBedTime){
        TextView sleepTextView = (TextView)findViewById(R.id.averageSleepTimeTextView_id);
        TextView wakeUpTextView = (TextView)findViewById(R.id.averageWakeUpTimeTextView_id);
        TextView goBedTextView = (TextView)findViewById(R.id.averageGoBedTimeTextView_id);

        sleepTextView.setText(sleepTime);
        wakeUpTextView.setText(wakeUpTime);
        goBedTextView.setText(goBedTime);
    }

    public void SetAverageMealTime(String BreakfastTime,String LunchTime, String DinnerTime){
        TextView BreakfastTextView = (TextView)findViewById(R.id.averageBreakfastTimeTextView_id);
        TextView LunchTextView = (TextView)findViewById(R.id.averageLunchTimeTextView_id);
        TextView DinnerTextView = (TextView)findViewById(R.id.averageDinnerTimeTextView_id);

        BreakfastTextView.setText(BreakfastTime);
        LunchTextView.setText(LunchTime);
        DinnerTextView.setText(DinnerTime);
    }
}
