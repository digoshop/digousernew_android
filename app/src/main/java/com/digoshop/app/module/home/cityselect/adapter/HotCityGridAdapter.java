package com.digoshop.app.module.home.cityselect.adapter;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.home.cityselect.model.City;
import com.digoshop.app.module.home.cityselect.model.HotCityBean;

/**
 * author zaaach on 2016/1/26.
 */
public class HotCityGridAdapter extends BaseAdapter {
	private Context mContext;
	public ArrayList<City> mCities;
	public HotCityGridAdapter(Context context,ArrayList<City> hotCityBeens) {
		this.mContext = context;
		this.mCities = hotCityBeens;
//		mCities.add("天津");
//	    mCities.add("上海");
//		mCities.add("杭州");
//		mCities.add("广州");
//		mCities.add("成都");
//		mCities.add("苏州");
//		mCities.add("深圳");
//		mCities.add("南京");
//		mCities.add("重庆");
//		mCities.add("厦门");
//		mCities.add("武汉");
	}

	@Override
	public int getCount() {
		return mCities == null ? 0 : mCities.size();
	}

	@Override
	public Object getItem(int position) {
		return mCities == null ? null : mCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		HotCityViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_hot_city_gridview, parent, false);
			holder = new HotCityViewHolder();
			holder.name = (TextView) view.findViewById(R.id.tv_hot_city_name);
			view.setTag(holder);
		} else {
			holder = (HotCityViewHolder) view.getTag();
		}
		holder.name.setText(mCities.get(position).getName());
		return view;
	}

	public static class HotCityViewHolder {
		TextView name;
	}
}
