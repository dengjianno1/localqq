package com.djsoft.localqq.util;

import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.intent.LineBroadcast;

/**
 * Created by dengjian on 2017/6/11.
 */

public class FriendStatus {
    public static int getFriendStatus(String address,String hostName){
        Friend friend=new Friend();
        friend.setAddress(address);
        friend.setHostName(hostName);
        return getFriendStatus(friend);
    }
    public static int getFriendStatus(Friend friend){
        if (LineBroadcast.friends.contains(friend)){
            return Constant.STATUS_ONLINE;
        }
        return Constant.STATUS_OFFLINE;
    }
}
