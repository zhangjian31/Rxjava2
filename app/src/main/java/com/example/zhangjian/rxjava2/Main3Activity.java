package com.example.zhangjian.rxjava2;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private TextView tvTitle;
    private LinearLayout container;
    private RelativeLayout title_layout;
    private Button btnAdd, btnRemove;
    private HorizontalScrollView scrollview;
    private int initTitleX;
    private RecyclerView mRecycleview;
    private InterestAdapter mInterestAdapter;

    private ImageView mIvSkip;
    private TextView mTvSkip;

    private Map<Integer, InterestBean> selectMap = new HashMap<>();
    private int curPage = 0;
    private int total = 3;
    private List<InterestBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);
        container = (LinearLayout) findViewById(R.id.contain_layout);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
        mRecycleview = (RecyclerView) findViewById(R.id.recycleview);

        mIvSkip = (ImageView) findViewById(R.id.iv_skip);
        mTvSkip = (TextView) findViewById(R.id.tv_skip);

        mIvSkip.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnRemove = (Button) findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(this);

        mRecycleview.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
//        mRecycleview.addItemDecoration(new GridSpacingItemDecoration(3, 19, true));

        mInterestAdapter = new InterestAdapter();
        mInterestAdapter.setOnItemClickListener(this);
        mRecycleview.setAdapter(mInterestAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initSource();
                initSlectMap();
                initTopList();
                inData();
            }


        }, 3000);

    }

    private void initTopList() {
        Set<Map.Entry<Integer, InterestBean>> set = selectMap.entrySet();
        Iterator<Map.Entry<Integer, InterestBean>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, InterestBean> entry = iterator.next();
            InterestBean bean = entry.getValue();
            addItem(bean);
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] location = new int[2];
        tvTitle.getLocationOnScreen(location);
        initTitleX = location[0];
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

    private void removeItem(InterestBean bean) {
        if (container.getVisibility() == View.VISIBLE) {
            if (container.getChildCount() > 0) {
                container.removeViewAt(container.getChildCount() - 1);
            }
            if (container.getChildCount() == 0) {
                container.setVisibility(View.GONE);
                ValueAnimator valueAnimator = ValueAnimator.ofInt(dip2px(Main3Activity.this, 20), initTitleX);
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

    private void addItem(InterestBean bean) {
        if (container.getVisibility() != View.VISIBLE) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(initTitleX, dip2px(Main3Activity.this, 20));
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
        View root = LayoutInflater.from(this).inflate(R.layout.item_top, null);
        SimpleDraweeView imageView = (SimpleDraweeView) root.findViewById(R.id.avatar);
        show(imageView, bean.getUrl());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(this, 50), dip2px(this, 50));
//        params.leftMargin = dip2px(this, 10);
//        params.rightMargin = dip2px(this, 10);
        container.addView(root,0);
    }


    public int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        InterestBean bean = mInterestAdapter.getData().get(position);
        if (selectMap.containsKey(bean.getId())) {
            selectMap.remove(bean.getId());
            removeItem(bean);
        } else {
            selectMap.put(bean.getId(), bean);
            addItem(bean);
        }
        mInterestAdapter.setNeedShowAnimal(false);
        mInterestAdapter.notifyDataSetChanged();
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
