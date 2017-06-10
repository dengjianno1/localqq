package com.djsoft.localqq.util;

import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;

import com.djsoft.localqq.MyApplication;
import com.djsoft.localqq.intent.OwnAddress;

import java.text.SimpleDateFormat;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by dengjian on 2017/6/1.
 */

public abstract class Constant {
    public static final int PORT=2426;
    public static final SimpleDateFormat SDF =new SimpleDateFormat("MM月dd日 HH:mm:ss");
    public static final SimpleDateFormat SDF_DB =new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static final  int STATUS_ONLINE=1;
    public static final int STATUS_OFFLINE=-1;
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    public static final byte[] PACKET_ONLINE = doPacket((byte)1, OwnAddress.HOST_NAME);
    public static final byte[] PACKET_OFFLINE = doPacket((byte)-1,OwnAddress.HOST_NAME);
    public static final LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(MyApplication.getContext());
    public static final long[] pattern = {250, 250, 250, 250};
    public static final Vibrator vibrator = (Vibrator) MyApplication.getContext().getSystemService(VIBRATOR_SERVICE);
    public static byte[] doPacket(byte b,String string){
        byte[] strByte=string.getBytes();
        byte[] result=new byte[strByte.length+1];
        result[0]=b;
        for (int i = 1; i < result.length; i++) {
            result[i]=strByte[i-1];
        }
        return result;
    }
}
