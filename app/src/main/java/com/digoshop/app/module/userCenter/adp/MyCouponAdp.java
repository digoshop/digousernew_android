package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.utils.Tool;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.iv_islingqu;
import static com.digoshop.R.id.re_coupon_item_bg_ctid;

public class MyCouponAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<Discountcoupons> mlist;

    public void appendData(List<Discountcoupons> list) {
        this.mlist.addAll(list);
        notifyDataSetChanged();
    }

    public MyCouponAdp(Context context, ArrayList<Discountcoupons> list) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        mlist = list;

    }

    @Override
    public int getCount() {
        return this.mlist != null ? this.mlist.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(
                    R.layout.home_goucoupon_item, null);
            holder = new  viewholder();
            holder.tv_coupon_item_cbn = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_cbn);
            holder.tv_coupon_item_money_cbca = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_money_cbca);
            holder.tv_coupon_item_cbsnu = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_cbsnu);
            holder.re_coupon_item_bg_ctid = (RelativeLayout) convertView
                    .findViewById(re_coupon_item_bg_ctid);
            holder.tv_coupon_item_bg_ctid = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_bg_ctid);
            holder.tv_coupon_item_operates = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_operates);
            holder.iv_islingqu = (ImageView) convertView
                    .findViewById(iv_islingqu);
            holder.tv_couponactive_dazhe_dianji1 = (TextView) convertView
                    .findViewById(R.id.tv_couponactive_dazhe_dianji1);
            holder.re_couponright = (RelativeLayout) convertView.findViewById(R.id.re_couponright);
            holder.re_couponleft = (RelativeLayout) convertView.findViewById(R.id.re_couponleft);

            holder.tv_coupon_item_tiaojian_cbcf = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_tiaojian_cbcf);
            holder.tv_coupon_item_money_cbca_name = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_money_cbca_name);
            holder.tv_coupon_item_time_cbvsd_cbved = (TextView) convertView
                    .findViewById(R.id.tv_coupon_item_time_cbvsd_cbved);
            holder.lin_shengyulin = (LinearLayout) convertView.findViewById(R.id.lin_shengyulin);
            convertView.setTag(holder);
        } else {
            holder = ( viewholder) convertView.getTag();
        }
        holder.tv_coupon_item_cbn.setText(mlist.get(position).getTname());
        holder.tv_coupon_item_cbsnu.setText(mlist.get(position).getCbsnu());

        if (mlist.get(position).getCtid().equals("1000000")) {//优惠券类型//代金券/1000000
            holder.tv_coupon_item_bg_ctid.setText("代金券");
            holder.tv_coupon_item_tiaojian_cbcf.setVisibility(View.GONE);
            holder.tv_coupon_item_bg_ctid.setTextColor(mContext.getResources().getColor(R.color.textcolor_lv));
            holder.tv_coupon_item_operates.setTextColor(mContext.getResources().getColor(R.color.textcolor_lv));
            holder.re_couponright.setBackgroundResource(R.drawable.shape_lv_right);
            holder.re_couponleft.setBackgroundResource(R.drawable.shape_lv);
        } else if (mlist.get(position).getCtid().equals("1000001")) {// 折扣券/1000001
            holder.tv_coupon_item_tiaojian_cbcf.setVisibility(View.VISIBLE);

            holder.tv_coupon_item_tiaojian_cbcf.setText("满" + mlist.get(position).getCbcf() + "可以用");

            // holder.re_coupon_item_bg_ctid.setBackgroundResource(R.drawable.zhekouquan);
            holder.re_couponright.setBackgroundResource(R.drawable.shape_fen_right);
            holder.re_couponleft.setBackgroundResource(R.drawable.shape_fen);
            holder.tv_coupon_item_bg_ctid.setTextColor(mContext.getResources().getColor(R.color.textcolor_fen));
            holder.tv_coupon_item_operates.setTextColor(mContext.getResources().getColor(R.color.textcolor_fen));
            holder.tv_coupon_item_bg_ctid.setText("折扣券");
        } else if (mlist.get(position).getCtid().equals("1000002")) {//满减券/1000002
            holder.tv_coupon_item_tiaojian_cbcf.setVisibility(View.VISIBLE);

            //  holder.re_coupon_item_bg_ctid.setBackgroundResource(R.drawable.manjianquan);
            holder.tv_coupon_item_bg_ctid.setText("满减券");
            holder.tv_coupon_item_tiaojian_cbcf.setText("满" + mlist.get(position).getCbcf() + "可以用");

            holder.tv_coupon_item_bg_ctid.setTextColor(mContext.getResources().getColor(R.color.textcolor_lan));
            holder.tv_coupon_item_operates.setTextColor(mContext.getResources().getColor(R.color.textcolor_lan));
            holder.re_couponright.setBackgroundResource(R.drawable.shape_lan_right);
            holder.re_couponleft.setBackgroundResource(R.drawable.shape_lan);
        }

        if (mlist.get(position).getOperatesstr() == null || "".equals(mlist.get(position).getOperatesstr())) {

        } else {
            holder.tv_coupon_item_operates.setText(mlist.get(position).getOperatesstr());
        }


        holder.tv_coupon_item_time_cbvsd_cbved.setText(Tool.getUninxToJavaDay(Long.parseLong(mlist.get(position).getCbvsd())) + "-" + Tool.getUninxToJavaDay(Long.parseLong(mlist.get(position).getCbved())));
        if (mlist.get(position).getCtid().equals("1000001")) {//折扣券
            String strzhe = mlist.get(position).getCbr() * 10 + "";
            if (strzhe != null) {
                if (strzhe.length() >= 3) {
                    strzhe = strzhe.substring(0, 3);
                }
            }

            holder.tv_coupon_item_money_cbca.setText(strzhe);
            holder.tv_coupon_item_money_cbca_name.setText("折");
        } else {
            //代金额度
            holder.tv_coupon_item_money_cbca.setText(mlist.get(position).getCbca());
            holder.tv_coupon_item_money_cbca_name.setText("元");

        }
        if ("3".equals(mlist.get(position).getStatus())) {
            holder.tv_couponactive_dazhe_dianji1.setVisibility(View.GONE);
            holder.tv_couponactive_dazhe_dianji1.setText("");
            holder.iv_islingqu.setBackgroundResource(R.drawable.gouyouhui_yilingqu);
            holder.iv_islingqu.setVisibility(View.VISIBLE);
            holder.lin_shengyulin.setVisibility(View.GONE);
        } else if ("0".equals(mlist.get(position).getStatus())) {
            holder.iv_islingqu.setBackgroundResource(R.drawable.gouyouhui_yiqiangguang);
            holder.iv_islingqu.setVisibility(View.VISIBLE);
            holder.tv_couponactive_dazhe_dianji1.setVisibility(View.GONE);

            holder.tv_couponactive_dazhe_dianji1.setText("");
            holder.lin_shengyulin.setVisibility(View.GONE);
        } else if ("5".equals(mlist.get(position).getStatus())) {
            holder.iv_islingqu.setBackgroundResource(R.drawable.gouyouhui_yiguoqi);
            holder.lin_shengyulin.setVisibility(View.GONE);
            holder.iv_islingqu.setVisibility(View.VISIBLE);
            holder.tv_couponactive_dazhe_dianji1.setVisibility(View.GONE);

            holder.tv_couponactive_dazhe_dianji1.setText("");
        } else if ("1".equals(mlist.get(position).getStatus())) {
            holder.tv_couponactive_dazhe_dianji1.setVisibility(View.VISIBLE);

            holder.iv_islingqu.setVisibility(View.GONE);
            holder.lin_shengyulin.setVisibility(View.VISIBLE);
            holder.tv_couponactive_dazhe_dianji1.setText("点击领取");
        }
        return convertView;
    }

    public class viewholder {
        private LinearLayout lin_shengyulin;
        private TextView tv_coupon_item_money_cbca, tv_coupon_item_money_cbca_name;//优惠券抵扣金额
        private TextView tv_coupon_item_cbn;//优惠券名字
        private TextView tv_coupon_item_cbsnu;//剩余数量
        private RelativeLayout re_couponright, re_couponleft, re_coupon_item_bg_ctid;//优惠券类型//代金券/1000000 折扣券/1000001 满减券/1000002
        private ImageView iv_coupon_itme_icon_cbi, iv_couponactive_dazhe_arrow1, iv_islingqu;//优惠券的icon
        private TextView tv_coupon_item_bg_ctid;//优惠券类型//代金券/1000000 折扣券/1000001 满减券/1000002
        private TextView tv_coupon_item_tiaojian_cbcf;//使用条件
        private TextView tv_coupon_item_operates;//使用品类单位空的时候就默认显示全部品类
        private TextView tv_coupon_item_time_cbvsd_cbved, tv_couponactive_dazhe_dianji1;//使用时间
    }
}
