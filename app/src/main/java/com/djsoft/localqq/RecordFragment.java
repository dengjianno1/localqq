package com.djsoft.localqq;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djsoft.localqq.adapter.RecordAdapter;
import com.djsoft.localqq.db.Record;
import com.djsoft.localqq.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class RecordFragment extends BaseFragment {


    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private LinearLayoutManager manager;
    private UpdateLastContentReceiver updateLastContentReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_record, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.record_recycler_view);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RecordAdapter(getRecords());
        recyclerView.setAdapter(adapter);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.djsoft.localqq.ChatActivity.UPDATE_LAST_CONTENT");
        updateLastContentReceiver=new UpdateLastContentReceiver();
        Constant.broadcastManager.registerReceiver(updateLastContentReceiver,intentFilter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Constant.broadcastManager.unregisterReceiver(updateLastContentReceiver);
    }

    private List<Record> getRecords(){
        List<String> addressList=new ArrayList<>();
        List<Record> recordList=new ArrayList<>();
        //查找联系过的好友
        Cursor cursor= DataSupport.findBySQL("select distinct address from Msg");
        if (cursor.moveToFirst()){
            do{
                String address=cursor.getString(cursor.getColumnIndex("address"));
                addressList.add(address);
            }while (cursor.moveToNext());
        }
        cursor.close();
        //查找最近一条记录
        for (String address:addressList) {
            //Msg msg=DataSupport.select("address","content","dateTime").where("address=?",address).order("id").findLast(Msg.class);
            cursor=DataSupport.findBySQL("select hostName,address,content,dateTime from Msg where address=? order by id desc",address);
            if (cursor!=null){
                while (cursor.moveToNext()){
                    Record record=new Record();
                    record.setHostName(trimContent(cursor.getString(cursor.getColumnIndex("hostname"))));
                    record.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    record.setContent(trimContent(cursor.getString(cursor.getColumnIndex("content"))));
                    record.setDateTime(cursor.getString(cursor.getColumnIndex("datetime")));
                    recordList.add(record);
                    break;
                }
            }
            cursor.close();
        }
        return  recordList;
    }
    private String trimContent(String string){
        if (string.length()>15){
            string=string.substring(0,12)+"...";
        }
        return string;
    }
    private void notifItem(String string){
        int firstPosition=manager.findFirstVisibleItemPosition();
        int lastPosition=manager.findLastVisibleItemPosition();
        for (int i = firstPosition; i <= lastPosition; i++) {
            View childView=manager.findViewByPosition(i);
            if (childView != null&&childView.getTag()!=null) {
                RecordAdapter.ViewHolder holder=(RecordAdapter.ViewHolder) childView.getTag();
                if (holder != null) {
                    adapter.notifyItemChanged(i);
                }
            }
        }
    }
    class UpdateLastContentReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
           String address=intent.getStringExtra("address");
            //notifItem(address);
            Log.d("局部更新",address);
        }
    }
}
