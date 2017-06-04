package com.djsoft.localqq.intent;

import android.os.Build;

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
}
