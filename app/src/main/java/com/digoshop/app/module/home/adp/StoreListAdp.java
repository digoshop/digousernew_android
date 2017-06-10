package com.digoshop.app.module.home.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.storedetail.model.StoreDetailInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsqbeyond on 2017/1/16.
 */

public class StoreListAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<StoreDetailInfo> mlist;
    private DisplayImageOptions options;

    public void appendData(List<StoreDetailInfo> list) {
        this.mlist.addAll(list);
        notifyDataSetChanged();
    }

    public StoreListAdp(Context context, ArrayList<StoreDetailInfo> list) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        mlist = list;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();

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
        final viewholder holder;
        if (convertView == null) {
            convertView = minflater.inflate(
                    R.layout.storeitem, null);
            holder = new viewholder();
            holder.iv_store_icon = (ImageView) convertView
                    .findViewById(R.id.iv_store_icon);
            holder.tv_store_title = (TextView) convertView
                    .findViewById(R.id.tv_store_title);
            holder.iv_store_icon = (ImageView) convertView.findViewById(R.id.iv_store_icon);
            holder.tv_store_tel = (TextView) convertView
                    .findViewById(R.id.tv_store_tel);
            holder.tv_store_timeone = (TextView) convertView
                    .findViewById(R.id.tv_store_timeone);
            holder.tv_store_timetwo = (TextView) convertView
                    .findViewById(R.id.tv_store_timetwo);
            holder.tv_store_address = (TextView) convertView
                    .findViewById(R.id.tv_store_address);
            holder.tv_store_aty = (TextView) convertView
                    .findViewById(R.id.tv_store_aty);
            holder.lin_opentime = (LinearLayout) convertView.findViewById(R.id.lin_opentime);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }


        holder.tv_store_title.setText(mlist.get(position).getS_name());


        holder.tv_store_tel.setText(mlist.get(position).getS_tel());
        if (!TextUtils.isEmpty(mlist.get(position).getS_open_time())) {
            if (mlist.get(position).getS_open_time().contains(",")) {
                holder.lin_opentime.setVisibility(View.VISIBLE);
            } else {
                holder.lin_opentime.setVisibility(View.GONE);
            }
            String[] sourceStrArray = mlist.get(position).getS_open_time().split(",");
            if(sourceStrArray.length>0){
                holder.tv_store_timeone.setText(sourceStrArray[0]);
            }
            if(sourceStrArray.length>1){
                holder.tv_store_timetwo.setText(sourceStrArray[1]);
            }
        }

        holder.tv_store_address.setText("商场地址："+mlist.get(position).getS_mba());
        if(TextUtils.isEmpty(mlist.get(position).getS_mnti())){
            holder.tv_store_aty.setText("暂无活动");

        }else{
            holder.tv_store_aty.setText(mlist.get(position).getS_mnti());

        }
        ImageLoader.getInstance().displayImage(mlist.get(position).getS_logo(), holder.iv_store_icon, options);

        return convertView;
    }

    public class viewholder {
        private ImageView iv_store_icon;
        private TextView tv_store_title;
        private TextView tv_store_tel;
        private TextView tv_store_timeone;
        private TextView tv_store_timetwo;
        private TextView tv_store_address;
        private TextView tv_store_aty;
        private LinearLayout lin_opentime;
    }
}
