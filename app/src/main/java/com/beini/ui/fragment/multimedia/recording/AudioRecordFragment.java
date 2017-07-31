package com.beini.ui.fragment.multimedia.recording;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.beini.ui.fragment.multimedia.bean.RecorderBean;
import com.beini.util.BLog;
import com.beini.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.FlowableEmitter;


/**
 * Create by beini 2017/7/27
 * 基于字节流
 */
public class AudioRecordFragment extends VoiceFragment {

    private static final int BUFFER_SIZE = 2048;
    private byte[]  mBuffer = new byte[BUFFER_SIZE];

    private FileOutputStream mFileOutPutStream;
    //文件流录音API
    private AudioRecord mAudioRecord;

    @Override
    public void initView() {
        super.initView();


    }


    @Override
    public void startRecorder(FlowableEmitter<String> e) {
//        super.startRecorder(e);
        try {
            //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
            mAudioFile = new File(outputFile + System.currentTimeMillis() + ".pcm");
            //创建父文件夹
            mAudioFile.getParentFile().mkdirs();
            //创建文件
            mAudioFile.createNewFile();
            //创建文件输出流
            mFileOutPutStream = new FileOutputStream(mAudioFile);
            //配置AudioRecord
            //从麦克风采集数据
            int audioSource = MediaRecorder.AudioSource.MIC;
            //设置采样频率
            int sampleRate = 44100;
            //设置单声道输入
            int channelConfig = AudioFormat.CHANNEL_IN_MONO;
            //设置格式，安卓手机都支持的是PCM16
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            //计算AudioRecord内部buffer大小
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
            //根据上面的设置参数初始化AudioRecord
            mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, Math.max(minBufferSize, BUFFER_SIZE));
            //开始录音
            mAudioRecord.startRecording();
            //记录开始时间
            startTime = System.currentTimeMillis();
            //写入数据到文件
            while (isRecordering) {
                int read = mAudioRecord.read(mBuffer, 0, BUFFER_SIZE);
                BLog.e("              "+(mAudioRecord==null)+"      "+(mBuffer==null));
                if (read > 0) {
                    //保存到指定文件
                    mFileOutPutStream.write(mBuffer, 0, read);
                }
            }
        } catch (IOException ioException) {
            e.onError(ioException);
        }
    }

    @Override
    public void stopRecorder() {
//        super.stopRecorder();
        try {
            //停止录音
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            mFileOutPutStream.close();
            //记录时长
            endTime = System.currentTimeMillis();
            //录音时间处理，比如只有大于2秒的录音才算成功
            int time = (int) ((endTime - startTime) / 1000);
            if (time >= 3) {
                //录音成功,添加数据
                RecorderBean bean = new RecorderBean();
                bean.setFilePath(mAudioFile.getAbsolutePath());
                recorderBeans.add(bean);
                recorderAdapter.notifyDataSetChanged();
                //录音成功,发Message
                ToastUtils.showShortToast("录制成功");
            } else {
                mAudioFile = null;
                showFailed();
            }
        } catch (Exception e) {
            showFailed();
        }
    }
}
