package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.MycardBagEntity;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MyCardAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private List<MycardBagEntity> MreList;
    private DisplayImageOptions options;

    public void appendData(List<MycardBagEntity> list) {
        this.MreList.addAll(list);
        notifyDataSetChanged();
    }

    public MyCardAdp(Context context, ArrayList<MycardBagEntity> MreLists) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.MreList = MreLists;
//		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
//				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
//				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
//				.build();
        //圆角图片
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.kcx_001)
//				.showImageForEmptyUri(R.drawable.kcx_001)
//				.showImageOnFail(R.drawable.kcx_001)
//				.cacheInMemory(true)
//				.cacheOnDisc(true)
//				.bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
//				.displayer(new RoundedBitmapDisplayer(20))
//				.build();
        //圆形图片
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultyuan)
                .showImageForEmptyUri(R.drawable.defaultyuan)
                .showImageOnFail(R.drawable.defaultyuan)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();
    }

    @Override
    public int getCount() {
        return this.MreList != null ? this.MreList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return MreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewholder holder;
        if (convertView == null) {
            holder = new viewholder();
            convertView = minflater.inflate(R.layout.activity_mycard_lv_item, null);
            holder.tv_vip_code = (TextView) convertView.findViewById(R.id.tv_vip_code);
            holder.tv_intg = (TextView) convertView.findViewById(R.id.tv_intg);
            holder.tv_vip_level = (TextView) convertView.findViewById(R.id.tv_vip_level);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_sava = (ImageView) convertView.findViewById(R.id.iv_sava);
            holder.tv_create_tim = (TextView) convertView.findViewById(R.id.tv_create_tim);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);

            holder.iv_isuser = (ImageView) convertView.findViewById(R.id.iv_isuser);


            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        holder.tv_name.setText(MreList.get(position).getShopinfo().getName());
        holder.tv_vip_code.setText("卡号：" + MreList.get(position).getVip_code());
//        SpannableString styledTextnow = new SpannableString("会员积分卡 :  总积分" + MreList.get(position).getIntg()+"币");
//        styledTextnow.setSpan(new TextAppearanceSpan(mContext, R.style.score_yellowt), 12,
//                12 + MreList.get(position).getIntg().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.tv_intg.setText(styledTextnow, TextView.BufferType.SPANNABLE);

        holder.tv_intg.setText(MreList.get(position).getIntg());
        holder.tv_vip_level.setText(MreList.get(position).getVip_level());
        holder.tv_address.setText("办卡地点:" + Tool.isNullStr(MreList.get(position).getShopinfo().getAddress()));
        String Ytime = Tool.getUninxToJavaDayJ(MreList.get(position).getCreate_time());
        holder.tv_create_tim.setText("办卡时间:" + Ytime);

        ImageLoader.getInstance().displayImage(MreList.get(position).getShopinfo().getSava(), holder.iv_sava,
                options);
        if (MreList.get(position).getStatus().equals("1")) {
            holder.iv_isuser.setVisibility(View.GONE);
        } else {
            holder.iv_isuser.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class viewholder {
        ImageView iv_isuser; //是否可以使用//status 状态  1可用 0 冻结
        TextView tv_vip_code;// 会员卡号
        TextView tv_intg;// 消费积分总计
        TextView tv_vip_level;// 会员级别
        TextView tv_name;// 店铺名称
        TextView tv_create_tim;// 创建时间
        TextView tv_address;// 地址
        ImageView iv_sava;// 会员卡logo
    }
}
