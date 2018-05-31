package com.example.zhangjian.rxjava2.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhangjian.rxjava2.BezierEvaluator;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main3Activity extends Activity implements View.OnClickListener, InterestAdapter.OnItemClickListener {

    private Button btnAdd, btnRemove;
    private RecyclerView mRecycleview;
    private InterestAdapter mInterestAdapter;

    private ImageView mIvSkip;
    private TextView mTvSkip;

    private Map<Integer, InterestBean> selectMap = new HashMap<>();
    private int curPage = 0;
    private int total = 3;
    private List<InterestBean> list;
    private InterestView interestView;
    private SimpleDraweeView mAnimAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mRecycleview = (RecyclerView) findViewById(R.id.recycleview);
        mAnimAvatar = (SimpleDraweeView) findViewById(R.id.avatar_anim);

        mIvSkip = (ImageView) findViewById(R.id.iv_skip);
        mTvSkip = (TextView) findViewById(R.id.tv_skip);

        interestView = (InterestView) findViewById(R.id.interestView);

        mIvSkip.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnRemove = (Button) findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(this);

        mRecycleview.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));

        mInterestAdapter = new InterestAdapter();
        mInterestAdapter.setOnItemClickListener(this);
        mRecycleview.setAdapter(mInterestAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initSource();
//                initSlectMap();
//                initTopList();
                inData();
            }
        }, 500);

    }

    private void initTopList() {
        Set<Map.Entry<Integer, InterestBean>> set = selectMap.entrySet();
        Iterator<Map.Entry<Integer, InterestBean>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, InterestBean> entry = iterator.next();
            InterestBean bean = entry.getValue();
            interestView.addItem(bean);
        }
    }

    private void initSlectMap() {

        selectMap.put(list.get(0).getId(), list.get(0));
        selectMap.put(list.get(3).getId(), list.get(3));
        selectMap.put(list.get(12).getId(), list.get(12));
    }


    private void initSource() {
        list = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            InterestBean bean = new InterestBean();
            bean.setId(i);
            bean.setTitle("title-" + i);
            bean.setUrl(arr[i]);
            list.add(bean);
        }

    }

    private void inData() {
        List<InterestBean> temp = new ArrayList<>();
        curPage = curPage % total;
        for (int i = curPage * 9; i < Math.min((curPage + 1) * 9, list.size()); i++) {
            temp.add(list.get(i));
        }
        curPage++;
        mInterestAdapter.setData(temp, selectMap);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
//            addItem();
        } else if (v.getId() == R.id.btn_remove) {
//            removeItem();
        } else if (v.getId() == R.id.iv_skip || v.getId() == R.id.tv_skip) {
            inData();
        }
    }

    @Override
    public void onItemClick(final View itemView, int position) {
        final InterestBean bean = mInterestAdapter.getData().get(position);
        if (selectMap.containsKey(bean.getId())) {
            showRemoveAnim(itemView, bean);
            interestView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectMap.remove(bean.getId());
                    interestView.removeItem(bean);
                    mInterestAdapter.setNeedShowAnimal(false);
                    mInterestAdapter.notifyDataSetChanged();
                }
            }, 500);

        } else {
            selectMap.put(bean.getId(), bean);
            View view = interestView.addItem(bean);
            showAddAnim(itemView, bean, view);
            mInterestAdapter.setNeedShowAnimal(false);
            mInterestAdapter.notifyDataSetChanged();
        }

        mInterestAdapter.setNeedShowAnimal(false);
        mInterestAdapter.notifyDataSetChanged();
    }

    private void showRemoveAnim(View itemView, InterestBean bean) {
        show(mAnimAvatar, bean.getUrl());

        int[] startLocation = new int[2];
        interestView.getLocationOnScreen(startLocation);
        Point startPoint = new Point();
        startPoint.set(interestView.getContainerLeft(), (int) (startLocation[1] - WindowUtil.getStatusBarHeight(this) - DpAndPx.dip2px(this, 5.5f)));


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
                mAnimAvatar.setScaleX(size / DpAndPx.dip2px(Main3Activity.this, 64));
                mAnimAvatar.setScaleY(size / DpAndPx.dip2px(Main3Activity.this, 64));
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


    private void showAddAnim(View itemView, InterestBean bean, final View desView) {
        show(mAnimAvatar, bean.getUrl());

        int[] startLocation = new int[2];
        itemView.findViewById(R.id.avatar).getLocationOnScreen(startLocation);
        Point startPoint = new Point();
        startPoint.set(startLocation[0], (int) (startLocation[1] - WindowUtil.getStatusBarHeight(this)));


        int[] endLocation = new int[2];
        interestView.getLocationOnScreen(endLocation);
        Point endPoint = new Point();
        endPoint.set(interestView.getContainerLeft(), (int) (endLocation[1] - WindowUtil.getStatusBarHeight(this) - DpAndPx.dip2px(this, 5.5f)));


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
                mAnimAvatar.setScaleX(size / DpAndPx.dip2px(Main3Activity.this, 64));
                mAnimAvatar.setScaleY(size / DpAndPx.dip2px(Main3Activity.this, 64));
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
                desView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimAvatar.setVisibility(View.GONE);
                desView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                desView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setDuration(500);
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


    String[] arr = {
            "http://img0.imgtn.bdimg.com/it/u=1717056030,451974468&fm=200&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4085174256,2543265040&fm=200&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=845061817,3482904951&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2913061120,181996338&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2534733008,3328446862&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3024515029,2082669158&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2869031647,3499077470&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2988065565,1735810570&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1780538556,1183961030&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2188003138,392765800&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3099062169,2136497672&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1085267197,1471777511&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=1769067601,404951922&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3363129096,733650575&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=737470215,726833067&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3120202809,152452100&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1345300409,2233606009&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=291415079,761330040&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3651324592,2597153327&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3711505074,3910698905&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=704139227,1008355068&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=162602129,2576598321&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2329734498,1899200243&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=467113224,2345770724&fm=200&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3287998025,3231581159&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3788682560,2600075021&fm=27&gp=0.jpg",
    };

}
