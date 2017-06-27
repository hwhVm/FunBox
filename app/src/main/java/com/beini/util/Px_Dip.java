package com.beini.util;

import android.content.Context;

/**
 * dip与px互转
 */
public class Px_Dip {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public  static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }/**
     * 迭代相减法
     * @param n 参数1
     * @param m 参数2
     * @return 最大公约数
     */
    public static int getGreatestCommonMeasure_1(int n,int m){
        if(n == 0 || m == 0)
            return 0;

        while(n != 0 &&  m != 0){
            if(n > m)
                n = n - m;
            else{
                m = m - n;
            }
        }
        return n + m;
    }
}
