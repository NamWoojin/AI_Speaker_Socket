package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class medicineListLinearLayout extends LinearLayout {

    public medicineListLinearLayout(Context context){
        super(context);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.healthinfo_medicine_listview,this,true);
    }
}
