package com.hansung.android.ai_speaker_socket;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetailItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ArrayList<DetailItemData> itemList;

    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int viewMode = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    Drawable o;
    Drawable x;

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public DetailItemAdapter(OnLoadMoreListener onLoadMoreListener,int viewMode) {
        this.onLoadMoreListener=onLoadMoreListener;
        this.viewMode = viewMode;
        itemList =new ArrayList<>();
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    public void getDrawables(Drawable OO, Drawable XX){
        o = OO;
        x = XX;
    }

    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            if(viewMode == 0)
                return new MealViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_meal_recyclerview, parent, false));

            else if(viewMode == 1)
                return new SleepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_sleep_recyclerview, parent, false));
            else
                return new PulseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_heartbeat_recyclerview,parent,false));

        }
        else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }

    public void addAll(List<DetailItemData> lst){
        itemList.clear();
        itemList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<DetailItemData> lst){
        itemList.addAll(lst);
        notifyItemRangeChanged(0,itemList.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DetailItemData did = (DetailItemData) itemList.get(position);
        if(holder instanceof MealViewHolder){
            ((MealViewHolder)holder).MealDate.setText(did.getMealDate());
            if(did.getBreakfast())
                ((MealViewHolder)holder).BreakFast.setImageDrawable(o);
            else
                ((MealViewHolder)holder).BreakFast.setImageDrawable(x);
            if(did.getLunch())
                ((MealViewHolder)holder).Lunch.setImageDrawable(o);
            else
                ((MealViewHolder)holder).Lunch.setImageDrawable(x);
            if(did.getDinner())
                ((MealViewHolder)holder).Dinner.setImageDrawable(o);
            else
                ((MealViewHolder)holder).Dinner.setImageDrawable(x);
        }
        else if (holder instanceof SleepViewHolder) {
            ((SleepViewHolder) holder).SleepDate.setText(did.getDate());
            ((SleepViewHolder) holder).WakeUp.setText(did.getWakeUpTime());
            ((SleepViewHolder) holder).goBed.setText(did.getGoBedTime());
        }
        else if(holder instanceof PulseViewHolder){
            ((PulseViewHolder) holder).Date.setText(did.getPulseDate());
            ((PulseViewHolder) holder).Pulse.setText(did.getPulse());

        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading=isMoreLoading;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    itemList.add(null);
                    notifyItemInserted(itemList.size() - 1);
                }
            });
        } else {
            itemList.remove(itemList.size() - 1);
            notifyItemRemoved(itemList.size());
        }
    }

    static class SleepViewHolder extends RecyclerView.ViewHolder {
        public TextView SleepDate;
        public TextView WakeUp;
        public TextView goBed;

        public SleepViewHolder(View v) {
            super(v);
            SleepDate = (TextView) v.findViewById(R.id.detail_sleep_date_textview_id);
            WakeUp = (TextView) v.findViewById(R.id.detail_sleep_wakeup_textview_id);
            goBed = (TextView) v.findViewById(R.id.detail_sleep_gobed_textview_id);
        }
    }

    static class MealViewHolder extends RecyclerView.ViewHolder{
        public TextView MealDate;
        public ImageView BreakFast;
        public ImageView Lunch;
        public ImageView Dinner;

        public MealViewHolder(View v){
            super(v);
            MealDate = (TextView)v.findViewById(R.id.detail_meal_date_textview_id);
            BreakFast= (ImageView)v.findViewById(R.id.detail_meal_breakfast_ImageView_id);
            Lunch = (ImageView)v.findViewById(R.id.detail_meal_lunch_ImageView_id);
            Dinner = (ImageView)v.findViewById(R.id.detail_meal_dinner_ImageView_id);
        }
    }

    static class PulseViewHolder extends RecyclerView.ViewHolder {
        public TextView Date;
        public TextView Pulse;

        public PulseViewHolder(View v) {
            super(v);
            Date = (TextView) v.findViewById(R.id.detail_heartbeat_date_textview_id);
            Pulse = (TextView) v.findViewById(R.id.detail_heartbeat_pulse_textview_id);

        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;
        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }

}