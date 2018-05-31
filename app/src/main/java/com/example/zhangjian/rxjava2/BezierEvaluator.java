package com.example.zhangjian.rxjava2;

import android.animation.TypeEvaluator;

import com.example.zhangjian.rxjava2.bean.Point;

/**
 * Created by zhangjian on 2018/5/31.
 */

public class BezierEvaluator implements TypeEvaluator<Point> {
    private Point controllPoint;


    public BezierEvaluator(Point controllPoint) {
        this.controllPoint = controllPoint;
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
        return new Point(x, y);
    }

}
