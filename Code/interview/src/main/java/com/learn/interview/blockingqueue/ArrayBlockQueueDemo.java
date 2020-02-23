package com.learn.interview.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ArrayBlockQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

        // 抛出异常
        blockingQueue.add("a");
        // 会返回特殊值, 布尔值.
        blockingQueue.offer("b");
        // 一直阻塞等待
        blockingQueue.put("e");
        // 阻塞等待 有超时时间
        blockingQueue.offer("c", 2L, TimeUnit.SECONDS);

        // 返回队列头的元素.
        blockingQueue.element();
        blockingQueue.peek();

        // 抛出异常
        blockingQueue.remove();
        // 会返回特殊值, null
        blockingQueue.poll();
        // 一直阻塞等待
        blockingQueue.take();
        // 阻塞等待, 有超时时间
        blockingQueue.poll(2L, TimeUnit.SECONDS);
    }
}
