package com.djsoft.localqq;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djsoft.localqq.adapter.RecordAdapter;
import com.djsoft.localqq.db.Record;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class RecordFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecordAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_record, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.record_recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RecordAdapter(getRecords());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(adapter);
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
}
