package com.hansung.android.ai_speaker_socket;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class medicineListViewAdapter extends BaseAdapter {
    private ArrayList<medicineListViewItem> medicineListItem = new ArrayList<medicineListViewItem>() ;

    @Override
    public int getCount() {
        return medicineListItem.size() ;
    }


    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return medicineListItem.get(position) ;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();



        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.healthinfo_medicine_listview, null);
        }

        TextView medicineNameTextView = (TextView)convertView.findViewById(R.id.medicine_name_id);
        TextView medicineCycleTextView = (TextView)convertView.findViewById(R.id.medicine_cycle_id);
        TextView medicineTypeTextView = (TextView)convertView.findViewById(R.id.medicine_type_id);

        medicineListViewItem mLVI = medicineListItem.get(position);
        medicineNameTextView.setText(mLVI.getMedicineName());
        medicineCycleTextView.setText(mLVI.getMedicineCycle());
        medicineTypeTextView.setText(mLVI.getMedicineType());

        return convertView;
    }

    public void addItem(medicineListViewItem mLVI){
        medicineListItem.add(mLVI);

    }



}
