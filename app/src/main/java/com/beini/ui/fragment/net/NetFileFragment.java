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
import com.beini.net.help.ProgressDownloader;
import com.beini.net.response.ProgressResponseBody;
import com.beini.ui.fragment.net.model.NetModel;
import com.beini.utils.BLog;
import com.beini.utils.NetUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;


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

    @Event({R.id.btn_download_file, R.id.btn_download_file_break, R.id.btn_download_file_stop, R.id.btn_download_file_contiunte,R.id.btn_get_file_size})
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
                Toast.makeText(getActivity(), "下载暂停", Toast.LENGTH_SHORT).show();
                // 存储此时的totalBytes，即断点位置。
                breakPoints = totalBytes;
                break;
            case R.id.btn_download_file_contiunte:
//                downloader.download(breakPoints,url);
                downloader.download(breakPoints);
                break;
            case R.id.btn_get_file_size:
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "aa.mp3");
                BLog.d("        file.exists()="+file.exists());
                if (file.exists()) {
                    Toast.makeText(getActivity(), FileUtil.getSize(file)+"   --", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_upload_single:
                File file1 = new File("");
                NetUtil.getSingleton().uploadFileSingle(file1);
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
}
