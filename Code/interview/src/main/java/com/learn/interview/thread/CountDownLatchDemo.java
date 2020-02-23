package com.learn.interview.thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    /**
     * 当6个人上完自习之后，班长最后关门走人。
     * 如果不使用CountDownLatch, 则每次执行, 不一定班长这一条最后执行. 比如下图所示.
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t上完自习离开教室。");
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t班长最后关门走人。");
    }
}
