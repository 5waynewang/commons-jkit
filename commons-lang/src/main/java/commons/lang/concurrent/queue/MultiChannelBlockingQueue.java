package commons.lang.concurrent.queue;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 多通道blockingQueue
 * @author pangu
 * @version $Id: MultiChannelBlockingQueue.java,v 0.1 2010-6-9 下午04:24:28 pangu Exp $
 */
public class MultiChannelBlockingQueue<E> extends AbstractMultiChannelQueue<E, BlockingQueue<E>>
                                                                                      implements
                                                                                      BlockingQueue<E> {

    private static final long serialVersionUID = 1L;
    protected static final long TAKE_INTERVAL    = 1000L;

    public MultiChannelBlockingQueue(Collection<? extends E> c, int channelSize, Arranger<E> arranger) {
        super(c, channelSize, arranger);
    }

    public MultiChannelBlockingQueue(int channelSize, Arranger<E> arranger) {
        super(channelSize, arranger);
    }

    public MultiChannelBlockingQueue(int capacity, int channelSize, Arranger<E> arranger) {
        super(capacity, channelSize, arranger);
    }

    @SuppressWarnings("unchecked")
	public BlockingQueue<E> newQueue(int subCapacity) {
        return new LinkedBlockingQueue<E>(subCapacity);
    }

    public boolean offer(E o, long timeout, TimeUnit unit) throws InterruptedException {
        return getChannel(getArranger().arrange(o)).offer(o, timeout, unit);
    }

    public void put(E o) throws InterruptedException {
        getChannel(getArranger().arrange(o)).put(o);
    }

    /**
     * repeatedly <code>poll(TAKE_INTERVAL, TimeUnit.MICROSECONDS)</code>, until
     * retrieving an element or interrupted. the reason not using channelQueue.take(),
     * is to try to rearranging channel to avoid thread tied to an empty channel
     * endlessly
     * 
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting.
     */
    public E take() throws InterruptedException {
        E e = null;
        do {
            e = getChannel(getArranger().select()).poll(TAKE_INTERVAL, TimeUnit.MICROSECONDS);
        } while (e == null);
        return e;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return getChannel(getArranger().select()).poll(timeout, unit);
    }

    /**
     * 从channel 0开始drain
     */
    public int drainTo(Collection<? super E> c) {
        int ret = 0;
        for (BlockingQueue<E> q : getWorkQueues()) {
            ret += q.drainTo(c);
        }
        return ret;
    }

    /**
     * 从channel 0开始drain
     */
    public int drainTo(Collection<? super E> c, int maxElements) {
        int ret = 0;
        for (BlockingQueue<E> q : getWorkQueues()) {
            ret += q.drainTo(c, maxElements - ret);
            if (ret >= maxElements)
                break;
        }
        return ret;
    }

}
