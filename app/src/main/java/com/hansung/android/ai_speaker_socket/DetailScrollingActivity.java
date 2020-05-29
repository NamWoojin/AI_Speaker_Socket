package com.hansung.android.ai_speaker_socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailScrollingActivity extends AppCompatActivity implements DetailItemAdapter.OnLoadMoreListener{

    String DeviceId = "";
    int position = 0;
    int loadNum = 0;

    String MealBreakfastTime = "";
    String MealLunchTime = "";
    String MealDinnerTime = "";

    String SleepAverageTime="";
    String SleepWakeUpTime ="";
    String SleepGoBedTime="";

    String averagePulse = "";

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
        LinearLayout pulseBar = (LinearLayout)findViewById(R.id.averageHeartBeatBarLinearLayout_id);
        LinearLayout mealMenuBar = (LinearLayout)findViewById(R.id.detail_meal_menu_linear_layout);
        LinearLayout sleepMenuBar = (LinearLayout)findViewById(R.id.detail_sleep_menu_linear_layout);
        LinearLayout pulseMenuBar = (LinearLayout)findViewById(R.id.detail_heartbeat_menu_linear_layout);
        LinearLayout[] linearLayouts = {mealBar,sleepBar,pulseBar,mealMenuBar,sleepMenuBar,pulseMenuBar};

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
    protected void onResume() {
        super.onResume();
        loadNum = 0;
        if(position == 0)
            GetMeal();

        else if(position == 1)
            GetSleepTime();

        else if(position == 2)
            GetPulse();


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

                else if(position == 1){
                    loadNum ++;
                    GetPulse();
                }


            }
        }, 2000);
    }

    //-------------------------Meal---------------------------------
    int MealReadTerm = 14;
    void GetMeal(){ //식사 상세 정보 불러오기
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, loadNum*(-MealReadTerm));
        String curDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        Calendar twoWeekBefore = Calendar.getInstance();
        twoWeekBefore.add(Calendar.DATE, (loadNum+1)*(-MealReadTerm));
        String yesterDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(twoWeekBefore.getTime());

        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+","+PublicFunctions.MakeMsg("from",yesterDate+" 00:00:00")+","+PublicFunctions.MakeMsg("to",curDate+" 23:59:59")+"}";
        new Socket_GetInfo(this,"GetMeal",params,loadNum);
    }

    public ArrayList<DetailItemData> MakeMealMaterial(String input){//json 식사정보 분해 후 가공
        ArrayList<PublicFunctions.Tag> arrayList = PublicFunctions.getArrayListFromJSONString(input);

        ArrayList<DetailItemData> itemData = new ArrayList<>();

        if(arrayList.size()==0) {
            if(loadNum>0)
                --loadNum;
            return itemData;
        }

        String[] BreakfastTime = new String[]{"0","0","0","0","0","0","0"};
        String[] LunchTime = new String[]{"0","0","0","0","0","0","0"};
        String[] DinnerTime = new String[]{"0","0","0","0","0","0","0"};
        DetailItemData did;

        for (int i = 0; i < MealReadTerm; i++) {
            did = new DetailItemData();
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DATE, (loadNum) * (-MealReadTerm) - i);
            did.setMealDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(day.getTime()));
            itemData.add(did);
        }
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 0; j < itemData.size(); j++) {
                    if (arrayList.get(i).Time.substring(0, 10).equals(itemData.get(j).getMealDate())) {

                        if (arrayList.get(i).Type.equals("아침")) {
                            if(j < 7)
                                BreakfastTime[j] = arrayList.get(i).Time.substring(11, 16);
                            itemData.get(j).setBreakfast(true);
                        } else if (arrayList.get(i).Type.equals("점심")){
                            if(j < 7)
                                LunchTime[j] = arrayList.get(i).Time.substring(11, 16);
                            itemData.get(j).setLunch(true);
                        }else if (arrayList.get(i).Type.equals("저녁")){
                            if(j < 7)
                                DinnerTime[j] = arrayList.get(i).Time.substring(11,16);
                            itemData.get(j).setDinner(true);
                        }
                        break;
                    }
                }
            }

            if(loadNum == 0){//평균 식사 시간 계산(맨 처음 1회만)
                MealBreakfastTime = CalculateAverageTime(BreakfastTime);
                MealLunchTime = CalculateAverageTime(LunchTime);
                MealDinnerTime = CalculateAverageTime(DinnerTime);
            }
        }

        return itemData;
    }

    public void SetAverageMealTime(){//평균시간 UI 설정
        TextView BreakfastTextView = (TextView)findViewById(R.id.averageBreakfastTimeTextView_id);
        TextView LunchTextView = (TextView)findViewById(R.id.averageLunchTimeTextView_id);
        TextView DinnerTextView = (TextView)findViewById(R.id.averageDinnerTimeTextView_id);
        BreakfastTextView.setText(MealBreakfastTime);
        LunchTextView.setText(MealLunchTime);
        DinnerTextView.setText(MealDinnerTime);
    }


    String CalculateAverageTime(String[] TimeArray){//평균시간 계산
        int countNum=0;
        int wholeTime = 0;
        int averageNum = 0;
        for(int i = 0; i< TimeArray.length;i++){
            if(!TimeArray[i].equals("0")) {
                wholeTime += CalculateMinutefromString(TimeArray[i]);
                ++countNum;
            }
        }
        if(countNum != 0)
            averageNum = wholeTime / countNum;

        return averageNum/60 + "시 "+averageNum % 60 +"분";

    }

    int CalculateMinutefromString(String st){// HH:mm => 분단위 시간으로 변환
        String[] stArray = st.split(":");
        int time = 0;

        try{
            time = Integer.parseInt(stArray[0].trim());
            time = (time * 60) + Integer.parseInt(stArray[1]);
        }
        catch (NumberFormatException e){}
        return time;
    }


    //---------------------------Sleep--------------------------------
    int SleepReadTerm = 14;
    void GetSleepTime(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, loadNum*(-SleepReadTerm));
        String curDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        Calendar twoWeekBefore = Calendar.getInstance();
        twoWeekBefore.add(Calendar.DATE, (loadNum+1)*(-SleepReadTerm));
        String yesterDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(twoWeekBefore.getTime());

        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+","+PublicFunctions.MakeMsg("from",yesterDate+" 00:00:00")+","+PublicFunctions.MakeMsg("to",curDate+" 23:59:59")+"}";
        new Socket_GetInfo(this,"GetSleep",params,loadNum);
    }

    public ArrayList<DetailItemData> MakeSleepMaterial(String input){
        ArrayList<PublicFunctions.Tag> arrayList = PublicFunctions.getArrayListFromJSONString(input);
        ArrayList<PublicFunctions.Tag> sleepListViewItem = new ArrayList<>();
        ArrayList<PublicFunctions.Tag> wakeListViewItem = new ArrayList<>() ;
        ArrayList<DetailItemData> itemData = new ArrayList<>();
        DetailItemData did;

        if(arrayList.size()==0) {
            if (loadNum > 0)
                --loadNum;
            return itemData;
        }

        //가장 최근 시간의 기록이 리스트의 맨 앞, 내림차순
        for(int i = 0; i< arrayList.size();i++){
            if(arrayList.get(i).Type.equals("취침")){
                if(sleepListViewItem.size()== 0)
                    sleepListViewItem.add(arrayList.get(i));

                for(int j =0; j< sleepListViewItem.size();j++){
                    if(arrayList.get(i).Time.compareTo(sleepListViewItem.get(j).Time) > 0){
                        sleepListViewItem.add(j,arrayList.get(i));
                        break;
                    }
                    else
                        continue;
                }
            }
            else if(arrayList.get(i).Type.equals("기상")){
                if(wakeListViewItem.size()== 0)
                    wakeListViewItem.add(arrayList.get(i));

                for(int j =0; j< wakeListViewItem.size();j++){
                    if(arrayList.get(i).Time.compareTo(wakeListViewItem.get(j).Time) > 0){
                        wakeListViewItem.add(j,arrayList.get(i));
                        break;
                    }
                    else
                        continue;
                }
            }
        }

        for(int i= 0;i<SleepReadTerm;i++){
            did = new DetailItemData();
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DATE, (loadNum)*(-SleepReadTerm)-i);
            did.setDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(day.getTime()));
            itemData.add(did);
        }

        for(int i = 0; i< itemData.size();i++){
            for(int j = 0; j<sleepListViewItem.size();j++){
                if(itemData.get(i).getDate().equals(sleepListViewItem.get(j).Time.substring(0,10))){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    ParsePosition pos = new ParsePosition(0);
                    Date sleep = sdf.parse(sleepListViewItem.get(j).Time,pos);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    itemData.get(i).setGoBedTime(sdf1.format(sleep));
                    break;
                }
            }
            for(int j = 0; j<wakeListViewItem.size();j++){
                if(itemData.get(i).getDate().equals(wakeListViewItem.get(j).Time.substring(0,10))){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    ParsePosition pos = new ParsePosition(0);
                    Date sleep = sdf.parse(wakeListViewItem.get(j).Time,pos);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    itemData.get(i).setWakeUpTime(sdf1.format(sleep));
                    break;
                }
            }
        }
        if(loadNum == 0) {
            SleepAverageTime = CalculateSleepAverageTime(itemData);
            SleepWakeUpTime = CalculateAverageTime(itemData, 1);
            SleepGoBedTime = CalculateAverageTime(itemData, 0);
        }
        return itemData;


    }

    String CalculateSleepAverageTime(ArrayList<DetailItemData> itemData){
        int countNum=0;
        int wholeTime=0;
        for(int i = 0;i<7;i++){
            if(!itemData.get(i).getWakeUpTime().equals("") && !itemData.get(i+1).getGoBedTime().equals("")){
                if(CalculateMinutefromString(itemData.get(i).getGoBedTime()) <360)// 아침 6시 이전 취침
                    wholeTime += CalculateMinutefromString(itemData.get(i).getWakeUpTime()) - CalculateMinutefromString(itemData.get(i).getGoBedTime());
                else
                    wholeTime += CalculateMinutefromString(itemData.get(i).getWakeUpTime()) +1440 - CalculateMinutefromString(itemData.get(i).getGoBedTime());
                countNum++;
            }

        }
        int averageNum = 0;
        if(countNum != 0)
            averageNum = wholeTime/countNum;

        return averageNum/60 + "시간 "+averageNum % 60 +"분";
    }

    String CalculateAverageTime(ArrayList<DetailItemData> itemData,int mode){
        int countNum=0;
        int wholeTime = 0;
        String string = "";
        int averageNum = 0;

        for(int i = 0; i < 7;i++){

            if(mode == 0)
                string = itemData.get(i).getGoBedTime();
            else
                string  = itemData.get(i).getWakeUpTime();

            if(!string.equals("")){
                int time=CalculateMinutefromString(string);
                if(time<360 && mode == 0) // 새벽 취침 평균 시간 구하기
                    time += 1440;
                wholeTime+=time;
                countNum ++;
            }

        }
        if(countNum != 0)
            averageNum=wholeTime/countNum;
        int hour = averageNum/60;
        int minute = averageNum % 60;
        if(hour>= 24)
            hour -= 24;
        return hour + "시 "+minute+"분";

    }

    public void SetAverageSleepTime(){
        TextView sleepTextView = (TextView)findViewById(R.id.averageSleepTimeTextView_id);
        TextView wakeUpTextView = (TextView)findViewById(R.id.averageWakeUpTimeTextView_id);
        TextView goBedTextView = (TextView)findViewById(R.id.averageGoBedTimeTextView_id);

        sleepTextView.setText(SleepAverageTime);
        wakeUpTextView.setText(SleepWakeUpTime);
        goBedTextView.setText(SleepGoBedTime);
    }


    //----------------------------Pulse-----------------------------------------------
    void GetPulse(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, loadNum*(-14));
        String curDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        Calendar twoWeekBefore = Calendar.getInstance();
        twoWeekBefore.add(Calendar.DATE, (loadNum+1)*(-14));
        String yesterDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(twoWeekBefore.getTime());

        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+","+PublicFunctions.MakeMsg("from",yesterDate+" 00:00:00")+","+PublicFunctions.MakeMsg("to",curDate+" 23:59:59")+"}";
        new Socket_GetInfo(this,"GetPulse",params,loadNum);
    }

    public ArrayList<DetailItemData> MakePulseMaterial(String input){
        ArrayList<PublicFunctions.PulseTag> arrayList = PublicFunctions.getPulseFromJSONString(input);
        ArrayList<DetailItemData> DIDArrayList = new ArrayList<>();
        DetailItemData DID;

        for (int i = arrayList.size()-1; i >= 0; --i) {
            DID = new DetailItemData();
            DID.setPulse(arrayList.get(i).Pulse);
            DID.setPulseDate(arrayList.get(i).Time.substring(0, 16));
            DIDArrayList.add(DID);
        }

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        int averagePulseInt = 0;
        int count = 0;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date date;
        for(int i = 0;i<arrayList.size();++i){
            try {
                date = new Date();
                date = transFormat.parse(arrayList.get(i).Time);
            }
            catch (ParseException e){break;}

            if(cal.getTime().compareTo(date)<0){
                ++count;
                averagePulseInt += Integer.parseInt(arrayList.get(i).Pulse);
            }
        }
        if(count != 0)
            averagePulseInt /= count;

        averagePulse = Integer.toString(averagePulseInt);

        return DIDArrayList;
    }

    public void SetAveragePulse(){
        TextView pulseTextView = (TextView)findViewById(R.id.averagePulseTextView_id);
        pulseTextView.setText(averagePulse + " bpm");
    }



    public void AddData(ArrayList<DetailItemData> did){
        mAdapter.addAll(did);
    }
    public void AddMoreData (ArrayList<DetailItemData> did){
        mAdapter.addItemMore(did);
        mAdapter.setMoreLoading(false);
    }

    public void Toast(String input){
        if(input.length()>1400)
            Toast.makeText(DetailScrollingActivity.this,input.substring(1400),Toast.LENGTH_LONG).show();
    }




}
