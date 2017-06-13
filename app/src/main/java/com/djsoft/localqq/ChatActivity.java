package com.djsoft.localqq;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.djsoft.localqq.adapter.MsgAdapter;
import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.db.Msg;
import com.djsoft.localqq.intent.TransportMessage;
import com.djsoft.localqq.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

public class ChatActivity extends BaseActivity {
    public static final int MESSAGE_CONTENT=1;
    private static Friend friend;
    private static ListView listView;
    private static List<Msg> msgList;
    private static MsgAdapter msgAdapter;
    private static NotificationManager manager=(NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    public static Handler handler=new Handler(Looper.getMainLooper().getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case MESSAGE_CONTENT :
                    Constant.vibrator.vibrate(Constant.pattern,-1);//震动
                    Msg receiveMsg=(Msg)message.obj;
                    Friend fromFriend = DataSupport.select("id","address","hostname","iconid")
                            .where("id=?",String.valueOf(receiveMsg.getFriendId())).findFirst(Friend.class);
                    //当还没有进入ChatActivity时,listView没必要更新,发送通知
                    if (whatActivity==ChatActivity.class&&fromFriend.getAddress().equals(friend.getAddress())){
                        msgAdapter.add(receiveMsg);
                        listView.smoothScrollToPosition(listView.getMaxScrollAmount());
                    }else {
                        Intent intent=new Intent(MyApplication.getContext(),ChatActivity.class);
                        intent.putExtra("friend", fromFriend);
                        intent.putExtra("status", Constant.STATUS_ONLINE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent=PendingIntent.getActivity(MyApplication.getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        Notification notification=new NotificationCompat.Builder(MyApplication.getContext())
                                .setContentTitle(fromFriend.getHostName()).setContentText(receiveMsg.getContent())
                                .setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.small_icon)
                                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.drawable.large_icon))
                                .setPriority(NotificationCompat.PRIORITY_MAX).setDefaults(NotificationCompat.DEFAULT_SOUND)
                                .setContentIntent(pendingIntent).setAutoCancel(true)
                                .setLights(Color.GREEN,1000,1000).build();
                        manager.notify(Constant.MSG_NOTIFICATION,notification);
                    }
                    receiveMsg.save();
                    //实时更新聊天记录(不好弄战时搁置)
                    //Intent intent=new Intent("com.djsoft.localqq.ChatActivity.UPDATE_LAST_CONTENT");
                    //intent.putExtra("address",receiveMsg.getAddress());
                    //Constant.broadcastManager.sendBroadcast(intent);
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
        friend=(Friend) intent.getSerializableExtra("friend");
        int status=intent.getIntExtra("status",Constant.STATUS_OFFLINE);//默认不在线
        TextView titleView=(TextView) findViewById(R.id.title_text);
        titleView.setText(Constant.trimContent(friend.getHostName()));
        msgList=getChatRecord(friend.getId());
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
                    Msg sendMsg=selfMessage(friend.getAddress(),friend.getHostName(),input);
                    msgAdapter.add(sendMsg);
                    sendMsg.save();
                    TransportMessage.sendMessage(input,friend.getAddress());
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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private Msg selfMessage(String address, String hostName, String content){
        Msg msg=new Msg();
        msg.setFriendId(DataSupport.select("id").where("address=? and hostname=?",address,hostName).order("id").findLast(Friend.class).getId());
        msg.setContent(content);
        msg.setType(Constant.TYPE_SENT);
        msg.setDateTime(Constant.SDF_DB.format(new Date()));
        return msg;
    }
    private List<Msg> getChatRecord(int friendId){
        return DataSupport.select("content","type").where("friendId=?",String.valueOf(friendId))
                .order("id").find(Msg.class);
    }

}
