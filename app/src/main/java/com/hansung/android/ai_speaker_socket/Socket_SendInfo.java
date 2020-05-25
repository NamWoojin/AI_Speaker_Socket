package com.hansung.android.ai_speaker_socket;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Socket_SendInfo {
    String ip;
    int port;
    String mode="";
    String output = "";
    public static Boolean sendMsg = false;
    public static Boolean sendSuccess = false;
    String app = "app";

    private Socket socket;
    OutputStream os;
    byte[]bytes;


    public Socket_SendInfo(String mode, String send_msg){
        this.ip = PublicFunctions.ip;
        this.port = PublicFunctions.port;
        this.mode = mode;
        this.output = send_msg;

        checkStart.start();
    }


    private Thread checkStart = new Thread() {

        public void run() {
            try {
//                socket = new Socket(ip, port);
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                socket.connect(socketAddress,3000);
                os = socket.getOutputStream();
                String send_msg = app+"/"+mode+"/"+output;
                Log.i("SendInfo_output",send_msg);
                bytes = send_msg.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(bytes.length);
                os.write(buffer.array(),0,4);
                os.write(bytes);
                os.flush();
                sendSuccess = true;
                socket.close();

            } catch (IOException e) {
                sendSuccess = false;
            }

        sendMsg = true;
        }
    };
}
