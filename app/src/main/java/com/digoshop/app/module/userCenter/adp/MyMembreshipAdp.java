package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.MembreEntity;
import com.digoshop.app.utils.Tool;

import java.util.ArrayList;
import java.util.List;

public class MyMembreshipAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<MembreEntity> MreList;
	public void appendData(List<MembreEntity> list) {
		this.MreList.addAll(list);
		notifyDataSetChanged();
	}

	public MyMembreshipAdp(Context context, ArrayList<MembreEntity> MreLists) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.MreList = MreLists;
	}

	@Override
	public int getCount() {
		return this.MreList != null ? this.MreList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return MreList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final viewholder holder;
		if (convertView == null) {
			holder = new viewholder();
			convertView = minflater.inflate(R.layout.activity_membreshi_lv_item, null);
			holder.tv_intg = (TextView) convertView.findViewById(R.id.tv_intg);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_create_tim = (TextView) convertView.findViewById(R.id.tv_member_time);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);

			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		holder.tv_name.setText(MreList.get(position).getName());
		SpannableString styledTextnow = new SpannableString("积" + MreList.get(position).getIntg() + "分");
		styledTextnow.setSpan(new TextAppearanceSpan(mContext, R.style.score_yellowt), 1,
				1 +  MreList.get(position).getIntg().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.tv_intg.setText(styledTextnow, TextView.BufferType.SPANNABLE);
		String Ytime = Tool.getUninxToJavaDayJ(MreList.get(position).getCreate_time());
		holder.tv_create_tim.setText(Ytime);

		SpannableString styledTextnowarr = new SpannableString("消费" + MreList.get(position).getAmount() + "元");
		styledTextnowarr.setSpan(new TextAppearanceSpan(mContext, R.style.score_yellowt), 2,
				2 +  MreList.get(position).getAmount().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.tv_amount.setText(styledTextnowarr, TextView.BufferType.SPANNABLE);
		return convertView;
	}

	public class viewholder {
		TextView tv_intg;// 积分总计
		TextView tv_name;// 店铺名称
		TextView tv_create_tim;// 创建时间
		TextView tv_amount;// 消费积分
	}
}
