package com.digoshop.app.module.customServices.adp;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.digoshop.R;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;

public class CategoryChooseAdp extends BaseAdapter {
	private List<CategoryChooseBean> categoryChooseBeans;
	private Context mContext;
	private LayoutInflater minflater;
	private int choicePos = -1;

	public CategoryChooseAdp(Context context, List<CategoryChooseBean> MreLists) {
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.categorychooseatyitem, null);
			holder.tv_categorychoose_item = (RadioButton) convertView.findViewById(R.id.tv_categorychoose_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_categorychoose_item.setText(categoryChooseBeans.get(position).getName());
		if (choicePos == position) {
			holder.tv_categorychoose_item.setChecked(true);
		} else {
			holder.tv_categorychoose_item.setChecked(false);
		}
		return convertView;
	}

	class ViewHolder {
		private RadioButton tv_categorychoose_item;
	}

	public void setChoicePos(int position) {
		this.choicePos = position;
	}
}
