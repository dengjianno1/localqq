package com.djsoft.localqq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.djsoft.localqq.intent.LineBroadcast;
import com.djsoft.localqq.intent.OwnAddress;

public class NetworkReceiver extends BroadcastReceiver {
    public static boolean isConnected=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLING:
                    Log.e("H3C", "WIFI关闭中...");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.e("H3C", "WIFI已关闭");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    Log.e("H3C", "WIFI开启中...");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.e("H3C", "WIFI已开启");
                    break;
                default:
                    break;
            }
        }
        // 这个监听wifi的连接状态即是否连上了一个有效wifi，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                Log.e("H3C", "isConnected" + isConnected);
                if (isConnected) {
                    OwnAddress.getOwnAddress().refreshAddress();
                    //refreshFriend();//WIFI连上了刷新好友列表
                } else {
                    LineBroadcast.friends.clear();//WIFI掉了清空好友列表
                    //adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
