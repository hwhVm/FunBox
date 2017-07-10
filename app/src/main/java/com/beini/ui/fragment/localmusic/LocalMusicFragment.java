package com.beini.ui.fragment.localmusic;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.localmusic.service.MusicService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * create by beini  2017/2/10
 */
@ContentView(R.layout.fragment_local_music)
public class LocalMusicFragment extends BaseFragment {
    @ViewInject(R.id.text_all)
    private TextView text_all;
    @ViewInject(R.id.text_title)
    private TextView text_title;
    @ViewInject(R.id.text_surplus)
    private TextView text_surplus;
    @ViewInject(R.id.seekbar_music)
    SeekBar seekBar;
    @ViewInject(R.id.btn_play_net_music)
    Button btn_play_net_music;

    private MusicService musicService;
    private MusicConnect musicConnet;

    private boolean isStop = true;
    private double totalTimeInt;

    @Override
    public void initView() {
        initRevice();
        initService();
    }

    private void initRevice() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.MFILTER);
        filter.addAction(MusicService.NAME);
        filter.addAction(MusicService.TOTALTIME);
        filter.addAction(MusicService.CURTIME);
        baseActivity.registerReceiver(new MusicReceiver(), filter);
    }

    private void initService() {
        Intent intent = new Intent(baseActivity, MusicService.class);
        musicConnet = new MusicConnect();
        baseActivity.bindService(intent, musicConnet, BIND_AUTO_CREATE);
    }

    private class MusicConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MusicBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    }


    @Event({R.id.btn_last, R.id.btn_next, R.id.btn_start, R.id.btn_stop,R.id.btn_play_net_music})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last:
                musicService.playPrevious();
                break;
            case R.id.btn_next:
                musicService.playNext();
                break;
            case R.id.btn_start:
                if (isStop) {
                    musicService.restart();
                } else {
                    musicService.parse();
                }
                isStop = !isStop;
                break;
            case R.id.btn_stop:
                Intent intent = new Intent("com.intent.musicplayer.MusicService");
                baseActivity.unbindService(musicConnet);
                baseActivity.stopService(intent);
                break;
            case R.id.btn_play_net_music:
                String path="http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3";     //这里给一个歌曲的网络地址就行了
                Uri uri  =  Uri.parse(path);
                MediaPlayer player  =   new MediaPlayer();
                player.create(getActivity(),uri);
                player.start();
//                player.setDataSource();
                player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {

                    }
                });
                break;
        }
    }

    private class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(MusicService.CURTIME, 0) != 0) {
                double curTimeInt = intent.getIntExtra(MusicService.CURTIME, 0);
                text_surplus.setText(transferMilliToTime((int) curTimeInt));
                double result = curTimeInt / totalTimeInt * 100;
                seekBar.setProgress((int) Math.floor(result));
            } else if (intent.getIntExtra(MusicService.TOTALTIME, 0) != 0) {
                totalTimeInt = intent.getIntExtra(MusicService.TOTALTIME, 0);
                text_all.setText(transferMilliToTime((int) (totalTimeInt)));
            } else if (!TextUtils.isEmpty(intent.getStringExtra(MusicService.NAME))) {
                text_title.setText(intent.getStringExtra(MusicService.NAME));
            }
        }

    }

    private String transferMilliToTime(int millis) {
        DateFormat format = new SimpleDateFormat("mm:ss");
        String result = format.format(new Date(millis));
        return result;
    }

}
