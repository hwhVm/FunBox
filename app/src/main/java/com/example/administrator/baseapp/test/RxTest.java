package com.example.administrator.baseapp.test;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by beini on 2017/3/9.
 */

public class RxTest {
    public static void main(String[] args) {
        //
        Flowable.just("hellow").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("  s==" + s);
                List<String> list = new ArrayList<String>();
                list.get(33);
            }
        });
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                
            }
        });
//        //
//        Flowable flowable = Flowable.create(new FlowableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(FlowableEmitter<Object> e) throws Exception {
//                e.onNext("");
//            }
//        }, BackpressureStrategy.BUFFER);
//        flowable.subscribe(subscriber);

//        List<Integer> list = Flowable.range(1, 100).toList().blockingGet();
//        for(int i=0;i<list.size();i++) {
//            System.out.println("  i=="+i);
//        }
//        List<String> list = null;
//        Flowable.fromIterable(list)
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println("  s"+s);
//                    }
//                });

    System.out.println("  "+(1%2));
    }

    static ResourceSubscriber<Integer> subscriber = new ResourceSubscriber<Integer>() {
        @Override
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(Integer t) {
            System.out.println(t);
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("Done");
        }
    };

}
