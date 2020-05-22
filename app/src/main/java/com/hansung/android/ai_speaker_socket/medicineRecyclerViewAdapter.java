package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class medicineRecyclerViewAdapter extends RecyclerView.Adapter<medicineRecyclerViewAdapter.ViewHolder>{

    private ArrayList<medicineListViewItem> mLVIArray = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName;
        TextView medicineCycle;
        TextView medicineType;

        ViewHolder(View itemView) {
            super(itemView) ;
            medicineName = (TextView)itemView.findViewById(R.id.medicine_RecyclerView_name_id);
            medicineCycle = (TextView)itemView.findViewById(R.id.medicine_RecyclerView_cycle_id);
            medicineType = (TextView)itemView.findViewById(R.id.medicine_RecyclerView_type_id);
        }
    }


    @Override
    public medicineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.healthinfo_medicine_recyclerview, parent, false) ;
        medicineRecyclerViewAdapter.ViewHolder vh = new medicineRecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(medicineRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.medicineName.setText(mLVIArray.get(position).getMedicineName());
        holder.medicineCycle.setText(mLVIArray.get(position).getMedicineCycle());
        holder.medicineType.setText(mLVIArray.get(position).getMedicineType());
    }

    @Override
    public int getItemCount() {
        return mLVIArray.size() ;
    }

    public medicineListViewItem getItem(int position) {
        return mLVIArray.get(position) ;
    }

    public void addItem(ArrayList<medicineListViewItem> mLVIA){
        mLVIArray = mLVIA;
    }




}
