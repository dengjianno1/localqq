package com.djsoft.localqq;

import android.app.Application;
import android.content.Context;

/**
 * Created by dengjian on 2017/6/3.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
