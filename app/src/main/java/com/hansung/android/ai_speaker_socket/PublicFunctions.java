package com.hansung.android.ai_speaker_socket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PublicFunctions {
//    public static String ip = "192.168.0.112"; // IP
    public static String ip = "192.168.0.27"; // IP
    public static int port = 8888; // PORT

    public static ArrayList<Tag> getArrayListFromJSONString(String jsonString) {
        ArrayList<Tag> output = new ArrayList();
        try {
            jsonString = jsonString.replace("\\\"","\"");
            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("data");
            if(jsonArray.length() >0){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                    Tag thing = new Tag(jsonObject.getString("Type"),
                            jsonObject.getString("Device_id"),
                            jsonObject.getString("Time")
                    );

                    output.add(thing);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static class Tag {
        String Type;
        String Device_id;
        String Time;

        public Tag(String type,String device_di,String time) {

            this.Type = type;
            this.Device_id =device_di;
            this.Time = time;

        }

        public String toString() {
            return String.format("Type : %s, Device_id: %s, Time: %s", Type,Device_id,Time);
        }
    }


    public static ArrayList<MedicineTag> getMedicineFromJSONString(String jsonString) {     //약 정보 Json 해석
        ArrayList<MedicineTag> output = new ArrayList();
        try {
            jsonString = jsonString.replace("\\\"","\"");


            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("data");
            if(jsonArray.length() >0){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                    MedicineTag thing = new MedicineTag(jsonObject.getString("Type"),
                            jsonObject.getString("Device_id"),
                            jsonObject.getString("Medicine_Name"),
                            jsonObject.getString("Cycle")
                    );

                    output.add(thing);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }


    public static class MedicineTag {   //약 정보 데이터 형식
        String Type;
        String Device_id;
        String Name;
        String Cycle;

        public MedicineTag(String type, String device_di, String name, String cycle) {

            this.Type = type;
            this.Device_id = device_di;
            this.Name = name;
            this.Cycle = cycle;

        }

        public String toString() {
            return String.format("Type : %s, Device_id: %s, Name: %s, Cycle: %s", Type, Device_id, Name, Cycle);
        }
    }

    public static ArrayList<MemberTag> getMemberListFromJSONString(String jsonString) { //노인 사용자 Json 해석
        ArrayList<MemberTag> output = new ArrayList();
        try {
            jsonString = jsonString.replace("\\\"","\"");

            JSONObject root = new JSONObject(jsonString);

            JSONArray jsonArray = root.getJSONArray("data");
            if(jsonArray.length() >0){

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                    MemberTag thing = new MemberTag(jsonObject.getString("RegisterTime"),
                            jsonObject.getString("MemberGender"),
                            jsonObject.getString("SocialWorkerName"),
                            jsonObject.getString("Device_id"),
                            jsonObject.getString("MemberAge"),
                            jsonObject.getString("MemberName")

                    );
                    output.add(thing);
                }
            }
            else{
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;
    }


    public static class MemberTag { //노인 사용자 정보 데이터 형식
        String SocialWorkerName;
        String MemberName;
        String MemberAge;
        String Device_Id;
        String MemberGender;
        String RegisterTime;

        public MemberTag(String time,String member_Gender,String worker_Name,String Device_Id, String member_Age, String member_Name) {


            this.SocialWorkerName = worker_Name;
            this.MemberName = member_Name;
            this.Device_Id = Device_Id;
            this.MemberAge = member_Age;
            this.MemberGender = member_Gender;
            this.RegisterTime = time;
        }

        public String toString() {
            return String.format("WorkerName : %s, MemberName: %s, MemberAge: %s,  MemberGender: %s, RegisterTime: %s", SocialWorkerName, MemberName,MemberAge,MemberGender,RegisterTime);
        }
    }

    public static ArrayList<PulseTag> getPulseFromJSONString(String jsonString) { //심박 정보 Json 해석
        ArrayList<PulseTag> output = new ArrayList();
        try {
            jsonString = jsonString.replace("\\\"","\"");

            JSONObject root = new JSONObject(jsonString);

            JSONArray jsonArray = root.getJSONArray("data");
            if(jsonArray.length() >0){

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                    PulseTag thing = new PulseTag(
                            jsonObject.getString("Device_id"),
                            jsonObject.getString("Pulse"),
                            jsonObject.getString("Time")

                    );
                    output.add(thing);
                }
            }
            else{
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;
    }


    public static class PulseTag {   //심박 정보 데이터 형식
        String Device_id;
        String Pulse;
        String Time;

        public PulseTag(String device_Id,String pulse,String time) {


            this.Device_id = device_Id;
            this.Pulse = pulse;
            this.Time=time;

        }

        public String toString() {
            return String.format("Device_Id : %s, Pulse: %s, Time: %s", Device_id,Pulse,Time);
        }
    }

    public static String MakeMsg(String key, String value){
        return "\'"+key+"\':\'"+value+"\'";
    }



}
