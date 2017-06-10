package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.customServices.model.ImgeUrl;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.userCenter.model.MerchanRreplyEntity;
import com.digoshop.app.module.userCenter.module.MerchantreplyActivity;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;

public class MyCustomServiceAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private List<MerchanRreplyEntity> MreList;
    private int currentItem = -1;
    private DisplayImageOptions options;
    private String[] imageid;

    public void appendData(List<MerchanRreplyEntity> list) {
        this.MreList.addAll(list);
        notifyDataSetChanged();
    }

    public MyCustomServiceAdp(Context context, ArrayList<MerchanRreplyEntity> MreLists) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.MreList = MreLists;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.defaluttfang)
                .showImageForEmptyUri(R.drawable.defaluttfang).showImageOnFail(R.drawable.defaluttfang).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.activity_customservice_cs_item, null);
            holder = new viewholder();
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            holder.iv_customservice_item_arrow = (ImageView) convertView.findViewById(R.id.iv_customservice_item_arrow);
            holder.rl_middle = (RelativeLayout) convertView.findViewById(R.id.rl_middle);
            holder.rl_counten = (RelativeLayout) convertView.findViewById(R.id.rl_counten);
            holder.tv_Replynumber = (TextView) convertView.findViewById(R.id.tv_Replynumber);
            holder.ll_countenance = (LinearLayout) convertView.findViewById(R.id.ll_countenance);
            holder.ll_mycust = (LinearLayout) convertView.findViewById(R.id.ll_mycust);
            holder.tv_tn = (TextView) convertView.findViewById(R.id.tv_tn);
            holder.tv_customservice_item_type = (TextView) convertView.findViewById(R.id.tv_customservice_item_type);
            holder.tv_customservice_item_time = (TextView) convertView.findViewById(R.id.tv_customservice_item_time);
            holder.tv_iurls = (ImageView) convertView.findViewById(R.id.tv_iurls);
            holder.tv_iurls1 = (ImageView) convertView.findViewById(R.id.tv_iurls1);
            holder.tv_iurls2 = (ImageView) convertView.findViewById(R.id.tv_iurls2);

            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        holder.tv_tn.setText(Tool.isNullStr(MreList.get(position).getTn()));
        holder.tv_count.setText(Tool.isNullStr(MreList.get(position).getDesc()));
        String type = Tool.isNullStr(MreList.get(position).getTp());
        if (type.equals("1")) {
            holder.tv_customservice_item_type.setText("定制服务");
        } else if (type.equals("2")) {
            holder.tv_customservice_item_type.setText("商品采购");
        }
        holder.tv_Replynumber.setText(MreList.get(position).getRc());
        long n = Long.parseLong(MreList.get(position).getEt());
        // 结束时间
        String Ytime = Tool.getUninxToJavaDay(n);
        holder.tv_customservice_item_time.setText("截至日期:" + Ytime);
        holder.ll_countenance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String num = MreList.get(position).getCid();
                intent.putExtra("cid", num);
                intent.setClass(mContext, MerchantreplyActivity.class);
                mContext.startActivity(intent);
            }
        });
        // 判断当前是展开还是隐藏
        holder.ll_mycust.setTag(position);
        if (currentItem == position) {
            holder.rl_middle.setVisibility(View.VISIBLE);
            holder.iv_customservice_item_arrow.setImageResource(R.drawable.xx_jiantou_down);
            ArrayList<ImgeUrl> imgurls = MreList.get(position).getImgurls();
            if (imgurls != null & imgurls.size() > 0) {
                if (imgurls.get(0).getUrl() != null & imgurls.size() == 1) {
                    holder.tv_iurls.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imgurls.get(0).getUrl(), holder.tv_iurls, options);
                } else if (imgurls.get(1).getUrl() != null & imgurls.size() == 2) {
                    holder.tv_iurls.setVisibility(View.VISIBLE);
                    holder.tv_iurls1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imgurls.get(0).getUrl(), holder.tv_iurls, options);
                    ImageLoader.getInstance().displayImage(imgurls.get(1).getUrl(), holder.tv_iurls1, options);
                } else if (imgurls.get(2).getUrl() != null & imgurls.size() == 3) {
                    holder.tv_iurls.setVisibility(View.VISIBLE);
                    holder.tv_iurls1.setVisibility(View.VISIBLE);
                    holder.tv_iurls2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imgurls.get(0).getUrl(), holder.tv_iurls, options);
                    ImageLoader.getInstance().displayImage(imgurls.get(1).getUrl(), holder.tv_iurls1, options);
                    ImageLoader.getInstance().displayImage(imgurls.get(2).getUrl(), holder.tv_iurls2, options);
                } else {
                    holder.tv_iurls.setVisibility(View.GONE);
                    holder.tv_iurls1.setVisibility(View.GONE);
                    holder.tv_iurls2.setVisibility(View.GONE);
                }
            }
            holder.tv_iurls.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urls = new ArrayList<String>();
                    for (int i = 0; i < MreList.get(position).getImgurls().size(); i++) {
                        urls.add(MreList.get(position).getImgurls().get(i).getUrl());
                    }

                    if(urls!=null){
                        if(urls.size()>0){
                            Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                            intenta.putExtra(EXTRA_IMAGE_URLS, urls);
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                            mContext.startActivity(intenta);
                        }
                    }

                }
            });
            holder.tv_iurls1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urls = new ArrayList<String>();
                    for (int i = 0; i < MreList.get(position).getImgurls().size(); i++) {
                        urls.add(MreList.get(position).getImgurls().get(i).getUrl());
                    }

                    if(urls!=null){
                        if(urls.size()>0){
                            Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                            intenta.putExtra(EXTRA_IMAGE_URLS, urls);
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
                            mContext.startActivity(intenta);
                        }
                    }

                }
            });
            holder.tv_iurls2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urls = new ArrayList<String>();
                    for (int i = 0; i < MreList.get(position).getImgurls().size(); i++) {
                        urls.add(MreList.get(position).getImgurls().get(i).getUrl());
                    }

                    if(urls!=null){
                        if(urls.size()>0){
                            Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                            intenta.putExtra(EXTRA_IMAGE_URLS, urls);
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 2);
                            mContext.startActivity(intenta);
                        }
                    }

                }
            });
        } else {
            holder.iv_customservice_item_arrow.setImageResource(R.drawable.xx_jiantou_right);
            holder.rl_middle.setVisibility(View.GONE);
        }
        holder.ll_mycust.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { // 再次点击
                    currentItem = -1; // 给 currentItem 一个无效值
                } else {
                    currentItem = tag;
                }
                // 通知adapter数据改变需要重新加载
                notifyDataSetChanged(); // 必须有的一步
            }
        });
        return convertView;
    }

    public class viewholder {
        TextView tv_count, tv_tn, tv_customservice_item_type, tv_customservice_item_time;// 说明
        ImageView iv_customservice_item_arrow, tv_iurls, tv_iurls1, tv_iurls2;// 打开关闭箭头
        RelativeLayout rl_middle;// 包裹内容显示隐藏
        RelativeLayout rl_counten;// 顶部点击显示隐藏
        TextView tv_Replynumber;// 回复数量
        LinearLayout ll_countenance;// 商家回复
        LinearLayout ll_mycust;// 背景点击
    }
}