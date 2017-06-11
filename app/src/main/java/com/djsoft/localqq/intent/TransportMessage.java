package com.djsoft.localqq.intent;

import android.os.Message;
import android.util.Log;

import com.djsoft.localqq.ChatActivity;
import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

/**
 * Created by dengjian on 2017/6/1.
 */

public class TransportMessage {
    /**
     * 处理好友发过来的消息
     */
    public static void receiveMessage(DatagramPacket packet){
        byte[] data=packet.getData();
        int len=packet.getLength();
        Msg msg=new Msg();
        msg.setContent (new String(data,0,len));
        Log.d("packet", "主机名："+packet.getAddress().getHostName()+"  IP地址："+packet.getAddress().getHostAddress());
        msg.setAddress(packet.getAddress().getHostAddress());
        msg.setHostName(getFriendName(packet.getAddress().getHostAddress()));
        msg.setDateTime(Constant.SDF_DB.format(new Date()));
        msg.setType(Constant.TYPE_RECEIVED);
        Message message=new Message();
        message.what= ChatActivity.MESSAGE_CONTENT;
        message.obj=msg;
        ChatActivity.handler.sendMessage(message);
    }

    /**
     * 向好友发送消息(待优化)
     */
    public static void sendMessage(final String content, final String address){
        new Thread(new Runnable() {
            public void run() {
                try {
                    DatagramPacket packet=new DatagramPacket(content.getBytes(),content.getBytes().length, InetAddress.getByName(address),Constant.PORT);
                    ReceiveService.socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 从数据库中取好友主机名
     */
    public static String getFriendName(String address){
        List<Friend> friendList= DataSupport.select("hostName").where("address=?",address).find(Friend.class);
        if (friendList.isEmpty()){
            Log.e("TransportMessage", "从数据库中获取好友主机名数量大于1",new Exception() );
        }else if (friendList.size()>1){
            Log.e("TransportMessage", "从数据库中获取好友主机名数量大于1",new Exception() );
        }else {
            return friendList.get(0).getHostName();
        }
        return address;
    }
}
