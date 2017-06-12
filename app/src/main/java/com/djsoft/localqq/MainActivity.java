package com.djsoft.localqq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.djsoft.localqq.service.ReceiveService;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.exit_button:
                        mDrawerLayout.closeDrawers();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent stopIntent=new Intent(MainActivity.this,ReceiveService.class);
                        stopService(stopIntent);
                        finish();
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
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
