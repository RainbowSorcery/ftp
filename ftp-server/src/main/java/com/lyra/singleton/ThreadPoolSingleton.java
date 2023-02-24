package com.lyra.singleton;

import java.util.concurrent.*;

public class ThreadPoolSingleton {
    private static volatile ThreadPoolExecutor threadPoolExecutor = null;

    public static ThreadPoolExecutor getSingleton() {
        if (threadPoolExecutor == null) {
            synchronized (ExecutorService.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(10,
                            20,
                            10,
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<>(100),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return threadPoolExecutor;
    }
}
