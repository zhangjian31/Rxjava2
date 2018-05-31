package com.example.zhangjian.rxjava2.utils;

import android.content.Context;

public class DpAndPx {
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }
}
