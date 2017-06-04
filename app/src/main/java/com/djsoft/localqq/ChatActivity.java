package com.djsoft.localqq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.djsoft.localqq.adapter.MsgAdapter;
import com.djsoft.localqq.db.Msg;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<Msg> msgList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initMsg();
        MsgAdapter msgAdapter=new MsgAdapter(this, R.layout.msg_item,msgList);
        ListView listView=(ListView) findViewById(R.id.msg_list_view);
        listView.setAdapter(msgAdapter);
    }
    private void initMsg(){
        for (int i = 0; i < 20; i++) {
            if (i%2==0){
                Msg msg=new Msg();
                msg.setType(Msg.TYPE_RECEIVED);
                msg.setContent("你好吗？");
                msgList.add(msg);
            }else {
                Msg msg=new Msg();
                msg.setType(Msg.TYPE_SENT);
                msg.setContent("我很好！");
                msgList.add(msg);
            }
        }
    }
}
