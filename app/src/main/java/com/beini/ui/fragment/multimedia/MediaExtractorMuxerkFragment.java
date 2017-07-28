package com.beini.ui.fragment.multimedia;


import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.constants.Constants;
import com.beini.util.BLog;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Create by beini 2017/7/28
 */
@ContentView(R.layout.fragment_media_extractor_muxerk)
public class MediaExtractorMuxerkFragment extends BaseFragment {

    private final String SDCARD_PATH = Constants.URL_ALL_FILE + "/Media";

    private final String INPUT_FILEPATH = SDCARD_PATH + "/input.mp4";
    private final String OUTPUT_FILEPATH = SDCARD_PATH + "/output.mp4";


    @Override
    public void initView() {
        try {
            transcode(INPUT_FILEPATH, OUTPUT_FILEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void extractorVideo() {
//
//
//    }

    protected boolean transcode(String input, String output) throws IOException {

        BLog.e("start processing...");
        MediaMuxer muxer = null;

        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(input);

        BLog.e("start demuxer: " + input);
        int mVideoTrackIndex = -1;
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (!mime.startsWith("video")) {
                BLog.e("mime not video, continue search");
                continue;
            }
            extractor.selectTrack(i);
            muxer = new MediaMuxer(output, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoTrackIndex = muxer.addTrack(format);
            muxer.start();
            BLog.e("start muxer: " + output);
        }

        if (muxer == null) {
            BLog.e("no video found !");
            return false;
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 2);
        while (true) {
            int sampleSize = extractor.readSampleData(buffer, 0);
            if (sampleSize < 0) {
                BLog.e("read sample data failed , break !");
                break;
            }
            info.offset = 0;
            info.size = sampleSize;
            info.flags = extractor.getSampleFlags();
            info.presentationTimeUs = extractor.getSampleTime();
            boolean keyframe = (info.flags & MediaCodec.BUFFER_FLAG_SYNC_FRAME) > 0;
            BLog.e("write sample " + keyframe + ", " + sampleSize + ", " + info.presentationTimeUs);
            muxer.writeSampleData(mVideoTrackIndex, buffer, info);
            extractor.advance();
        }

        extractor.release();

        muxer.stop();
        muxer.release();
        BLog.e("process success !");

        return true;
    }
}
