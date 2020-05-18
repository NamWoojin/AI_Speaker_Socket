package com.hansung.android.ai_speaker_socket;

import android.content.Context;
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


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public medicineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.healthinfo_medicine_recyclerview, parent, false) ;
        medicineRecyclerViewAdapter.ViewHolder vh = new medicineRecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(medicineRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.medicineName.setText(mLVIArray.get(position).getMedicineName());
        holder.medicineCycle.setText(mLVIArray.get(position).getMedicineCycle());
        holder.medicineType.setText(mLVIArray.get(position).getMedicineType());
    }

    // getItemCount() - 전체 데이터 개수 리턴.
    @Override
    public int getItemCount() {
        return mLVIArray.size() ;
    }

    public void addItem(ArrayList<medicineListViewItem> mLVIA){
        mLVIArray = mLVIA;
    }




}
