package com.digoshop.app.module.shopdetailnew.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.tv_istypebg;

/**
 * Created by lsqbeyond on 2017/3/10.
 */

public class ShopProductAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ShopProduct> shopInfoBeans = null;
    private DisplayImageOptions options;

    public ShopProductAdp(Context context, ArrayList<ShopProduct> shopInfoBeans) {
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
            convertView = minflater.inflate(R.layout.a_shopproduct_item, null);
            holder = new Viewholder();
            holder.lin_shopn_pro_car = (LinearLayout) convertView.findViewById(R.id.lin_shopn_pro_car);
            holder.iv_shopn_pro_img = (ImageView) convertView.findViewById(R.id.iv_shopn_pro_img);
            holder.tv_shopproduct_price = (TextView) convertView.findViewById(R.id.tv_shopproduct_price);
            holder.tv_shopproduct_name = (TextView) convertView.findViewById(R.id.tv_shopproduct_name);
            holder.tv_istypebg = (TextView) convertView.findViewById(tv_istypebg);
            holder.tv_shopproduct_r_price = (TextView) convertView.findViewById(R.id.tv_shopproduct_r_price);
            holder.tv_shopproduct_dbi = (TextView) convertView.findViewById(R.id.tv_shopproduct_dbi);


            convertView.setTag(holder);
        } else {

            holder = (Viewholder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getPicture(), holder.iv_shopn_pro_img, options);
        if (TextUtils.isEmpty(shopInfoBeans.get(position).getPrice())) {
            holder.tv_shopproduct_price.setText(R.string.ddzx);
        } else {
            holder.tv_shopproduct_price.setText("¥" + shopInfoBeans.get(position).getPrice());
        }
        holder.tv_shopproduct_name.setText(shopInfoBeans.get(position).getName());
        Log.v("ceshi", "epg" + shopInfoBeans.get(position).getEpg());
        if ("2".equals(shopInfoBeans.get(position).getType())) {
            //• 2兑换 3普通商品
            holder.lin_shopn_pro_car.setVisibility(View.VISIBLE);
            holder.tv_istypebg.setVisibility(View.VISIBLE);
            holder.tv_shopproduct_dbi.setVisibility(View.VISIBLE);
            holder.tv_shopproduct_dbi.setText("+" + shopInfoBeans.get(position).getEpg() + "币");
            holder.tv_shopproduct_r_price.setVisibility(View.GONE);
        } else if ("3".equals(shopInfoBeans.get(position).getType())) {
            holder.tv_shopproduct_dbi.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(shopInfoBeans.get(position).getR_price())) {
                holder.tv_shopproduct_r_price.setVisibility(View.VISIBLE);
                holder.tv_shopproduct_r_price.setText("¥" + shopInfoBeans.get(position).getR_price());
                holder.tv_shopproduct_r_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.tv_shopproduct_r_price.setVisibility(View.GONE);

            }

            holder.tv_istypebg.setVisibility(View.GONE);
            holder.lin_shopn_pro_car.setVisibility(View.GONE);

        }


        return convertView;
    }

    public void appendData(List<ShopProduct> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        LinearLayout lin_shopn_pro_car;
        ImageView iv_shopn_pro_img;
        TextView tv_shopproduct_name, tv_shopproduct_price, tv_istypebg, tv_shopproduct_r_price, tv_shopproduct_dbi;
    }
}

