package com.example.zhangjian.rxjava2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {
    private static String TAG = MainActivity.class.getSimpleName();
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(3);
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                test2();
            }
        }, 1000);

    }

    public void test0() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Disposable disposable = observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "apply:" + s);
            }
        });
        compositeDisposable.add(disposable);
    }


    public void test1() {
        String str = "ABC";
        Observable.just(str).subscribeOn(Schedulers.from(mThreadPool)).map(new Function<String, Integer>() {
            @Override
            public Integer apply(@NonNull String s) throws Exception {
                Log.d(TAG, "apply:" + Thread.currentThread().getName());
                return s.length();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept:" + Thread.currentThread().getName());
                Log.d(TAG, "String" + integer);
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

    public void test2() {
        Observable<Bitmap> observable = Observable.just("").map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(@NonNull String s) throws Exception {
                return null;
            }
        });
        observable.subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {

            }
        });

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {

            }

        }, BackpressureStrategy.BUFFER).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });


        String[] arr = {"A", "B"};

        Observable.fromArray(arr).flatMap(new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(@NonNull String s) throws Exception {
                return Observable.fromArray("flatMap->" + s);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("@@@@", "result=" + s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        compositeDisposable.dispose();
    }
}
