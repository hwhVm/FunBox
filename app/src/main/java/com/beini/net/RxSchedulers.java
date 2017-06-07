package com.beini.net;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by beini on 2017/6/7.
 */

public class RxSchedulers {
    /**
     * IO线程进行请求，在主线程进行回调
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
//                                if (!Utils.isNetworkConnected()) {
//                                    Toast.makeText(context, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
//                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }



}
