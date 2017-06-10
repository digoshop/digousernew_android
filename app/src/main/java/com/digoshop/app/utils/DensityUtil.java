package com.digoshop.app.utils;

import android.content.Context;

/**
 * Created by lsqbeyond on 2016/12/6.
 */

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int floatToInt(float f) {
        int i = 0;
        if (f > 0) //正数
        {
            i = (int) ((f * 10 + 5) / 10);
        } else if (f < 0) //负数
        {
            i = (int) ((f * 10 - 5) / 10);
        } else {
            i = 0;
        }

        return i;

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
