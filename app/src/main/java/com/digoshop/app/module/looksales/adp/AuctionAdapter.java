package com.digoshop.app.module.looksales.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.AuctionBean;
import com.digoshop.app.utils.Tool;

import java.util.List;

import static com.digoshop.R.id.tv_name;
import static com.digoshop.R.id.tv_price;
import static com.digoshop.R.id.tv_time;

public class AuctionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private List<AuctionBean> mauctionBeenList;

    public AuctionAdapter(Context context, List<AuctionBean> auctionBeenList) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        mauctionBeenList = auctionBeenList;
    }

    @Override
    public int getCount() {
        return mauctionBeenList.size();

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mauctionBeenList.get(position);

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
                    R.layout.auctionitem, null);
            holder = new viewholder();
            holder.tv_time = (TextView) convertView
                    .findViewById(tv_time);
            holder.tv_price = (TextView) convertView
                    .findViewById(tv_price);
            holder.tv_name = (TextView) convertView
                    .findViewById(tv_name);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        holder.tv_name.setText(mauctionBeenList.get(position).getNk());
        holder.tv_price.setText("￥"+mauctionBeenList.get(position).getPpr());
        holder.tv_time.setText(Tool.TimeStamp2Date(mauctionBeenList.get(position).getCt(),""));

        return convertView;
    }

    public class viewholder {
        TextView tv_time, tv_price, tv_name;

    }
}
