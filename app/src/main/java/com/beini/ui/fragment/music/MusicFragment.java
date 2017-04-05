package com.beini.ui.fragment.music;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.event.DLNAActionEvent;
import com.beini.ui.fragment.SystemService;
import com.beini.ui.fragment.music.utils.DivoomUpnpService;
import com.beini.ui.fragment.music.utils.MediaPlayerController;
import com.beini.ui.fragment.music.utils.PlaybackCommand;
import com.beini.ui.fragment.music.utils.SystemManager;
import com.beini.utils.BLog;

import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportState;
import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutionException;

/**
 * Created by beini on 2017/2/14.
 */
@ContentView(R.layout.dlna_main_fragment)
public class MusicFragment extends BaseFragment {

    @ViewInject(R.id.img_dlna_play)
     ImageButton mPlayPauseButton;
    @ViewInject(R.id.dlna_cur_time)
     TextView mCurTime;
    @ViewInject(R.id.dlna_total_time)
     TextView mTotalTime;
    @ViewInject(R.id.dlna_seekbar)
     SeekBar mSeekBar;
    private int mTrackDurationSeconds = 0;

//    @ViewInject(R.id.song_title)
//     TextView mTitleTextView;
    @ViewInject(R.id.song_singer)
     TextView mSingerTextView;

    private static MediaPlayerController mMediaPlayerController;
    /**
     * Use this factory method to create a new instance.
     *
     * @return A new instance of fragment DLNAMainFragment.
     */
    static Intent upnpServiceIntent = null;
    static Intent systemServiceIntent = null;

    @Override
    public void initView() {
        fragmentChange();

        EventBus.getDefault().register(this);

        mMediaPlayerController = MediaPlayerController.getInstance();
        // Bind UPnP service
        BLog.d("   upnpServiceIntent == null=="+(upnpServiceIntent == null));
        if (upnpServiceIntent == null) {
            upnpServiceIntent = new Intent(getActivity(), DivoomUpnpService.class);
            getActivity().startService(upnpServiceIntent);
        }
        // Bind System service
        if (systemServiceIntent == null) {
            systemServiceIntent = new Intent(getActivity(), SystemService.class);
            getActivity().startService(systemServiceIntent);
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                DLog.i(TAG, "Start Seek");
                try {
                    mMediaPlayerController.pauseUpdateSeekBar();
                } catch (InterruptedException e) {
//                    DLog.i(TAG, "Interrupt update seekbar thread.");
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float percent = (float) seekBar.getProgress() / seekBar.getMax();
                int seekToSecond = (int) (mTrackDurationSeconds * percent);
//                DLog.i(TAG, "Seek to second:" + ModelUtil.toTimeString(seekToSecond));
                //Send command and receive result.
                PlaybackCommand.seek(ModelUtil.toTimeString(seekToSecond));

                //这里延时执行，让设备seek之后再读取比较好点
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            mMediaPlayerController.startUpdateSeekBar();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().postSticky(new DLNAActionEvent(DLNAActionEvent.REFRESH_OBJECTS));
                    }

                }).start();
            }
        });
    }
    public static final String DIALOG_FRAGMENT_TAG = "dialog";
//    @Event(R.id.text_dlna_list)
//    private void mEvent(View view) {
//        switch (view.getId()) {
//            case R.id.text_dlna_list:
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//
//                // Create and show the diaDLog.
//                DialogFragment newFragment = DeviceListDialogFragment.newInstance();
//                newFragment.show(ft, DIALOG_FRAGMENT_TAG);
//
//                break;
//        }
//    }
    /**
     * UI更新时候调用
     */
    private void fragmentChange(){
//        itb.setTitle(getString(R.string.vod_item_local));
//        itb.setCloseVisible(true);
//        itb.setPlusView(getResources().getDrawable(R.drawable.dibot_icon_caidan));
//        itb.setPlusVisible(View.VISIBLE);
//
//        itb.setToolBarVisible(View.VISIBLE);
//        itb.setBottomTabVisible(View.VISIBLE);
        mMediaPlayerController = MediaPlayerController.getInstance();
        // Inflate the layout for this fragment
        //如果当前能获取到当前歌曲，就刷新
        Item item = SystemManager.getInstance().getCurSong();
        if (item != null) {
//            mTitleTextView.setText(item.getTitle());

            if (!item.getCreator().equals("<unknown>"))
                mSingerTextView.setText(item.getCreator());
            else
                mSingerTextView.setText("");
            item.getFirstResource();
        }

        TransportState state = mMediaPlayerController.getCurrentState();
        BLog.d("      (mPlayPauseButton==null)="+(mPlayPauseButton==null));
        if (state == TransportState.PLAYING) {
            mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_zt);
            mPlayPauseButton.setBackgroundColor(getResources().getColor(R.color.app_backound_color));
            try {
                mMediaPlayerController.startUpdateSeekBar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
//            mPlayPauseButton.setBackgroundColor(R.color.app_backound_color);
        } else if (state == TransportState.PAUSED_PLAYBACK || state == TransportState.STOPPED) {
//            mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_bf);
//            mPlayPauseButton.setBackgroundColor(getResources().getColor(R.color.app_backound_color));
        }

    }


    @Event(value = {R.id.img_dlna_select, R.id.img_dlna_play, R.id.img_dlna_next, R.id.img_dlna_prev})
    private void buttonClick(View view) {
        switch (view.getId()){
            case R.id.img_dlna_next:
                SystemManager.getInstance().playNext();
                EventBus.getDefault().postSticky(new DLNAActionEvent(DLNAActionEvent.REFRESH_OBJECTS));
                break;
            case R.id.img_dlna_prev:
                SystemManager.getInstance().playPrev();
                EventBus.getDefault().postSticky(new DLNAActionEvent(DLNAActionEvent.REFRESH_OBJECTS));
                break;
            case R.id.img_dlna_play:
                TransportState state = mMediaPlayerController.getCurrentState();
                if (state == TransportState.PLAYING) {
                    //pause song
                    if (SystemManager.getInstance().playSong(false)) {
                        mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_bf);
                        mMediaPlayerController.setCurrentState(TransportState.PAUSED_PLAYBACK);
                    }
                }
                //play song
                else if (state == TransportState.PAUSED_PLAYBACK || state == TransportState.STOPPED) {
                    //如果没有选择设备就弹出框来选择设备
                    if (SystemManager.getInstance().getSelectedDevice() == null) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        // Create and show the diaDLog.
                        DialogFragment newFragment = DeviceListDialogFragment.newInstance();
                        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
                    } else {
                        if (SystemManager.getInstance().playSong(true)) {
                            PlaybackCommand.getPositionInfo();
                            mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_zt);
                            try {
                                mMediaPlayerController.startUpdateSeekBar();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            mMediaPlayerController.setCurrentState(TransportState.PLAYING);

                            PlaybackCommand.getTransportInfo();
                        }
                    }
                }
                break;
            case R.id.img_dlna_select:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the diaDLog.
                DialogFragment newFragment = DeviceListDialogFragment.newInstance();
                newFragment.show(ft, DIALOG_FRAGMENT_TAG);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DLNAActionEvent event) {
//        DLog.i(TAG, "DLNAActionEvent " + event.action);
        try {
            switch (event.action) {
                case DLNAActionEvent.PLAY_ACTION:
//                    DLog.i(TAG, "Execute PLAY_ACTION");
                    mMediaPlayerController.setCurrentState(TransportState.PLAYING);
                    //Start syncing
                    mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_zt);
                    mMediaPlayerController.startUpdateSeekBar();
                    break;
                case DLNAActionEvent.PAUSE_ACTION:
//                    DLog.i(TAG, "Execute PAUSE_ACTION");
                    mMediaPlayerController.setCurrentState(TransportState.PAUSED_PLAYBACK);
                    mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_bf);
//                        mMediaPlayerController.pauseUpdateSeekBar();
                    break;
                case DLNAActionEvent.STOP_ACTION:
//                    DLog.i(TAG, "Execute STOP_ACTION");
                    mMediaPlayerController.setCurrentState(TransportState.STOPPED);
                    mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_bf);
//                        mMediaPlayerController.pauseUpdateSeekBar();
                    SystemManager.getInstance().stopSong();
                    break;
                case DLNAActionEvent.GET_MEDIA_INFO_ACTION:
                    MediaInfo mediaInfo = event.mediaInfo;
                    if (mediaInfo != null) {
//                        DLog.i(TAG, "Execute GET_MEDIA_INFO_ACTION:" + mediaInfo);
                    }
                    break;
                case DLNAActionEvent.GET_POSITION_INFO_ACTION:
                    PositionInfo positionInfo = event.positionInfo;
                    if (positionInfo != null) {
                        //Set rel time and duration time.
                        String relTime = positionInfo.getRelTime();
                        String trackDuration = positionInfo.getTrackDuration();
                        mCurTime.setText(relTime);
                        mTotalTime.setText(trackDuration);

                        int elapsedSeconds = (int) positionInfo.getTrackElapsedSeconds();
                        int durationSeconds = (int) positionInfo.getTrackDurationSeconds();
                        mSeekBar.setProgress(elapsedSeconds);
                        mSeekBar.setMax(durationSeconds);

//                        DLog.d(TAG, "elapsedSeconds:" + elapsedSeconds);
//                        DLog.d(TAG, "durationSeconds:" + durationSeconds);

                        //Record the current track's duration seconds
                        mTrackDurationSeconds = durationSeconds;
                    }
                    break;
//                case RESUME_SEEKBAR_ACTION:
//                    mMediaPlayerController.startUpdateSeekBar();
//                    break;
//                case GET_VOLUME_ACTION:
//                    //Get the current volume from arg1.
//                    SystemManager.getInstance().setDeviceVolume(event.volValue);
//                    break;
//                case SET_VOLUME_ACTION:
//                    SystemManager.getInstance().setDeviceVolume(event.volValue);
//                    break;
                case DLNAActionEvent.GET_PLAY_STATUS:
                    boolean isPlay = event.isPlay;
//                    DLog.i(TAG, "GET_PLAY_STATUS " + isPlay);
                    if (isPlay) {
                        mMediaPlayerController.setCurrentState(TransportState.PLAYING);
                        mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_bf);
                    } else {
                        mMediaPlayerController.setCurrentState(TransportState.PAUSED_PLAYBACK);
                        mPlayPauseButton.setImageResource(R.mipmap.dibot_icon_zt);
                    }
                    break;
                case DLNAActionEvent.REFRESH_OBJECTS:
                    Item item = SystemManager.getInstance().getCurSong();
//                    DLog.i(TAG, "REFRESH_OBJECTS");
                    if (item != null) {
//                        mTitleTextView.setText(item.getTitle());
                        if (!item.getCreator().equals("<unknown>"))
                            mSingerTextView.setText(item.getCreator());
                        else
                            mSingerTextView.setText("");
                    }
                    PlaybackCommand.getVolume();
                    break;
            }
        } catch (InterruptedException e) {
//            DLog.i(TAG, "SetCurrentStatus InterruptedException:" + e.getMessage());
        } catch (ExecutionException e) {
//            DLog.i(TAG, "SetCurrentStatus ExecutionException:" + e.getMessage());
        }
    }
    /**按back页面返回的时候调用
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //show
        if(!hidden){
            fragmentChange();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
