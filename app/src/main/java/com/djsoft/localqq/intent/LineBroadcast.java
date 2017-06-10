package com.djsoft.localqq.intent;

import android.content.Intent;

import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.Constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.djsoft.localqq.util.Constant.PACKET_OFFLINE;
import static com.djsoft.localqq.util.Constant.PACKET_ONLINE;

/**
 * Created by dengjian on 2017/6/1.
 */

public class LineBroadcast {
    private static final String TAG = "LineBroadcast";
    public static List<Friend> friends=new ArrayList<>();
    public static Friend myself;
    /**
     * 发送网络上线通知
     */
    public static void sendOnLine(final DatagramSocket socket, final int port) {
        new Thread( new Runnable() {
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(PACKET_ONLINE, PACKET_ONLINE.length, address, port);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送网络下线通知
     */
    public static void sendOffLine(final DatagramSocket socket, final int port) {
        new Thread( new Runnable() {
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(PACKET_OFFLINE, PACKET_OFFLINE.length, address, port);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 处理好友上线逻辑,并给此好友发送一条我在线的消息
     */
    public static void doFriendOnLine(DatagramPacket packet){
        String address = packet.getAddress().getHostAddress();
        String hostName=new String(packet.getData(),1,packet.getLength()-1);
        Friend friend=new Friend();
        friend.setHostName(hostName);
        friend.setAddress(address);
        if (!friends.contains(friend)){
            friends.add(friend);
            Constant.broadcastManager.sendBroadcast(new Intent("com.djsoft.localqq.online"));
        }else {
            return;
        }
        //回一个在线消息给发送方
        if (!address.equals(OwnAddress.HOST_WIFIADDRESS)){
            try {
                DatagramPacket onLinePacket=new DatagramPacket(PACKET_ONLINE,PACKET_ONLINE.length,InetAddress.getByName(address), Constant.PORT);
                Thread.sleep(500);
                ReceiveService.socket.send(onLinePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            myself=friend;
        }
    }
    /**
     * 处理好友下线逻辑
     */
    public static void doFriendOffLine(DatagramPacket packet){
        String address = packet.getAddress().getHostAddress();
        String hostName = new String(packet.getData(),1,packet.getLength()-1);
        Friend friend=new Friend();
        friend.setAddress(address);
        friend.setHostName(hostName);
        if (friends.contains(friend)){
            friends.remove(friend);
            //发送好友列表碎片更新广播
            Constant.broadcastManager.sendBroadcast(new Intent("com.djsoft.localqq.offline"));
        }
    }

}
