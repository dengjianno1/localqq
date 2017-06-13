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
import com.djsoft.localqq.util.Constant;
import com.djsoft.localqq.util.FriendIcon;

import java.util.List;

/**
 * Created by dengjian on 2017/6/3.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<Friend> mFriendList;
    private Context mContext;
    public FriendAdapter(List<Friend> mFriendList) {
        this.mFriendList = mFriendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Friend friend=mFriendList.get(position);
                Intent chatIntent=new Intent(mContext, ChatActivity.class);
                chatIntent.putExtra("friend",friend);
                chatIntent.putExtra("status", Constant.STATUS_ONLINE);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(chatIntent);
                //也可以写成
                //mContext.startActivity(chatIntent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend=mFriendList.get(position);
        holder.friendImage.setImageResource(FriendIcon.getFriendIcon(friend.getIconId()));
        //Glide.with(mContext).load(FriendIcon.getFriendIcon(friend.getIconId())).into(holder.friendImage);这句话的展示效果并不好
        holder.friendName.setText(Constant.trimContent(friend.getHostName()));
        holder.friendAddress.setText(friend.getAddress());
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View friendView;
        ImageView friendImage;
        TextView friendName;
        TextView friendAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            friendView=itemView;
            friendImage=(ImageView) itemView.findViewById(R.id.friend_image);
            friendName=(TextView) itemView.findViewById(R.id.friend_name);
            friendAddress=(TextView) itemView.findViewById(R.id.friend_address);
        }
    }

}
