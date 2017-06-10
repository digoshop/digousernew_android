package com.digoshop.app.module.shopdetail.adp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.shopdetail.model.ShopCommentData;
import com.digoshop.app.utils.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class ShopComentAdp extends BaseAdapter {
	private Context mContext;
	private LayoutInflater minflater;
	private ArrayList<ShopCommentData> commentinfos;
	private DisplayImageOptions options;

	public void appendData(List<ShopCommentData> list) {
		this.commentinfos.addAll(list);
		notifyDataSetChanged();
	}
	public ShopComentAdp(Context context, ArrayList<ShopCommentData> commentinfos) {
		this.mContext = context;
		minflater = LayoutInflater.from(context);
		this.commentinfos = commentinfos;
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
	}

	@Override
	public int getCount() {
		return this.commentinfos != null ? this.commentinfos.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return commentinfos.get(position);
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
			convertView = minflater.inflate(R.layout.shopcommentitem, null);
			holder.lin_myreply_bg = (LinearLayout) convertView.findViewById(R.id.lin_myreply_bg);

			holder.iv_shop_eva_type = (ImageView) convertView.findViewById(R.id.iv_shop_eva_type);
			
			holder.iv_shop_eva_icon = (ImageView) convertView.findViewById(R.id.iv_shop_eva_icon);

			holder.tv_shopcomnick = (TextView) convertView.findViewById(R.id.tv_shopcomnick);
			holder.tv_shopcomtime = (TextView) convertView.findViewById(R.id.tv_shopcomtime);
			holder.tv_shop_eva_type = (TextView) convertView
					.findViewById(R.id.tv_shop_eva_type);
			holder.tv_shopcommoint = (TextView) convertView
					.findViewById(R.id.tv_shopcommoint);
			
			convertView.setTag(holder);
		} else {
			holder = (viewholder) convertView.getTag();
		}
		ShopCommentData commentInfo = commentinfos.get(position);
		holder.tv_shopcomnick.setText(commentInfo.getNick());
		
		ImageLoader.getInstance().displayImage(commentInfo.getAvatar(),
				holder.iv_shop_eva_icon, options);
		holder.tv_shopcomtime.setText(Tool.getUninxToJavaDayJ(commentInfo.getCreate_time()));
		if (commentInfo.getType().equals("0")) {// 0好评 1中评 2 差评
			holder.tv_shop_eva_type.setText("好评");
			holder.iv_shop_eva_type.setBackgroundResource(R.drawable.reply_a);
		} else if (commentInfo.getType().equals("1")) {
			holder.tv_shop_eva_type.setText("中评");
			holder.iv_shop_eva_type.setBackgroundResource(R.drawable.reply_b);
		} else if (commentInfo.getType().equals("2")) {
			holder.tv_shop_eva_type.setText("差评");
			holder.iv_shop_eva_type.setBackgroundResource(R.drawable.reply_c);
		}
		holder.tv_shopcommoint.setText(commentInfo.getText());
		if (position % 2 == 0) {
			holder.lin_myreply_bg.setBackgroundResource(R.color.white_text);
		} else {
			holder.lin_myreply_bg.setBackgroundResource(R.color.store_bg_group_unselected);
		}
		 
 
		return convertView;
	}

	public class viewholder {
		TextView tv_shopcomnick,tv_shopcomtime,tv_shop_eva_type,tv_shopcommoint;
		ImageView iv_shop_eva_icon,iv_shop_eva_type;
		LinearLayout lin_myreply_bg;
	}
}
