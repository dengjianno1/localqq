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
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
            viewHolder=new ViewHolder();
            viewHolder.leftLayout=(LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout=(LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMsg=(TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg=(TextView) view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        if (msg.getType()==Msg.TYPE_RECEIVED){
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.leftMsg.setText(msg.getContent());
        }else if (msg.getType()==Msg.TYPE_SENT){
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;
    }
    class ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
    }
}
