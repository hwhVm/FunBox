package com.beini.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import java.util.ArrayList;

/**
 * Created by beini on 2017/7/10.
 */

public class VoiceUtil {
    private ArrayList<byte[]> list = new ArrayList<>();
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

    public VoiceUtil() {
//        am = (AudioManager) GlobalApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
    }


}
