package com.example.administrator.baseapp.ui.fragment.net.model;

import android.os.Environment;
import android.os.Looper;

import com.example.administrator.baseapp.db.io.FileUtil;
import com.example.administrator.baseapp.net.NetUtil;
import com.example.administrator.baseapp.ui.fragment.net.NetFileFragment;
import com.example.administrator.baseapp.utils.BLog;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by beini on 2017/3/30.
 */

public class NetModel {
    NetFileFragment netFileFragment;

    public NetModel(NetFileFragment netFileFragment) {
        this.netFileFragment = netFileFragment;
    }

    public void downLoadFile() {
        String url = "http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3";

        NetUtil.getSingleton().downloadFile(url).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Flowable.just(response).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
//                        response.body().contentLength()  文件总大小
                        FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/mm.mp3", response.body().bytes());
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void downLoadFileBreak() {
        String url = "http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3";

        NetUtil.getSingleton().downLoadFile("200", url).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Flowable.just(response).subscribeOn(Schedulers.io()).subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        ResponseBody responsBody = response.body();
                        BLog.d("      contentLength=="+responsBody.contentLength());
                        FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/aa.mp3", responsBody.bytes());
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
