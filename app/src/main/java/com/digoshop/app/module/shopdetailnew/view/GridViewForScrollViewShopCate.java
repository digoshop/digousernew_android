package com.digoshop.app.module.shopdetailnew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewForScrollViewShopCate extends GridView {
    public GridViewForScrollViewShopCate(Context context) {
        super(context);
    }

    public GridViewForScrollViewShopCate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScrollViewShopCate(Context context, AttributeSet attrs,
                                         int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使GridView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private int rownum;

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        int colnum = getNumColumns(); //获取列数
//        int total = getChildCount();  //获取Item总数
//
//        //计算行数
//        if (total % colnum == 0) {
//            rownum = total / colnum;
//        } else {
//            rownum = (total / colnum) + 1; //当余数不为0时，要把结果加上1
//        }
//        Paint localPaint; //设置画笔
//        localPaint = new Paint();
//        localPaint.setStyle(Paint.Style.STROKE); //画笔实心
//        localPaint.setColor(getContext().getResources().getColor(R.color.qianhui));//画笔颜色
//
//        View view0 = getChildAt(0); //第一个view
//        View viewColLast = getChildAt(colnum - 1);//第一行最后一个view
//        View viewRowLast = getChildAt((rownum - 1) * colnum); //第一列最后一个view
//
//
//        for (int i = 1, c = 1; i < rownum || c < colnum; i++, c++) {
//            //画横线
//            canvas.drawLine(view0.getLeft(), view0.getBottom() * i, viewColLast.getRight(), viewColLast.getBottom() * i, localPaint);
//            //画竖线
//            canvas.drawLine(view0.getRight() * c, view0.getTop(), viewRowLast.getRight() * c, viewRowLast.getBottom(), localPaint);
//        }
//    }
}
