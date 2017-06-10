package com.digoshop.app.module.storedetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.tv_store_atyname;

public class Preferrential_GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ActivityDetailBean> mlist;
    private DisplayImageOptions options;
    private int width, height;

    public Preferrential_GridViewAdapter(Context context,
                                         ArrayList<ActivityDetailBean> list, int width, int height) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.width = width;
        this.height = height;
        minflater = LayoutInflater.from(context);
        mlist = list;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001)
                .showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc().build();
    }

    @Override
    public int getCount() {
        return this.mlist != null ? this.mlist.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
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
                    R.layout.fragment_preferential_listview_item, null);
            holder = new viewholder();
            holder.tv_store_atyname = (TextView) convertView
                    .findViewById(tv_store_atyname);
            holder.tv_stroe_atytime = (TextView) convertView
                    .findViewById(R.id.tv_stroe_atytime);
            holder.tv_stroe_atyshopname = (TextView) convertView
                    .findViewById(R.id.tv_stroe_atyshopname);
            holder.tv_stroe_atygps = (TextView) convertView
                    .findViewById(R.id.tv_stroe_atygps);
            holder.iv_store_atyimg = (ImageView) convertView
                    .findViewById(R.id.iv_store_atyimg);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }


        holder.tv_store_atyname.setVisibility(View.VISIBLE);
        if (mlist.get(position).getDistance() < 1) {
        } else {
            holder.tv_stroe_atygps.setText(Tool.getGpskmorm(mlist.get(position).getDistance()));
        }
        if (!TextUtils.isEmpty(mlist.get(position).getMnvsd()) & !TextUtils.isEmpty(mlist.get(position)
                .getMnved())) {
            holder.tv_stroe_atytime.setText(Tool.getUninxToJavaDayJDian(mlist.get(position).getMnvsd())
                    + "~"
                    + Tool.getUninxToJavaDayJDian(mlist.get(position)
                    .getMnved()));
        }

        holder.tv_stroe_atyshopname.setText(mlist.get(position).getTname() + " | ");
        holder.tv_store_atyname.setText(mlist.get(position).getMnti());
        ViewGroup.LayoutParams params = holder.iv_store_atyimg.getLayoutParams();
        params.height = height;
        params.width = width;
        holder.iv_store_atyimg.setLayoutParams(params);
        holder.iv_store_atyimg.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(
                mlist.get(position).getMnp(), holder.iv_store_atyimg,
                options);
        return convertView;
    }

    public void appendData(List<ActivityDetailBean> list) {
        this.mlist.addAll(list);
        notifyDataSetChanged();
    }

    public class viewholder {
        TextView tv_store_atyname, tv_stroe_atytime, tv_stroe_atyshopname,
                tv_stroe_atygps;
        ImageView iv_store_atyimg;
    }
}
