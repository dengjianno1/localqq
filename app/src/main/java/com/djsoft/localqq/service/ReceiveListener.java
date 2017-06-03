package com.djsoft.localqq.service;

import com.djsoft.localqq.db.Friend;

/**
 * Created by dengjian on 2017/6/3.
 */

public interface ReceiveListener {
    void onLine(Friend friend);
    void offLine(Friend friend);
    void getMessage();
}
