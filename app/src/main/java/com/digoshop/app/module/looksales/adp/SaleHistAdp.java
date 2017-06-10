package com.digoshop.app.module.looksales.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.SaleHistoryBean;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/10/28.
 */

public class SaleHistAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<SaleHistoryBean> mlist;

    public SaleHistAdp(Context context, ArrayList<SaleHistoryBean> list) {
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
        final  viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(
                    R.layout.fragment_arountshop_listitem, null);
            holder = new  viewholder();
            holder.tv_salehistory_listitme_left = (TextView) convertView
                    .findViewById(R.id.tv_arountshop_listitme_left);

            holder.tv_salehistory_listitme_right = (TextView) convertView
                    .findViewById(R.id.tv_arountshop_listitme_right);

            convertView.setTag(holder);
        } else {
            holder = ( viewholder) convertView.getTag();
        }
        holder.tv_salehistory_listitme_left.setText(mlist.get(position)
                .getNick());
        holder.tv_salehistory_listitme_right.setText(mlist.get(position)
                .getExchange_time());
        return convertView;
    }

    public class viewholder {
        TextView tv_salehistory_listitme_left, tv_salehistory_listitme_right;

    }
}
