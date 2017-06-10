package com.digoshop.app.module.arrountshops.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;

import java.util.List;
import java.util.Map;

public class ShopListAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<Map<String, Object>> mlist;

	public ShopListAdp(Context context, List<Map<String, Object>> list) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		mlist = list;
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
			convertView = minflater.inflate(R.layout.list_shop_item, null);
			holder = new viewholder();
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.imageView1 = (ImageView) convertView
					.findViewById(R.id.imageView1);

			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		holder.imageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "电话服务还没有开通哦", 1).show();
			}
		});
		return convertView;
	}

	public class viewholder {
		TextView tv_title;
		ImageView imageView1;
	}
}