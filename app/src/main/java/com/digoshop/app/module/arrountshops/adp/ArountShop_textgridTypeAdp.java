package com.digoshop.app.module.arrountshops.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.arrountshops.model.ArrountitemBean;

import java.util.ArrayList;

public class ArountShop_textgridTypeAdp extends BaseAdapter {
    public static final String TAG = "ArountShop_textgridAdp";
    private Context mContext;
    private ArrayList<ArrountitemBean> mClubsItemModelList;
    private int choicePos = -1;

    public ArountShop_textgridTypeAdp(Context context, ArrayList<ArrountitemBean> ClubsItemModels) {
        mContext = context;
        mClubsItemModelList = ClubsItemModels;

    }

    @Override
    public int getCount() {
        return mClubsItemModelList.size();
    }

    @Override
    public ArrountitemBean getItem(int position) {
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

        ArrountitemBean clubsItemModel = mClubsItemModelList.get(position);
        String clubTitle = clubsItemModel.getName() != null ? clubsItemModel.getName() : "";

        if (itemView == null) {
            viewHolder = new ViewHolder();
            itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_arountshop_gvitemnew, null);
            viewHolder.re_coupintypebg = (RelativeLayout) itemView.findViewById(R.id.re_coupintypebg);
            viewHolder.tv_coupontype_name = (TextView) itemView.findViewById(R.id.tv_coupontype_name);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        viewHolder.tv_coupontype_name.setText(clubTitle);
        if (choicePos == position) {
            viewHolder.tv_coupontype_name.setTextColor(mContext.getResources().getColor(R.color.white_text));
            viewHolder.re_coupintypebg.setBackgroundResource(R.drawable.a_shape_grid_atype_lan);
        } else {
            viewHolder.tv_coupontype_name.setTextColor(mContext.getResources().getColor(R.color.textcolor_gvhuib));

            viewHolder.re_coupintypebg.setBackgroundResource(R.drawable.a_shape_grid_atype_hui);
        }
        return itemView;
    }

    private class ViewHolder {
        RelativeLayout re_coupintypebg;
        TextView tv_coupontype_name;
    }

    public void setChoicePos(int position) {
        this.choicePos = position;
    }
}


