package com.djsoft.localqq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.djsoft.localqq.service.ReceiveService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationBar BNBar=(BottomNavigationBar) findViewById(R.id.BNBar);
        BNBar.addItem(new BottomNavigationItem(R.drawable.ic_friends,"联系人"))
                .addItem(new BottomNavigationItem(R.drawable.ic_comment,"消息"))
                .addItem(new BottomNavigationItem(R.drawable.ic_settings,"设置")).initialise();
        replaceFragment(new FriendFragment() );
        BNBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        replaceFragment(new FriendFragment());
                        break;
                    case 1:
                        replaceFragment(new RecordFragment());
                        break;
                    case 2:
                        replaceFragment(new OtherFragment());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        Button exitButton=(Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent=new Intent(MainActivity.this,ReceiveService.class);
                stopService(stopIntent);

                finish();
            }
        });
        //初始化各组件后启动接收消息服务
        Intent intent=new Intent(this, ReceiveService.class);
        startService(intent);
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.table_view,fragment);
        transaction.commit();
    }

}
