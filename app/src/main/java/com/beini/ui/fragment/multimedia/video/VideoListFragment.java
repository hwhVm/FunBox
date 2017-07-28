package com.beini.ui.fragment.multimedia.video;


import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.constants.Constants;
import com.beini.ui.fragment.multimedia.video.adapter.VideoListAdapter;
import com.beini.ui.view.RecycleDecoration;
import com.beini.util.BLog;

import org.reactivestreams.Subscription;

import java.io.File;
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
 * Create by beini 2017/7/8
 */
@ContentView(R.layout.fragment_video_list)
public class VideoListFragment extends BaseFragment {
    @ViewInject(R.id.recycler_video_list)
    RecyclerView recycler_video_list;
    private VideoListAdapter videoListAdapter;
    private List<Bitmap> bitmaps = new ArrayList<>();

    @Override
    public void initView() {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                String filePath = Constants.URL_ALL_FILE
                        + "/APIC/";
                File files = new File(filePath);
                File[] files1 = files.listFiles();
                for (int i = 0; i < files1.length; i++) {
                    bitmaps.add(VideoUtil.getVideoScreenshot(files1[i].getAbsolutePath()));
                    BLog.e("      " + files1[i].getAbsolutePath() + "     " + (bitmaps == null));
                }
                e.onNext(true);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<Boolean>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Boolean o) {
                BLog.e("            o="+o);
                videoListAdapter = new VideoListAdapter(new BaseBean<>(R.layout.item_vieo_list, bitmaps));
                recycler_video_list.setLayoutManager(new LinearLayoutManager(baseActivity));
                recycler_video_list.addItemDecoration(new RecycleDecoration(getActivity()));
                recycler_video_list.setAdapter(videoListAdapter);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

}
