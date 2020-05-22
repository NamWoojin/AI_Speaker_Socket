package com.hansung.android.ai_speaker_socket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HealthInfoActivity extends AppCompatActivity {
    String MemberName;
    String DeviceId;

    LinearLayout nonInfoLinearLayout;

    Boolean breakfast =false;
    Boolean lunch=false;
    Boolean dinner=false;

    Drawable morning_image;
    Drawable lunch_image;
    Drawable dinner_image;
    Drawable morning_not_image;
    Drawable lunch_not_image;
    Drawable dinner_not_image;


    String wholeTime="";
    String sleepTime="";
    String wakeupTime="";
    Boolean viewflag = true;

    SwipeRefreshLayout refreshLayout;
    ListViewAdapter adapter = new ListViewAdapter();
    ListView listView;

    medicineListViewAdapter mLVA = new medicineListViewAdapter();
    ListView medicineListView;
    ArrayList<medicineListViewItem> mLVIArray = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        GetMedicine();
        GetMeal();
        GetSleepTime();
        GetPulse();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_info);

        Intent intent = getIntent();
        MemberName = intent.getStringExtra("MemberName");
        DeviceId = intent.getStringExtra("DeviceId");

        TextView CareMemberNameTextView = (TextView)findViewById(R.id.CareMemberNameTextView_id);
        CareMemberNameTextView.setText(MemberName+"님의 건강정보입니다");

        listView = (ListView)findViewById(R.id.HealthInfoListView_id);
        listView.setAdapter(adapter);

        morning_image = ContextCompat.getDrawable(this,R.drawable.meal_morning);
        lunch_image=ContextCompat.getDrawable(this,R.drawable.meal_afternoon);
        dinner_image=ContextCompat.getDrawable(this,R.drawable.meal_night);
        morning_not_image = ContextCompat.getDrawable(this,R.drawable.meal_morning_not);
        lunch_not_image=ContextCompat.getDrawable(this,R.drawable.meal_afternoon_not);
        dinner_not_image=ContextCompat.getDrawable(this,R.drawable.meal_night_not);

        adapter.setContext(HealthInfoActivity.this);
        adapter.addItem(mLVIArray);
        adapter.addItem(morning_image, lunch_image, dinner_image);
        adapter.addItem(wholeTime,sleepTime,wakeupTime);
        adapter.addItem("","");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(position == 0){
                    intent = new Intent(HealthInfoActivity.this,MedicineEditActivity.class);
                    intent.putExtra("DeviceId",DeviceId);
                }
                else{
                    intent = new Intent(HealthInfoActivity.this,DetailScrollingActivity.class);
                    intent.putExtra("position",position-1);
                    intent.putExtra("DeviceId",DeviceId);
                }
                startActivity(intent);

            }
        });

        FloatingActionButton sendMessegeButton = (FloatingActionButton)findViewById(R.id.sendMessagePopUpButton_id);
        sendMessegeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(HealthInfoActivity.this,SendMessagePopUpActivity.class);
                intent.putExtra("MemberName",MemberName);
                intent.putExtra("device_id",DeviceId);
                startActivity(intent);
            }
        });

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetMedicine();
                GetSleepTime();
                GetMeal();
                GetPulse();
                refreshFinish();

            }

        });
    }

    void refreshFinish(){
        refreshLayout.setRefreshing(false);
    }


    //--------------------------------medicine-----------------------------------------
    void GetMedicine(){
        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+"}";
        new Socket_GetInfo(this,"GetMedicine",params);
    }

    public void SetMedicineUI(String input){
        if(!input.equals("")) {
            ArrayList<PublicFunctions.MedicineTag> arrayList = PublicFunctions.getMedicineFromJSONString(input);
            mLVIArray = new ArrayList<>();
            medicineListViewItem mLVI;
            adapter.removeItem(0);
            for (int i = 0; i < arrayList.size(); i++) {
                mLVI = new medicineListViewItem();
                mLVI.setMedicineName(arrayList.get(i).Name);
                mLVI.setMedicineType(arrayList.get(i).Type);
                mLVI.setMedicineCycle(arrayList.get(i).Cycle);
                mLVIArray.add(mLVI);
                adapter.addItem(mLVIArray);
            }

        }
        else{
            listView.setAdapter(adapter);
        }
    }


    //--------------------------------meal-----------------------------------------
    void GetMeal(){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = date.format(today);

        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+","+PublicFunctions.MakeMsg("from",curDate+" 00:00:00")+","+PublicFunctions.MakeMsg("to",curDate+" 23:59:59")+"}";
        new Socket_GetInfo(this,"GetMeal",params);
    }

    public void CalculateMealTimes(String input){
        ArrayList<PublicFunctions.Tag> arrayList = PublicFunctions.getArrayListFromJSONString(input);

        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).Type.equals("아침")) {
                    breakfast = true;
                } else if (arrayList.get(i).Type.equals("점심")) {
                    lunch = true;
                } else if (arrayList.get(i).Type.equals("저녁")) {
                    dinner = true;
                }
            }
        }
        else {
            Toast.makeText(this, "식사 정보가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void SetMealUI(){
        Drawable breakfast_img = morning_not_image;
        Drawable lunch_img = lunch_not_image;
        Drawable dinner_img = dinner_not_image;

        if(breakfast)
            breakfast_img=morning_image;
        if(lunch)
            lunch_img=lunch_image;
        if(dinner)
            dinner_img = dinner_image;

        adapter.removeItem(1);
        adapter.addItem(breakfast_img, lunch_img, dinner_img);
        listView.setAdapter(adapter);
    }


    //--------------------------------sleep-----------------------------------------
    void GetSleepTime(){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = date.format(today);

        Calendar twoWeekBefore = Calendar.getInstance();
        twoWeekBefore.add(Calendar.DATE, -1);
        String yesterDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(twoWeekBefore.getTime());

        String params = "/{"+ PublicFunctions.MakeMsg("device_id",DeviceId)+","+PublicFunctions.MakeMsg("from",yesterDate+" 00:00:00")+","+PublicFunctions.MakeMsg("to",curDate+" 23:59:59")+"}";
        new Socket_GetInfo(this,"GetSleep",params);
    }


    void CalculateSleepTimes(String input){
        ArrayList<PublicFunctions.Tag> arrayList = PublicFunctions.getArrayListFromJSONString(input);
        ArrayList<PublicFunctions.Tag> sleepListViewItem = new ArrayList<>();
        ArrayList<PublicFunctions.Tag> wakeListViewItem = new ArrayList<>() ;
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


        viewflag = true;
        if(sleepListViewItem.size() != 0 && wakeListViewItem.size() != 0) {

            sleepTime = sleepListViewItem.get(0).Time;
            wakeupTime = wakeListViewItem.get(0).Time;

            if(sleepTime.compareTo(wakeupTime)>0){  //기상시간은 수면시간보다 나중이어야 한다.
                if(sleepListViewItem.size()>1)
                    sleepTime = sleepListViewItem.get(1).Time;
                else
                    viewflag = false;
            }

            if(sleepTime.compareTo(wakeupTime)<0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                ParsePosition pos = new ParsePosition(0);
                Date sleep = sdf.parse(sleepTime,pos);
                pos = new ParsePosition(0);
                Date wake = sdf.parse(wakeupTime,pos);
                long diffDate = wake.getTime()-sleep.getTime();



                long diffHours = diffDate / (60 * 60 * 1000);
                long diffMinutes = diffDate%(60*60*1000) / (60 * 1000);

                wholeTime = diffHours + "시간 "+diffMinutes+"분";
                SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd HH:mm");
                sleepTime = sdf3.format(sleep);
                wakeupTime = sdf3.format(wake);


            }
            else
                viewflag = false;
        }
        else//취침정보 혹은 기상정보 없을 때
            viewflag = false;

    }

    void SetSleepUI(){
        if(viewflag) {
            adapter.removeItem(2);
            adapter.addItem(wholeTime,sleepTime,wakeupTime);
            listView.setAdapter(adapter);
        }
        else{
            adapter.removeItem(2);
            adapter.addItem("정보가 없습니다.","","");
            listView.setAdapter(adapter);
        }
    }



    void GetPulse(){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = date.format(today);

        String params = "/{"+PublicFunctions.MakeMsg("device_id",DeviceId)+","+PublicFunctions.MakeMsg("from",curDate+" 00:00:00")+","+PublicFunctions.MakeMsg("to",curDate+" 23:59:59")+"}";
        new Socket_GetInfo(this,"GetPulse",params);
    }

    public void SetHeartUI(String input){
        ArrayList<PublicFunctions.PulseTag> arrayList = PublicFunctions.getPulseFromJSONString(input);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date i_time, j_time;
        PublicFunctions.PulseTag temp;


        if(arrayList.size()>0){
            if(arrayList.size()>1){
                for (int i = 0; i < arrayList.size(); i++) {
                    for (int j = i; j < arrayList.size(); j++) {
                        ParsePosition pos = new ParsePosition(0);
                        i_time = sdf.parse(arrayList.get(i).Time, pos);
                        pos = new ParsePosition(0);
                        j_time = sdf.parse(arrayList.get(j).Time, pos);
                        if (i_time.compareTo(j_time) < 0) {
                            temp = arrayList.get(i);
                            arrayList.set(i, arrayList.get(j));
                            arrayList.set(j, temp);
                        }
                    }
                }
            }
            adapter.removeItem(3);
            adapter.addItem(arrayList.get(0).Pulse,arrayList.get(0).Time.substring(5,16));
        }
        else{
            adapter.removeItem(3);
            adapter.addItem("정보가 없습니다.","");
        }
    }
}
