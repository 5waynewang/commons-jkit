/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2005 All Rights Reserved.
 */
package commons.lang.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可以暂停的线程池执行器。
 * 
 * @author calvin.lil@alibaba-inc.com
 *
 * @version $Id$
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor implements PausableExecutor {
    private boolean       isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition     unpaused  = pauseLock.newCondition();

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param handler
     */
    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                      TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                      RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     */
    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                      TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                      ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     */
    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                      TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                      ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                      TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
     */
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused)
                unpaused.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * 暂停工作
     */
    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * 恢复工作
     */
    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * 目前是否处于暂停状态
     * 
     * @return
     */
    public boolean isPaused() {
        return isPaused;
    }
}
