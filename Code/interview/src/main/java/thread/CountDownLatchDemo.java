package thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    /**
     * 当6个人上完自习之后，班长最后关门走人。
     * 如果不使用CountDownLatch
     * @param args
     * @throws InterruptedException
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
