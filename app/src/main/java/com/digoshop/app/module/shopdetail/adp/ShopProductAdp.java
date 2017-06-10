package com.digoshop.app.module.shopdetail.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.ExChangeBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class ShopProductAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ExChangeBean> arrayList;
    private DisplayImageOptions options;

    public ShopProductAdp(Context context, ArrayList<ExChangeBean> arrayList) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    @Override
    public int getCount() {
        return this.arrayList != null ? this.arrayList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.shopproductdetailitem, null);
            holder = new viewholder();
            holder.tv_newproductshopname = (TextView) convertView.findViewById(R.id.tv_newproductshopname);
            holder.tv_newproduct_type = (TextView) convertView.findViewById(R.id.tv_newproduct_type);
            holder.tv_newproductmoney = (TextView) convertView.findViewById(R.id.tv_newproductmoney);
            holder.iv_shopproductimg = (ImageView) convertView.findViewById(R.id.iv_shopproductimg);


            convertView.setTag(holder);
        } else {

            holder = (viewholder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(arrayList.get(position).getPpi(), holder.iv_shopproductimg, options);//ppi 商品图片
        StringBuffer sb = new StringBuffer();
        holder.tv_newproductshopname.setText(arrayList.get(position).getPna());//pna 商品名称
        //pt 商品类型(1竞拍,2换购,3销售)
        if("2".equals(arrayList.get(position).getPt())){
            // eps4兑换中 5已结束 6已兑换
            if("4".equals(arrayList.get(position).getEps())){
                holder.tv_newproduct_type.setText("兑换中");// pd  描述
            }else if("5".equals(arrayList.get(position).getEps())){
                holder.tv_newproduct_type.setText("已结束");// pd  描述
            }if("6".equals(arrayList.get(position).getEps())){
                holder.tv_newproduct_type.setText("已兑换");// pd  描述
            }if("7".equals(arrayList.get(position).getEps())){
                holder.tv_newproduct_type.setText("兑换中");// pd  描述
            }if("8".equals(arrayList.get(position).getEps())){
                holder.tv_newproduct_type.setText("未开始");// pd  描述
            }
            holder.tv_newproductmoney.setText("￥" + arrayList.get(position).getEpp());    // 商品价格

        }else if("1".equals(arrayList.get(position).getPt())){
            //aps竞拍状态（4未开始 5竞拍中 6已结束)
            if("4".equals(arrayList.get(position).getAps())){
                holder.tv_newproduct_type.setText("未开始");// pd  描述
            }else if("5".equals(arrayList.get(position).getAps())){
                holder.tv_newproduct_type.setText("竞拍中");// pd  描述
            }if("6".equals(arrayList.get(position).getAps())){
                holder.tv_newproduct_type.setText("已结束");// pd  描述
            }
            holder.tv_newproductmoney.setText("￥" + arrayList.get(position).getApp());    // 商品价格

        }
        return convertView;
    }

    public void appendData(List<ExChangeBean> exchangeProductList) {
        this.arrayList.addAll(exchangeProductList);
        notifyDataSetChanged();
    }

    public class viewholder {
        TextView tv_newproductshopname, tv_newproduct_type, tv_newproductmoney;
        ImageView iv_shopproductimg;
    }
}
