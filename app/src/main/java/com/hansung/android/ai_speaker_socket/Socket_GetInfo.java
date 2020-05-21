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
    Context context;
    String ip = PublicFunctions.ip;
    int port = PublicFunctions.port;
    String send_msg;
    String mode="";
    Boolean getAnswer = false;
    Boolean detail = false;
    int loadNum = 0;
    String app = "app/";
    String input="";
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

    void DoNext(){
        while(true) {
            if (getAnswer) {
                switch (mode){
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
                break;
            }
        }
    }


    void DoNextDetail(){
        while(true) {
            if (getAnswer) {
                if (loadNum == 0) {
                    ((DetailScrollingActivity) context).AddData(itemData);
                    switch (mode) {
                        case "GetMeal":
                            ((DetailScrollingActivity) context).SetAverageMealTime();
                            break;
                        case "GetSleep":
                            ((DetailScrollingActivity) context).SetAverageSleepTime();
                            break;
                    }
                }
                else {
                    ((DetailScrollingActivity) context).AddMoreData(itemData);
                }

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
                bytes = output.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(bytes.length);
                os.write(buffer.array(),0,4);
                os.write(bytes);
                os.flush();
                socketConnected = true;
            } catch (IOException e) {
                Log.i("TAG","연결안됨...........................");
                socketConnected = false;
            }


            long start = System.currentTimeMillis();

            try {
                if(socketConnected) {
                    while (true) {
                        long end = System.currentTimeMillis();
                        Log.i("TIMETAG",start + "------------"+end);
                        if ((end - start) / 1000 < 5) {
                            is = socket.getInputStream();
                            bytes = new byte[4];
                            size = is.read(bytes, 0, 4);
                            ByteBuffer buffer = ByteBuffer.wrap(bytes);
                            buffer.order(ByteOrder.LITTLE_ENDIAN);
                            int length = buffer.getInt();
                            bytes = new byte[length];
                            size = is.read(bytes, 0, length);
                            input = new String(bytes, "UTF-8");

                            if (size > 0) {
                                Log.i("GetInfo", input);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if(!input.equals(""))
                    DoCalculate();

                getAnswer = true;
                socket.close();
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
