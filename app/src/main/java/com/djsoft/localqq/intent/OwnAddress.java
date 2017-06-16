package com.djsoft.localqq.intent;

import android.content.Context;
import android.net.DhcpInfo;
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
    public final String HOST_NAME= Build.BRAND+" "+Build.MODEL;
    public String HOST_ADDRESS;
    public String HOST_WIFIADDRESS;
    public String DHCP_ADDRESS;
    public static OwnAddress ownAddress=new OwnAddress();

    private OwnAddress() {
        HOST_WIFIADDRESS=getWiFiAddress();
        DHCP_ADDRESS=getDHCPAddress();
    }
    public static OwnAddress getOwnAddress(){
        return ownAddress;
    }
    public void refreshAddress(){
        HOST_WIFIADDRESS=getWiFiAddress();
        DHCP_ADDRESS=getDHCPAddress();
    }
    private String getLocalIpAddress() {
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
    private static String getDHCPAddress(){
        WifiManager wifiManager = (WifiManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo di = wifiManager.getDhcpInfo();
        long getewayIpL=di.gateway;
        return (getewayIpL & 0xFF ) + "." +
                ((getewayIpL >> 8 ) & 0xFF) + "." +
                ((getewayIpL >> 16 ) & 0xFF) + "." +
                ( getewayIpL >> 24 & 0xFF) ;
    }
}
