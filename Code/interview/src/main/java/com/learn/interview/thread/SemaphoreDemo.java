package com.learn.interview.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

    /**
     * 多个线程抢多个资源
     */
    public static void main(String[] args) {
        // 模拟3个车位
        Semaphore semaphore = new Semaphore(3);

        // 模拟6辆车
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                try {
                    // 此时如果执行了, 说明这辆车已经占到了这个车位
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t抢到车位.");
                    // 暂停3s, 停车3s. 然后释放, 剩下的线程抢占这个资源.
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(Thread.currentThread().getName() + "\t停车3s后离开车位.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
