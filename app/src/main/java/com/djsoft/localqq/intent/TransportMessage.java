package com.djsoft.localqq.intent;

import android.os.Message;

import com.djsoft.localqq.ChatActivity;
import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.Constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;

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
        //这里的主机名还是IP地址
        msg.setHostName(packet.getAddress().getHostName());
        msg.setAddress(packet.getAddress().getHostAddress());
        msg.setDataTime(Constant.SDF_DB.format(new Date()));
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

}
