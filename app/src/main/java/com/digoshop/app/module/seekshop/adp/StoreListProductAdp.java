package com.digoshop.app.module.seekshop.adp;

import java.util.List;

import com.digoshop.R;
import com.digoshop.app.module.home.model.ShopInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreListProductAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<ShopInfo> mlist;

	public StoreListProductAdp(Context context, List<ShopInfo> list) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final viewholder holder;
		if (convertView == null) {
			convertView = minflater.inflate(
					R.layout.activity_onetext, null);
			holder = new viewholder();
			holder.tv_storelist_producttype = (TextView) convertView
					.findViewById(R.id.tv_storelist_producttype);

			 

			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		holder.tv_storelist_producttype.setText(mlist.get(position)
				.getShop_type());
		 
		return convertView;
	}

	public class viewholder {
		TextView tv_storelist_producttype;

	}
}
