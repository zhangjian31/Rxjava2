package com.example.zhangjian.rxjava2.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhangjian.rxjava2.adapter.SelectedAdapter;
import com.example.zhangjian.rxjava2.bean.InterestBean;
import com.example.zhangjian.rxjava2.R;
import com.example.zhangjian.rxjava2.utils.DpAndPx;
import com.example.zhangjian.rxjava2.utils.WindowUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by zhangjian on 2018/5/31.
 */

public class InterestView extends RelativeLayout {
    private List<InterestBean> data;

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private SelectedAdapter selectedAdapter;
    private int initTitleX;
    private LinearLayoutManager manager;

    public InterestView(Context context) {
        super(context);
        init();
    }

    public InterestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public List<InterestBean> getData() {
        return data;
    }

    public void setData(List<InterestBean> data) {
        this.data = data;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.interest_top_layout, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.top_recycleview);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        selectedAdapter = new SelectedAdapter();
        recyclerView.setAdapter(selectedAdapter);
        selectedAdapter.setOnItemClickListener(new SelectedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                InterestBean bean = selectedAdapter.removeData(position);
                if (mOnItemClickToRemove != null) {
                    mOnItemClickToRemove.onRemove(bean);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        initTitleX = (WindowUtil.getScreenWidth(getContext()) - tvTitle.getWidth()) / 2;
    }

    public void addItem(final InterestBean bean) {
        if (selectedAdapter.getItemCount() == 0) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(initTitleX, DpAndPx.dip2px(getContext(), 30));
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int x = (Integer) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
                    p.leftMargin = x;
                    p.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                    tvTitle.setLayoutParams(p);
                }
            });
            valueAnimator.start();

            selectedAdapter.addData(bean);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }, 300);
        } else {
            selectedAdapter.addData(bean);
            manager.findViewByPosition(0).setVisibility(INVISIBLE);
            manager.findViewByPosition(0).postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.findViewByPosition(0).setVisibility(VISIBLE);
                }
            }, 300);
        }
    }


    public void removeItem(InterestBean bean) {
        if (selectedAdapter.getItemCount() != 0) {
            selectedAdapter.removeData(bean);
            if (selectedAdapter.getItemCount() == 0) {
                recyclerView.setVisibility(View.GONE);
                ValueAnimator valueAnimator = ValueAnimator.ofInt(DpAndPx.dip2px(getContext(), 30), initTitleX);
                valueAnimator.setDuration(300);
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int x = (Integer) animation.getAnimatedValue();
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
                        p.leftMargin = x;
                        tvTitle.setLayoutParams(p);
                    }
                });
                valueAnimator.start();
            }
        }
    }

    public int getContainerLeft() {
        return DpAndPx.dip2px(getContext(), 30) + tvTitle.getMeasuredWidth();
    }

    private OnItemClickToRemove mOnItemClickToRemove;

    public void setOnItemClickToRemove(OnItemClickToRemove mOnItemClickToRemove) {
        this.mOnItemClickToRemove = mOnItemClickToRemove;
    }

    public interface OnItemClickToRemove {
        void onRemove(InterestBean bean);
    }
}
