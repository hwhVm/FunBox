package com.example.administrator.baseapp.ui.fragment.localmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

	private final IBinder musicBinder = new MusicBinder();
	private List<File> musicList;
	private MediaPlayer player;
	private int curPage;
	public static final String MFILTER = "broadcast.intent.action.text";
	public static final String NAME = "name";
	public static final String TOTALTIME = "totaltime";
	public static final String CURTIME = "curtime";

	@Override
	public void onCreate() {
		super.onCreate();
		musicList = new ArrayList<>();
		File rootDir = Environment.getExternalStorageDirectory();
		fillMusicList(rootDir);
		player = new MediaPlayer();
		startPlay();
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.d("com.beini", "setOnCompletionListener");
				player.reset();
				curPage = curPage == musicList.size() - 1 ? (curPage + 1) % musicList.size() : curPage + 1;
				startPlay();
			}
		});
	}

	public MusicService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return musicBinder;
	}


	public class MusicBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}

		public MediaPlayer getPlayer() {
			return player;
		}

	}

	/*迭代获取 音乐 文件*/
	private void fillMusicList(File dir) {
		File[] sourceFiles = dir.listFiles();
		Log.d("长度", String.valueOf(sourceFiles.length));
		for (File file : sourceFiles) {
			if (file.isDirectory()) {
				Log.d("文件夹名称", String.valueOf(file.getName()));
				fillMusicList(file);
			} else {
				if (file.length() > 3 * 1048576) {//大于3M才显示
//					Log.d("com.beini", (file.length() / 1048576) + "M");
					String name = file.getName();
					Log.d("com.beini", file.getName());
					if (name.endsWith(".mp3") || name.endsWith(".acc")) {//支持的格式
						musicList.add(file);
					}
				}
			}
		}
	}

	private void startPlay() {
		Log.d("com.beini", "curPage==" + curPage + "  musicList.size()=" + musicList.size());
		if (musicList.size() != 0) {
			mSendBroadCast(NAME, musicList.get(curPage).getName());//4
			try {
				player.setDataSource(musicList.get(curPage).getAbsolutePath());
				player.prepare();
				player.start();
				player.getDuration();
				mSendBroadCast(TOTALTIME, player.getDuration());
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						mSendBroadCast(CURTIME, player.getCurrentPosition());
					}
				}, 0, 1000);

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void playNext() {
		curPage = curPage == musicList.size() - 1 ? (curPage + 1) % musicList.size() : curPage + 1;
		Log.d("com.beini", String.valueOf(curPage));
		player.reset();
		startPlay();
	}

	public void playPrevious() {
		curPage = curPage == 0 ? 0 : curPage - 1;
		Log.d("com.beini", String.valueOf(curPage));
		player.reset();
		startPlay();
	}

	public void parse() {
		player.pause();
	}

	public void restart() {
		player.start();
	}

	private void mSendBroadCast(String key, String value) {
		Intent intent = new Intent(MFILTER);
		intent.putExtra(key, value);//发送广播
		sendBroadcast(intent);
	}

	private void mSendBroadCast(String key, int value) {
		Intent intent = new Intent(MFILTER);
		intent.putExtra(key, value);//发送广播
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("com.beini", "onDestory() service");
	}
}
