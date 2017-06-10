package com.digoshop.app.module.shopdetailnew.adp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.customServices.model.ChildernBean;
import com.digoshop.app.module.shopdetailnew.ShopDetailProductListActivity;

import java.util.ArrayList;

public class ShopnAllCateTwoAdp extends BaseAdapter {
    private ArrayList<ChildernBean> childernBeans;
    private Context mContext;
    private LayoutInflater minflater;

    @SuppressWarnings("deprecation")
    public ShopnAllCateTwoAdp(Context context, ArrayList<ChildernBean> MreLists) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.childernBeans = MreLists;

    }

    @Override
    public int getCount() {
        return this.childernBeans != null ? this.childernBeans.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return childernBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shopnallcatetwo_item, null);
            holder.tv_threename = (TextView) convertView.findViewById(R.id.tv_threename);
            holder.ll_three = (LinearLayout) convertView.findViewById(R.id.ll_three);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_threename.setText(childernBeans.get(position).getName());
        holder.ll_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("moid", childernBeans.get(position).getMoid());
                intent.putExtra("sid", childernBeans.get(position).getSid());
                intent.setClass(mContext, ShopDetailProductListActivity.class);
                mContext.startActivity(intent);
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tv_threename;

        private LinearLayout ll_three;
    }

}
