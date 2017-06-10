package com.djsoft.localqq.intent;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.djsoft.localqq.MyApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by dengjian on 2017/6/4.
 */

public class OwnAddress {
    public static final String HOST_NAME= Build.BRAND+" "+Build.MODEL;
    public static final String HOST_ADDRESS=getLocalIpAddress();
    public static final String HOST_WIFIADDRESS=getWiFiAddress();
    private static String getLocalIpAddress() {
        try {
            Enumeration enumNetwork = NetworkInterface.getNetworkInterfaces();
            while (enumNetwork.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumNetwork.nextElement();
                Enumeration enumAddress= networkInterface.getInetAddresses();
                while (enumAddress.hasMoreElements()){
                    InetAddress inetAddress = (InetAddress) enumAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String getWiFiAddress(){
        WifiManager wifiManager = (WifiManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);    
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF ) + "." +
        ((ipAddress >> 8 ) & 0xFF) + "." +
        ((ipAddress >> 16 ) & 0xFF) + "." +
        ( ipAddress >> 24 & 0xFF) ;
    }
}
