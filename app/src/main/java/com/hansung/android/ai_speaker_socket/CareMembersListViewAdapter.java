package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CareMembersListViewAdapter extends BaseAdapter {
    private ArrayList<CareMembersData> careMembersDataList = new ArrayList<CareMembersData>() ;

    @Override
    public int getCount() {
        return careMembersDataList.size() ;
    }


    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return careMembersDataList.get(position) ;
    }

    public void addItem(String name, String gender, String age, String deviceid, String Photo){
        CareMembersData cmData = new CareMembersData();
        cmData.setName(name);
        cmData.setGender(gender);
        cmData.setAge(age);
        cmData.setDeviceId(deviceid);
        cmData.setPhoto(Photo);
        careMembersDataList.add(cmData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.caremembers_listview, null);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.CareMembersName_id) ;
        TextView genderTextView = (TextView)  convertView.findViewById(R.id.CareMembersGender_id);
        TextView ageTextView = (TextView) convertView.findViewById(R.id.CareMembersAge_id) ;
        TextView deviceIdTextView = (TextView)convertView.findViewById(R.id.CareMembersDeviceId_id) ;
        ImageView photoImageView = (ImageView)convertView.findViewById(R.id.CareMemberPhoto_id);

        CareMembersData listViewItem = careMembersDataList.get(position);

        nameTextView.setText(listViewItem.getName());

        if(listViewItem.getGender().equals("Male"))
            genderTextView.setText("남");
        else
            genderTextView.setText("여");
        ageTextView.setText(String.valueOf(listViewItem.getAge()));
        deviceIdTextView.setText(listViewItem.getDeviceId());
        Bitmap bitmap = PublicFunctions.byteArrayToBitmap(listViewItem.getPhoto().getBytes());
        photoImageView.setImageBitmap(PublicFunctions.byteArrayToBitmap(listViewItem.getPhoto().getBytes()));

        return convertView;
    }

    public void removeALL(){
        int size = careMembersDataList.size();
        for(int i = size-1;i>=0;--i)
            careMembersDataList.remove(i);
    }



}
