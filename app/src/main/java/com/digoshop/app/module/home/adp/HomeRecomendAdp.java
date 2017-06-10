package com.digoshop.app.module.home.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class HomeRecomendAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ShopInfoBean> shopInfoBeans = null;
    private DisplayImageOptions options;

    public HomeRecomendAdp(Context context, ArrayList<ShopInfoBean> shopInfoBeans) {
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
            convertView = minflater.inflate(R.layout.list_recomdeshop_item, null);
            holder = new Viewholder();
            holder.tv_oper = (TextView) convertView.findViewById(R.id.textView2);
            holder.tv_good = (TextView) convertView.findViewById(R.id.textView4);
            holder.tv_gps = (TextView) convertView.findViewById(R.id.textView5);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_shopaddres = (TextView) convertView.findViewById(R.id.tv_shopaddres);
            holder.iv_stag = (ImageView) convertView.findViewById(R.id.iv_stag);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.imageView4 = (ImageView) convertView.findViewById(R.id.imageView4);
            holder.tv_dui_comshop = (TextView) convertView.findViewById(R.id.tv_dui_comshop);
            holder.tv_hui_comshop = (TextView) convertView.findViewById(R.id.tv_hui_comshop);
            holder.tv_quan_comshop = (TextView) convertView.findViewById(R.id.tv_quan_comshop);
            convertView.setTag(holder);
        } else {

            holder = (Viewholder) convertView.getTag();
        }

        //兑换商品
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getProduct())) {
            holder.tv_dui_comshop.setVisibility(View.GONE);
        } else {
            holder.tv_dui_comshop.setVisibility(View.VISIBLE);
        }
        //优惠活动
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getNews())) {
            holder.tv_hui_comshop.setVisibility(View.GONE);
        } else {
            holder.tv_hui_comshop.setVisibility(View.VISIBLE);
        }
        //优惠券
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getCoupon())) {
            holder.tv_quan_comshop.setVisibility(View.GONE);
        } else {
            holder.tv_quan_comshop.setVisibility(View.VISIBLE);
        }

        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getCover(), holder.iv_icon, options);
       // holder.tv_type.setText(Tool.isNullStr(shopInfoBeans.get(position).getOperate()) + "|");
        if(TextUtils.isEmpty(shopInfoBeans.get(position).getSigned())|"0".contains(shopInfoBeans.get(position).getSigned())){
            holder.tv_oper.setText("认证商家 | ");
        }else{
            holder.tv_oper.setText("签约折扣"+shopInfoBeans.get(position).getSigned()+"折"+" | ");
        }
        holder.tv_good.setText(shopInfoBeans.get(position).getGoods());

        holder.tv_title.setText(shopInfoBeans.get(position).getName());
        holder.tv_shopaddres.setText(Tool.isNullStr(shopInfoBeans.get(position).getAddress()));

        if (shopInfoBeans.get(position).getDistance()<1) {
            holder.imageView4.setVisibility(View.INVISIBLE);
        } else {
            holder.imageView4.setVisibility(View.INVISIBLE);
            holder.tv_gps.setText(Tool.getGpskmorm(shopInfoBeans.get(position).getDistance()));
        }
        return convertView;
    }

    public void appendData(List<ShopInfoBean> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        TextView tv_dui_comshop, tv_hui_comshop, tv_quan_comshop;
        TextView tv_title, tv_type, tv_oper, tv_good, tv_gps, tv_shopaddres ;
        ImageView iv_icon, iv_stag, imageView4;
    }
}
