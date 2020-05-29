package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Socket_GetInfo {
    String ip = PublicFunctions.ip;
    int port = PublicFunctions.port;
    Context context;
    String send_msg ="";
    String mode="";
    String remark = "";
    String input="";
    String app = "app/";
    Boolean getAnswer = false;
    Boolean detail = false;
    int loadNum = 0;

    ArrayList<DetailItemData> itemData = new ArrayList<>();



    public Socket_GetInfo(Context context,String mode,String send_msg){
        this.send_msg = send_msg;
        this.mode = mode;
        this.context = context;
        checkStart.start();
        DoNext();
    }

    public Socket_GetInfo(Context context,String mode,String send_msg,int loadNum){
        this.send_msg = send_msg;
        this.mode = mode;
        this.context = context;
        this.detail = true;
        this.loadNum = loadNum;
        checkStart.start();
        DoNextDetail();
    }

    public Socket_GetInfo(Context context, String mode, String send_msg,String remark){
        this.send_msg = send_msg;
        this.mode = mode;
        this.remark = remark;
        this.context = context;
        checkStart.start();
        DoNext();
    }

    void DoNext(){
        while(true) {
            if (getAnswer) {
                if(!remark.equals("")){
                    ((DeleteCareMemberActivity)context).SetUI(input);
                }
                else{
                    switch (mode) {
                        case "GetMember":
                            ((CareMembersListActivity) context).SetUI(input);
                            break;
                        case "GetMedicine":
                            ((HealthInfoActivity) context).SetMedicineUI(input);
                            break;
                        case "GetSleep":
                            ((HealthInfoActivity) context).SetSleepUI();
                            break;
                        case "GetMeal":
                            ((HealthInfoActivity) context).SetMealUI();
                            break;
                        case "GetPulse":
                            ((HealthInfoActivity) context).SetHeartUI(input);
                    }
                }
                break;
            }
        }
    }


    void DoNextDetail(){
        while(true) {
            if (getAnswer) {
                if (loadNum == 0) {

                    switch (mode) {
                        case  "GetMedicine":
                            ((MedicineEditActivity) context).SetMedicineUI(input);
                            break;
                        case "GetMeal":
                            ((DetailScrollingActivity) context).AddData(itemData);
                            ((DetailScrollingActivity) context).SetAverageMealTime();
                            break;
                        case "GetSleep":
                            ((DetailScrollingActivity) context).AddData(itemData);
                            ((DetailScrollingActivity) context).SetAverageSleepTime();
                            break;
                        case "GetPulse":
                            ((DetailScrollingActivity) context).AddData(itemData);
                            ((DetailScrollingActivity) context).SetAveragePulse();
                    }
                }
                else {
                    ((DetailScrollingActivity) context).AddMoreData(itemData);
                }
            break;
            }
        }
    }


    private Thread checkStart = new Thread() {
        private Socket socket;
        OutputStream os;
        InputStream is;
        byte[]bytes;
        int size = 0;
        boolean socketConnected = false;
        public void run() {
            try {
                Log.i("IP_n_Port",ip+"________"+port);
                //socket = new Socket(ip,port);
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                socket.connect(socketAddress,3000);
                os = socket.getOutputStream();
                String output = app+mode+send_msg;
                Log.i("GetInfo_output",output);
                bytes = output.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(bytes.length);
                os.write(buffer.array(),0,4);
                os.write(bytes);
                os.flush();
                socketConnected = true;
            } catch (IOException e) {
                Log.i("TAG","연결 실패");
                socketConnected = false;
            }


            try {
                if(socketConnected) {
                    while (true) {
                        if (true) {
                            is = socket.getInputStream();
                            bytes = new byte[4];
                            size = is.read(bytes, 0, 4);
                            ByteBuffer buffer = ByteBuffer.wrap(bytes);
                            buffer.order(ByteOrder.LITTLE_ENDIAN);
                            int length = buffer.getInt();
                            bytes = new byte[length];
                            //size = is.read(bytes, 0, length);

                            int bytesRead = 0;
                            String response = "";

                            while (bytesRead < length) {
                                int result = is.read(bytes, bytesRead, length);
                                if (result == -1) break;
                                response= new String(bytes, bytesRead, result, "UTF-8");
                                input += response;
                                bytesRead += result;
                            }

                            if (size > 0) {
                                Log.i("GetInfo_input", input);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }

                socket.close();

                if(!input.equals(""))
                    DoCalculate();

                getAnswer = true;

            } catch (IOException e) {

            }


        }
    };


    void DoCalculate(){
        if(detail){
            switch (mode) {
                case "GetMeal":
                    itemData=((DetailScrollingActivity) context).MakeMealMaterial(input);
                    break;
                case "GetSleep":
                    itemData=((DetailScrollingActivity) context).MakeSleepMaterial(input);
                    break;
                case "GetPulse":
                    itemData=((DetailScrollingActivity) context).MakePulseMaterial(input);
                    break;
            }
        }
        else {
            switch (mode) {
                case "GetMeal":
                    ((HealthInfoActivity) context).CalculateMealTimes(input);
                    break;
                case "GetSleep":
                    ((HealthInfoActivity) context).CalculateSleepTimes(input);
                    break;

            }
        }
    }


}
