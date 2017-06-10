package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.digoshop.R;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class Point_AtyAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<ActivityDetailBean> mlist;
    private DisplayImageOptions options;
    private int width, height;

    public Point_AtyAdp(Context context,
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
                    R.layout.point_listview_item, null);
            holder = new viewholder();

            holder.iv_store_atyimg = (ImageView) convertView
                    .findViewById(R.id.iv_store_atyimg);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }



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

        ImageView iv_store_atyimg;
    }
}
