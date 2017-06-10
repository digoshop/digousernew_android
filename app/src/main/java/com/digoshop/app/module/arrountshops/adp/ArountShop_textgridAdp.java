package com.digoshop.app.module.arrountshops.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.utils.db.CityBean;

import java.util.ArrayList;

public class ArountShop_textgridAdp extends BaseAdapter {
    public static final String TAG = "ArountShop_textgridAdp";
    private Context mContext;
    private ArrayList<CityBean> mClubsItemModelList;
    private int choicePos = -1;

    public ArountShop_textgridAdp(Context context, ArrayList<CityBean> ClubsItemModels ) {
        mContext = context;
        mClubsItemModelList = ClubsItemModels;

    }

    @Override
    public int getCount() {
        return mClubsItemModelList.size();
    }

    @Override
    public CityBean getItem(int position) {
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

        CityBean clubsItemModel = mClubsItemModelList.get(position);
        String clubTitle = clubsItemModel.getNn() != null ? clubsItemModel.getNn() : "";

        if (itemView == null) {
            viewHolder = new ViewHolder();
            itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_arountshop_gvtem, null);
            viewHolder.re_coupintypebg = (RelativeLayout) itemView.findViewById(R.id.re_coupintypebg);
            viewHolder.iv_coupon_honggou = (ImageView) itemView.findViewById(R.id.iv_coupon_honggou);
            viewHolder.tv_coupontype_name = (TextView) itemView.findViewById(R.id.tv_coupontype_name);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        viewHolder.tv_coupontype_name.setText(clubTitle);
        if (choicePos == position) {
            viewHolder.tv_coupontype_name.setTextColor(mContext.getResources().getColor(R.color.textcolor_gvred));
            viewHolder.re_coupintypebg.setBackgroundResource(R.drawable.a_shape_grdi_red);
            viewHolder.iv_coupon_honggou.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_coupontype_name.setTextColor(mContext.getResources().getColor(R.color.textcolor_gvhui));

            viewHolder.re_coupintypebg.setBackgroundResource(R.drawable.a_shape_grdi_hui);
            viewHolder.iv_coupon_honggou.setVisibility(View.GONE);
        }
        return itemView;
    }

    private class ViewHolder {
        RelativeLayout re_coupintypebg;
        ImageView iv_coupon_honggou;
        TextView tv_coupontype_name;
    }

    public void setChoicePos(int position) {
        this.choicePos = position;
    }
}


