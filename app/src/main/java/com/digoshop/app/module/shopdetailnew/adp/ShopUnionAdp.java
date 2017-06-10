package com.digoshop.app.module.shopdetailnew.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsqbeyond on 2017/3/10.
 */

public class ShopUnionAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ShopInfoBean> shopInfoBeans = null;
    private DisplayImageOptions options;
    private DisplayImageOptions optionsyuan;

    public ShopUnionAdp(Context context, ArrayList<ShopInfoBean> shopInfoBeans) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.shopInfoBeans = shopInfoBeans;
        //圆形图片
//        optionsyuan = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.defaultyuan)
//                .showImageForEmptyUri(R.drawable.defaultyuan)
//                .showImageOnFail(R.drawable.defaultyuan)
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
//                .displayer(new Displayer(0))
//                .build();
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
            convertView = minflater.inflate(R.layout.a_shop_union_item, null);
            holder = new Viewholder();
            holder.iv_shopn_shopbg = (ImageView) convertView.findViewById(R.id.iv_shopn_shopbg);
            holder.tv_shopn_name = (TextView) convertView.findViewById(R.id.tv_shopn_name);

            convertView.setTag(holder);
        } else {

            holder = (Viewholder) convertView.getTag();
        }
        //商铺图片
        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getCover(), holder.iv_shopn_shopbg, optionsyuan);
        //商铺名字
        holder.tv_shopn_name.setText(shopInfoBeans.get(position).getName());


        return convertView;
    }

    public void appendData(List<ShopInfoBean> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        ImageView iv_shopn_shopbg;
        TextView tv_shopn_name;
    }
}
