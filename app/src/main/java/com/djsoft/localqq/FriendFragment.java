package com.djsoft.localqq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djsoft.localqq.adapter.FriendAdapter;
import com.djsoft.localqq.intent.LineBroadcast;
import com.djsoft.localqq.intent.OwnAddress;
import com.djsoft.localqq.service.ReceiveService;
import com.djsoft.localqq.util.Constant;

public class FriendFragment extends BaseFragment {
    private FriendAdapter adapter;
    private RecyclerView recyclerView;
    private LineReceiver lineReceiver;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_friend,container,false);
        refreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView=(RecyclerView) view.findViewById(R.id.friend_recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter=new FriendAdapter(LineBroadcast.friends);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFriend();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(adapter);
        lineReceiver = new LineReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.djsoft.localqq.online");
        intentFilter.addAction("com.djsoft.localqq.offline");
        Constant.broadcastManager.registerReceiver(lineReceiver,intentFilter);
        Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("在线好友");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Constant.broadcastManager.unregisterReceiver(lineReceiver);
    }
    private void refreshFriend(){
        LineBroadcast.friends.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这是在子线程中
                try {
                    LineBroadcast.sendOffLine(ReceiveService.socket,Constant.PORT);
                    Thread.sleep(3000);
                    LineBroadcast.sendOnLine(ReceiveService.socket,Constant.PORT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //这是在主线程中
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), OwnAddress.getOwnAddress().HOST_WIFIADDRESS, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    class LineReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if("com.djsoft.localqq.online".equals(intent.getAction())||
                    "com.djsoft.localqq.offline".equals(intent.getAction())){
                /**
                 * 这个判断好像没什么用，按后退键会取消注册广播接收器
                 * 按home键和在聊天界面这个判断仍为true
                 */
                if (getActivity() instanceof MainActivity){
                    adapter.notifyDataSetChanged();//全部刷新，虽然效率不高，但是上线人数本来就不多
                }
            }
        }
    }
}
