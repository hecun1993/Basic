

### CountDownLatch

![image-20200222221946349](../Images\image-20200222221946349.png)

类似于, 火箭发射的倒计时. 必须等全部的子线程执行完之后, 主线程才会执行.

```java
public class CountDownLatchDemo {
    
    /**
     * 当6个人上完自习之后，班长最后关门走人。
     * 如果不使用CountDownLatch, 则每次执行, 不一定班长这一条最后执行. 比如下图所示.
     */
    public static void main(String[] args) throws InterruptedException {

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t上完自习离开教室。");
            }, String.valueOf(i)).start();
        }

        System.out.println(Thread.currentThread().getName() + "\t班长最后关门走人。");
    }
}

2	上完自习离开教室。
5	上完自习离开教室。
6	上完自习离开教室。
3	上完自习离开教室。
1	上完自习离开教室。
main	班长最后关门走人。
4	上完自习离开教室。
```

当加上CountDownLatch之后, 就可以保证上面的要求. 

```java
public class CountDownLatchDemo {

    /**
     * 当6个人上完自习之后，班长最后关门走人。
     */
    public static void main(String[] args) throws InterruptedException {
        // 设置count的计数值为6;
        CountDownLatch countDownLatch = new CountDownLatch(6);
        
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t上完自习离开教室。");
                // 每个线程执行完之后, count的值减一. 一直减到0为止.
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }

        // 主线程因为await()方法, 在进行等待.
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t班长最后关门走人。");
    }
}

2	上完自习离开教室。
6	上完自习离开教室。
5	上完自习离开教室。
4	上完自习离开教室。
3	上完自习离开教室。
1	上完自习离开教室。
main	班长最后关门走人。
```



### CyclicBarrier

![image-20200222221750494](..\Images\image-20200222221750494.png)

```java
public class CyclicBarrierDemo {

    /**
     * 被调用await的线程, 会等待全部完成后, 才会执行CyclicBarrier创建的线程.
     */
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println(Thread.currentThread().getName() + "\t 召唤神龙!");
        });

        for (int i = 1; i <= 7; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t集齐第" + finalI + "颗龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}

2	集齐第2颗龙珠
5	集齐第5颗龙珠
6	集齐第6颗龙珠
1	集齐第1颗龙珠
4	集齐第4颗龙珠
3	集齐第3颗龙珠
7	集齐第7颗龙珠
7	 召唤神龙!
```



### Semaphore

![image-20200222223019038](..\Images\image-20200222223019038.png)

```java
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

1	抢到车位.
3	抢到车位.
2	抢到车位.
1	停车3s后离开车位.
3	停车3s后离开车位.
2	停车3s后离开车位.
4	抢到车位.
5	抢到车位.
6	抢到车位.
6	停车3s后离开车位.
4	停车3s后离开车位.
5	停车3s后离开车位.
```



## 阻塞队列

* 阻塞队列也有有价值的一面
* 如果不得不阻塞, 应该如何管理
* 主要使用在:
  * 生产者消费者模式
  * 线程池
  * 消息中间件

![image-20200222224738834](..\Images\image-20200222224738834.png)

有了BlockingQueue之后, 我们不需要关心什么时候需要唤醒线程, 什么时候需要阻塞线程. 而是交给了BlockingQueue.



Iterator -> Collection -> Queue / List -> BlockingQueue -> 7类常见的实现类

![image-20200222225352788](..\Images\image-20200222225352788.png)

![image-20200222230704386](..\Images\image-20200222230704386.png)

### ArrayBlockingQueue & LinkedBlockingQueue

```java
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
```

### SynchronizedBlockingQueue

0库存阻塞队列, 没有容量. 不存储元素, 每一个put操作, 必须有一个take操作. 否则不能添加元素.

```java
public class SynchronizedBlockingQueueDemo {

     /**
      * 如果这里使用2个线程生产和消费, 则用while和if是等价的. 
      * 但如果有2个以上的线程, 则必须用while
     */
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

AAA	 put 1
BBB	 take 1
AAA	 put 2
BBB	 take 2
AAA	 put 3
```

## 生产者消费者

### 传统版

```java
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

AAA	1
BBB	0
AAA	1
BBB	0
AAA	1
BBB	0
AAA	1
BBB	0
AAA	1
BBB	0
AAA	1
BBB	0
```

