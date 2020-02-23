package com.learn.interview.thread.producerconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 资源类. 是高内聚的, java操作的是对象, 修改某一个属性的值, 要靠操作此资源类.
 */
class ShareData {

    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 生产线程
     */
    public void increment() throws InterruptedException {
        lock.lock();
        try {
            // 1. 判断
            // 多线程的判断, 要用while, 不要用if.
            // 数量不为0, 此时生产的线程要暂停阻塞.
            while (number != 0) {
                condition.await();
            }

            // 2. 干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);

            // 3. 通知唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    /**
     * 消费线程
     */
    public void decrement() throws InterruptedException {

        lock.lock();

        try {
            // 1. 判断
            // 多线程的判断, 要用while, 不要用if.
            // 数量为0, 此时消费的线程要暂停阻塞.
            while (number == 0) {
                condition.await();
            }

            // 2. 干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);

            // 3. 通知唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 生产者消费者模式:
 *
 * 1. 线程操作资源类;
 * 2. 判断干活并通知;
 * 3. 防止虚假的唤醒;
 */
public class TraditionalDemo {

    /**
     * 如果这里使用2个线程生产和消费, 则用while和if是等价的.
     * 但如果有2个以上的线程, 则必须用while
     */
    public static void main(String[] args) {
        // 创建资源类
        ShareData shareData = new ShareData();

        // 生产线程
        new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                try {
                    shareData.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AAA").start();

        // 消费线程
        new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                try {
                    shareData.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BBB").start();
    }
}
