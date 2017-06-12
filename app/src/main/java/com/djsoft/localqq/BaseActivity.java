package com.djsoft.localqq;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by dengjian on 2017/6/11.
 */

public class BaseActivity extends AppCompatActivity {
    public static Class whatActivity;

    @Override
    protected void onStart() {
        super.onStart();
        whatActivity=getClass();
    }

}
