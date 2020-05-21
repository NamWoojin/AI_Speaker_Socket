package com.hansung.android.ai_speaker_socket;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class ListViewItem {
    private int type;

    private ArrayList<medicineListViewItem> mLVI;

    private Drawable morningImage ;
    private Drawable afternoonImage;
    private Drawable nightImage;

    private String sleepTime;
    private String goBedTime;
    private String getUpTime;

    private String heartBeat;
    private String heartTime;

    public void setType(int type) {
        this.type = type ;
    }
    public int getType(){
        return this.type;
    }


    public ArrayList<medicineListViewItem> getmLVI() {
        return mLVI;
    }
    public void setmLVI(ArrayList<medicineListViewItem> mLVI) {
        this.mLVI = mLVI;
    }

    public void setMorningImage(Drawable icon) {
        this.morningImage = icon ;
    }
    public void setAfternoonImage(Drawable icon) {
        this.afternoonImage = icon ;
    }
    public void setNightImage(Drawable icon) {
        this.nightImage = icon ;
    }
    public Drawable getMorningImage(){
        return this.morningImage;
    }
    public Drawable getAfternoonImage(){
        return this.afternoonImage;
    }
    public Drawable getNightImage(){
        return this.nightImage;
    }


    public void setSleepTime(String time){
        this.sleepTime = time;
    }
    public void setGoBedTime(String time){
        this.goBedTime = time;
    }
    public void setGetUpTime(String time){
        this.getUpTime = time;
    }
    public String getSleepTime(){
        return this.sleepTime;
    }
    public String getGoBedTime(){
        return this.goBedTime;
    }
    public String getGetUpTime(){
        return this.getUpTime;
    }


    public void setHeartBeat(String beat){
        this.heartBeat = beat;
    }
    public String getHeartBeat(){
        return this.heartBeat;
    }

    public void setHeartTime(String heartTime) {
        this.heartTime = heartTime;
    }

    public String getHeartTime() {
        return heartTime;
    }
}
