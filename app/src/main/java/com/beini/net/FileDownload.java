package com.beini.net;


import com.beini.constants.NetConstants;
import com.beini.net.response.ProgressResponseBody;
import com.beini.net.server.ApiServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//带进度监听功能的辅助类
public class FileDownload {

    public static final String TAG = "ProgressDownloader";

    private ProgressResponseBody.ProgressListener progressListener;
    private File destination;

    retrofit2.Call call;
    private static Retrofit retrofit;
    private static ApiServer apiServer;


    public FileDownload(File destination, final ProgressResponseBody.ProgressListener progressListener) {
        this.destination = destination;
        this.progressListener = progressListener;

        OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                .Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetConstants.baseUrl)
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }


    // startsPoint指定开始下载的点
    public void download(final long startsPoint, String mUrl) {
        String string = "bytes=" + startsPoint + "-";
        call = apiServer.downloadBreakpoint(string, mUrl);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {

            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Flowable.just(response).subscribeOn(Schedulers.io()).subscribe(new Consumer<retrofit2.Response<ResponseBody>>() {
                    @Override
                    public void accept(retrofit2.Response<ResponseBody> response) throws Exception {
                        save(response, startsPoint);
                    }
                });
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void pause() {
        if (call != null) {
            call.cancel();
        }
    }

    private void save(retrofit2.Response<ResponseBody> response, long startsPoint) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
// 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(destination, "rwd");
//Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
// 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。

            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}