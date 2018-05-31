package com.example.zhangjian.rxjava2.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhangjian.rxjava2.bean.InterestBean;
import com.example.zhangjian.rxjava2.R;
import com.example.zhangjian.rxjava2.utils.DpAndPx;
import com.example.zhangjian.rxjava2.utils.WindowUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
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
    private LinearLayout container;
    private RelativeLayout title_layout;

    private HorizontalScrollView scrollview;
    private int initTitleX;


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
        LayoutInflater.from(getContext()).inflate(R.layout.interest_top_layout, this, true);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);
        container = (LinearLayout) findViewById(R.id.contain_layout);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        initTitleX = (WindowUtil.getScreenWidth(getContext()) - tvTitle.getWidth()) / 2;
    }

    public View addItem(InterestBean bean) {
        if (container.getVisibility() != View.VISIBLE) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(initTitleX, DpAndPx.dip2px(getContext(), 20));
            valueAnimator.setDuration(100);
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
            container.setVisibility(View.VISIBLE);
        }
        View root = LayoutInflater.from(getContext()).inflate(R.layout.item_top, null);
        SimpleDraweeView imageView = (SimpleDraweeView) root.findViewById(R.id.avatar);
        show(imageView, bean.getUrl());
//        root.setVisibility(INVISIBLE);
        container.addView(root, 0);
        return root;
    }


    public void removeItem(InterestBean bean) {
        if (container.getVisibility() == View.VISIBLE) {
            if (container.getChildCount() > 0) {
                container.removeViewAt(0);
            }
            if (container.getChildCount() == 0) {
                container.setVisibility(View.GONE);
                ValueAnimator valueAnimator = ValueAnimator.ofInt(DpAndPx.dip2px(getContext(), 20), initTitleX);
                valueAnimator.setDuration(100);
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


    private void show(DraweeView targetView, String url) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(300, 300))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(targetView.getController())
                .setImageRequest(request)
                .build();
        targetView.setController(controller);
    }

    public int getContainerLeft() {
        return DpAndPx.dip2px(getContext(), 20) + tvTitle.getMeasuredWidth();
    }
}
