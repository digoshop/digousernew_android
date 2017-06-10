package com.digoshop.app.module.home.hscrollview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.digoshop.app.module.product.ProductExchangeAty;

import java.util.ArrayList;


public class ClubsHorizonScrollView extends HorizontalScrollView {

    public static final String TAG = "ClubsHorizonScrollView";
    private Context mContext;
    private ArrayList<PoitGoodBean> poitGoodBeanArrayList;

    private LinearLayout mClubContainerLayout;
    private ClubsItemAdapter mClubsItemAdapter;
    private final int mMarginLeftDp = 10;

    public ClubsHorizonScrollView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ClubsHorizonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.clubs_horizon_scrollview, this);
        mClubContainerLayout = (LinearLayout) findViewById(R.id.horizonscrollview_linearlayout);
    }

    public void setListData(ArrayList<PoitGoodBean> mpoitGoodBeanArrayList) {
        poitGoodBeanArrayList = mpoitGoodBeanArrayList;
        mClubsItemAdapter = new ClubsItemAdapter(mContext, poitGoodBeanArrayList);
       if(mClubContainerLayout!=null){
           mClubContainerLayout.removeAllViews();
       }
        addViews();
    }

    // 提供外部设置adapter
    public void setAdapter() {

    }

    private void addViews() {
        Log.v("ceshi","addViews+"+mClubsItemAdapter.getCount());
        for (int i = 0; i < mClubsItemAdapter.getCount(); i++) {
            View itemView = mClubsItemAdapter.getView(i, null, null);
            itemView.setOnClickListener(onClickListener);
//			LinearLayout.LayoutParams layoutParams = UIUtils.getLllp(UIUtils.LLW, UIUtils.LLW);
//			layoutParams.leftMargin = UIUtils.dip2px(mContext, mMarginLeftDp);
//			itemView.setLayoutParams(layoutParams);
            mClubContainerLayout.addView(itemView);
        }
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            PoitGoodBean clubsItemModel = (PoitGoodBean) view.getTag(R.id.tag_clubsitem);
            Intent intent = new Intent(mContext,
                    ProductExchangeAty.class);
//            Intent intent = new Intent(mContext,
//                    SaleDetailsActivity.class);
            intent.putExtra("pid", clubsItemModel
                    .getPid());
            intent.putExtra("pt", "2");
            mContext.startActivity(intent);

        }
    };
}
