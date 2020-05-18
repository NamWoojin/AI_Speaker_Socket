package com.hansung.android.ai_speaker_socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Socket_SetInfo {
    String ip;
    int port;
    String mode="";
    String output = "";
    Boolean getAnswer = false;

    String app = "app";

    private Socket socket;
    OutputStream os;
    byte[]bytes;


    public Socket_SetInfo(String ip, int port, String mode, String send_msg){
        this.ip = ip;
        this.port = port;
        this.mode = mode;
        this.output = send_msg;

        checkStart.start();
//        while(true) {
//            if (getAnswer) {
//                if (!input.equals(""))
//                    ((CareMembersListActivity) context).SetUI(getArrayListFromJSONString(input));
//                else
//                    ((CareMembersListActivity) context).NonInfo();
//
//                break;
//
//            }
//        }
    }


    private Thread checkStart = new Thread() {

        public void run() {


            try {
                socket = new Socket(ip, port);
                os = socket.getOutputStream();
                String send_msg = app+"/"+mode+"/"+output;
                bytes = send_msg.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(bytes.length);
                os.write(buffer.array(),0,4);
                os.write(bytes);
                os.flush();

                socket.close();

            } catch (Exception e) {

            }


        }
    };
}
