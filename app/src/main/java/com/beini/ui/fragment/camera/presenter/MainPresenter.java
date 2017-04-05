package com.beini.ui.fragment.camera.presenter;


import com.beini.ui.fragment.camera.utils.Util;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by beini on 2016/7/14.
 */
public class MainPresenter {


    public Observable<Boolean> _picToSd(byte[] bytes) {

        return Observable.just(bytes)
                .map(new Function<byte[], Boolean>() {
                    @Override
                    public Boolean apply(byte[] bytes) throws Exception {
                        try {
                            Util.saveToSDCard(bytes);// 保存图片到sd卡中
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                }).subscribeOn(Schedulers.io());
    }

}
