package com.djsoft.localqq.db;

import org.litepal.crud.DataSupport;

/**
 * Created by dengjian on 2017/6/4.
 */

public class Msg extends DataSupport{
    private int id;
    private String content;
    private int type;
    private String hostName;
    private String address;
    private String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dataTime) {
        this.dateTime = dataTime;
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
}
