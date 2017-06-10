package com.digoshop.app.module.home.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/10/21.
 */

public class HotkeyAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<String> mlist;

    public HotkeyAdp(Context context, ArrayList<String> list) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlist.get(position);

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(
                    R.layout.lin_hot, null);
            holder = new viewholder();
            holder.tv_hot_text = (TextView) convertView
                    .findViewById(R.id.tv_hot_text);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        holder.tv_hot_text.setText(mlist.get(position)
        );

        return convertView;
    }

    public class viewholder {
        TextView tv_hot_text;

    }
}


