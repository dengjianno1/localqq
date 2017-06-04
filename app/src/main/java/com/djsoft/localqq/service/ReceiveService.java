package com.djsoft.localqq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.intent.CustomPort;
import com.djsoft.localqq.intent.LineBroadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveService extends Service {
    public static DatagramSocket socket;
    private DatagramPacket packet;
    public static String selfHostName;
    public static String selfAddress;
    private LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(this);
    private ReceiveListener listener=new ReceiveListener() {
        @Override
        public void onLine(Friend friend) {
            //发送上线广播
            Log.d("ReceiveService", "onLine: 回调后上线");
            Intent onLineIntent=new Intent("com.djsoft.localqq.online");
            broadcastManager.sendBroadcast(onLineIntent);
        }

        @Override
        public void offLine(Friend friend) {
            //发送下线广播
            Log.d("ReceiveService", "onLine: 回调后下线");
            Intent offLineIntent=new Intent("com.djsoft.localqq.offline");
            broadcastManager.sendBroadcast(offLineIntent);
        }

        @Override
        public void getMessage() {
            //处理接收消息逻辑,使用handle
        }
    };
    public ReceiveService() {
    }

    /**
     * 创建服务时初始化Socket和Packet
     */
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socket=new DatagramSocket(CustomPort.PORT);
            packet=new DatagramPacket(new byte[1024], 1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务时,首先发送上线通知,再启动接收Packet线程,
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LineBroadcast.sendOnLine(socket,CustomPort.PORT);
        LineBroadcast.ReceivePacket(socket,packet,listener);
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 停止服务时,发送下线通知,并关闭Socket
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LineBroadcast.sendOffLine(socket,CustomPort.PORT);
        socket.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
