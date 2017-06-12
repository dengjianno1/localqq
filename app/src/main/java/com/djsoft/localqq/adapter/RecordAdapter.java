package com.djsoft.localqq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djsoft.localqq.ChatActivity;
import com.djsoft.localqq.MyApplication;
import com.djsoft.localqq.R;
import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.db.Record;
import com.djsoft.localqq.util.FormatDateTime;
import com.djsoft.localqq.util.FriendIcon;
import com.djsoft.localqq.util.FriendStatus;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by dengjian on 2017/6/11.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> mRecordList;
    private Context mContext;
    public RecordAdapter(List<Record> recordList) {
        this.mRecordList = recordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.recordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Record record=mRecordList.get(position);
                Intent chatIntent=new Intent(mContext, ChatActivity.class);
                Friend friend=DataSupport.select("hostName","address","iconId")
                        .where("id=?",String.valueOf(record.getFriendId())).findFirst(Friend.class);
                chatIntent.putExtra("friend",friend);
                chatIntent.putExtra("status", FriendStatus.getFriendStatus(friend.getAddress(),friend.getHostName()));
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(chatIntent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record=mRecordList.get(position);
        Friend friend=DataSupport.select("hostName","address","iconId")
                .where("id=?",String.valueOf(record.getFriendId())).findFirst(Friend.class);
        holder.friendName.setText(friend.getHostName());
        holder.lastRecord.setText(record.getContent());
        holder.friendImage.setImageResource(FriendIcon.getFriendIcon(friend.getIconId()));
        holder.dataTime.setText(FormatDateTime.getFromatDataTime(record.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View recordView;
        ImageView friendImage;
        TextView friendName;
        TextView lastRecord;
        TextView dataTime;
        public ViewHolder(View itemView) {
            super(itemView);
            recordView=itemView;
            friendImage=(ImageView) itemView.findViewById(R.id.friend_image);
            friendName=(TextView) itemView.findViewById(R.id.friend_name);
            lastRecord=(TextView) itemView.findViewById(R.id.last_record);
            dataTime=(TextView) itemView.findViewById(R.id.record_datetime);
        }
    }

}
