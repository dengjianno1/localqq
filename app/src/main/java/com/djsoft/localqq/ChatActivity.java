package com.djsoft.localqq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.djsoft.localqq.adapter.MsgAdapter;
import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.intent.CustomPort;
import com.djsoft.localqq.intent.OwnAddress;
import com.djsoft.localqq.intent.TransportMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    public static final int MESSAGE_CONTENT=1;
    private static List<Msg> msgList=new ArrayList<>();
    public static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case MESSAGE_CONTENT :
                    msgList.add((Msg)message.obj);
                    msgAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private static MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent=getIntent();
        String hostName=intent.getStringExtra("hostname");
        final String address=intent.getStringExtra("address");
        msgAdapter = new MsgAdapter(this, R.layout.msg_item,msgList);
        ListView listView=(ListView) findViewById(R.id.msg_list_view);
        listView.setAdapter(msgAdapter);
        //视情况练习ListView的点击事件
        final EditText contentText=(EditText) findViewById(R.id.input_text);
        Button sendButton=(Button) findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input=contentText.getText().toString().trim();
                if (TextUtils.isEmpty(input)){
                    Toast.makeText(ChatActivity.this, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    //发送消息
                    msgList.add(selfMessage(input));
                    msgAdapter.notifyDataSetChanged();
                    TransportMessage.sendMessage(input,address);
                    contentText.setText("");
                }
            }
        });
    }
    private Msg selfMessage(String content){
        Msg msg=new Msg();
        msg.setHostName(OwnAddress.HOST_NAME);
        msg.setAddress(OwnAddress.HOST_ADDRESS);
        msg.setContent(content);
        msg.setType(Msg.TYPE_SENT);
        msg.setDataTime(CustomPort.sdf.format(new Date()));
        return msg;
    }
}
