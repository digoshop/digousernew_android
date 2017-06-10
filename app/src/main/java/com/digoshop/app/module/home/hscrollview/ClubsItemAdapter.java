package com.digoshop.app.module.home.hscrollview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

/**
 * 社刊 adapter
 * 
 * @author zhangzhilai 2014.10.9
 * 
 */
public class ClubsItemAdapter extends BaseAdapter {

	public static final String TAG = "ClubsItemAdapter";
	private Context mContext;
	private ArrayList<PoitGoodBean> mClubsItemModelList;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	public ClubsItemAdapter(Context context, ArrayList<PoitGoodBean> ClubsItemModels) {
		mContext = context;
		mClubsItemModelList = ClubsItemModels;
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
	}

	@Override
	public int getCount() {
		return mClubsItemModelList.size();
	}

	@Override
	public PoitGoodBean getItem(int position) {
		return mClubsItemModelList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		ViewHolder viewHolder;

		PoitGoodBean clubsItemModel = mClubsItemModelList.get(position);
		String clubImagePath = clubsItemModel.getPpi();
		String clubTitle = clubsItemModel.getPrice() != null ? clubsItemModel.getPrice() : "";
		String clubNum = clubsItemModel.getCost() != null ? clubsItemModel.getCost() : "0";

		if (itemView == null) {
			itemView = LayoutInflater.from(mContext).inflate(R.layout.clubs_scrollview_item, null);
			viewHolder = new ViewHolder();

			viewHolder.clubsImageView = (ImageView) itemView.findViewById(R.id.clubs_imageview);
			viewHolder.clubsTitleTextView = (TextView) itemView.findViewById(R.id.clubs_title);
			viewHolder.clubsNumTextView = (TextView) itemView.findViewById(R.id.clubs_num);
			itemView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) itemView.getTag();
		}
		itemView.setTag(R.id.tag_clubsitem, clubsItemModel);
		ImageLoader.getInstance().displayImage(clubImagePath,viewHolder.clubsImageView, options);
		if(!TextUtils.isEmpty(clubsItemModel.getPrice())){
			viewHolder.clubsTitleTextView.setVisibility(View.VISIBLE);
			viewHolder.clubsTitleTextView.setText("¥"+clubsItemModel.getPrice());
		}else{
			viewHolder.clubsTitleTextView.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(clubsItemModel.getCost())){
			viewHolder.clubsNumTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
			viewHolder.clubsNumTextView.setVisibility(View.VISIBLE);
			viewHolder.clubsNumTextView.setText("¥"+clubsItemModel.getCost());
		}else{
			viewHolder.clubsNumTextView.setVisibility(View.GONE);
		}

		return itemView;
	}

	private class ViewHolder {
		ImageView clubsImageView;
		TextView clubsTitleTextView;
		TextView clubsNumTextView;
	}

}
