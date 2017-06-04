package com.djsoft.localqq.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djsoft.localqq.R;
import com.djsoft.localqq.db.Msg;

import java.util.List;

/**
 * Created by dengjian on 2017/6/4.
 */

public class MsgAdapter extends ArrayAdapter<Msg> {
    private int resourceId;
    public MsgAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Msg> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Msg msg=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        LinearLayout leftLayout=(LinearLayout) view.findViewById(R.id.left_layout);
        LinearLayout rightLayout=(LinearLayout) view.findViewById(R.id.right_layout);
        TextView leftMsg=(TextView) view.findViewById(R.id.left_msg);
        TextView rightMsg=(TextView) view.findViewById(R.id.right_msg);
        if (msg.getType()==Msg.TYPE_RECEIVED){
            rightLayout.setVisibility(View.GONE);
            leftLayout.setVisibility(View.VISIBLE);
            leftMsg.setText(msg.getContent());
        }else if (msg.getType()==Msg.TYPE_SENT){
            leftLayout.setVisibility(View.GONE);
            rightLayout.setVisibility(View.VISIBLE);
            rightMsg.setText(msg.getContent());
        }
        return view;
    }
}
