package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.Pat;
import com.digoshop.app.module.userCenter.model.ConsumerSettlementrecords;
import com.digoshop.app.module.userCenter.module.MyCostHistoryActivity;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2017/1/22.
 */

public class GridCateAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<Pat> MreList;

    public void appendData(ArrayList<Pat> list) {
        this.MreList.addAll(list);
        notifyDataSetChanged();
    }

    public GridCateAdp(Context context, ArrayList<Pat> list
    ) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        MreList = list;

    }

    public GridCateAdp(MyCostHistoryActivity myCostHistoryActivity, ArrayList<ConsumerSettlementrecords> cstList) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        return this.MreList != null ? this.MreList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return MreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final  viewholder holder;
        if (convertView == null) {
            holder = new viewholder();
            convertView = minflater.inflate(R.layout.costhis_gr_item, null);
            holder.tv_gv_cate_name = (TextView) convertView.findViewById(R.id.tv_gv_cate_name);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }

        holder.tv_gv_cate_name.setText(MreList.get(position).getStr());

        return convertView;
    }

    public class viewholder {
        TextView tv_gv_cate_name;
    }


}

