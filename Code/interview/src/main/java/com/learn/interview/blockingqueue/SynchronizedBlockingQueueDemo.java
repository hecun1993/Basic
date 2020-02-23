package com.learn.interview.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronizedBlockingQueueDemo {

    public static void main(String[] args) {
        // 同步阻塞队列
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "\t put 1");
                blockingQueue.put("a");

                System.out.println(Thread.currentThread().getName() + "\t put 2");
                blockingQueue.put("2");

                System.out.println(Thread.currentThread().getName() + "\t put 3");
                blockingQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "AAA").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + "\t take 1");

                TimeUnit.SECONDS.sleep(3L);
                blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + "\t take 2");

                TimeUnit.SECONDS.sleep(3L);
                blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + "\t take 3");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "BBB").start();
    }
}
