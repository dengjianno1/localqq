package com.djsoft.localqq.intent;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.djsoft.localqq.MyApplication;
import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.service.ReceiveListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengjian on 2017/6/1.
 */

public class LineBroadcast {
    private static final String TAG = "LineBroadcast";
    private static final byte[] ONLINE = {1};
    private static final byte[] OFFLINE = {-1};
    /**
     * 好友列表(暂时放这里)
     */
    public static List<Friend> friends=new ArrayList<>();
    /**
     * 发送上线通知
     */
    public static void sendOnLine(final DatagramSocket socket, final int port) {
        new Thread( new Runnable() {
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(ONLINE, ONLINE.length, address, port);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送下线通知
     */
    public static void sendOffLine(final DatagramSocket socket, final int port) {
        new Thread( new Runnable() {
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(OFFLINE, OFFLINE.length, address, port);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 接收packet线程
     */
    public static void ReceivePacket(final DatagramSocket socket, final DatagramPacket packet, final ReceiveListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        socket.receive(packet);
                        if (packet.getData()[0] == 1) {
                           doFriendOnLine(packet,listener);
                        } else if (packet.getData()[0] == -1) {
                           doFriendOffLine(packet,listener);
                        }else {
                            TransportMessage.receiveMessage();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 处理好友上线逻辑
     */
    public static void doFriendOnLine(DatagramPacket packet,ReceiveListener listener){
        String address = packet.getAddress().getHostAddress();
        String hostName = packet.getAddress().getHostName();
        Friend friend=new Friend();
        friend.setAddress(address);
        friend.setHostName(hostName);
        //很可能要重写contains的规则
        if (!friends.contains(friend)){
            friends.add(friend);
        }
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(MyApplication.getContext());
        broadcastManager.sendBroadcast(new Intent("com.djsoft.localqq.online"));
        //回调发送好友列表碎片更新广播
        //listener.onLine(friend);
    }
    /**
     * 处理好友下线逻辑
     */
    public static void doFriendOffLine(DatagramPacket packet,ReceiveListener listener){
        String address = packet.getAddress().getHostAddress();
        String hostName = packet.getAddress().getHostName();
        Friend friend=new Friend();
        friend.setAddress(address);
        friend.setHostName(hostName);
        //很可能要重写contains的规则
        if (friends.contains(friend)){
            friends.remove(friend);
        }
        //回调发送好友列表碎片更新广播
        listener.offLine(friend);
    }


}
