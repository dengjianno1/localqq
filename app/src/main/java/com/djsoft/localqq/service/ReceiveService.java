package com.djsoft.localqq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.djsoft.localqq.intent.LineBroadcast;
import com.djsoft.localqq.intent.TransportMessage;
import com.djsoft.localqq.util.Constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveService extends Service {
    public static DatagramSocket socket;
    private Thread receiveThread;

    public ReceiveService() {
    }

    /**
     * 创建服务时初始化Socket和Packet
     */
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socket = new DatagramSocket(Constant.PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务时,首先发送上线通知,再启动接收Packet线程,
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * 开启线程，接收Packet
         */
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet=new DatagramPacket(new byte[1024], 1024);
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        socket.receive(packet);
                        if (packet.getData()[0] == 1) {
                            LineBroadcast.doFriendOnLine(packet);
                        } else if (packet.getData()[0] == -1) {
                            LineBroadcast.doFriendOffLine(packet);
                        } else {
                            TransportMessage.receiveMessage(packet);
                        }
                    } catch (IOException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
        });
        receiveThread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LineBroadcast.sendOnLine(socket, Constant.PORT);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 停止服务时,发送下线通知,并关闭Socket
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //打中断标志
        receiveThread.interrupt();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //处理下线逻辑
        LineBroadcast.sendOffLine(socket, Constant.PORT);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
