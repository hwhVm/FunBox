package com.example.administrator.baseapp.ui.fragment.camera;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bean.BaseBean;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.event.ImageEvent;
import com.example.administrator.baseapp.ui.fragment.camera.View.DividerGridItemDecoration;
import com.example.administrator.baseapp.ui.fragment.camera.adapter.PicAdapter;
import com.example.administrator.baseapp.ui.fragment.camera.bean.ImageBean;
import com.example.administrator.baseapp.ui.fragment.camera.presenter.ShowPicPresenter;

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
                        recycle_show_pic.addItemDecoration(new DividerGridItemDecoration(baseActivity));
                        recycle_show_pic.setLayoutManager(new GridLayoutManager(baseActivity, 2));
                        PicAdapter picAdapter = new PicAdapter(new BaseBean(R.layout.pic_item, imageBeens));

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
