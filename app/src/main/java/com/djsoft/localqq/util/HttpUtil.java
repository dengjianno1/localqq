package com.djsoft.localqq.util;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 向服务器发送请求
 * Created by dengjian on 2017/6/14.
 */

public class HttpUtil {

    public static void sendUpOkHttpRequest(String address,String string,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("content",string).build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendDownOkHttpRequest(String address,String string,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("address",string).build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        //Request request=new Request.Builder().url("http://192.168.31.101:8080/localqq/down/address=192.168.0.1").build();
        client.newCall(request).enqueue(callback);
    }
}
