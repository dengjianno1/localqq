package com.djsoft.localqq.db;

import org.litepal.crud.DataSupport;

/**
 * Created by dengjian on 2017/6/1.
 */

public class Friend extends DataSupport{
    private int id;
    private String hostName;
    private String address;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "hostName='" + hostName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result=17;
        result=result*31+hostName.hashCode();
        result=result+address.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Friend){
            Friend friend=(Friend) obj;
            return this.address.equals(friend.address)&&this.hostName.equals(friend.hostName);
        }
        return super.equals(obj);
    }
}
