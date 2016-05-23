package com.adventorium.lab6;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Андрей on 23.05.2016.
 */
public class OneHundredThreads {

    private int PARALLEL_THREADS;

    OneHundredThreads() {
        PARALLEL_THREADS=100;
        final AtomicLong counter = new AtomicLong();
        Increase(counter, 100000);
        System.out.println("Counter = " + counter);
    }

    public void Increase(AtomicLong counter, long value) {

        CountDownLatch latch = new CountDownLatch(PARALLEL_THREADS);

        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < PARALLEL_THREADS; i++) {
            service.submit(new Runnable() {
                public void run() {
                    for (int j = 0; j < value; j++) {
                        counter.getAndIncrement();
                    }
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
            service.shutdown();
            while (!service.isTerminated()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
