package com.digoshop.app.module.shopdetailnew.adp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.shopdetailnew.ShopDetailProductListActivity;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.tv_catetwoname;

public class ShopnAllCateAdp extends BaseAdapter {
    private ArrayList<CategoryChooseBean> categoryChooseBeans;
    private Context mContext;
    private LayoutInflater minflater;
    private ShoppAllcateClickListener messageClickListener;

    public ShopnAllCateAdp(Context context, ArrayList<CategoryChooseBean> categoryChooseBeans, ShoppAllcateClickListener messageClickListener) {
        this.mContext = context;
        this.messageClickListener = messageClickListener;
        minflater = LayoutInflater.from(context);
        this.categoryChooseBeans = categoryChooseBeans;

    }

    @Override
    public int getCount() {
        return this.categoryChooseBeans != null ? this.categoryChooseBeans.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return categoryChooseBeans.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shopnallcatelistitem, null);
            holder.tv_catetwoname = (TextView) convertView.findViewById(tv_catetwoname);
            holder.gr_itemchildren = (GridView) convertView.findViewById(R.id.gr_itemchildren);
            holder.re_shopallcate_all = (RelativeLayout) convertView.findViewById(R.id.re_shopallcate_all);
            holder.lin_shopallcaten = (LinearLayout) convertView.findViewById(R.id.lin_shopallcaten);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_catetwoname.setText(categoryChooseBeans.get(position).getName());
        holder.re_shopallcate_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("sid", categoryChooseBeans.get(position).getSid());
                intent.putExtra("moid", categoryChooseBeans.get(position).getMoid());
                intent.setClass(mContext, ShopDetailProductListActivity.class);
                mContext.startActivity(intent);
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                }
            }
        });
//		holder.lin_shopallcaten.setTag(position);
//		holder.lin_shopallcaten.setOnClickListener(messageClickListener);

        holder.gr_itemchildren
                .setAdapter(new ShopnAllCateTwoAdp(mContext, categoryChooseBeans.get(position).getChildderlis()));
        return convertView;
    }

    class ViewHolder {
        private LinearLayout lin_shopallcaten;
        private RelativeLayout re_shopallcate_all;
        private TextView tv_catetwoname;
        private GridView gr_itemchildren;
    }

    public void appendData(List<CategoryChooseBean> list) {
        this.categoryChooseBeans.addAll(list);
        notifyDataSetChanged();
    }

    public static abstract class ShoppAllcateClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            Log.v("lsq", "++" + (Integer) v.getTag());
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
