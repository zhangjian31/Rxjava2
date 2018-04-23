package com.example.zhangjian.rxjava2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                test1();
            }
        }, 1000);

    }

    public void test1() {
        String str ="ABC";
        Observable.just(str).subscribeOn(Schedulers.from(mThreadPool)).map(new Function<String, Integer>() {
            @Override
            public Integer apply(@NonNull String s) throws Exception {
                Log.d(TAG,"apply:"+Thread.currentThread().getName());
                return s.length();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG,"accept:"+Thread.currentThread().getName());
                Log.d(TAG,"String"+integer);
            }
        });

//        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                e.onNext("方形-" + 1);
//                e.onNext("方形-" + 2);
//                e.onNext("方形-" + 3);
//                e.onComplete();
//            }
//        });
//        observable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, "accept-->" + s);
//            }
//        });

    }

}
