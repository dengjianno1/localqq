package com.djsoft.localqq.util;

import android.util.Log;

import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.intent.OwnAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 数据与Json格式转换
 * Created by dengjian on 2017/6/14.
 */

public class Utility {
    public static JSONArray doMsgToJsonArray(List<Msg> Msgs){
        JSONArray jsonArray=new JSONArray();
        for (Msg msg:Msgs) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("address", OwnAddress.getOwnAddress().HOST_WIFIADDRESS);
                jsonObject.put("id",msg.getId());
                jsonObject.put("content",msg.getContent());
                jsonObject.put("type",msg.getType());
                jsonObject.put("friendId",msg.getFriendId());
                jsonObject.put("dateTime",msg.getDateTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        Log.d("jonsarray", jsonArray.toString());
        return jsonArray;
    }
}
