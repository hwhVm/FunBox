package com.beini.ui.fragment.rx;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bean.green.User;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.util.BLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Create by beini 2017/8/11
 */
@ContentView(R.layout.fragment_rx_all)
public class RxAllFragment extends BaseFragment {

    @Override
    public void initView() {

    }

    @Event({R.id.btn_rx_filter})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_rx_filter:
//                rxFilter();
//                rxMap();
                flatMap();
                break;
        }
    }

    private void flatMap() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        users.add(user1);
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("user1");
        users.add(user2);

    }

    private void rxMap() {
        Flowable.just(1, 2, 2)
                .map(new Function<Integer, Object>() {
                    @Override
                    public Object apply(Integer integer) throws Exception {
                        return integer + 1;
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

    private void rxFilter() {
        Flowable.just(33).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                BLog.e(" ----------->test");
                return integer > 10;
            }
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer < 5;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                BLog.e(" ------------>integer==" + integer);
            }
        });
    }
}
