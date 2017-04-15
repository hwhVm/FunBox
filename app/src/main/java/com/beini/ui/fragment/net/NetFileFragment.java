package com.beini.ui.fragment.net;

import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.db.cache.FileUtil;
import com.beini.net.NetUtil;
import com.beini.net.RxNetUtil;
import com.beini.net.help.ProgressDownloader;
import com.beini.net.request.PageRequest;
import com.beini.net.request.UserRequest;
import com.beini.net.response.BaseResponseJson;
import com.beini.net.response.ProgressResponseBody;
import com.beini.ui.fragment.net.model.NetModel;
import com.beini.utils.BLog;
import com.beini.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.annotation.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Create by beini 2017/3/30
 */
@ContentView(R.layout.fragment_net_file)
public class NetFileFragment extends BaseFragment implements ProgressResponseBody.ProgressListener {
    NetModel netModel;
    @ViewInject(R.id.progressBar)
    ProgressBar progressBar;
    private long breakPoints;
    //    private FileDownload downloader;
    private ProgressDownloader downloader;
    private File file;
    private long totalBytes;
    private long contentLength;
    String url = "http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3";

    @Override
    public void initView() {
        netModel = new NetModel(this);
    }

    @Event({R.id.btn_mybatis_xml, R.id.btn_request_body, R.id.btn_query_by_page, R.id.btn_insert_web_user, R.id.btn_upload_part, R.id.btn_upload_mutile, R.id.btn_download_file, R.id.btn_download_file_break, R.id.btn_download_file_stop, R.id.btn_download_file_contiunte, R.id.btn_get_file_size, R.id.btn_upload_single, R.id.btn_text_connect})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_download_file:
                netModel.downLoadFileBreak();
                break;
            case R.id.btn_download_file_break:
                // 新下载前清空断点信息
                breakPoints = 0L;
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "aa.mp3");
                if (file.exists()) {
                    file.delete();
                }
//                downloader = new FileDownload(file, this);
//                downloader.download(0L,url);
                downloader = new ProgressDownloader(url, file, this);
                downloader.download(0L);
                break;
            case R.id.btn_download_file_stop:
                downloader.pause();
                Toast.makeText(baseActivity, "下载暂停", Toast.LENGTH_SHORT).show();
                // 存储此时的totalBytes，即断点位置。
                breakPoints = totalBytes;
                break;
            case R.id.btn_download_file_contiunte:
//                downloader.download(breakPoints,url);
                downloader.download(breakPoints);
                break;
            case R.id.btn_get_file_size:
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "aa.mp3");
                BLog.d("        file.exists()=" + file.exists());
                if (file.exists()) {
                    Toast.makeText(baseActivity, FileUtil.getSize(file) + "   --", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_upload_single:
                String path = Environment.getExternalStorageDirectory() + File.separator + "aa.mp3";
                BLog.d("   path==" + path);
                File file1 = new File(path);
                NetUtil.getSingleton().uploadFileSingle(file1).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        BLog.d("        " + response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        BLog.d("   onFailure     " + t.getMessage());
                    }
                });
                break;
            case R.id.btn_text_connect:
                NetUtil.getSingleton().getMethod("queryAllM").enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        BLog.d("    getMethod    " + response.isSuccessful());
                        if (response.isSuccessful()) {
                            BLog.d("      " + response.body().toString());

                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        BLog.d("getMethod   onFailure     " + t.getMessage());
                    }
                });
                break;
            case R.id.btn_upload_mutile:
                NetUtil.getSingleton().uploadFileMultiPart(returnFileLists()).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        BLog.d("   upload  mutile   " + response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        BLog.d("   upload  mutile   " + t.getLocalizedMessage());
                    }
                });
                break;
            case R.id.btn_upload_part:
                NetUtil.getSingleton().uploadFilePart(returnFileLists()).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        BLog.d("  uploadFilePart  " + response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        BLog.d("  onFailure  " + t.getLocalizedMessage());
                    }
                });
                break;
            case R.id.btn_insert_web_user:
                UserRequest userRequest = new UserRequest();
                userRequest.setAge(22);
                userRequest.setName("beini");
//                NetUtil.getSingleton().insertUserRequest("insertUserM", userRequest).enqueue(new Callback() {
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                     BLog.d("     response.isSuccessful()="+response.isSuccessful());
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//                        BLog.d(" "+t.getLocalizedMessage());
//                    }
//                });
//                RxNetUtil.getSingleton().insertRxUserRequest(userRequest, new ResourceSubscriber<BaseResponseJson>() {
//
//                    @Override
//                    public void onNext(@NotNull BaseResponseJson baseResponseJson) {
//                        BLog.d("        onNext  " + (baseResponseJson == null));
//                        if (baseResponseJson != null) {
//                            BLog.d("        " + baseResponseJson.getReturnCode());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        BLog.d("        onError  " + t.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        BLog.d("        onComplete  ");
//                    }
//                }, AndroidSchedulers.mainThread());

                RxNetUtil.getSingleton().sendRequest("insertUserM", userRequest, new ResourceSubscriber<BaseResponseJson>() {
                    @Override
                    public void onNext(BaseResponseJson baseResponseJson) {
                        BLog.d("        onNext  ");
                    }

                    @Override
                    public void onError(Throwable t) {
                        BLog.d("        onError  " + t.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        BLog.d("        onComplete  ");
                    }
                }, AndroidSchedulers.mainThread());
                break;
            case R.id.btn_query_by_page:
                PageRequest pageRequest = new PageRequest();
                pageRequest.setStart(1);
                pageRequest.setNum(2);
                NetUtil.getSingleton().sendRequestPost("getUserByPage", pageRequest).enqueue(new Callback<BaseResponseJson>() {
                    @Override
                    public void onResponse(Call<BaseResponseJson> call, Response<BaseResponseJson> response) {

                    }

                    @Override
                    public void onFailure(Call<BaseResponseJson> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn_request_body:
                NetUtil.getSingleton().getMethod("test_request_body").enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        BLog.d("    test_request_body    " + response.isSuccessful());
                        if (response.isSuccessful()) {
                            BLog.d("     response.body().toString()= " + response.body().toString());
                            BLog.d("     response.body()= " + response.body());
                            BLog.d("     response.message()= " + response.message());
                            BLog.d("      response.code()= " + response.code());

                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        BLog.d("test_request_body   onFailure     " + t.getMessage());
                    }
                });
                break;
            case R.id.btn_mybatis_xml:
                NetUtil.getSingleton().getMethod("testConnect").enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        BLog.d("   queryAllLeader  " + response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        BLog.d("      queryAllLeader  " + t.getLocalizedMessage());
                    }
                });
                break;
        }

    }


    @Override
    public void onPreExecute(long contentLength) {
        // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;
            BLog.d("          文件总长度contentLength ==" + contentLength);
            progressBar.setMax((int) (contentLength / 1024));
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        progressBar.setProgress((int) (totalBytes + breakPoints) / 1024);
        if (done) {
            // 切换到主线程
            Observable
                    .empty()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe();
        }
    }

    public List<File> returnFileLists() {
        List<File> files = new ArrayList<>();
        File f1 = new File(Environment.getExternalStorageDirectory() + File.separator + "aa.mp3");
        files.add(f1);
        File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + "Bb.mp3");
        files.add(f2);
        File f3 = new File(Environment.getExternalStorageDirectory() + File.separator + "Jjj.apk");
        files.add(f3);
        return files;
    }
}
