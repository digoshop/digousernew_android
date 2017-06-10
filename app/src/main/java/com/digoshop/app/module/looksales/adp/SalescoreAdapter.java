package com.digoshop.app.module.looksales.adp;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.digoshop.app.utils.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class SalescoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<PoitGoodBean> exchangeProductListadp;
    private DisplayImageOptions options;
    private LayoutInflater minflater;
    private int heightiv;

    public SalescoreAdapter(Context context, List<PoitGoodBean> exchangeProductList, int heightiv) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.exchangeProductListadp = exchangeProductList;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        this.heightiv = heightiv;


    }

    @Override
    public int getCount() {
        return this.exchangeProductListadp != null ? this.exchangeProductListadp.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return exchangeProductListadp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(
                    R.layout.sale_score_list, null);
            holder = new viewholder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.Name = (TextView) convertView.findViewById(R.id.textView1);
            holder.textView4 = (TextView) convertView.findViewById(R.id.textView4);


            holder.Score = (TextView) convertView.findViewById(R.id.textView3);
            holder.Day = (TextView) convertView.findViewById(R.id.textView2);

            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();
        params.height = this.heightiv;
        holder.mImageView.setLayoutParams(params);
        ImageLoader.getInstance().displayImage(exchangeProductListadp.get(position).getPpi(), holder.mImageView, options);
        holder.Name.setText(exchangeProductListadp.get(position).getPna());
        String sc = exchangeProductListadp.get(position).getEpp() + "+" + exchangeProductListadp.get(position).getEpg();
        SpannableString styledText = new SpannableString("￥" + sc + "币");
        //  private String  eps ;//4兑换中 5已结束 6已兑换
        if ("4".equals(exchangeProductListadp.get(position).getEps())) {
            holder.textView4.setText("兑换中");
        } else if ("5".equals(exchangeProductListadp.get(position).getEps())) {
            holder.textView4.setText("已结束");
        } else if ("6".equals(exchangeProductListadp.get(position).getEps())) {
            holder.textView4.setText("已兑换");
        }else if ("7".equals(exchangeProductListadp.get(position).getEps())) {
            holder.textView4.setText("兑换中");
        }else if ("8".equals(exchangeProductListadp.get(position).getEps())) {
            holder.textView4.setText("未开始");
        }
        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.score_yellow), 1, 1 + sc.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(),(float)mContext.getResources().getDimensionPixelSize(R.dimen.base_dimen_22)),true),  1, 1 + sc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.Score.setText(styledText, TextView.BufferType.SPANNABLE);
        if (exchangeProductListadp.get(position).getEprd() != null) {
            //            if (exchangeProductListadp.get(position).getEprd().equals("0")) {
            //                holder.Day.setText("剩余" + "1" + "天");
            //            } else {
            holder.Day.setText("剩余" + exchangeProductListadp.get(position).getEprd() + "天");
            //    }
        }
        return convertView;
    }

    public class viewholder {
        ImageView mImageView;
        TextView Name, Score, Day, textView4;
    }

    public void appendData(List<PoitGoodBean> exchangeProductList) {
        this.exchangeProductListadp.addAll(exchangeProductList);
        notifyDataSetChanged();
    }


}