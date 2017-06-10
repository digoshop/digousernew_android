package com.digoshop.app.module.arrountshops.htextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.digoshop.R;
import com.digoshop.app.utils.db.CityBean;
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
public class TextsItemAdapter extends BaseAdapter {

	public static final String TAG = "ClubsItemAdapter";
	private Context mContext;
	private ArrayList<CityBean> mClubsItemModelList;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private int choicePos = -1;
	private int posti;
	private int istype =0;
	public TextsItemAdapter(Context context, ArrayList<CityBean> ClubsItemModels,int mposti) {
		mContext = context;
		mClubsItemModelList = ClubsItemModels;
		posti = mposti;
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
	}
	public TextsItemAdapter(Context context, ArrayList<CityBean> ClubsItemModels,int mposti,int istype) {
		mContext = context;
		mClubsItemModelList = ClubsItemModels;
		posti = mposti;
		this.istype = istype;
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
	public CityBean getItem(int position) {
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

		CityBean clubsItemModel = mClubsItemModelList.get(position);
		String clubTitle = clubsItemModel.getNn() != null ? clubsItemModel.getNn() : "";

		if (itemView == null) {
			viewHolder = new ViewHolder();
			if(istype==1){
				itemView = LayoutInflater.from(mContext).inflate(R.layout.text_scrollview_itemarout, null);
			}else{
				itemView = LayoutInflater.from(mContext).inflate(R.layout.text_scrollview_item, null);
			}
			viewHolder.text_aty_title = (RadioButton) itemView.findViewById(R.id.text_aty_title);
			itemView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) itemView.getTag();
		}
		viewHolder.text_aty_title.setText( clubTitle);
		if (posti == position) {
			viewHolder.text_aty_title.setChecked(true);
		} else {
			viewHolder.text_aty_title.setChecked(false);
		}
		return itemView;
	}

	private class ViewHolder {
		LinearLayout linlin;
		RadioButton text_aty_title;
	}
	public void setChoicePos(int position) {
		this.choicePos = position;
	}
}
