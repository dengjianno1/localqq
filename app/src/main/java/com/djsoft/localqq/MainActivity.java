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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.BackupMsg;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        actionBar.setTitle("在线好友");
        final NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
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
                        //mDrawerLayout.closeDrawers();
                        navigationView.setCheckedItem(R.id.exit_button);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                BackupMsg.doBackupMsg();
                break;
            case R.id.delete:
                Toast.makeText(this, "你点击了删除按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                BackupMsg.doDownloadMsg();
                break;
            default:
                break;
        }
        return true;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.table_view,fragment);
        transaction.commit();
    }

}
