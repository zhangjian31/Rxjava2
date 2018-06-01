package com.example.zhangjian.rxjava2.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangjian.rxjava2.BezierEvaluator;
import com.example.zhangjian.rxjava2.Constants;
import com.example.zhangjian.rxjava2.adapter.InterestAdapter;
import com.example.zhangjian.rxjava2.bean.InterestBean;
import com.example.zhangjian.rxjava2.utils.DpAndPx;
import com.example.zhangjian.rxjava2.view.InterestView;
import com.example.zhangjian.rxjava2.bean.Point;
import com.example.zhangjian.rxjava2.R;
import com.example.zhangjian.rxjava2.utils.WindowUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InterestActivity extends Activity implements View.OnClickListener, InterestAdapter.OnItemClickListener {

    private Button mBtnChange, mBtnOk;
    private ImageView mIvSkip;
    private TextView mTvSkip;
    private InterestView mInterestView;
    private SimpleDraweeView mAnimAvatar;

    private RecyclerView mRecycleview;
    private InterestAdapter mInterestAdapter;

    private Map<Integer, InterestBean> mSelectedMap = new LinkedHashMap<>();
    private int mCurPage = 0;
    private int mTotalPage = 3;
    private List<InterestBean> mCachedList;
    private long mLastChangeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_interest);
        findViews();
        setListener();
        initData();

    }

    private void findViews() {
        mRecycleview = (RecyclerView) findViewById(R.id.recycleview);
        mAnimAvatar = (SimpleDraweeView) findViewById(R.id.avatar_anim);
        mIvSkip = (ImageView) findViewById(R.id.iv_skip);
        mTvSkip = (TextView) findViewById(R.id.tv_skip);
        mInterestView = (InterestView) findViewById(R.id.interestView);
        mBtnChange = (Button) findViewById(R.id.btn_change);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mRecycleview.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        mInterestAdapter = new InterestAdapter();
        mRecycleview.setAdapter(mInterestAdapter);
    }

    private void setListener() {
        mBtnChange.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
        mIvSkip.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);
        mInterestAdapter.setOnItemClickListener(this);
        mInterestView.setOnItemClickToRemove(new InterestView.OnItemClickToRemove() {
            @Override
            public void onRemove(InterestBean bean) {
                if (bean == null) {
                    return;
                }
                mSelectedMap.remove(bean.getId());
                mInterestAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        getDataFromNet();
//        initSlectMap();
//        initTopList();
        updateCurrentPage();
    }

    /**
     * 获取网络数据
     */
    private void getDataFromNet() {
        mCachedList = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            InterestBean bean = new InterestBean();
            bean.setId(i);
            bean.setTitle("title-" + i);
            bean.setUrl(Constants.urls[i]);
            mCachedList.add(bean);
        }
    }

    /**
     * 初始化 mSelectedMap
     */
    private void initSlectMap() {
        mSelectedMap.put(mCachedList.get(0).getId(), mCachedList.get(0));
        mSelectedMap.put(mCachedList.get(3).getId(), mCachedList.get(3));
//        mSelectedMap.put(mCachedList.get(12).getId(), mCachedList.get(12));
    }

    /**
     * 初始化顶部列表
     */
    private void initTopList() {
        Set<Map.Entry<Integer, InterestBean>> set = mSelectedMap.entrySet();
        Iterator<Map.Entry<Integer, InterestBean>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, InterestBean> entry = iterator.next();
            InterestBean bean = entry.getValue();
            mInterestView.addItem(bean);
        }
    }

    /**
     * 更新当前页面九宫格
     */
    private void updateCurrentPage() {
        List<InterestBean> list = new ArrayList<>();
        mCurPage = mCurPage % mTotalPage;
        for (int i = mCurPage * 9; i < Math.min((mCurPage + 1) * 9, mCachedList.size()); i++) {
            list.add(mCachedList.get(i));
        }
        mCurPage++;
        mInterestAdapter.setData(list, mSelectedMap);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_change) {
            if (System.currentTimeMillis() - mLastChangeTime > 1000) {
                mLastChangeTime = System.currentTimeMillis();
                updateCurrentPage();
            }
        } else if (v.getId() == R.id.btn_ok) {
            Toast.makeText(this, "已上传", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.iv_skip || v.getId() == R.id.tv_skip) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public void onItemClick(final View itemView, int position) {
        final InterestBean bean = mInterestAdapter.getData().get(position);
        if (mSelectedMap.containsKey(bean.getId())) {
//            showRemoveAnim(itemView, bean);
            mSelectedMap.remove(bean.getId());
            mInterestView.removeItem(bean);
            mInterestAdapter.setNeedShowAnimal(false);
            mInterestAdapter.notifyItemChanged(position);
        } else {
            mSelectedMap.put(bean.getId(), bean);
            mInterestView.addItem(bean);
            showAddAnim(itemView, bean);
            mInterestAdapter.setNeedShowAnimal(false);
            mInterestAdapter.notifyItemChanged(position);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat( itemView, "scaleX", 1.0f, 1.2f, 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat( itemView, "scaleY", 1.0f, 1.2f, 1.0f);
            AnimatorSet set = new AnimatorSet();
            set.play(scaleX).with(scaleY);
            set.setDuration(1000);
            set.start();
        }
    }

    private void showRemoveAnim(View itemView, InterestBean bean) {
        show(mAnimAvatar, bean.getUrl());

        int[] startLocation = new int[2];
        mInterestView.getLocationOnScreen(startLocation);
        Point startPoint = new Point();
        startPoint.set(mInterestView.getContainerLeft(), (int) (startLocation[1] - WindowUtil.getStatusBarHeight(this) - DpAndPx.dip2px(this, 5.5f)));


        int[] endLocation = new int[2];
        itemView.findViewById(R.id.avatar).getLocationOnScreen(endLocation);
        Point endPoint = new Point();
        endPoint.set(endLocation[0], (int) (endLocation[1] - WindowUtil.getStatusBarHeight(this)));


        int pointX = WindowUtil.getScreenWidth(this) / 2;
        if (Math.abs(endLocation[0] - pointX) < DpAndPx.dip2px(this, 50)) {
            pointX = endLocation[0];
        }
        int pointY = (startPoint.y + endPoint.y) * 2 / 3;
        Point controllPoint = new Point(pointX, pointY);


        AnimatorSet set = new AnimatorSet();
        ValueAnimator scalAnim = ValueAnimator.ofFloat(DpAndPx.dip2px(this, 34), DpAndPx.dip2px(this, 64));
        scalAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float size = (Float) animation.getAnimatedValue();
                mAnimAvatar.setScaleX(size / DpAndPx.dip2px(InterestActivity.this, 64));
                mAnimAvatar.setScaleY(size / DpAndPx.dip2px(InterestActivity.this, 64));
                mAnimAvatar.invalidate();
            }
        });

        ValueAnimator animator = ValueAnimator.ofObject(new BezierEvaluator(controllPoint), startPoint, endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                mAnimAvatar.setX(point.x);
                mAnimAvatar.setY(point.y);
                mAnimAvatar.invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimAvatar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimAvatar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setDuration(500);
        set.play(scalAnim).with(animator);
        set.start();

    }


    private void showAddAnim(View itemView, InterestBean bean) {
        show(mAnimAvatar, bean.getUrl());

        int[] startLocation = new int[2];
        itemView.findViewById(R.id.avatar).getLocationOnScreen(startLocation);
        Point startPoint = new Point();
        startPoint.set(startLocation[0], (int) (startLocation[1] - WindowUtil.getStatusBarHeight(this)));


        int[] endLocation = new int[2];
        mInterestView.getLocationOnScreen(endLocation);
        Point endPoint = new Point();
        endPoint.set(mInterestView.getContainerLeft(), (int) (endLocation[1] - WindowUtil.getStatusBarHeight(this) - DpAndPx.dip2px(this, 5.5f)));


        int pointX = WindowUtil.getScreenWidth(this) / 2;
        if (Math.abs(startLocation[0] - pointX) < DpAndPx.dip2px(this, 50)) {
            pointX = startLocation[0];
        }
        int pointY = (startPoint.y + endPoint.y) * 2 / 3;
        Point controllPoint = new Point(pointX, pointY);


        AnimatorSet set = new AnimatorSet();
        ValueAnimator scalAnim = ValueAnimator.ofFloat(DpAndPx.dip2px(this, 64), DpAndPx.dip2px(this, 34));
        scalAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float size = (Float) animation.getAnimatedValue();
                mAnimAvatar.setScaleX(size / DpAndPx.dip2px(InterestActivity.this, 64));
                mAnimAvatar.setScaleY(size / DpAndPx.dip2px(InterestActivity.this, 64));
                mAnimAvatar.invalidate();
            }
        });

        ValueAnimator animator = ValueAnimator.ofObject(new BezierEvaluator(controllPoint), startPoint, endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                mAnimAvatar.setX(point.x);
                mAnimAvatar.setY(point.y);
                mAnimAvatar.invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimAvatar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimAvatar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setDuration(300);
        set.play(scalAnim).with(animator);
        set.start();

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


}
