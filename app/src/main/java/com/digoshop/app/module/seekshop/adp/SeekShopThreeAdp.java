package com.digoshop.app.module.seekshop.adp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.customServices.model.ChildernBean;
import com.digoshop.app.module.seekshop.SeekShopListAty;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class SeekShopThreeAdp extends BaseAdapter {
	private ArrayList<ChildernBean> childernBeans;
	private Context mContext;
	private LayoutInflater minflater;
	private DisplayImageOptions options;

	@SuppressWarnings("deprecation")
	public SeekShopThreeAdp(Context context, ArrayList<ChildernBean> MreLists) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.childernBeans = MreLists;
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001_zeekshop)
				.showImageForEmptyUri(R.drawable.kcx_001_zeekshop).showImageOnFail(R.drawable.kcx_001_zeekshop).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
	}

	@Override
	public int getCount() {
		return this.childernBeans != null ? this.childernBeans.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return childernBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.categorythreegriviewitem, null);
			holder.tv_threename = (TextView) convertView.findViewById(R.id.tv_threename);
			holder.iv_threeicon = (ImageView) convertView.findViewById(R.id.iv_threeicon);
			holder.ll_three = (LinearLayout) convertView.findViewById(R.id.ll_three);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_threename.setText(childernBeans.get(position).getName());
		ImageLoader.getInstance().displayImage(childernBeans.get(position).getIcon(), holder.iv_threeicon, options);
		holder.ll_three.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("operateId", childernBeans.get(position).getMoid());
				intent.putExtra("operatename", childernBeans.get(position).getName());
				intent.setClass(mContext, SeekShopListAty.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView tv_threename;
		private ImageView iv_threeicon;
		private LinearLayout ll_three;
	}

}
