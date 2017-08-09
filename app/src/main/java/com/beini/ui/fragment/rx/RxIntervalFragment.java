package com.beini.ui.fragment.rx;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.util.BLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Create by beini 2017/8/9
 * CompositeDisposable
 */
@ContentView(R.layout.fragment_rx_interval)
public class RxIntervalFragment extends BaseFragment {
    private Subscriber subscriber;
    private CompositeDisposable composite2;
    @Override
    public void initView() {
       composite2 = new CompositeDisposable();

    }

    @Event({R.id.text_rx_interval, R.id.btn_rx_stop})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.text_rx_interval:
                startInterval();
                break;
            case R.id.btn_rx_stop:
                BLog.e("  ------------------------>" + (disposable != null) + "     " + disposable.isDisposed());
//                if (disposable != null && !disposable.isDisposed()) {
//                    disposable.dispose();
//                }
                composite2.clear();
                break;
        }
    }

    Disposable disposable;

    private void startInterval() {
        final int time = 5;
        disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        BLog.e("        doOnSubscribe  ");
                    }
                })
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        BLog.e("           aLong.intValue()=  " + aLong.intValue() + "   time=" + time);
                        return time - aLong.intValue();
                    }
                }).take(time + 1)//执行次数
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        BLog.e(" ------->integer=" + integer + "   time=" + time);
                    }
                });
        composite2.add(disposable);
    }
}
