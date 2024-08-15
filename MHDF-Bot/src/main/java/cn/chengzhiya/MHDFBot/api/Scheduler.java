package cn.ChengZhiYa.MHDFBot.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Scheduler {
    private final ScheduledExecutorService executorService;

    public Scheduler() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void runTask(Runnable task) {
        executorService.submit(task);
    }

    public void runTaskLater(Runnable task, long delay) {
        executorService.schedule(task, delay, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
