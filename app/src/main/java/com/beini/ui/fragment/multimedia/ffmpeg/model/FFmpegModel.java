package com.beini.ui.fragment.multimedia.ffmpeg.model;

import android.net.Uri;
import android.widget.VideoView;

import com.beini.constants.Constants;
//import com.netcompss.loader.LoadJNI;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by beini on 2017/7/31.
 */

public class FFmpegModel {

    public static void startVideoView(String url, VideoView videoView) {
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public static Flowable<String> rotateVideo(int rotate, final String workFolder, final String srcPath) {

        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                String targerPath = Constants.URL_ALL_FILE + "/ffmpeg/" + System.currentTimeMillis() + ".mp4";
                String commandStr = "ffmpeg -y -i " + srcPath + " -strict experimental -vf transpose=1 -s 160x120 -r 30 -aspect 4:3 -ab 48000 -ac 2 -ar 22050 -b 2097k" + targerPath;
//
//                LoadJNI vk = new LoadJNI();
//                try {
//                    vk.run(GeneralUtils.utilConvertToComplex(commandStr), workFolder, GlobalApplication.getInstance().getApplicationContext());
//                    e.onNext(targerPath);
//                } catch (CommandValidationException exception) {
//                    exception.printStackTrace();
//                    e.onNext(targerPath);
//                }
            }
        }, BackpressureStrategy.BUFFER).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());

    }
}
