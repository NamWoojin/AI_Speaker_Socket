package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeleteCareMembersListViewAdapter extends RecyclerView.Adapter<DeleteCareMembersListViewAdapter.ViewHolder>{
    private ArrayList<CareMembersData> careMembersDataArrayList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CareMemberName;
        TextView CareMeberGender;
        TextView CareMeberAge;
        TextView CareMeberDeviceId;

        ViewHolder(View itemView) {
            super(itemView) ;
            CareMemberName = (TextView)itemView.findViewById(R.id.DeleteCareMembersName_id);
            CareMeberGender = (TextView)itemView.findViewById(R.id.DeleteCareMembersGender_id);
            CareMeberAge = (TextView)itemView.findViewById(R.id.DeleteCareMembersAge_id);
            CareMeberDeviceId= (TextView)itemView.findViewById(R.id.DeleteCareMembersDeviceId_id);
        }
    }


    @Override
    public DeleteCareMembersListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.delete_care_member_recyclerview, parent, false) ;
        DeleteCareMembersListViewAdapter.ViewHolder vh = new DeleteCareMembersListViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(DeleteCareMembersListViewAdapter.ViewHolder holder, int position) {
        holder.CareMemberName.setText(careMembersDataArrayList.get(position).getName());
        holder.CareMeberGender.setText(careMembersDataArrayList.get(position).getGender());
        holder.CareMeberAge.setText(careMembersDataArrayList.get(position).getAge());
        holder.CareMeberDeviceId.setText(careMembersDataArrayList.get(position).getDeviceId());
    }

    @Override
    public int getItemCount() {
        return careMembersDataArrayList.size() ;
    }

    public CareMembersData getItem(int position) {
        return careMembersDataArrayList.get(position) ;
    }

    public void addItem(ArrayList<CareMembersData> arrayList){
        careMembersDataArrayList = arrayList;
    }

    public void removeALL(){
        int size = careMembersDataArrayList.size();
        for(int i = size-1;i>=0;--i)
            careMembersDataArrayList.remove(i);
    }


}
