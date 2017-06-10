package com.digoshop.app.module.home.adp;

import java.util.List;

import com.digoshop.R;
import com.digoshop.app.utils.db.CityBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuYuAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<CityBean> mlist;

	public QuYuAdp(Context context, List<CityBean> list) {
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
					R.layout.quyulist_item, null);
			holder = new viewholder();
			holder.tv_quyu_text = (TextView) convertView
					.findViewById(R.id.tv_quyu_text);
			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		holder.tv_quyu_text.setText(mlist.get(position)
				.getNn());
	 
		return convertView;
	}

	public class viewholder {
		TextView tv_quyu_text ;

	}
}

