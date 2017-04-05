package com.beini.ui.fragment.music.utils;

import android.util.Log;

import org.fourthline.cling.support.model.TransportState;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by beini on 2017/2/20.
 */

public class MediaPlayerController {

    private static final String TAG = MediaPlayerController.class.getSimpleName();

    private TransportState mCurrentState = TransportState.STOPPED;
    private ExecutorService mMediaExecutorService = Executors.newSingleThreadExecutor();
    private Object mPlaybackLock = new Object();
    private volatile boolean isPaused = true;
    private static MediaPlayerController mediaPlayerController  = null;

//    static private Handler mhandler;

    static public MediaPlayerController getInstance(){
//        mhandler = handler;
        if(mediaPlayerController == null)
            mediaPlayerController = new MediaPlayerController();
        return mediaPlayerController;
    }
    public MediaPlayerController(){
        //Create SeekBar sync thread,mSeekFuture will be set null when press stop.
        mMediaExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                Log.i(TAG, "Create SeekBar sync thread.");
                while (true) {
                    synchronized (mPlaybackLock) {
                        try {
                            if (isPaused) {
                                Log.i(TAG, "mPlaybackLock wait.");
                                index = 0;
                                mPlaybackLock.wait();
                            }
                            PlaybackCommand.getPositionInfo();
                            if(index++%3==2){
                                PlaybackCommand.getTransportInfo();
                            }


                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "MediaPlayer shutdown");
                        }
                    }
                }
            }
        });
    }

    public TransportState getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(TransportState currentState) {
        if (this.mCurrentState != currentState) {
            this.mCurrentState = currentState;
        }
    }

    public void startUpdateSeekBar() throws InterruptedException, ExecutionException {
        Log.i(TAG, "Execute startUpdateSeekBar");
//        if (this.mCurrentState == TransportState.PLAYING && isPaused) {
        if (isPaused) {
            synchronized (mPlaybackLock) {
                isPaused = false;
                Log.i(TAG, "Resume seekbar sync thread.");
                mPlaybackLock.notifyAll();
            }
        }
    }

    public void pauseUpdateSeekBar() throws InterruptedException {
        Log.i(TAG, "Execute pauseUpdateSeekBar");
        isPaused = true;
    }

    public boolean seekbarIsPaused(){
        return isPaused;
    }

    public void destroy() {
        mMediaExecutorService.shutdownNow();
    }
}
