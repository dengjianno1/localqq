package com.djsoft.localqq.util;

import android.util.Log;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by dengjian on 2017/6/11.
 */

public class FormatDateTime {
    public static String getFromatDataTime(String string){
        Date now=new Date();
        Date target = null;
        try {
            target=Constant.SDF_DB.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int between=(int)(now.getTime()-target.getTime())/1000/60/60;//转换成相差多少个小时
        Log.d("DateTime",Constant.SDF_A.format(target)+"  "+Constant.SDF_B.format(target)+"  "+Constant.SDF_C.format(target) );
        if (between<24){
            return Constant.SDF_A.format(target);
        }else if (between/24<7){
            return Constant.SDF_B.format(target);
        }else {
            return Constant.SDF_C.format(target);
        }
    }
}
