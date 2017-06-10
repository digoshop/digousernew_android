package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.MybagEntity;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class MyGiftAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private List<MybagEntity> MreList;
	private DisplayImageOptions options;

	public MyGiftAdp(Context context, ArrayList<MybagEntity> MreLists) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.MreList = MreLists;
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
	}

	public void appendData(List<MybagEntity> list) {
		this.MreList.addAll(list);
		notifyDataSetChanged();
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
			convertView = minflater.inflate(R.layout.activity_mygift_lv_item, null);
			holder = new viewholder();
			holder.tv_pan = (TextView) convertView.findViewById(R.id.tv_pan);
			holder.tv_epg = (TextView) convertView.findViewById(R.id.tv_epg);
			holder.tv_gifttime = (TextView) convertView.findViewById(R.id.tv_gifttime);
			holder.tv_ppr = (TextView) convertView.findViewById(R.id.tv_ppr);
			holder.tv_epp = (TextView) convertView.findViewById(R.id.tv_epp);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.iv_bag = (ImageView) convertView.findViewById(R.id.iv_bag);
			holder.tv_giftstate = (TextView) convertView.findViewById(R.id.tv_giftstate);

			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}

		String giftstate = Tool.isNullStr(MreList.get(position).getSas());
		if (giftstate.equals("0")) {
			holder.tv_giftstate.setText("未领取");
		} else if (giftstate.equals("1")) {
			holder.tv_giftstate.setText("已领取");
		}else if (giftstate.equals("2")) {
			holder.tv_giftstate.setText("已过期");
		}

		String type = Tool.isNullStr(MreList.get(position).getPt());
		if (type.equals("1")) {
			holder.tv_type.setText("类型:" + "限时竞拍");
		} else if (type.equals("2")) {
			holder.tv_type.setText("类型:" + "积分换购");
		}
		holder.tv_pan.setText(Tool.isNullStr(MreList.get(position).getPna()));
		holder.tv_epg.setText("消耗:" + Tool.isNullStr(MreList.get(position).getEpg()) + "积分");
		holder.tv_ppr.setText("价值:￥" + Tool.isNullStr(MreList.get(position).getPpr()));
		holder.tv_epp.setText("认购金额:" + Tool.isNullStr(MreList.get(position).getEpp()) + "元");
		long n = Long.parseLong(MreList.get(position).getCt());
		// 结束时间
		String Ytime = Tool.getUninxToJava(n);
		holder.tv_gifttime.setText(Ytime);
		ImageLoader.getInstance().displayImage(MreList.get(position).getPpi(), holder.iv_bag, options);
		return convertView;
	}

	public class viewholder {
		TextView tv_gifttime;// 时间
		TextView tv_pan;// 商品名称
		TextView tv_epg;// 消耗
		TextView tv_ppr;// 商品价格
		TextView tv_epp;// 兑换价格
		TextView tv_type;// 类型
		TextView tv_giftstate;// 状态
		ImageView iv_bag;
	}
}