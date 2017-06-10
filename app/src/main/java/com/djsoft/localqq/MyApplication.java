package com.djsoft.localqq;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by dengjian on 2017/6/3.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static Context getContext(){
        return context;
    }
}
