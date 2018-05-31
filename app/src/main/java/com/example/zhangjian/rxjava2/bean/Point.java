package com.example.zhangjian.rxjava2.bean;

import java.io.Serializable;

/**
 * Created by zhangjian on 2018/5/31.
 */

public class Point implements Serializable {
    public int x;
    public int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point src) {
        this.x = src.x;
        this.y = src.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}