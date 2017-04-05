package com.beini.utils.threadpool;

import android.os.SystemClock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by beini on 2017/3/15.
 */

public class ThreadPoolUtil {

    Runnable command = new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(2000);
        }
    };

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * 并不会自动销毁空闲状态的线程
     */
    public void fixThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        fixedThreadPool.execute(command);
    }

    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * 处于空闲状态的线程被保留60s
     * 有任务时候可以无限创建
     */
    public void cacheThreadPool() {
        ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
        cacheThreadPool.execute(command);
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行。
     */

    public void scheduledThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);
        scheduledThreadPool.schedule(command, 2000, TimeUnit.MILLISECONDS);//2000ms后执行
        scheduledThreadPool.scheduleAtFixedRate(command, 10, 1000, TimeUnit.MILLISECONDS);//延迟10ms后，每隔1000ms执行一次command
    }

    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     */
    public void singleThreadPool() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        singleThreadPool.execute(command);

    }
}
