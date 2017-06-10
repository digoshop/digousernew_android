package com.digoshop.app.module.arrountshops.adp;

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

public class ArroutShopAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ShopInfoBean> shopInfoBeans = null;
    private DisplayImageOptions options;

    public ArroutShopAdp(Context context, ArrayList<ShopInfoBean> shopInfoBeans) {
        this.mContext = context;
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
        Log.i("zzzppp", position + "");
        Viewholder holder = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.list_shop_item, null);
            holder = new Viewholder();
            holder.tv_type = (TextView) convertView.findViewById(R.id.textView3);
            holder.tv_oper = (TextView) convertView.findViewById(R.id.textView2);
            holder.tv_good = (TextView) convertView.findViewById(R.id.textView4);
            holder.tv_gps = (TextView) convertView.findViewById(R.id.textView5);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_shopaddres = (TextView) convertView.findViewById(R.id.tv_shopaddres);
            holder.iv_stag = (ImageView) convertView.findViewById(R.id.iv_stag);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.tv_shop_quanmessage = (TextView) convertView.findViewById(R.id.tv_shop_quanmessage);
            holder.tv_shop_huimessage = (TextView) convertView.findViewById(R.id.tv_shop_huimessage);
            holder.tv_shop_duimessage = (TextView) convertView.findViewById(R.id.tv_shop_duimessage);
            holder.lin_shop_duimess = (LinearLayout) convertView.findViewById(R.id.lin_shop_duimess);
            holder.lin_shop_huimess = (LinearLayout) convertView.findViewById(R.id.lin_shop_huimess);
            holder.lin_shop_quanmess = (LinearLayout) convertView.findViewById(R.id.lin_shop_quanmess);
            holder.v_shop_line = (View) convertView.findViewById(R.id.v_shop_line);
            convertView.setTag(holder);
        } else {

            holder = (Viewholder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(shopInfoBeans.get(position).getCoupon()) | !TextUtils.isEmpty(shopInfoBeans.get(position).getProduct()) | !TextUtils.isEmpty(shopInfoBeans.get(position).getNews())) {
            holder.v_shop_line.setVisibility(View.VISIBLE);
        }else{
            holder.v_shop_line.setVisibility(View.GONE);
        }
        //兑换商品
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getProduct())) {
            holder.lin_shop_duimess.setVisibility(View.GONE);
        } else {
            holder.lin_shop_duimess.setVisibility(View.VISIBLE);
            holder.tv_shop_duimessage.setText(shopInfoBeans.get(position).getProduct());
        }
        //优惠活动
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getNews())) {
            holder.lin_shop_huimess.setVisibility(View.GONE);
        } else {
            holder.lin_shop_huimess.setVisibility(View.VISIBLE);
            holder.tv_shop_huimessage.setText(shopInfoBeans.get(position).getNews());
        }
        //优惠券
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getCoupon())) {
            holder.lin_shop_quanmess.setVisibility(View.GONE);
        } else {
            holder.lin_shop_quanmess.setVisibility(View.VISIBLE);
            holder.tv_shop_quanmessage.setText(shopInfoBeans.get(position).getCoupon());
        }

        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getCover(), holder.iv_icon, options);
        holder.tv_type.setText(Tool.isNullStr(shopInfoBeans.get(position).getOperate()) + "|");
        holder.tv_oper.setText(Tool.isNullStr(shopInfoBeans.get(position).getOperate()) + " | ");
        holder.tv_good.setText(shopInfoBeans.get(position).getGoods());

        holder.tv_title.setText(shopInfoBeans.get(position).getName());
        holder.tv_shopaddres.setText(Tool.isNullStr(shopInfoBeans.get(position).getAddress()));

        if (shopInfoBeans.get(position).getDistance()<1) {
        } else {
            holder.tv_gps.setText(Tool.getGpskmorm(shopInfoBeans.get(position).getDistance()));
        }
        if (shopInfoBeans.get(position).getStag() != null & "true".equals(shopInfoBeans.get(position).getStag())) {
            holder.iv_stag.setVisibility(View.GONE);

        } else {
            holder.iv_stag.setVisibility(View.GONE);

        }
        return convertView;
    }

    public void appendData(List<ShopInfoBean> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        View v_shop_line;
        LinearLayout lin_shop_duimess, lin_shop_huimess, lin_shop_quanmess;
        TextView tv_title, tv_type, tv_oper, tv_good, tv_gps, tv_shopaddres, tv_shop_quanmessage, tv_shop_huimessage, tv_shop_duimessage;
        ImageView iv_icon, iv_stag;
    }
}
