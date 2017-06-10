package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.userCenter.model.CurlistEntity;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;

public class MerchantreplyAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private List<CurlistEntity> CirList;
    private DisplayImageOptions options;
    private MyMessageClickListener messageClickListener;
    public MerchantreplyAdapter(Context context, ArrayList<CurlistEntity> CirList,MyMessageClickListener messageClickListener) {
        this.mContext = context;
        this.messageClickListener = messageClickListener;
        minflater = LayoutInflater.from(context);
        this.CirList = CirList;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    @Override
    public int getCount() {
        return this.CirList != null ? this.CirList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return CirList.get(position);
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
            convertView = minflater.inflate(R.layout.activity_merchantrepl_item, null);
            holder.iv_scov = (ImageView) convertView.findViewById(R.id.iv_scov);
            holder.lin_huifulinurl = (LinearLayout) convertView.findViewById(R.id.lin_huifulinurl);


            holder.iv_huifuurl1 = (ImageView) convertView.findViewById(R.id.iv_huifuurl1);
            holder.iv_huifuurl2 = (ImageView) convertView.findViewById(R.id.iv_huifuurl2);
            holder.iv_huifuurl3 = (ImageView) convertView.findViewById(R.id.iv_huifuurl3);
            holder.iv_huifuurl4 = (ImageView) convertView.findViewById(R.id.iv_huifuurl4);
            holder.iv_huifuurl5 = (ImageView) convertView.findViewById(R.id.iv_huifuurl5);
            holder.tv_sn = (TextView) convertView.findViewById(R.id.tv_sn);
            holder.tv_cuttime = (TextView) convertView.findViewById(R.id.tv_cuttime);
            holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
            holder.tv_csreply_item_mst = (TextView) convertView.findViewById(R.id.tv_csreply_item_mst);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        //1未标记  2已标记
        if ("1".equals(CirList.get(position).getMst())) {
            holder.tv_csreply_item_mst.setText("未标记");
        } else if ("2".equals(CirList.get(position).getMst())) {
            holder.tv_csreply_item_mst.setText("已标记");
        }
        //点击标记
        holder.tv_csreply_item_mst.setTag(position);
        holder.tv_csreply_item_mst.setOnClickListener(messageClickListener);


        ImageLoader.getInstance().displayImage(CirList.get(position).getScov(), holder.iv_scov, options);// 店铺封面图
        holder.tv_sn.setText(CirList.get(position).getSn());// 名称
        holder.tv_explain.setText(CirList.get(position).getRc());// 回复内容
        long n = Long.parseLong(CirList.get(position).getRt());
        // 结束时间
        String Ytime = Tool.getUninxToJava(n);
        holder.tv_cuttime.setText(Ytime);// 时间
          ArrayList<String> huifugoodimgs = CirList.get(position).getHuifugoodimgs();
           Log.v("ceshi","ceshi++"+huifugoodimgs.size());
        if (huifugoodimgs != null & huifugoodimgs.size() > 0) {
            if (huifugoodimgs.get(0).toString() != null & huifugoodimgs.size() == 1) {
                holder.iv_huifuurl1.setVisibility(View.VISIBLE);
                Log.v("ceshi","huifugoodimgs++"+huifugoodimgs.get(0));
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(0), holder.iv_huifuurl1, options);
            } else if (huifugoodimgs.get(1).toString() != null & huifugoodimgs.size() == 2) {
                holder.iv_huifuurl1.setVisibility(View.VISIBLE);
                holder.iv_huifuurl2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(0), holder.iv_huifuurl1, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(1), holder.iv_huifuurl2, options);
            } else if (huifugoodimgs.get(2) != null & huifugoodimgs.size() == 3) {
                holder.iv_huifuurl1.setVisibility(View.VISIBLE);
                holder.iv_huifuurl2.setVisibility(View.VISIBLE);
                holder.iv_huifuurl3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(0), holder.iv_huifuurl1, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(1), holder.iv_huifuurl2, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(2), holder.iv_huifuurl3, options);
            }  else if (huifugoodimgs.get(2) != null & huifugoodimgs.size() == 4) {
                holder.iv_huifuurl1.setVisibility(View.VISIBLE);
                holder.iv_huifuurl2.setVisibility(View.VISIBLE);
                holder.iv_huifuurl3.setVisibility(View.VISIBLE);
                holder.iv_huifuurl4.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(0), holder.iv_huifuurl1, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(1), holder.iv_huifuurl2, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(2), holder.iv_huifuurl3, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(3), holder.iv_huifuurl4, options);


            }  else if (huifugoodimgs.get(2) != null & huifugoodimgs.size() == 5) {
                holder.iv_huifuurl1.setVisibility(View.VISIBLE);
                holder.iv_huifuurl2.setVisibility(View.VISIBLE);
                holder.iv_huifuurl3.setVisibility(View.VISIBLE);
                holder.iv_huifuurl4.setVisibility(View.VISIBLE);
                holder.iv_huifuurl5.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(0), holder.iv_huifuurl1, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(1), holder.iv_huifuurl2, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(2), holder.iv_huifuurl3, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(3), holder.iv_huifuurl4, options);
                ImageLoader.getInstance().displayImage(huifugoodimgs.get(4), holder.iv_huifuurl5, options);

            } else {
                holder.iv_huifuurl1.setVisibility(View.GONE);
                holder.iv_huifuurl2.setVisibility(View.GONE);
                holder.iv_huifuurl3.setVisibility(View.GONE);
                holder.iv_huifuurl4.setVisibility(View.GONE);
                holder.iv_huifuurl5.setVisibility(View.GONE);
            }
            holder.lin_huifulinurl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CirList.get(position).getHuifugoodimgs()!=null){
                        if(CirList.get(position).getHuifugoodimgs().size()>0){
                            Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                            intenta.putExtra(EXTRA_IMAGE_URLS, CirList.get(position).getHuifugoodimgs());
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                            mContext.startActivity(intenta);
                        }
                    }

                }
            });
        }


        return convertView;
    }

    public class viewholder {
        TextView tv_csreply_item_mst;
        ImageView iv_scov, iv_huifuurl1, iv_huifuurl2, iv_huifuurl3,iv_huifuurl4,iv_huifuurl5;
        TextView tv_sn, tv_cuttime, tv_explain;
        LinearLayout lin_huifulinurl;
    }


    public static abstract class MyMessageClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            Log.v("lsq", "++"+(Integer) v.getTag());
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }


}