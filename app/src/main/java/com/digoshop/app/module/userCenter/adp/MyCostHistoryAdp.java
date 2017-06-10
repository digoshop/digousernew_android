package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.userCenter.model.ConsumerSettlementrecords;
import com.digoshop.app.module.userCenter.module.MyCostHistoryActivity;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.tv_shoppro_name;
import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;

public class MyCostHistoryAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private List<ConsumerSettlementrecords> MreList;
    private MyCostHistoryClickListener clickListener;
    private DisplayImageOptions optionstwo;

    public void appendData(List<ConsumerSettlementrecords> list) {
        this.MreList.addAll(list);
        notifyDataSetChanged();
    }

    public MyCostHistoryAdp(Context context, List<ConsumerSettlementrecords> list,
                            MyCostHistoryClickListener clickListener) {
        this.mContext = context;
        this.clickListener = clickListener;
        minflater = LayoutInflater.from(context);
        MreList = list;
        optionstwo = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    public MyCostHistoryAdp(MyCostHistoryActivity myCostHistoryActivity, ArrayList<ConsumerSettlementrecords> cstList) {
        // TODO Auto-generated constructor stub
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewholder holder;
        if (convertView == null) {
            holder = new viewholder();
            convertView = minflater.inflate(R.layout.activity_costhistory_list_item, null);
            holder.iv_ping_type = (ImageView) convertView.findViewById(R.id.iv_ping_type);
            holder.iv_shopppi = (ImageView) convertView.findViewById(R.id.iv_shopppi);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_ping_type = (TextView) convertView.findViewById(R.id.tv_ping_type);
            holder.tv_amount_a = (TextView) convertView.findViewById(R.id.tv_amount_a);

            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_shoppro_name = (TextView) convertView.findViewById(tv_shoppro_name);
            holder.tv_shoppro_ppr = (TextView) convertView.findViewById(R.id.tv_shoppro_ppr);

            holder.tv_shoppro_pat = (TextView) convertView.findViewById(R.id.tv_shoppro_pat);
            holder.re_costhis = (RelativeLayout) convertView.findViewById(R.id.re_costhis);
           holder.gv_cs_category= (GridView) convertView.findViewById(R.id.gv_cs_category);
            holder.lin_ping_type = (LinearLayout) convertView.findViewById(R.id.lin_ping_type);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        if (TextUtils.isEmpty(MreList.get(position).getS_pna())) {
            holder.re_costhis.setVisibility(View.GONE);
            holder.gv_cs_category.setVisibility(View.VISIBLE);
            holder.gv_cs_category.setAdapter(new GridCateAdp(mContext,MreList.get(position).getPats()));
        } else {
            holder.gv_cs_category.setVisibility(View.GONE);
            holder.re_costhis.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(MreList.get(position).getS_ppi(), holder.iv_shopppi, optionstwo);
            holder.tv_shoppro_name.setText(  MreList.get(position).getS_pna());
            holder.tv_shoppro_ppr.setText("¥"+ MreList.get(position).getS_ppr());
            holder.tv_shoppro_ppr .getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            holder.tv_shoppro_pat.setText( MreList.get(position).getS_pat());
            holder.iv_shopppi.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urls = new ArrayList<String>();
                    urls.add(MreList.get(position).getS_ppi());
                    if (urls != null) {
                        if (urls.size() > 0) {
                            Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                            intenta.putExtra(EXTRA_IMAGE_URLS, urls);
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                            mContext.startActivity(intenta);
                        }
                    }

                }
            });
        }

        long n = Long.parseLong(MreList.get(position).getCreate_time());
        String time = Tool.getUninxToJava(n);
        holder.tv_name.setText(MreList.get(position).getShop_info());
        holder.tv_time.setText(time);
        holder.tv_amount_a.setText("¥"+MreList.get(position).getAmount()+"    ");
        holder.tv_amount.setText("¥"+MreList.get(position).getAmount());
        if (!TextUtils.isEmpty(MreList.get(position).getIs_comment()) & MreList.get(position).getIs_comment().contains("0")) {
            // 未评价
            holder.iv_ping_type.setBackgroundResource(R.drawable.ping_add);
            holder.tv_ping_type.setText("添加评论");
        } else if (!TextUtils.isEmpty(MreList.get(position).getIs_comment())
                & MreList.get(position).getIs_comment().contains("1")) {
            holder.tv_ping_type.setText("查看评论");
            holder.iv_ping_type.setBackgroundResource(R.drawable.ping_look);
        } else {
        }
        holder.lin_ping_type.setOnClickListener(clickListener);
        holder.lin_ping_type.setTag(position);
        return convertView;
    }

    public class viewholder {
        private LinearLayout lin_ping_type;
        ImageView iv_ping_type, iv_shopppi;
        TextView tv_name;
        TextView tv_time,tv_ping_type;
        private GridView gv_cs_category;
        private RelativeLayout re_costhis;
        TextView tv_amount,tv_amount_a, tv_shoppro_name, tv_shoppro_ppr, tv_shoppro_pat;
    }

    /**
     * 用于回调的抽象类
     *
     * @author Ivan Xu 2014-11-26
     */
    public static abstract class MyCostHistoryClickListener implements OnClickListener {
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
