package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_MEDICINE = 0 ;
    private static final int ITEM_VIEW_TYPE_MEAL = 1 ;
    private static final int ITEM_VIEW_TYPE_SLEEP = 2;
    private static final int ITEM_VIEW_TYPE_BEAT = 3;
    private static final int ITEM_VIEW_TYPE_MAX = 4 ;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    Context context;

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX ;
    }

    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            ListViewItem listViewItem = listViewItemList.get(position);

            switch (viewType) {
                case ITEM_VIEW_TYPE_MEDICINE:
                    convertView = inflater.inflate(R.layout.healthinfo_medicine, parent, false);
                    TextView medicineName;
                    TextView medicineCycle;
                    TextView medicineType;
                    LinearLayout medicineLL = (LinearLayout)convertView.findViewById(R.id.medicine_healthInfo_LinearLayout_id);
                    LinearLayout nonInfoLinearLayout = (LinearLayout)convertView.findViewById(R.id.medicine_nonInfo_LinearLayout_id);

                    if(listViewItem.getmLVI().size()>0) {
                        nonInfoLinearLayout.setVisibility(View.GONE);
                        for (int i = 0; i < listViewItem.getmLVI().size(); i++) {
                            medicineListLinearLayout mLLL = new medicineListLinearLayout(this.context);
                            medicineName = (TextView) mLLL.findViewById(R.id.medicine_name_id);
                            medicineCycle = (TextView) mLLL.findViewById(R.id.medicine_cycle_id);
                            medicineType = (TextView) mLLL.findViewById(R.id.medicine_type_id);
                            medicineName.setText(listViewItem.getmLVI().get(i).getMedicineName());
                            medicineCycle.setText(listViewItem.getmLVI().get(i).getMedicineCycle());
                            medicineType.setText(listViewItem.getmLVI().get(i).getMedicineType());
                            medicineLL.addView(mLLL);
                        }
                    }
                    else
                        nonInfoLinearLayout.setVisibility(View.VISIBLE);


                    break;
                case ITEM_VIEW_TYPE_MEAL:
                    convertView = inflater.inflate(R.layout.healthinfo_meal,parent, false);

                    ImageView morningImageView = (ImageView) convertView.findViewById(R.id.meal_morning_id) ;
                    ImageView afternoonImageView = (ImageView) convertView.findViewById(R.id.meal_afternoon_id) ;
                    ImageView nightImageView = (ImageView) convertView.findViewById(R.id.meal_night_id) ;

                    morningImageView.setImageDrawable(listViewItem.getMorningImage());
                    afternoonImageView.setImageDrawable(listViewItem.getAfternoonImage());
                    nightImageView.setImageDrawable(listViewItem.getNightImage());

                    break;
                case ITEM_VIEW_TYPE_SLEEP:
                    convertView = inflater.inflate(R.layout.healthinfo_sleep,parent, false);

                    TextView sleepText = (TextView)convertView.findViewById(R.id.sleepText_id);
                    LinearLayout sleepInfoLinearLayout = (LinearLayout)convertView.findViewById(R.id.sleepInfoLinearLayout_id);
                    if(listViewItem.getSleepTime().equals("정보가 없습니다.")){
                        sleepText.setVisibility(View.VISIBLE);
                        sleepInfoLinearLayout.setVisibility(LinearLayout.GONE);
                        sleepText.setText(listViewItem.getSleepTime());

                    }
                    else{
                        sleepText.setVisibility(View.GONE);
                        sleepInfoLinearLayout.setVisibility(LinearLayout.VISIBLE);
                        TextView sleepTextView = (TextView)convertView.findViewById(R.id.sleepTime_id);
                        TextView goBedTextView = (TextView)convertView.findViewById(R.id.goBedTime_id);
                        TextView getUpTextView = (TextView)convertView.findViewById(R.id.getUpTime_id);

                        sleepTextView.setText(listViewItem.getSleepTime());
                        goBedTextView.setText(listViewItem.getGoBedTime());
                        getUpTextView.setText(listViewItem.getGetUpTime());
                    }

                    break;

                case ITEM_VIEW_TYPE_BEAT:
                    convertView = inflater.inflate(R.layout.healthinfo_heartbeat,parent, false);
                    LinearLayout nonInfoHeartLinearLayout = (LinearLayout)convertView.findViewById(R.id.heartbeat_nonInfo_LinearLayout_id);
                    LinearLayout InfoHeartLinearLayout = (LinearLayout)convertView.findViewById(R.id.heartbeat_LinearLayout_id);
                    if(!listViewItem.getHeartBeat().equals("정보가 없습니다.")) {
                        nonInfoHeartLinearLayout.setVisibility(View.GONE);
                        InfoHeartLinearLayout.setVisibility(View.VISIBLE);
                        TextView heartbeatTextView = (TextView) convertView.findViewById(R.id.HeartBeat_TextView_id);
                        TextView heartbeatTimeTextView = (TextView) convertView.findViewById(R.id.heart_Time_HealthInfo_TextView_id);
                        heartbeatTextView.setText(listViewItem.getHeartBeat());
                        heartbeatTimeTextView.setText(listViewItem.getHeartTime() + "에 측정되었습니다.");
                    }
                    else{
                        InfoHeartLinearLayout.setVisibility(View.GONE);
                        nonInfoHeartLinearLayout.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(ArrayList<medicineListViewItem> mLVI){
        ListViewItem item = new ListViewItem();
        item.setType(ITEM_VIEW_TYPE_MEDICINE) ;
        item.setmLVI(mLVI);
        if(listViewItemList.size()>3)
            listViewItemList.set(0,item);
        else
            listViewItemList.add(0,item);
    }
    public void addItem(Drawable morningImage, Drawable afternoonImage,Drawable nightImage) {
        ListViewItem item = new ListViewItem() ;
        item.setType(ITEM_VIEW_TYPE_MEAL) ;
        item.setMorningImage(morningImage);
        item.setAfternoonImage(afternoonImage);
        item.setNightImage(nightImage);
        if(listViewItemList.size()>3)
            listViewItemList.set(1,item);
        else
            listViewItemList.add(1,item);
    }

    public void addItem(String sleepTime,String goBedTime,String getUpTime) {
        ListViewItem item = new ListViewItem() ;
        item.setType(ITEM_VIEW_TYPE_SLEEP) ;
        item.setSleepTime(sleepTime);
        item.setGoBedTime(goBedTime);
        item.setGetUpTime(getUpTime);
        if(listViewItemList.size()>3)
            listViewItemList.set(2,item);
        else
            listViewItemList.add(2,item);
    }

    public void addItem(String heartBeat,String time) {
        ListViewItem item = new ListViewItem() ;
        item.setType(ITEM_VIEW_TYPE_BEAT) ;
        item.setHeartBeat(heartBeat);
        item.setHeartTime(time);
        if(listViewItemList.size()>3)
            listViewItemList.set(3,item);
        else
            listViewItemList.add(3,item);
    }

    public void removeItem(int index){
        listViewItemList.remove(index);
    }

    public void setContext(Context context){
        this.context = context;
    }
}
