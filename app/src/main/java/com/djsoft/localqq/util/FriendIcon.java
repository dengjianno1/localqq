package com.djsoft.localqq.util;

import com.djsoft.localqq.R;

/**
 * Created by dengjian on 2017/6/12.
 */

public class FriendIcon {
    public static int getFriendIcon(int i){
        switch (i){
            case 1:
                return R.drawable.friend_icon_1;
            case 2:
                return R.drawable.friend_icon_2;
            case 3:
                return R.drawable.friend_icon_3;
            case 4:
                return R.drawable.friend_icon_4;
            case 5:
                return R.drawable.friend_icon_5;
            case 6:
                return R.drawable.friend_icon_6;
            case 7:
                return R.drawable.friend_icon_7;
            case 8:
                return R.drawable.friend_icon_8;
            case 9:
                return R.drawable.friend_icon_9;
            case 10:
                return R.drawable.friend_icon_10;
            default:
                return R.drawable.friend_icon_10;
        }
    }
}
