package com.digoshop.app.module.home.adp;

import java.util.List;

import com.digoshop.R;
import com.digoshop.app.module.home.model.ShopInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Category_textAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<ShopInfo> mlist;

	public Category_textAdp(Context context, List<ShopInfo> list) {
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
					R.layout.fragment_arountshop_listitem, null);
			holder = new viewholder();
			holder.tv_arountshop_listitme_left = (TextView) convertView
					.findViewById(R.id.tv_arountshop_listitme_left);

			holder.tv_arountshop_listitme_right = (TextView) convertView
					.findViewById(R.id.tv_arountshop_listitme_right);

			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		holder.tv_arountshop_listitme_left.setText(mlist.get(position)
				.getShop_name());
		holder.tv_arountshop_listitme_right.setText(mlist.get(position)
				.getShop_gps());
		return convertView;
	}

	public class viewholder {
		TextView tv_arountshop_listitme_left, tv_arountshop_listitme_right;

	}
}
