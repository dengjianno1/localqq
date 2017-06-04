package com.djsoft.localqq.db;

/**
 * Created by dengjian on 2017/6/4.
 */

public class Msg {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private int id;
    private String content;
    private int type;
    private Friend fromFriend;
    private String dataTime;

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

    public Friend getFromFriend() {
        return fromFriend;
    }

    public void setFromFriend(Friend fromFriend) {
        this.fromFriend = fromFriend;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
