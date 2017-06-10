package com.digoshop.app.module.product.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
public class ProCouponsItemAdapter extends BaseAdapter {

    public static final String TAG = "ClubsItemAdapter";
    private Context mContext;
    private ArrayList<Discountcoupons> mClubsItemModelList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private int choicePos = -1;
    private int posti;

    public ProCouponsItemAdapter(Context context, ArrayList<Discountcoupons> ClubsItemModels, int mposti) {
        mContext = context;
        mClubsItemModelList = ClubsItemModels;
        posti = mposti;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    public ProCouponsItemAdapter(Context context, ArrayList<Discountcoupons> ClubsItemModels, int mposti, int istype) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.a_prodetail_coupon_item, null);
            viewHolder.lin_promanjian = (LinearLayout) itemView.findViewById(R.id.lin_promanjian);
            viewHolder.lin_prozhe = (LinearLayout) itemView.findViewById(R.id.lin_prozhe);
            viewHolder.tv_pron_manjianname = (TextView) itemView.findViewById(R.id.tv_pron_manjianname);
            viewHolder.tv_pron_zhename = (TextView) itemView.findViewById(R.id.tv_pron_zhename);
            viewHolder.lin_daijin = (LinearLayout) itemView.findViewById(R.id.lin_daijin);
            viewHolder.tv_pron_daijinname = (TextView) itemView.findViewById(R.id.tv_pron_daijinname);


            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        if (clubsItemModel.getCtid().equals("1000000")) {//代金券
            viewHolder.lin_daijin.setVisibility(View.VISIBLE);
            viewHolder.lin_promanjian.setVisibility(View.GONE);
            viewHolder.lin_prozhe.setVisibility(View.GONE);
            viewHolder.tv_pron_daijinname.setText("¥"+clubsItemModel.getCbca() );
        } else if (clubsItemModel.getCtid().equals("1000001")) {//折扣券
            viewHolder.lin_promanjian.setVisibility(View.GONE);
            viewHolder.lin_prozhe.setVisibility(View.VISIBLE);
            viewHolder.lin_daijin.setVisibility(View.GONE);
            String strzhe = clubsItemModel.getCbr() * 10 + "";
            if (strzhe != null) {
                if (strzhe.length() >= 3) {
                    strzhe = strzhe.substring(0, 3);
                }
            }

            viewHolder.tv_pron_zhename.setText(strzhe + "折"+"");
        } else if (clubsItemModel.getCtid().equals("1000002")) {//满减券
            viewHolder.lin_promanjian.setVisibility(View.VISIBLE);
            viewHolder.lin_prozhe.setVisibility(View.GONE);
            viewHolder.lin_daijin.setVisibility(View.GONE);
            viewHolder.tv_pron_manjianname.setText("满" + clubsItemModel.getCbcf() + "减" + clubsItemModel.getCbca()+"");
        }

        return itemView;
    }

    private class ViewHolder {
        LinearLayout lin_promanjian, lin_prozhe,lin_daijin;
        private TextView tv_pron_manjianname, tv_pron_zhename,tv_pron_daijinname;
    }

    public void setChoicePos(int position) {
        this.choicePos = position;
    }
}
