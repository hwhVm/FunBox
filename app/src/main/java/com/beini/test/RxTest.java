package com.beini.test;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by beini on 2017/3/9.
 */

public class RxTest {
    public static void main(String[] args) {
        Subscriber<String> mSubscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println(" onSubscribe");
                s.request(1);
            }

            @Override
            public void onNext(String string) {
                System.out.println(" onNext");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(" onError");
            }

            @Override
            public void onComplete() {
                System.out.println(" onComplete");
            }
        };

        ResourceSubscriber<String> mResourceSubscriber = new ResourceSubscriber<String>() {

            @Override
            public void onNext(String string) {
                System.out.println("           onNext   string=" + string);

            }

            @Override
            public void onError(Throwable t) {
                System.out.println("           onError   ");
            }

            @Override
            public void onComplete() {
                System.out.println("           onComplete   ");
            }
        };

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("ddddddd");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribe(mResourceSubscriber);

    }


    private static void test1() {
        //
//        Flowable.just("hellow").subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                System.out.println("  s==" + s);
//                List<String> list = new ArrayList<String>();
//                list.get(33);
//            }
//        });
//        Observable.create(new ObservableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(ObservableEmitter<Object> e) throws Exception {
//
//            }
//        }).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//
//            }
//        });
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
    }

//    static ResourceSubscriber<Integer> subscriber = new ResourceSubscriber<Integer>() {
//        @Override
//        public void onStart() {
//            request(Long.MAX_VALUE);
//        }
//
//        @Override
//        public void onNext(Integer t) {
//            System.out.println(t);
//        }
//
//        @Override
//        public void onError(Throwable t) {
//            t.printStackTrace();
//        }
//
//        @Override
//        public void onComplete() {
//            System.out.println("Done");
//        }
//    };

}
