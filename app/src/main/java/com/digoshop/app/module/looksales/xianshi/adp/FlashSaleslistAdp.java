package com.digoshop.app.module.looksales.xianshi.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.module.looksales.model.FlashSaleListItemBean;
import com.digoshop.app.utils.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.tv_fsale_ptn;

/**
 * Created by lsqbeyond on 2016/11/17.
 */

public class FlashSaleslistAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<FlashSaleListItemBean> shopInfoBeans = null;
    private DisplayImageOptions options;

    public FlashSaleslistAdp(Context context, ArrayList<FlashSaleListItemBean> shopInfoBeans) {
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
        Viewholder holder = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.flashsale_item, null);
            holder = new Viewholder();

            holder.tv_flashetype = (TextView) convertView.findViewById(R.id.tv_flashetype);

            holder.tv_fslale_pna = (TextView) convertView.findViewById(R.id.tv_fslale_pna);
            holder.tv_fsale_aplg = (TextView) convertView.findViewById(R.id.tv_fsale_aplg);
            holder.tv_fsale_goods = (TextView) convertView.findViewById(R.id.tv_fsale_goods);
            holder.tv_fsale_ppr = (TextView) convertView.findViewById(R.id.tv_fsale_ppr);
            holder.tv_fsale_ptn = (TextView) convertView.findViewById(tv_fsale_ptn);



            holder.iv_flashsale = (ImageView) convertView.findViewById(R.id.iv_flashsale);
            convertView.setTag(holder);
        } else {

            holder = (Viewholder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getPpi(), holder.iv_flashsale, options);
        holder.tv_fslale_pna.setText(shopInfoBeans.get(position).getPna());
        holder.tv_fsale_aplg .setText("¥"+shopInfoBeans.get(position).getApp());
        holder.tv_fsale_ppr.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        holder.tv_fsale_ppr.setText("¥"+shopInfoBeans.get(position).getPpr());
        holder.tv_fsale_ptn.setText(shopInfoBeans.get(position).getPtn());

        SpannableString styledTextnow = new SpannableString("已有"+ shopInfoBeans.get(position).getApc() + "次出价");

        styledTextnow.setSpan(new TextAppearanceSpan(mContext, R.style.score_yellow), 2,
                2 + shopInfoBeans.get(position).getApc().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledTextnow.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(),(float)mContext.getResources().getDimensionPixelSize(R.dimen.base_dimen_24)),true),   2,
                2 + shopInfoBeans.get(position).getApc().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tv_fsale_goods.setText(styledTextnow, TextView.BufferType.SPANNABLE);
//        "竞拍状态（4未开始 5竞拍中 6已结束)
//        ）"

        if("4".equals(shopInfoBeans.get(position).getAps())){
            holder.tv_flashetype.setText("未开始");
        }else if("5".equals(shopInfoBeans.get(position).getAps())){
            holder.tv_flashetype.setText("马上拍");
        }else if("6".equals(shopInfoBeans.get(position).getAps())){
            holder.tv_flashetype.setText("已结束");
        }

        return convertView;
    }

    public void appendData(List<FlashSaleListItemBean> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        TextView  tv_fslale_pna,tv_flashetype,tv_fsale_aplg,tv_fsale_ppr,tv_fsale_ptn,tv_fsale_goods;
        ImageView iv_flashsale;
    }
}
