package com.example.zhangjian.rxjava2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by zhangjian on 2018/5/31.
 */

public class InterestView extends LinearLayout{
    private List<InterestBean> data;


    public InterestView(Context context) {
        super(context);
    }

    public InterestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<InterestBean> getData() {
        return data;
    }

    public void setData(List<InterestBean> data) {
        this.data = data;
    }
}
