package com.learn.interview.thread.producerconsumer;

import org.springframework.util.StringUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource {
    // 加了volatile关键字, 保证了可见性. 这个值在main线程修改后, 生产和消费线程可以立即看到.
    // 默认值为true, 开启生产和消费.
    private volatile boolean flag = true;

    // 多线程里, 不要使用++, 要用AtomicInteger. 默认值为0.
    private AtomicInteger atomicInteger = new AtomicInteger();

    // 为通用性考虑: 可以使用构造方法注入.
    private BlockingQueue<String> blockingQueue = null;
    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        // 为了便于排查, 要打出传入的类型具体是什么.
        System.out.println(blockingQueue.getClass().getName());
    }

    public void prod() throws Exception {
        String data = null;
        boolean retValue;

        // 先判断
        while (flag) {
            // 准备数据
            data = atomicInteger.incrementAndGet() + "";
            // 执行生产
            retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
            // 判断
            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t" + "插入队列成功. data=" + data);
            } else {
                System.out.println(Thread.currentThread().getName() + "\t" + "插入队列失败. data=" + data);
            }
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println(Thread.currentThread().getName() + "\t" + "生产者暂停. flag=false");
    }

    public void consumer() throws Exception {

        String result = null;
        while (flag) {
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(result)) {
                // 修改标记位
                flag = false;
                System.out.println(Thread.currentThread().getName() + "\t" + "超过2s无法消费, 退出");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "消费者消费了值. result=" + result);
        }
    }

    public void stop() {
        this.flag = false;
    }
}

public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        MyResource myResource = new MyResource(
                new ArrayBlockingQueue<>(10)
        );

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 生产者线程启动");
            try {
                myResource.prod();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "PROD").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 消费者线程启动");
            try {
                myResource.consumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "CONSUMER").start();

        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + "\t 5s后, 主线程停止. 生产消费要立刻停止!");
        myResource.stop();
    }
}
