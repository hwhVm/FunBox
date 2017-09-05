package com.beini.ui.fragment.rx;


import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by beini 2017/8/11
 */
@ContentView(R.layout.fragment_rx_all)
public class RxAllFragment extends BaseFragment {
    @ViewInject(R.id.text_show_success)
    TextView text_show_success;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @ViewInject(R.id.btn_stop_all)
    Button btn_stop_all;
    @ViewInject(R.id.btn_rx_filter)
    Button btn_rx_filter;

    @Override
    public void initView() {

    }

    private int i = 0;

    @Event({R.id.btn_rx_filter, R.id.btn_stop_all})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_rx_filter:
                mapThread();
                break;
            case R.id.btn_stop_all:

                break;
        }
    }

    /**
     * 线程切换问题
     * 1 默认情况： apply 都在主线程执行
     * 2 设置切换线程后，改变的是下游的线程
     * 3
     */
    public void mapThread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                BLog.e("  1  Thread=     " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                e.onNext(11);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                BLog.e("  2  Thread=     " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                BLog.e("  integer="+integer);
                return 22;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        BLog.e("     integer="+integer);
                        BLog.e("  3  Thread=     " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                    }
                });
//        Observable.just(1)
////                .observeOn(Schedulers.io())
//                .map(new Function<Integer, Integer>() {
//                    @Override
//                    public Integer apply(Integer integer) throws Exception {
//                        BLog.e("  1  Thread=     " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
//                        Thread.sleep(3000);
//                        return 2;
//                    }
//                })
//                .map(new Function<Integer, Integer>() {
//                    @Override
//                    public Integer apply(Integer integer) throws Exception {
//                        Thread.sleep(2000);
//                        BLog.e("  2  Thread=     " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
//                        return 3;
//                    }
//                })
////                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        BLog.e("  3  Thread=     " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
//                        BLog.e("          integer=       " + integer);
//                    }
//                });

    }


    private Observable createDelayObservable(int index) {
        return Observable.just(1 * index, 2 * index, 3 * index).delay(index, TimeUnit.SECONDS);
    }

    private void isNewThread() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> e) throws Exception {
                e.onNext(true);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
            }
        });
    }

    private void temp() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> e) throws Exception {

            }
        }, BackpressureStrategy.BUFFER).subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 背压
     */
    private void backPressure() {
        /**
         * Observable ：
         *不支持 backpressure 处理，不会发生 MissingBackpressureException 异常。
         *所有没有处理的数据都缓存在内存中，等待被订阅者处理。
         *坏处是：当产生的数据过快，内存中缓存的数据越来越多，占用大量内存。
         */
//        Observable.interval(1, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        Thread.sleep(1000);
//                        BLog.e("    aLong=" + aLong);
//                    }
//                });
        /**
         * onBackpressureDrop:128丢弃后面的数据
         * onBackpressureLatest 就是只保留最新的事件
         * onBackpressureBuffer
         *onBackpressureBuffer：默认情况下缓存所有的数据，不会丢弃数据，这个方法可以解决背压问题，但是它有像 Observable 一样的缺点，缓存数据太多，占用太多内存。
         *onBackpressureBuffer(int capacity) ：设置缓存队列大小，但是如果缓冲数据超过了设置的值，就会报错，发生崩溃。
         *
         */
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop() //onBackpressureDrop 一定要放在 interval 后面否则不会生效
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Thread.sleep(1000);
                        BLog.e("    aLong=" + aLong);
                    }
                });
    }


    /**
     * toList : 把数据转换成 List 集合
     */
    public void rxToList() {
        Observable
                .just(1, 2, 3, 4)
                .toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        BLog.e("  integers=" + integers);
                    }
                });

//        把数组转化成 List 集合
        Integer[] items = {0, 1, 2, 3, 4, 5};
        Observable
                .fromArray(items)  //遍历数组
                .toList()  //把遍历后的数组转化成 List
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {

                    }
                });
    }


    /**
     * 遍历数组,集合
     */
    private void rxFromArray() {
        Integer ints[] = {1, 2, 3, 4};
        Flowable.fromArray(ints).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                BLog.e("  integer==" + integer);
            }
        });
        List<String> lists = new ArrayList<>();
        lists.add("a");
        lists.add("b");
        lists.add("c");

        Observable
                .fromIterable(lists)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        BLog.e("  s==" + s);
                    }
                });
    }

    /**
     * range ：发射特定的整数序列
     */
    private void rxRange() {
        Flowable.range(1, 10).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                BLog.e("        integer=" + integer);
            }
        });
        //
        Flowable.rangeLong(1, 10).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                BLog.e("        aLong=" + aLong);
            }
        });
    }

    /**
     * repeat 操作符：重复的发射数据
     */
    private void rxRepeat() {
        Flowable.just(1).repeat(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                BLog.e("   integer=  " + integer);
            }
        });
    }

    /**
     * 倒计时  interval
     */
    private void rxInterval() {
//        Disposable dd = Flowable.interval(1, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(@NonNull Long aLong) throws Exception {
//                        BLog.e("         along=" + aLong);
//                    }
//                });
//        dd.dispose();

        final long time = 5;
        Observable.interval(1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return time - aLong;
                    }
                }).take(time + 1)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        BLog.e("            aLong=" + aLong);
                    }
                });
    }

    /**
     * zip 操作符
     * 1) zip 组合事件的过程就是分别从发射器A和发射器B各取出一个事件来组合，并且一个事件只能被使用一次，组合的顺序是严格按照事件发送的顺序来进行的，所以上面截图中，可以看到，1永远是和A 结合的，2永远是和B结合的。
     * <p>
     * 2) 最终接收器收到的事件数量是和发送器发送事件最少的那个发送器的发送事件数目相同，所以如截图中，5很孤单，没有人愿意和它交往，孤独终老的单身狗。
     */
    private void rxZip() {
        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {

                return s + integer;
            }
        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        BLog.e("        s=   " + s);
                    }
                });

    }

    //创建 String 发射器
    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                BLog.e("                --->" + Thread.currentThread().getName());
                e.onNext("A");
//                e.onNext("B");
//                e.onNext("C");
            }
        }).subscribeOn(Schedulers.io());
    }

    //创建 String 发射器
    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                BLog.e("                --->" + Thread.currentThread().getName());
                e.onNext(1);
//                e.onNext(2);
//                e.onNext(3);
//                e.onNext(4);
//                e.onNext(5);
            }
        }).subscribeOn(Schedulers.io());
    }

    private void onErrorTest() {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                e.onNext(true);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                i++;
                btn_rx_filter.setText("   " + i);
                BLog.e("          -------->" + i);
            }
        });

    }

    /**
     * 把一个发射器Observable 通过某种方法转换为多个Observables，然后再把这些分散的Observables装进一个单一的发射器Observable
     * concatMap:保证顺序
     */
    private void flatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                BLog.e("                --->" + Thread.currentThread().getName());
                BLog.e("        111111111");
                e.onNext(1);
                BLog.e("        2222222222");
                e.onNext(2);
                BLog.e("        3333");

            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add(integer + "   " + i);
                }
                return Observable.fromIterable(list);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        BLog.e("accept:  " + s);
                    }
                });
    }

    private void rxMap() {
        Flowable.just(1, 2, 2)
                .map(new Function<Integer, Object>() {
                    @Override
                    public Object apply(Integer integer) throws Exception {
                        return integer + 1;
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

    private void rxFilter() {
        Flowable.just(33).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                BLog.e(" ----------->test");
                return integer > 10;
            }
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer < 5;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                BLog.e(" ------------>integer==" + integer);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BLog.e(" RxAllFragment  onDestroy()");
    }
}
