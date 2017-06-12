package com.djsoft.localqq;

import android.support.v4.app.Fragment;

/**
 * Created by dengjian on 2017/6/11.
 */

public class BaseFragment extends Fragment {
    public static Class whatFragment;
    @Override
    public void onStart() {
        super.onStart();
        whatFragment=getClass();
    }
}
