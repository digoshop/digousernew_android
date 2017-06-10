package com.digoshop.app.module.userCenter.adp;

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
import com.digoshop.app.module.userCenter.model.MyLikesEntity;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class MyLikeAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<MyLikesEntity> MyjinList;
	private DisplayImageOptions options;
	private boolean isshow;
	public void appendData(List<MyLikesEntity> list) {
		this.MyjinList.addAll(list);
		notifyDataSetChanged();
	}
	public MyLikeAdp(Context context, ArrayList<MyLikesEntity> list ) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		MyjinList = list;
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
	}



	@Override
	public int getCount() {
		return this.MyjinList != null ? this.MyjinList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return MyjinList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final viewholder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.list_shop_item, null);
			holder = new viewholder();
			holder.tv_type = (TextView) convertView.findViewById(R.id.textView3);
			holder.tv_time = (TextView) convertView.findViewById(R.id.textView2);
			holder.tv_good = (TextView) convertView.findViewById(R.id.textView4);
			holder.tv_gps = (TextView) convertView.findViewById(R.id.textView5);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_shop_duimessage = (TextView) convertView.findViewById(R.id.tv_shop_duimessage);
			holder.tv_shop_huimessage = (TextView) convertView.findViewById(R.id.tv_shop_huimessage);
			holder.tv_shop_quanmessage = (TextView) convertView.findViewById(R.id.tv_shop_quanmessage);
			holder.tv_shopaddres = (TextView) convertView.findViewById(R.id.tv_shopaddres);
			holder.iv_stag = (ImageView) convertView.findViewById(R.id.iv_stag);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.imageView2);
			holder.lin_shop_duimess = (LinearLayout) convertView.findViewById(R.id.lin_shop_duimess);
			holder.lin_shop_huimess = (LinearLayout) convertView.findViewById(R.id.lin_shop_huimess);
			holder.lin_shop_quanmess = (LinearLayout) convertView.findViewById(R.id.lin_shop_quanmess);


			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		//兑换商品
		if (TextUtils.isEmpty(MyjinList.get(position).getProduct())) {
			holder.lin_shop_duimess.setVisibility(View.GONE);
		} else {
			holder.lin_shop_duimess.setVisibility(View.VISIBLE);
			holder.tv_shop_duimessage.setText(MyjinList.get(position).getProduct());
		}
		//优惠活动
		if (TextUtils.isEmpty(MyjinList.get(position).getNews())) {
			holder.lin_shop_huimess.setVisibility(View.GONE);
		} else {
			holder.lin_shop_huimess.setVisibility(View.VISIBLE);
			holder.tv_shop_huimessage.setText(MyjinList.get(position).getNews());
		}
		//优惠券
		if (TextUtils.isEmpty(MyjinList.get(position).getCoupon())) {
			holder.lin_shop_quanmess.setVisibility(View.GONE);
		} else {
			holder.lin_shop_quanmess.setVisibility(View.VISIBLE);
			holder.tv_shop_quanmessage.setText(MyjinList.get(position).getCoupon());
		}


		ImageLoader.getInstance().displayImage(MyjinList.get(position).getCover(), holder.iv_icon, options);
		holder.tv_type.setVisibility(View.VISIBLE);
		holder.tv_type.setText(Tool.isNullStr(MyjinList.get(position).getOperate())+" | ");
		holder.tv_time.setVisibility(View.GONE);
		//holder.tv_time.setText(Tool.isNullStr(MyjinList.get(position).getOpen_time()) );
		holder.tv_good.setText(MyjinList.get(position).getGoods());
		holder.tv_title.setText(MyjinList.get(position).getName());
		holder.tv_shopaddres.setText(Tool.isNullStr(MyjinList.get(position).getAddress()) );

		return convertView;
	}
	private class viewholder {

		LinearLayout lin_shop_duimess,lin_shop_huimess,lin_shop_quanmess;
		TextView tv_title,tv_shop_duimessage,tv_shop_huimessage,tv_shop_quanmessage, tv_type,tv_time,tv_good,tv_gps,tv_shopaddres;
		ImageView  iv_icon,iv_stag;
	}


}