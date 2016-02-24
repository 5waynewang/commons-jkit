package commons.lang.concurrent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import commons.lang.concurrent.queue.TakeCountMCBlockingQueue;

/**
 * 分通道的线程池，并为每个通道维护一个正在该通道working的线程计数（包括等待，执行）
 * 推荐根据每个通道的<pre>queueSize/workingThread</pre>作为权值来arrange每个channel
 * 的线程。
 * 
 * @see com.alipay.common.lang.concurrent.queue.QueueSizeWeightedArranger
 * @author pangu
 * @version $Id: MultiChannelThreadPoolExecutor.java,v 0.1 2010-6-9 下午04:47:12 pangu Exp $
 */
public class MultiChannelThreadPoolExecutor extends ThreadPoolExecutor {

    private final TakeCountMCBlockingQueue<Runnable> queue;

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        int ch = queue.getArranger().arrange(r);// working通道
        queue.getTakeCounter().get(ch).decrementAndGet();// working线程计数
        super.afterExecute(r, t);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        int ch = queue.getArranger().arrange(r);// working通道
        queue.getTakeCounter().get(ch).incrementAndGet();// working线程计数
    }

    public MultiChannelThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit,
                                          TakeCountMCBlockingQueue<Runnable> workQueue,
                                          RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.queue = workQueue;
    }

    public MultiChannelThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit,
                                          TakeCountMCBlockingQueue<Runnable> workQueue,
                                          ThreadFactory threadFactory,
                                          RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.queue = workQueue;
    }

    public MultiChannelThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit,
                                          TakeCountMCBlockingQueue<Runnable> workQueue,
                                          ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.queue = workQueue;
    }

    public MultiChannelThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit,
                                          TakeCountMCBlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.queue = workQueue;
    }

}
