package com.djsoft.localqq;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.BackupMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        WifiManager wifiManager = (WifiManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.record_recycler_view);
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                if (wifiManager.isWifiEnabled()){
                    if (NetworkReceiver.isConnected){
                        List<Msg> msgList= DataSupport.findAll(Msg.class);
                        if (msgList.isEmpty()){
                            Toast.makeText(this, "聊天记录为空，无需上传！", Toast.LENGTH_SHORT).show();
                        }else {
                            BackupMsg.doBackupMsg(msgList,new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("BackupMsg", e.getMessage());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "服务器无响应，聊天上传同步失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("BackupMsg", response.body().string());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "聊天记录上传成功！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }else {
                    Toast.makeText(this, "未连接WIFI，无法上传聊天记录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete:
                //final List<Msg> msgList=DataSupport.select("content","type","friendid","datetime").order("id").find(Msg.class);
                final List<Msg> msgList=DataSupport.findAll(Msg.class);
                DataSupport.deleteAll(Msg.class);
                Snackbar.make(mDrawerLayout,"删除本地聊天记录",Snackbar.LENGTH_SHORT)
                        .setAction("取消",new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                for (Msg msg:msgList) {
                                    Msg temp=new Msg();
                                    temp.setId(msg.getId());
                                    temp.setContent(msg.getContent());
                                    temp.setFriendId(msg.getFriendId());
                                    temp.setType(msg.getType());
                                    temp.setDateTime(msg.getDateTime());
                                    temp.save();
                                }
                                if (recyclerView!=null){
                                    replaceFragment(new RecordFragment());
                                }
                            }
                        }).show();
                if (recyclerView!=null){
                    replaceFragment(new RecordFragment());
                }
                break;
            case R.id.setting:
                if (wifiManager.isWifiEnabled()){
                    if (NetworkReceiver.isConnected){
                        BackupMsg.doDownloadMsg(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("BackupMsg", e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "服务器无响应，下载聊天记录失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Gson gson=new Gson();
                                Type type = new TypeToken<List<Msg>>() {}.getType();
                                String result= URLDecoder.decode(response.body().string(),"utf-8");
                                List<Msg> msgList=gson.fromJson(result,type);
                                if (msgList.isEmpty()){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "服务器上未发现您的聊天记录!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    for (Msg msg:msgList) {
                                        Log.d("BackupMsg", msg.getId()+"  "+msg.getContent()+"  "+msg.getType()+"  "+msg.getFriendId()+"  "+msg.getDateTime());
                                        msg.save();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (recyclerView!=null){
                                                replaceFragment(new RecordFragment());
                                            }
                                            Toast.makeText(MainActivity.this, "聊天记录下载成功！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(this, "未连接WIFI，无法下载聊天记录", Toast.LENGTH_SHORT).show();
                }
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
