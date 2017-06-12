package com.djsoft.localqq.db;

import org.litepal.crud.DataSupport;

/**
 * Created by dengjian on 2017/6/11.
 */

public class Record extends DataSupport{
    private int id;
    private int friendId;
    private String content;
    private String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
