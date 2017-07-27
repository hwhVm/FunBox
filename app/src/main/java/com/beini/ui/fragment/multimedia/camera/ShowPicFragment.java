package com.beini.ui.fragment.multimedia.camera;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.event.ImageEvent;
import com.beini.ui.fragment.multimedia.camera.adapter.PicAdapter;
import com.beini.ui.fragment.multimedia.camera.bean.ImageBean;
import com.beini.ui.fragment.multimedia.camera.presenter.ShowPicPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Create by beini 2017/3/10
 */
@ContentView(R.layout.fragment_show_pic)
public class ShowPicFragment extends BaseFragment {
    @ViewInject(R.id.recycle_show_pic)
    RecyclerView recycle_show_pic;

    ShowPicPresenter picPresenter;
    List<ImageBean> imageBeens;

    @Override
    public void initView() {
        picPresenter = new ShowPicPresenter();
    }

    @Override
    public void returnLoad() {
        super.returnLoad();
        showPic();
    }

    private void showPic() {
        picPresenter
                ._getImagURL()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ImageBean>>() {
                    @Override
                    public void accept(List<ImageBean> imageBeen) throws Exception {
                        imageBeens = imageBeen;
                        recycle_show_pic.addItemDecoration(new SpacesItemDecoration(8));
//                        recycle_show_pic.setLayoutManager(new GridLayoutManager(baseActivity, 2));
                        recycle_show_pic.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        PicAdapter picAdapter = new PicAdapter(new BaseBean<>(R.layout.pic_item, imageBeens));
                        recycle_show_pic.setAdapter(picAdapter);
                        picAdapter.setItemClick(itemClickListener);
                    }
                });

    }

    PicAdapter.OnItemClickListener itemClickListener = new PicAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            EventBus.getDefault().postSticky(new ImageEvent(imageBeens,position));
            baseActivity.replaceFragment(PicDetaiFragment.class);
        }
    };

    @Event(R.id.image_back)
    private void mEvent(View view) {
        baseActivity.back();
    }
}
