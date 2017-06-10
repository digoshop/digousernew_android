package com.digoshop.app.module.userCenter.adp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.PointsEntity;
import com.digoshop.app.utils.Tool;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class MyjifenAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<PointsEntity> MyjinList;
	public void appendData(List<PointsEntity> list) {
		this.MyjinList.addAll(list);
		notifyDataSetChanged();
	}
	public MyjifenAdp(Context context, ArrayList<PointsEntity> MyjinList) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.MyjinList = MyjinList;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_usercenter_jifen_item, null);
			holder = new ViewHolder();
			holder.tv_jifen_content = (TextView) convertView.findViewById(R.id.tv_jifen_content);
			holder.tv_jifen_time = (TextView) convertView.findViewById(R.id.tv_jifen_time);
			holder.tv_jifen_point = (TextView) convertView.findViewById(R.id.tv_jifen_point);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		long n = Long.parseLong(MyjinList.get(position).getCreate_time());
		holder.tv_jifen_time.setText(Tool.getUninxToJava(n));
		if(!TextUtils.isEmpty(MyjinList.get(position).getIntg())&!MyjinList.get(position).getIntg().contains("-")&!MyjinList.get(position).getIntg().equals("0")){
			//holder.tv_jifen_point.getResources().getColor(R.color.jui_text);
			holder.tv_jifen_point.setTextColor(Color.parseColor("#FFE700"));
			holder.tv_jifen_point.setText("+"+MyjinList.get(position).getIntg());
		}else{
			holder.tv_jifen_point.setTextColor(Color.parseColor("#C4C4C4"));

			//holder.tv_jifen_point.getResources().getColor(R.color.hui_color);
			holder.tv_jifen_point.setText(MyjinList.get(position).getIntg());
		}

		if (MyjinList.get(position).getType().equals("0")) {
			holder.tv_jifen_content.setText("注册获得");
		} else if (MyjinList.get(position).getType().equals("1")) {
			holder.tv_jifen_content.setText("消费获得");
		} else if (MyjinList.get(position).getType().equals("2")) {
			holder.tv_jifen_content.setText("兑换扣除");
		} else if (MyjinList.get(position).getType().equals("3")) {
			holder.tv_jifen_content.setText("签到获得");
		} else if (MyjinList.get(position).getType().equals("4")) {
			holder.tv_jifen_content.setText("竞拍扣除");
		} else if (MyjinList.get(position).getType().equals("5")) {
			holder.tv_jifen_content.setText("领取优惠劵扣除");
		} else if (MyjinList.get(position).getType().equals("6")) {
			holder.tv_jifen_content.setText("发布定制服务");
		}else if (MyjinList.get(position).getType().equals("7")) {
			holder.tv_jifen_content.setText("兑换结算");
		}else if (MyjinList.get(position).getType().equals("8")) {
			holder.tv_jifen_content.setText("竞拍结算");
		}else if (MyjinList.get(position).getType().equals("9")) {
			holder.tv_jifen_content.setText("签约折扣");
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_jifen_content, tv_jifen_time, tv_jifen_point;

	}
}
