package com.example.zhangjian.rxjava2.activity;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhangjian.rxjava2.R;


/**
 * Created by zhangjian on 2018/6/11.
 */

public class Main3Activity extends Activity {
    private TextView mTvPrise;
    private ImageView mIvPrise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mTvPrise = (TextView) findViewById(R.id.tv_item_video_prise);
        mIvPrise = (ImageView) findViewById(R.id.iv_item_video_prise);
        mTvPrise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale();
            }
        });
        mIvPrise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale();
            }
        });
    }

    private void scale() {
        ScaleAnimation animation1 = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(100);
        animation1.setFillAfter(true);
        ScaleAnimation animation2 = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(100);
        animation2.setFillAfter(true);
        animation2.setStartOffset(100);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        mIvPrise.startAnimation(set);
//        ViewPropertyAnimator viewPropertyAnimator=mIvPrise.animate();
//        viewPropertyAnimator.scaleXBy(1.0f).scaleX(1.2f).scaleYBy(1.0f).scaleY(1.3f).setDuration(500).start();
    }
}
