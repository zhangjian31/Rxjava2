package com.example.zhangjian.rxjava2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {
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
