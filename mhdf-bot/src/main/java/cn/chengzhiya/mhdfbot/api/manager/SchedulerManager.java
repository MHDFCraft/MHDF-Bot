package cn.chengzhiya.mhdfbot.api.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public final class SchedulerManager {
    private final ScheduledExecutorService executorService;

    public SchedulerManager() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 主线程执行
     *
     * @param runnable 运行的runnable实例
     */
    public void runTask(Runnable runnable) {
        runnable.run();
        this.shutdown();
    }

    /**
     * 主线程延迟执行
     *
     * @param runnable 运行的runnable实例
     * @param delay    延迟多少秒运行
     */
    public void runTaskLater(Runnable runnable, long delay) {
        try {
            Thread.sleep(delay * 1000);
            runnable.run();
            this.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步执行
     *
     * @param runnable 运行的runnable实例
     */
    public void runTaskAsynchronously(Runnable runnable) {
        this.executorService.submit(runnable);
        this.shutdown();
    }

    /**
     * 异步延迟执行
     *
     * @param runnable 运行的runnable实例
     * @param delay    延迟多少秒运行
     */
    public void runTaskAsynchronouslyLater(Runnable runnable, long delay) {
        this.executorService.schedule(runnable, delay, TimeUnit.SECONDS);
        this.shutdown();
    }

    /**
     * 异步定期执行
     *
     * @param runnable 运行的runnable实例
     * @param delay    延迟多少秒运行
     * @param period   每多少秒执行一次
     */
    public void runTaskAsynchronouslyTimer(Runnable runnable, long delay, long period) {
        this.executorService.scheduleAtFixedRate(runnable, delay, period, TimeUnit.SECONDS);
    }

    /**
     * 销毁线程池
     */
    public void shutdown() {
        this.executorService.shutdown();
    }
}
