package com.digoshop.app.module.seekshop.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;

import java.util.ArrayList;
import java.util.List;

public class SeekShopTwoAdp extends BaseAdapter {
	private ArrayList<CategoryChooseBean> categoryChooseBeans;
	private Context mContext;
	private LayoutInflater minflater;

	public SeekShopTwoAdp(Context context, ArrayList<CategoryChooseBean> categoryChooseBeans) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.categoryChooseBeans = categoryChooseBeans;

	}

	@Override
	public int getCount() {
		return this.categoryChooseBeans != null ? this.categoryChooseBeans.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return categoryChooseBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.categorythreeitem, null);
			holder.tv_catetwoname = (TextView) convertView.findViewById(R.id.tv_catetwoname);
			holder.gr_itemchildren = (GridView) convertView.findViewById(R.id.gr_itemchildren);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_catetwoname.setText(categoryChooseBeans.get(position).getName());
		holder.gr_itemchildren
				.setAdapter(new SeekShopThreeAdp(mContext, categoryChooseBeans.get(position).getChildderlis()));
		return convertView;
	}

	class ViewHolder {
		private TextView tv_catetwoname;
		private GridView gr_itemchildren;
	}
	public void appendData(List<CategoryChooseBean> list) {
		this.categoryChooseBeans.addAll(list);
		notifyDataSetChanged();
	}
}
