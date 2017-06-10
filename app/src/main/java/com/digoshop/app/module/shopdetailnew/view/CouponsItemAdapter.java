package com.digoshop.app.module.shopdetailnew.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

/**
 * 社刊 adapter
 *
 * @author zhangzhilai 2014.10.9
 */
public class CouponsItemAdapter extends BaseAdapter {

    public static final String TAG = "ClubsItemAdapter";
    private Context mContext;
    private ArrayList<Discountcoupons> mClubsItemModelList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private int choicePos = -1;
    private int posti;

    public CouponsItemAdapter(Context context, ArrayList<Discountcoupons> ClubsItemModels, int mposti) {
        mContext = context;
        mClubsItemModelList = ClubsItemModels;
        posti = mposti;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    public CouponsItemAdapter(Context context, ArrayList<Discountcoupons> ClubsItemModels, int mposti, int istype) {
        mContext = context;
        mClubsItemModelList = ClubsItemModels;
        posti = mposti;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    @Override
    public int getCount() {
        return mClubsItemModelList.size();
    }

    @Override
    public Discountcoupons getItem(int position) {
        return mClubsItemModelList.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder viewHolder;

        Discountcoupons clubsItemModel = mClubsItemModelList.get(position);
        if (itemView == null) {
            viewHolder = new ViewHolder();
            itemView = LayoutInflater.from(mContext).inflate(R.layout.a_shop_coupon_item, null);
            viewHolder.re_ashop_couponbg = (RelativeLayout) itemView.findViewById(R.id.re_ashop_couponbg);
            viewHolder.tv_shop_coupon_type = (TextView) itemView.findViewById(R.id.tv_shop_coupon_type);
            viewHolder.tv_shop_coupon_price = (TextView) itemView.findViewById(R.id.tv_shop_coupon_price);
            viewHolder.tv_shop_coupon_btn = (TextView) itemView.findViewById(R.id.tv_shop_coupon_btn);
            viewHolder.tv_shop_coupon_price_name = (TextView) itemView.findViewById(R.id.tv_shop_coupon_price_name);


            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        if(clubsItemModel.getCtid().equals("1000000")){//代金券
            viewHolder.tv_shop_coupon_type.setText("代金券");
            viewHolder.re_ashop_couponbg.setBackgroundResource(R.drawable.a_shop_coupon_dai);
            viewHolder.tv_shop_coupon_price.setText(clubsItemModel.getCbca());
            viewHolder.tv_shop_coupon_price_name.setText("元");
            viewHolder.tv_shop_coupon_btn.setTextColor(mContext.getResources().getColor(R.color.a_shop_daihuang));
        }else if(clubsItemModel.getCtid().equals("1000001")){//折扣券
            viewHolder.tv_shop_coupon_type.setText("折扣券");
            viewHolder.re_ashop_couponbg.setBackgroundResource(R.drawable.a_shop_coupon_zhe);
            String strzhe =clubsItemModel.getCbr() * 10 + "";
            if (strzhe != null) {
                if (strzhe.length() >= 3) {
                    strzhe = strzhe.substring(0, 3);
                }
            }
            viewHolder.tv_shop_coupon_btn.setTextColor(mContext.getResources().getColor(R.color.a_shop_zhelan));

            viewHolder.tv_shop_coupon_price.setText(strzhe);
            viewHolder.tv_shop_coupon_price_name.setText("折");
        }else if(clubsItemModel.getCtid().equals("1000002")){//满减券
            viewHolder.tv_shop_coupon_btn.setTextColor(mContext.getResources().getColor(R.color.a_shop_manred));
            viewHolder.tv_shop_coupon_type.setText("满减券");
            viewHolder.re_ashop_couponbg.setBackgroundResource(R.drawable.a_shop_couponman);
            viewHolder.tv_shop_coupon_price.setText(clubsItemModel.getCbca());
            viewHolder.tv_shop_coupon_price_name.setText("元");
        }
        if ("3".equals(clubsItemModel.getStatus())) {
            viewHolder.tv_shop_coupon_btn.setText("已领取");
            //已经领取
        }else if("0".equals(clubsItemModel.getStatus())){
            viewHolder.tv_shop_coupon_btn.setText("已抢光");
            //已经抢光
        }else if("5".equals(clubsItemModel.getStatus())){
            viewHolder.tv_shop_coupon_btn.setText("已过期");
            //已过期
        }else if("1".equals(clubsItemModel.getStatus())){
            viewHolder.tv_shop_coupon_btn.setText("领取");
        }
        return itemView;
    }

    private class ViewHolder {
        RelativeLayout re_ashop_couponbg;
        TextView tv_shop_coupon_type,tv_shop_coupon_price,tv_shop_coupon_price_name,tv_shop_coupon_btn;
    }

    public void setChoicePos(int position) {
        this.choicePos = position;
    }
}
