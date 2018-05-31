package com.example.zhangjian.rxjava2.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import com.example.zhangjian.rxjava2.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.util.ArrayList;
import java.util.List;
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

public class MainActivity2 extends Activity {
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "before");
//                test7().subscribe(new Consumer<List<Integer>>() {
//                    @Override
//                    public void accept(List<Integer> integers) throws Exception {
//                        for (int i = 0; i <integers.size() ; i++) {
//                            Log.d(TAG,""+integers.get(i));
//                        }
//                    }
//                });
//                Log.d(TAG, "after");
                test14();
            }
        }, 1000);

    }

    public void test5() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("方形-" + 1);
                e.onNext("方形-" + 2);
                e.onNext("方形-" + 3);
                e.onComplete();
            }
        });
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept-->" + s);
            }
        });

    }


    public void test4() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe--" + Thread.currentThread().getName());
                e.onNext(1);
                e.onComplete();
            }
        });
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept value=" + integer + "--" + Thread.currentThread().getName());
            }
        };
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                }).observeOn(Schedulers.computation())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(computation), current thread is : " + Thread.currentThread().getName());
                    }
                }).subscribe(consumer);

        //TODO 切断所有连接
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(null);
        compositeDisposable.clear();
    }

    public void test3() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe--" + Thread.currentThread().getName());
                Thread.sleep(3000);
                e.onNext(1);
                e.onComplete();
            }
        });
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept value=" + integer + "--" + Thread.currentThread().getName());
            }
        };

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.computation()).subscribe(consumer);
    }

    public void test2() {
        //下游不关心，不处理
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {

            }
        }).subscribe();
    }

    public void test1() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "Observable--" + Thread.currentThread().getName());
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe--" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext value=" + integer + "--" + Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError--" + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete--" + Thread.currentThread().getName());
            }
        };
        observable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private void test6() {
        Log.d("#######1", System.currentTimeMillis() + "");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.d("#######2", System.currentTimeMillis() + "");
                e.onNext("aaa");
                e.onComplete();
                Log.d("#######22", System.currentTimeMillis() + "");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d("#######4", System.currentTimeMillis() + "");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private Observable<List<Integer>> test7() {
        return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Integer>> e) throws Exception {

                List<Integer> list = new ArrayList<Integer>();
                Thread.sleep(1000);
                list.clear();
                list.add(1);
                e.onNext(list);

                Thread.sleep(1000);
                list.clear();
                list.add(2);
                e.onNext(list);

                Thread.sleep(1000);
                list.clear();
                list.add(3);
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    private void test8() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe");
                e.onNext(1);
                Log.d(TAG, "subscribe");
                e.onNext(2);
                Log.d(TAG, "subscribe");
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                Log.d(TAG, "apply");
                return "num=" + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept");
                Log.d(TAG, s);
            }
        });
    }

    private void test9() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe");
                e.onNext(1);
                Log.d(TAG, "subscribe");
                e.onNext(2);
                Log.d(TAG, "subscribe");
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                Log.d(TAG, "apply");
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(50, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept");
                Log.d(TAG, s);
            }
        });
    }

    private void test10() {
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
//               e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext(" hello");
                e.onNext(" world");
//                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {

            @Override
            public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                return integer + "##" + s;
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    private void test11() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                    Thread.sleep(1000);
                }
            }
        })
//                .sample(1,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(1000);
                Log.d(TAG, "value:" + integer);
            }
        });
    }

    private void test12() {//同步订阅=背压
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "Emitter 1");
                e.onNext(1);
                Log.d(TAG, "Emitter 2");
                e.onNext(2);
                Log.d(TAG, "Emitter 3");
                e.onNext(3);
                Log.d(TAG, "Emitter onComplete");
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
//                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "value=" + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, t);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    Subscription ss;
    private void test13() {//异步订阅=背压

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
//                Log.d(TAG, "Emitter 1");
                e.onNext(1);
//                Log.d(TAG, "Emitter 2");
//                e.onNext(2);
//                Log.d(TAG, "Emitter 3");
//                e.onNext(3);
//                Log.d(TAG, "Emitter onComplete");
//                e.onComplete();

//                for (int i = 0;i<110 ; i++) {
//                    Log.d(TAG, "Emitter " + i);
//                    e.onNext(i);
//                }

                Log.d(TAG,"requested:"+e.requested());
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                ss = s;
                Log.d(TAG, "onSubscribe");

                s.request(200);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "value=" + integer);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                ss.request(1);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, t);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    private void test14(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG,"Emitter 1" );
                e.onNext(1);
                Log.d(TAG,"Emitter 2" );
                Thread.sleep(100);
                e.onNext(2);
                Log.d(TAG,"Emitter 3" );
                Thread.sleep(100);
                e.onNext(3);
                Log.d(TAG,"subscribe "+Thread.currentThread().getName());
            }
        }).subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG,"accept "+Thread.currentThread().getName());
            }
        });
    }
}
