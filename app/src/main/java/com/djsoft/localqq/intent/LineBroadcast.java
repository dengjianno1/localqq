package com.djsoft.localqq.intent;

import android.content.Intent;
import android.util.Log;

import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.Constant;

import org.litepal.crud.DataSupport;

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
        /*
        接收自己上线消息是主机名为IP地址，接收别人的上线消息是确实有主机名
        但是主机名格式不好看，因此用手机品牌+型号替代主机名
         */
        Log.d("packet", "主机名："+packet.getAddress().getHostName()+"  IP地址："+packet.getAddress().getHostAddress());
        String hostName=new String(packet.getData(),1,packet.getLength()-1);
        Friend friend=new Friend();
        friend.setHostName(hostName);
        friend.setAddress(address);
        if (!friends.contains(friend)){
            friend=saveFriend(friend);//保存好友信息到数据库
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
    /**
     * 保存好友上线信息(IP地址和主机名)
     */
    public static Friend saveFriend(Friend friend){
        String hostName=friend.getHostName();
        String address=friend.getAddress();
        List<Friend> result=DataSupport.select("hostName","address","iconId")
                .where("hostName=? and address=?",hostName,address).find(Friend.class);
        //int result=DataSupport.where("hostName=? and address=?",hostName,address).count(Friend.class);
        if (result.isEmpty()){
            friend.save();
            friend.setIconId(friend.getId()%10+1);//根据ID计算头像ID
            friend.save();
            return friend;
        }else if (result.size()==1){
            return result.get(0);
        }
        Log.e(TAG, "数据库好友信息表数据有问题",new Exception() );
        return null;
    }

}
