package com.djsoft.localqq.util;

import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.intent.OwnAddress;

import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Callback;

/**
 * Created by dengjian on 2017/6/14.
 */

public class BackupMsg {
    /**
     * 上传聊天记录
     */
    public static void doBackupMsg(List<Msg> msgList,Callback callback){
        JSONArray jsonArray=Utility.doMsgToJsonArray(msgList);
        String string=String.valueOf(jsonArray);
        try {
            string= URLEncoder.encode(string,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address="http://"+OwnAddress.getOwnAddress().DHCP_ADDRESS+":8080/localqq/backup/";
        HttpUtil.sendUpOkHttpRequest(address, string,callback);
    }

    /**
     * 下载聊天记录
     */
    public static void doDownloadMsg(Callback callback){
        //下载前先清空本地聊天记录
        DataSupport.deleteAll(Msg.class);
        String address="http://"+OwnAddress.getOwnAddress().DHCP_ADDRESS+":8080/localqq/down/";
        String string= OwnAddress.getOwnAddress().HOST_WIFIADDRESS;
        HttpUtil.sendDownOkHttpRequest(address, string, callback);
    }
}
