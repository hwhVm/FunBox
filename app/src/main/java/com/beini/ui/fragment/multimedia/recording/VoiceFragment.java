package com.beini.ui.fragment.multimedia.recording;

import android.Manifest;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.constants.Constants;
import com.beini.ui.fragment.multimedia.bean.RecorderBean;
import com.beini.ui.view.decoration.RecycleDecoration;
import com.beini.util.BLog;
import com.beini.util.ToastUtils;

import org.reactivestreams.Subscription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Create by beini 2017/7/10
 */
@ContentView(R.layout.fragment_voice)
public class VoiceFragment extends BaseFragment {

    @ViewInject(R.id.recycler_recorder_list)
    RecyclerView recycler_recorder_list;
    @ViewInject(R.id.btn_recorder)
    Button btn_recorder;

    public MediaPlayer mediaPlayer;
    public File mAudioFile;   //录音所保存的文件
    public MediaRecorder mediaRecorder;
    public final String outputFile = Constants.URL_ALL_FILE + "/voice/";//录音文件保存位置
    public long startTime, endTime;  //录音开始时间与结束时间
    public List<RecorderBean> recorderBeans = new ArrayList<>();
    public RecorderAdapter recorderAdapter;
    //当前是否正在播放
    public volatile boolean isPlaying;
    //判断是否在录音
    public boolean isRecordering = false;

    @Override
    public void initView() {
        checkPermission(new CheckPermListener() {//申请权限
            @Override
            public void superPermission() {
                BLog.e("       superPermission         ");
            }
        }, 0x11, new String[]{Manifest.permission.RECORD_AUDIO});

        recycler_recorder_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recorderAdapter = new RecorderAdapter(new BaseBean<>(R.layout.item_recorder, recorderBeans));
        recycler_recorder_list.addItemDecoration(new RecycleDecoration(getActivity()));
        recycler_recorder_list.setAdapter(recorderAdapter);
        recorderAdapter.setItemClick(onItemClickListener);

        mediaPlayer = new MediaPlayer();

        btn_recorder.setOnTouchListener(onTouchListener);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN://按下操作
                    btn_recorder.setBackgroundColor(Color.BLACK);
                    Flowable.create(new FlowableOnSubscribe<String>() {
                        @Override
                        public void subscribe(FlowableEmitter<String> e) throws Exception {
                            isRecordering = true;
                            startRecorder(e);
                        }
                    }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(flowableSubscriber);
                    break;
                case MotionEvent.ACTION_CANCEL:     //松开操作
                case MotionEvent.ACTION_UP:
                    btn_recorder.setBackgroundColor(Color.WHITE);
                    flowableSubscriber.onNext("11");
                    break;
            }
            //对OnTouch事件做了处理，返回true
            return true;
        }
    };

    public void startRecorder(FlowableEmitter<String> e) {//调用stop方法之后必须重新配置
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);////从麦克风采集声音数据
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mediaRecorder.setAudioSamplingRate(44100);   //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); //设置声音数据编码格式,音频通用格式是AAC
        mediaRecorder.setAudioEncodingBitRate(96000);  //设置编码频率
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {

            }
        });
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mAudioFile = new File(outputFile + System.currentTimeMillis() + ".m4a");
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        //创建文件
        try {
            mAudioFile.createNewFile();
            //设置录音保存的文件
            mediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            //开始录音
            mediaRecorder.prepare();
            mediaRecorder.start();
            //记录开始录音时间
            startTime = System.currentTimeMillis();
        } catch (Exception e1) {
            e.onError(e1);
        }
    }

    FlowableSubscriber flowableSubscriber = new FlowableSubscriber<String>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(1);
        }

        @Override
        public void onNext(String s) {
            if (isRecordering) {
                isRecordering = false;
                stopRecorder();
            }
        }

        @Override
        public void onError(Throwable t) {
            isRecordering = false;
            showFailed();
        }

        @Override
        public void onComplete() {

        }
    };

    public void stopRecorder() {
        //停止录音
        mediaRecorder.stop();//必须在调用start后面
        mediaRecorder.release();//每次完成后释放,避免占用和消耗系统资源
        mediaRecorder = null;
        //记录停止时间
        endTime = System.currentTimeMillis();
        //录音时间处理，比如只有大于2秒的录音才算成功
        int time = (int) ((endTime - startTime) / 1000);
        if (time >= 3) {
            //录音成功,添加数据
            RecorderBean bean = new RecorderBean();
            bean.setFilePath(mAudioFile.getAbsolutePath());
            recorderBeans.add(bean);
            recorderAdapter.notifyDataSetChanged();
            ToastUtils.showShortToast("录制成功");
        } else {
            mAudioFile = null;
            showFailed();
        }

    }

    public void showFailed() {
        ToastUtils.showShortToast("录制失败");
    }

    public RecorderAdapter.OnItemClickListener onItemClickListener = new RecorderAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            String path = recorderBeans.get(position).getFilePath();
            File file = new File(path);
            if (file.exists() && !isPlaying) {
                isPlaying = true;
                startPlay(path);//使用MediaPlayer播放声音文件
            }
        }
    };

    public void startPlay(String path) {
        try {
            //设置播放音频数据文件
            mediaPlayer.setDataSource(path);
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }

    /**
     * 停止播放或播放失败处理
     */
    private void playEndOrFail(boolean isEnd) {
        isPlaying = false;
        if (isEnd) {
            ToastUtils.showShortToast("播放完成");
        } else {
            ToastUtils.showShortToast("播放失败");
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
