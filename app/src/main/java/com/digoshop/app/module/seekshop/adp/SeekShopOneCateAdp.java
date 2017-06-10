package com.digoshop.app.module.seekshop.adp;

import java.util.ArrayList;

import com.digoshop.R;
import com.digoshop.R.color;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SeekShopOneCateAdp extends BaseAdapter {
	private ArrayList<CategoryChooseBean> categoryChooseBeans;
	private Context mContext;
	private LayoutInflater minflater;

	public SeekShopOneCateAdp(Context context, ArrayList<CategoryChooseBean> MreLists) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.categoryChooseBeans = MreLists;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.store_item_tabgroup, null);

			holder.tv_tabgroup = (TextView) convertView.findViewById(R.id.tv_tabgroup);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_tabgroup.setText(categoryChooseBeans.get(position).getName());
		if (position == selectItem) {
			holder.tv_tabgroup.setTextColor(mContext.getResources().getColor(color.red));
		} else   {
			holder.tv_tabgroup.setTextColor(mContext.getResources().getColor(R.color.store_textColor_unselected));
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_tabgroup;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	private int selectItem = -1;
}
