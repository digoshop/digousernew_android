package com.digoshop.app.module.shopdetailnew.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.digoshop.R;
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.digoshop.app.module.product.ProductExchangeAty;
import com.digoshop.app.module.userCenter.model.Discountcoupons;

import java.util.ArrayList;


public class CouponsHorizonScrollView extends HorizontalScrollView {

    public static final String TAG = "ClubsHorizonScrollView";
    private Context mContext;
    private ArrayList<Discountcoupons> poitGoodBeanArrayList;

    private LinearLayout mClubContainerLayout;
    private final int mMarginLeftDp = 10;
//    private TextTypeClickListener textTypeClickListener;

    public CouponsHorizonScrollView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CouponsHorizonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.clubs_horizon_scrollview, this);
        mClubContainerLayout = (LinearLayout) findViewById(R.id.horizonscrollview_linearlayout);
    }

    public void setListData(ArrayList<Discountcoupons> mpoitGoodBeanArrayList, TextTypeClickListener mtextTypeClickListener, int i) {

        CouponsItemAdapter mClubsItemAdapter = new CouponsItemAdapter(mContext, mpoitGoodBeanArrayList, i);
        mClubContainerLayout.removeAllViews();
        addViews(mClubsItemAdapter,mtextTypeClickListener);

    }
    public void setListData(ArrayList<Discountcoupons> mpoitGoodBeanArrayList, TextTypeClickListener mtextTypeClickListener, int i,int type) {

        CouponsItemAdapter mClubsItemAdapter = new CouponsItemAdapter(mContext, mpoitGoodBeanArrayList, i,type);
        mClubContainerLayout.removeAllViews();
        addViews(mClubsItemAdapter,mtextTypeClickListener);

    }



    // 提供外部设置adapter
    public void setAdapter() {

    }

    private void addViews(CouponsItemAdapter mClubsItemAdapter, TextTypeClickListener textTypeClickListener) {
        for (int i = 0; i < mClubsItemAdapter.getCount(); i++) {
            View itemView = mClubsItemAdapter.getView(i, null, null);
            itemView.setOnClickListener(textTypeClickListener);
            itemView.setTag(i);
//			LinearLayout.LayoutParams layoutParams = UIUtils.getLllp(UIUtils.LLW, UIUtils.LLW);
//			layoutParams.leftMargin = UIUtils.dip2px(mContext, mMarginLeftDp);
//			itemView.setLayoutParams(layoutParams);
            mClubContainerLayout.addView(itemView);
        }
    }

    OnClickListener onscClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            PoitGoodBean clubsItemModel = (PoitGoodBean) view.getTag(R.id.tag_clubsitem);
            Intent intent = new Intent(mContext,
                    ProductExchangeAty.class);
            intent.putExtra("pid", clubsItemModel
                    .getPid());
            intent.putExtra("pt", "2");
            mContext.startActivity(intent);

        }
    };

    public static abstract class TextTypeClickListener implements OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
