package io.opentelemetry.example.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangyi
 * @version v 1.0
 * @date 2025/8/27 17:25
 */
@Configuration
public class ThreadPoolConfig {
  private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);
  // CPU核心数
  private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();

  /**
   * IO密集型任务线程池（如数据库操作、网络请求）
   */
  @Bean("io-executor")
  public ExecutorService ioIntensiveThreadPool() {
    // 核心线程数：CPU核心数 * 2
    int corePoolSize = CPU_CORES * 2;
    // 最大线程数：CPU核心数 * 4，不宜过大
    int maximumPoolSize = CPU_CORES * 4;
    // 空闲线程存活时间：60秒
    long keepAliveTime = 60L;
    // 工作队列：使用LinkedBlockingQueue，设置合适容量
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(3000);
    // 线程工厂：自定义命名，便于问题排查
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger threadNumber = new AtomicInteger(1);
      private final String namePrefix = "elegoo-io-task-";

      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        // 设置为守护线程，避免影响JVM退出
        thread.setDaemon(true);
        // 设置合理的优先级
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
      }
    };
    // 拒绝策略：记录日志并尝试重试，或使用CallerRunsPolicy
    RejectedExecutionHandler handler = new RejectedExecutionHandler() {
      @Override
      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 核心任务可抛出异常或采用其他处理方式
        log.error("Access Elegoo-io-task ThreadPool is busy, task rejected.");
      }
    };

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        TimeUnit.SECONDS,
        workQueue,
        threadFactory,
        handler
    );

    // 允许核心线程超时关闭，在空闲时释放资源
    executor.allowCoreThreadTimeOut(true);

    return executor;
  }

  /**
   * CPU密集型任务线程池（如复杂计算）
   */
  @Bean("cpu-executor")
  public  ExecutorService cpuIntensiveThreadPool() {
    // 核心线程数：CPU核心数 + 1
    int corePoolSize = CPU_CORES + 1;
    // 最大线程数：CPU核心数 * 2
    int maximumPoolSize = CPU_CORES * 2;
    // 工作队列：使用较小的队列，避免任务堆积
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);

    // 其他参数配置类似
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger threadNumber = new AtomicInteger(1);
      private final String namePrefix = "elegoo-cpu-task-";

      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        thread.setDaemon(true);
        // CPU密集型任务可适当提高优先级
        thread.setPriority(Thread.NORM_PRIORITY + 1);
        return thread;
      }
    };

    // 拒绝策略：对于非核心任务，可直接丢弃并记录
    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy() {
      @Override
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        // 记录丢弃日志
        log.error("Access Elegoo-cpu-task ThreadPool is busy, task rejected.");
      }
    };

    return new ThreadPoolExecutor(
        corePoolSize,
        maximumPoolSize,
        60L,
        TimeUnit.SECONDS,
        workQueue,
        threadFactory,
        handler
    );
}
}
