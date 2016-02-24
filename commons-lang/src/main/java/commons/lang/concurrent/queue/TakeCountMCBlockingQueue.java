package commons.lang.concurrent.queue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对每个通道正在取值(take/poll)的操作计数. 非blocking的取值计数可忽略
 * 
 * @author pangu
 * @version $Id: TakeCountMCBlockingQueue.java,v 0.1 2010-6-1 上午10:50:06 pangu Exp $
 */
public class TakeCountMCBlockingQueue<T> extends MultiChannelBlockingQueue<T> {

    /**
     * 分通道的taking计数
     */
    private final List<AtomicInteger> takeCounter;

    public TakeCountMCBlockingQueue(int capacity, int channelSize, Arranger<T> arranger,
                                    List<AtomicInteger> counter) {
        super(capacity, channelSize, arranger);
        takeCounter = counter;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        int ch = getArranger().select();
        AtomicInteger w = takeCounter.get(ch);
        w.incrementAndGet();
        try {
            return getChannel(ch).poll(timeout, unit);
        } finally {
            w.decrementAndGet();
        }
    }

    @Override
    public T take() throws InterruptedException {
        T e = null;
        do {
            int ch = getArranger().select();
            AtomicInteger w = takeCounter.get(ch);
            w.incrementAndGet();
            try {
                e = getChannel(ch).poll(TAKE_INTERVAL, TimeUnit.MICROSECONDS);
            } finally {
                w.decrementAndGet();
            }
        } while (e == null);
        return e;
    }

    public List<AtomicInteger> getTakeCounter() {
        return takeCounter;
    }
}
