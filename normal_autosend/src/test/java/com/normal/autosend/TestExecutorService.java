package com.normal.autosend;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author: fei.he
 */
public class TestExecutorService {

    final static Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("e: " + e.getMessage());
        }
    };

    private static ScheduledExecutorService dailyExecutor = new ScheduledThreadPoolExecutor(
            1, r -> {
        Thread t = new Thread("daily task");
        t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        return t;
    }
    );

    public static void main(String[] args) {
        Future<?> future = dailyExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("xxx");
                throw new NullPointerException();
            }
        });
//        future.get();
    }
}
