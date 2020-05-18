package com.hansung.android.ai_speaker_socket;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Socket_GetInfo {
    Context context;
    String ip;
    int port;
    String send_msg;
    String mode="";
    Boolean getAnswer = false;

    String app = "app/";

    private Socket socket;
    OutputStream os;
    InputStream is;
    byte[]bytes;
    int size = 0;
    String input="";


    public Socket_GetInfo(Context context, String ip, int port,String mode,String send_msg){
        this.ip = ip;
        this.port = port;
        this.send_msg = send_msg;
        this.mode = mode;
        this.context = context;

        checkStart.start();
        while(true) {
            if (getAnswer) {
                if (!input.equals("")){
                    if(mode.equals("GetMember"))
                        ((CareMembersListActivity) context).SetUI(input);
                }
                else {
                    if(mode.equals("GetMember"))
                        ((CareMembersListActivity) context).NonInfo();
                }
                break;

            }
        }
    }


    private Thread checkStart = new Thread() {

        public void run() {


            try {
                socket = new Socket(ip, port);
                os = socket.getOutputStream();
                String output = app+mode+send_msg;

                bytes = output.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(bytes.length);
                os.write(buffer.array(),0,4);
                os.write(bytes);
                os.flush();

            } catch (Exception e) {

            }


            long start = System.currentTimeMillis();


            while(true){
                long end = System.currentTimeMillis();
                if(( end - start )/1000 < 5) {
                    try {
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
                            Log.i("TAG", input);
                            socket.close();
                            getAnswer = true;
                            break;
                        }
                    } catch (Exception e) {

                    }
                }
                else{
                    try {
                        input = "";
                        socket.close();
                        getAnswer = true;
                        break;
                    } catch (Exception e){}
                }
            }



        }
    };





}
