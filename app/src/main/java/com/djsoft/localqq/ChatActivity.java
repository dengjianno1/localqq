package com.djsoft.localqq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.djsoft.localqq.adapter.MsgAdapter;
import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.intent.TransportMessage;
import com.djsoft.localqq.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

public class ChatActivity extends BaseActivity {
    public static final int MESSAGE_CONTENT=1;
    private static ListView listView;
    private static List<Msg> msgList;
    private static MsgAdapter msgAdapter;
    public static Handler handler=new Handler(Looper.getMainLooper().getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case MESSAGE_CONTENT :
                    Constant.vibrator.vibrate(Constant.pattern,-1);//震动
                    Msg receiveMsg=(Msg)message.obj;
                    if (whatActivity==ChatActivity.class){//当还没有进入ChatActivity时,listView没必要更新
                        msgAdapter.add(receiveMsg);
                        listView.smoothScrollToPosition(listView.getMaxScrollAmount());
                    }
                    receiveMsg.save();
                    Intent intent=new Intent("com.djsoft.localqq.ChatActivity.UPDATE_LAST_CONTENT");
                    intent.putExtra("address",receiveMsg.getAddress());
                    Constant.broadcastManager.sendBroadcast(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent=getIntent();
        final String hostName=intent.getStringExtra("hostName");
        final String address=intent.getStringExtra("address");
        int status=intent.getIntExtra("status",Constant.STATUS_OFFLINE);//默认不在线
        TextView titleView=(TextView) findViewById(R.id.title_text);
        titleView.setText(hostName);
        msgList=getChatRecord(address);
        msgAdapter = new MsgAdapter(this, R.layout.msg_item, msgList);
        listView = (ListView) findViewById(R.id.msg_list_view);
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
                    //发送消息并保存
                    Msg sendMsg=selfMessage(address,hostName,input);
                    msgAdapter.add(sendMsg);
                    sendMsg.save();
                    TransportMessage.sendMessage(input,address);
                    contentText.setText("");
                    listView.smoothScrollToPosition(listView.getMaxScrollAmount());
                }
            }
        });
        if (status==Constant.STATUS_OFFLINE){
            sendButton.setEnabled(false);
        }
        contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(listView.getMaxScrollAmount());
            }
        });
        Button backButton=(Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(listView.getWindowToken(),0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    private Msg selfMessage(String address, String hostName, String content){
        Msg msg=new Msg();
        msg.setAddress(address);
        msg.setHostName(hostName);
        msg.setContent(content);
        msg.setType(Constant.TYPE_SENT);
        msg.setDateTime(Constant.SDF_DB.format(new Date()));
        return msg;
    }
    private List<Msg> getChatRecord(String address){
        return DataSupport.select("content","type").where("address=?",address)
                .order("id").find(Msg.class);
    }

}
