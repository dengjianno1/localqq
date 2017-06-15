package com.djsoft.localqq.util;

import android.util.Log;

import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.intent.OwnAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dengjian on 2017/6/14.
 */

public class BackupMsg {
    /**
     * 上传聊天记录
     */
    public static void doBackupMsg(){
        List<Msg> MsgList= DataSupport.findAll(Msg.class);
        JSONArray jsonArray=Utility.doMsgToJsonArray(MsgList);
        String string=String.valueOf(jsonArray);
        try {
            string= URLEncoder.encode(string,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address="http://10.1.1.208:8080/localqq/backup/";
        HttpUtil.sendUpOkHttpRequest(address, string, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("BackupMsg", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("BackupMsg", response.body().string());
            }
        });
    }

    /**
     * 下载聊天记录
     */
    public static void doDownloadMsg(){
        String address="http://10.1.1.208:8080/localqq/down/";
        String string= OwnAddress.HOST_WIFIADDRESS;
        HttpUtil.sendDownOkHttpRequest(address, string, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("BackupMsg", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson=new Gson();
                Type type = new TypeToken<List<Msg>>() {}.getType();
                String result=URLDecoder.decode(response.body().string(),"utf-8");
                List<Msg> msgList=gson.fromJson(result,type);
                for (Msg msg:msgList) {
                    Log.d("BackupMsg", msg.getId()+"  "+msg.getContent()+"  "+msg.getType()+"  "+msg.getFriendId()+"  "+msg.getDateTime());
                }
            }
        });
    }
}
