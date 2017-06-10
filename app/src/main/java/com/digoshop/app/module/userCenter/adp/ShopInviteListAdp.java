package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsqbeyond on 2016/10/27.
 */

public class ShopInviteListAdp extends BaseAdapter {

    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ShopInfoBean> shopInfoBeans=null;
    private DisplayImageOptions options;
    private ShopInlistClickListener shopInlistClickListener ;
    public ShopInviteListAdp(Context context,ArrayList<ShopInfoBean> shopInfoBeans,ShopInlistClickListener shopInlistClickListener) {
        this.mContext = context;
        this.shopInlistClickListener= shopInlistClickListener;
        minflater = LayoutInflater.from(context);
        this.shopInfoBeans = shopInfoBeans;

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }



    @Override
    public int getCount() {

        return shopInfoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return shopInfoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder holder=null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.shopinvitelistitem, null);
            holder = new  Viewholder();
            holder.tv_shopinvite_itme_mobile = (TextView) convertView.findViewById(R.id.tv_shopinvite_itme_mobile);
            holder.tv_shopinvite_itme_name = (TextView) convertView.findViewById(R.id.tv_shopinvite_itme_name);
            holder.tv_shopinvite_itme_addres = (TextView) convertView.findViewById(R.id.tv_shopinvite_itme_addres);
            holder.tv_acceptet = (TextView) convertView.findViewById(R.id.tv_acceptet);


            holder.tv_replytime = (TextView) convertView.findViewById(R.id.tv_replytime);
            holder.lin_assect_name = (LinearLayout) convertView.findViewById(R.id.lin_assect_name);



            holder.iv_shopinvite_item_img = (ImageView) convertView.findViewById(R.id.iv_shopinvite_item_img);

            convertView.setTag(holder);
        } else {

            holder = (  Viewholder) convertView.getTag();
        }

        holder.lin_assect_name.setOnClickListener(shopInlistClickListener);
        holder.iv_shopinvite_item_img.setOnClickListener(shopInlistClickListener);
        holder.tv_acceptet.setOnClickListener(shopInlistClickListener);
        holder.tv_acceptet.setTag(position);
        holder.iv_shopinvite_item_img.setTag(position);
        holder.lin_assect_name.setTag(position);
        if(!TextUtils.isEmpty(shopInfoBeans.get(position).getTime())){
            holder.tv_replytime.setText(Tool.TimeStamp2Date(shopInfoBeans.get(position).getTime(),""));
        }
        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getCover(), holder.iv_shopinvite_item_img, options);
        holder.tv_shopinvite_itme_addres.setText(shopInfoBeans.get(position).getAddress());
        holder.tv_shopinvite_itme_name.setText("商家名称:"+shopInfoBeans.get(position).getName());
        Log.v("ceshi","***+"+Tool.isNullStr(shopInfoBeans.get(position).getContact()));
        holder.tv_shopinvite_itme_mobile.setText("联系电话:"+shopInfoBeans.get(position).getContact() );


        return convertView;
    }
    public void appendData(List<ShopInfoBean> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }
    private class Viewholder {
        private LinearLayout lin_assect_name;
        TextView tv_shopinvite_itme_name,tv_acceptet,tv_replytime,tv_shopinvite_itme_mobile,tv_shopinvite_itme_addres;
        ImageView  iv_shopinvite_item_img   ;
    }
    /**
     * 用于回调的抽象类
     *
     * @author Ivan Xu 2014-11-26
     */
    public static abstract class ShopInlistClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
