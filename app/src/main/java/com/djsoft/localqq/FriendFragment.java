package com.djsoft.localqq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djsoft.localqq.adapter.FriendAdapter;
import com.djsoft.localqq.db.Friend;
import com.djsoft.localqq.intent.LineBroadcast;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {
    /**
     * 好友列表
     */
    private List<Friend> friends=new ArrayList<>();
    private FriendAdapter adapter;
    private RecyclerView recyclerView;
    private FriendFragment.lineReceiver lineReceiver;
    private LocalBroadcastManager broadcastManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_friend,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.friend_recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter=new FriendAdapter(LineBroadcast.friends);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(adapter);
        lineReceiver = new lineReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.djsoft.localqq.online");
        intentFilter.addAction("com.djsoft.localqq.offline");
        broadcastManager.registerReceiver(lineReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(lineReceiver);
    }

    class lineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (("online").equals(intent.getAction().split("\\.")[3])){
                Toast.makeText(context, "有人上线", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(context, "有人下线", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
