package com.digoshop.app.module.home.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.home.model.ShopDetailInfo;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class HomeShopItemAdapter extends BaseAdapter {
    private List<ShopDetailInfo> detailInfos;
    private DisplayImageOptions options;
    private Context context;
    private ShopDetailInfo shopinfo;
    private MyClickListener mListener;
   private int ivheignt;
    public HomeShopItemAdapter(int ivheignt){
        this.ivheignt = ivheignt;
    }
    @Override
    public int getCount() {
        return detailInfos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return detailInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        shopinfo = detailInfos.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.home_shop_item, null);

            holder.iv_home_shop_item = (ImageView) convertView
                    .findViewById(R.id.iv_home_shop_item);


            ViewGroup.LayoutParams params = holder.iv_home_shop_item.getLayoutParams();
            params.height = ivheignt;
            holder.iv_home_shop_item.setLayoutParams(params);
            holder.tv_home_shopname_item = (TextView) convertView
                    .findViewById(R.id.tv_home_shopname_item);
            holder.tv_home_shop_type_item = (TextView) convertView
                    .findViewById(R.id.tv_home_shop_type_item);
            holder.tv_dui_comshop = (TextView) convertView.findViewById(R.id.tv_dui_comshop);
            holder.tv_hui_comshop = (TextView) convertView.findViewById(R.id.tv_hui_comshop);
            holder.tv_quan_comshop = (TextView) convertView.findViewById(R.id.tv_quan_comshop);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_home_shopname_item.setText(Tool.isNullStr(shopinfo.getName()));
        if(TextUtils.isEmpty(shopinfo.getSigned())|"0".contains(shopinfo.getSigned())){
            holder.tv_home_shop_type_item.setText("认证商家");
        }else{
            holder.tv_home_shop_type_item.setText("签约商家" );

          //  holder.tv_home_shop_type_item.setText("签约折扣"+shopinfo.getSigned()+"折"+" ");
        }
       // 兑换商品
        if (TextUtils.isEmpty(detailInfos.get(position).getProduct())) {
            holder.tv_dui_comshop.setVisibility(View.GONE);
        } else {
            holder.tv_dui_comshop.setVisibility(View.VISIBLE);
        }
        //优惠活动
        if (TextUtils.isEmpty(detailInfos.get(position).getNews())) {
            holder.tv_hui_comshop.setVisibility(View.GONE);
        } else {
            holder.tv_hui_comshop.setVisibility(View.VISIBLE);
        }
        //优惠券
        if (TextUtils.isEmpty(detailInfos.get(position).getCoupon())) {
            holder.tv_quan_comshop.setVisibility(View.GONE);
        } else {
            holder.tv_quan_comshop.setVisibility(View.VISIBLE);
        }



        ImageLoader.getInstance().displayImage(shopinfo.getCover(),
                holder.iv_home_shop_item, options);
        holder.iv_home_shop_item.setOnClickListener(mListener);
        holder.iv_home_shop_item.setTag(position);
        return convertView;
    }

    class ViewHolder {
        TextView tv_dui_comshop, tv_hui_comshop, tv_quan_comshop;
        private ImageView iv_home_shop_item;
        private TextView tv_home_shopname_item, tv_home_shop_type_item;
    }

    public void setData(Context mContext, List<ShopDetailInfo> detailInfos,
                        MyClickListener listener) {
        this.ivheignt = ivheignt;
        this.context = mContext;
        mListener = listener;
        this.detailInfos = detailInfos;
        options = new DisplayImageOptions.Builder()
                // .displayer(new RoundedBitmapDisplayer(45))
                .showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001)
                .showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc().build();
    }

    /**
     * 用于回调的抽象类
     *
     * @author Ivan Xu 2014-11-26
     */
    public static abstract class MyClickListener implements OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
