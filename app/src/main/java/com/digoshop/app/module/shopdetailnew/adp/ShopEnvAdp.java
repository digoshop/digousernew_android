package com.digoshop.app.module.shopdetailnew.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.digoshop.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsqbeyond on 2017/3/10.
 */

public class ShopEnvAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<String> shopInfoBeans = null;
    private DisplayImageOptions options;
    private int heightiv;

    public ShopEnvAdp(Context context, ArrayList<String> shopInfoBeans, int height) {
        this.mContext = context;
        this.heightiv = height;
        minflater = LayoutInflater.from(context);
        this.shopInfoBeans = shopInfoBeans;

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.no_big_png)
                .showImageForEmptyUri(R.drawable.no_big_png).showImageOnFail(R.drawable.no_big_png).cacheInMemory()
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
            convertView = minflater.inflate(R.layout.a_shop_env_item, null);
            holder = new Viewholder();
            holder.iv_shopenv = (ImageView) convertView.findViewById(R.id.iv_shopenv);


            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = holder.iv_shopenv.getLayoutParams();
        params.height = heightiv;
        holder.iv_shopenv.setLayoutParams(params);

        Glide.with(mContext).load(shopInfoBeans.get(position) + "").placeholder(R.drawable.no_big_png).error(R.drawable.no_big_png).into( holder.iv_shopenv);
        //ImageLoader.getInstance().displayImage(shopInfoBeans.get(position) + "", holder.iv_shopenv, options);
        return convertView;
    }

    public void appendData(List<String> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        ImageView iv_shopenv;
    }
}
