package com.hansung.android.ai_speaker_socket;

import android.graphics.Bitmap;

public class CareMembersData {
    private String Photo;
    private String Name;
    private String Gender;
    private String Age;
    private String DeviceId;

    public void setPhoto(String photo) {
        Photo = photo;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public void setAge(String age) {
        this.Age = age;
    }
    public void setGender(String gender) {
        this.Gender = gender;
    }
    public void setDeviceId(String deviceId){this.DeviceId = deviceId;}

    public String getPhoto() {
        return Photo;
    }
    public String getName(){
        return this.Name;
    }
    public String getAge(){
        return this.Age;
    }
    public String getGender(){
        return this.Gender;
    }
    public String getDeviceId(){
        return this.DeviceId;
    }
}