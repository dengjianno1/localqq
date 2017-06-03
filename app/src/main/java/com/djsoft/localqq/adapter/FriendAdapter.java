package com.djsoft.localqq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djsoft.localqq.R;
import com.djsoft.localqq.db.Friend;

import java.util.List;

/**
 * Created by dengjian on 2017/6/3.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<Friend> mFriendList;

    public FriendAdapter(List<Friend> mFriendList) {
        this.mFriendList = mFriendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend=mFriendList.get(position);
        holder.friendImage.setImageResource(R.mipmap.ic_launcher);
        holder.friendName.setText(friend.getHostName());
        holder.friendAddress.setText(friend.getAddress());
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendImage;
        TextView friendName;
        TextView friendAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            friendImage=(ImageView) itemView.findViewById(R.id.friend_image);
            friendName=(TextView) itemView.findViewById(R.id.friend_name);
            friendAddress=(TextView) itemView.findViewById(R.id.friend_address);
        }
    }

}
