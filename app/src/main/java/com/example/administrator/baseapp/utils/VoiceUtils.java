package com.example.administrator.baseapp.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import com.example.administrator.baseapp.base.BaseApplication;
import java.util.ArrayList;


/**
 *
 */
public class VoiceUtils {
	private ArrayList<byte[]> list = new ArrayList<byte[]>();
	public boolean isRecording = false;//是否录放的标记
	private boolean isPlayMusicStatus;
	public float oneVariations;
	public float twoVariations;
	public float threeVariations;
	private AudioManager am;
	private static final int frequency = 44100; //11025;
	private static final int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int recBufSize, playBufSize;
	private AudioRecord audioRecord;
	private AudioTrack audioTrack;


	public VoiceUtils() {
		am = (AudioManager) BaseApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * 初始化
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void Init() {

		recBufSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding) * 2;

		playBufSize = AudioTrack.getMinBufferSize(frequency, AudioFormat.CHANNEL_OUT_MONO, audioEncoding) * 2;//AudioFormat.CHANNEL_OUT_MONO

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, recBufSize);

		if (AcousticEchoCanceler.isAvailable()) {
			AcousticEchoCanceler canceler = AcousticEchoCanceler.create(audioRecord.getAudioSessionId());
			if (canceler!=null)
			canceler.setEnabled(true);
		}
		if (NoiseSuppressor.isAvailable()) {
			NoiseSuppressor noi = NoiseSuppressor.create(audioRecord.getAudioSessionId());
			if (noi!=null)
				noi.setEnabled(true);
		}

		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_OUT_MONO, audioEncoding, playBufSize, AudioTrack.MODE_STREAM, audioRecord.getAudioSessionId());

//版本大于4.3
		if (Build.VERSION.SDK_INT >= 0x12)
			audioTrack.setVolume(0.9f);//设置当前音量大小
		else
			audioTrack.setStereoVolume(0.9f, 0.9f);


	}

	byte[] tmpBuf = null;

	public void Start() {
		isRecording = true;
		isEnd = false;
		int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//			 & BaseApplication.getInstance().getPlayMusicStatus()
			if (ServiceUtils.isServiceWork(BaseApplication.getInstance(), BaseApplication.getInstance().getPackageName() + ".musicplayer.MusicService")) {
//				Intent pause = new Intent(MusicService.ACTION_PAUSE);
//				pause.setPackage(BaseApplication.getInstance().getPackageName());
//				BaseApplication.getInstance().startService(pause);
//				isPlayMusicStatus = true;
			}

		}
		BaseApplication.getInstance().getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				if (audioRecord == null | audioTrack == null) {
					Init();
				}
				if (audioRecord!=null&audioTrack!=null){
					try {
						byte[] buffer = new byte[recBufSize];
						audioRecord.startRecording();//开始录制
						audioTrack.play();//开始播放
						while (isRecording) {
							//从MIC保存数据到缓冲区
							int bufferReadResult = audioRecord.read(buffer, 0, recBufSize);
							tmpBuf = new byte[bufferReadResult];
							System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
							//byte[] data = GlobalApplication.getInstance().getNdk().variations(tmpBuf, oneVariations, twoVariations, threeVariations);//变音
							list.add(tmpBuf);
						}
					} catch (Throwable t) {
//
//							Looper.prepare();
//							ToastUtils.showShortToast("！！！！！！！！！");
//							Looper.loop();
					}
				}
			}
		});
	}

	private boolean isEnd = true;

	public void End() {
		if (isEnd|audioRecord==null|audioTrack==null)
			return;
		isRecording = false;
		isEnd = true;
		am.abandonAudioFocus(null);
		audioTrack.stop();
		audioTrack.release();
		audioTrack = null;
		audioRecord.stop();
		audioRecord.release();
		audioRecord = null;

		if (ServiceUtils.isServiceWork(BaseApplication.getInstance(), BaseApplication.getInstance().getPackageName() + ".musicplayer.MusicService") & isPlayMusicStatus) {
//			Intent pause = new Intent(MusicService.ACTION_PLAY);
//			pause.setPackage(BaseApplication.getInstance().getPackageName());
//			BaseApplication.getInstance().startService(pause);
		}
		if (list.size() != 0) {
			list.clear();
		}
//		SPPService.getInstance().write(CmdManager.setPlayVoice((byte) 2));
	}

	public boolean isStartTouch() {
		return isEnd;
	}

	public void write() {
		BaseApplication.getInstance().getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				if (list.size() == 0) {
					return;
				}
				isRecording = false;
				int byteSize = 0, start = 0, end;
				for (int s = 0; s < list.size(); s++) {

					byteSize += list.get(s).length;
				}

				byte[] bytes = new byte[byteSize + list.get(0).length];
				for (int i = 0; i < list.size(); i++) {
					end = list.get(i).length;
					System.arraycopy(list.get(i), 0, bytes, start, end);
					start += list.get(i).length;
				}

//				byte[] data = BaseApplication.getInstance().getNdk().variations(bytes, oneVariations, twoVariations, threeVariations);//变音
//
//				if (data != null) {
//					BLog.d("播放成功");
//					audioTrack.write(data, 0, data.length);    //写入数据即播放
//				}
				End();
			}
		});

	}

	public void setVariations(float one, float two, float three) {
		oneVariations = one;
		twoVariations = two;
		threeVariations = three;
	}


}
