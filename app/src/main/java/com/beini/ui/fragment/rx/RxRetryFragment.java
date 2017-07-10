package com.beini.ui.fragment.rx;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.util.BLog;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Create by beini 2017/6/8
 */
@ContentView(R.layout.fragment_rx_retry)
public class RxRetryFragment extends BaseFragment {

    @Override
    public void initView() {

    }


    @Event({R.id.btn_retry, R.id.btn_repeat, R.id.btn_retrywhen})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_retry:
                rxRetry();
                break;
            case R.id.btn_repeat:
                rxRepeat();
                break;
            case R.id.btn_retrywhen:
                rxRetryWhen();
                break;
            case R.id.btn_repeat_when:

                break;
        }
    }

    /**
     * Function像个工厂类，用来实现你自己的重试逻辑。
     * 输入的是一个Observable<Throwable>。
     * 输出的是一个Observable<?>。
     */
    public void rxRetryWhen() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                BLog.d("  subscribe");
                e.onNext(true);
                e.onComplete();
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {//实现重试逻辑
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                BLog.d("  retryWhen");
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        BLog.d("  apply");
                        if (throwable instanceof IOException) {
                            return Observable.just(null);
                        }
                        return Observable.error(throwable);
                    }
                });
            }
        }).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        BLog.d("    onSubscribe   ");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        BLog.d("    onNext   aBoolean=" + aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        BLog.d("    onError   e=" + e);
                    }

                    @Override
                    public void onComplete() {
                        BLog.d("    onComplete   ");
                    }
                });


    }

    public void rxRepeat() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                BLog.d("  subscribe");
                e.onNext(true);
                e.onComplete();
            }
        }).retry(2).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                BLog.d("    onSubscribe   ");
            }

            @Override
            public void onNext(Boolean aBoolean) {
                BLog.d("    onNext   aBoolean=" + aBoolean);
            }

            @Override
            public void onError(Throwable e) {
                BLog.d("    onError   e=" + e);
            }

            @Override
            public void onComplete() {
                BLog.d("    onComplete   ");
            }
        });
    }

    public void rxRetry() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                BLog.d("  subscribe");
                e.onNext(true);
                e.onComplete();
            }
        }).retry(2).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                BLog.d("    onSubscribe   ");
            }

            @Override
            public void onNext(Boolean aBoolean) {
                BLog.d("    onNext   aBoolean=" + aBoolean);
            }

            @Override
            public void onError(Throwable e) {
                BLog.d("    onError   e=" + e);
            }

            @Override
            public void onComplete() {
                BLog.d("    onComplete   ");
            }
        });
    }
}
