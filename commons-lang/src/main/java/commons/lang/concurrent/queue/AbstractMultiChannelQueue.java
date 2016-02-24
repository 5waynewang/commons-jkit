package commons.lang.concurrent.queue;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * 多个queues构成一个多通道的总queue。
 * 
 * @param E element type
 * @param Q concrete queue type
 * @author pangu
 * @version $Id: AbstractMultiChannelQueue.java,v 0.1 2010-5-31 下午05:12:35 pangu Exp $
 */
public abstract class AbstractMultiChannelQueue<E, Q extends Queue<E>> extends AbstractQueue<E>
                                                                                               implements
                                                                                               MultiChannelQueue<E>,
                                                                                               java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Q>     workQueues;
    private final int         channelSize;
    private final int         capacity;
    private Arranger<E>       arranger;

    public AbstractMultiChannelQueue(int channelSize, Arranger<E> arranger) {
        this(Integer.MAX_VALUE, channelSize, arranger);
    }

    public AbstractMultiChannelQueue(int capacity, int channelSize, Arranger<E> arranger) {
        if (arranger == null)
            throw new NullPointerException("arranger is null");
        if (channelSize < 1 || capacity < 1)
            throw new IllegalArgumentException("[capacity=" + capacity + ", channelSize="
                                               + channelSize + "] must >0.");

        this.channelSize = channelSize;
        this.capacity = capacity;

        workQueues = new ArrayList<Q>(channelSize);
        int subCapacity = capacity / channelSize + 1;
        int subMod = capacity % channelSize;
        for (int i = 0; i < channelSize; i++, subMod--) {
            if (subMod == 0)
                subCapacity--;
            workQueues.add(newQueue(subCapacity));
        }
        setArranger(arranger);
    }

    public AbstractMultiChannelQueue(Collection<? extends E> c, int channelSize,
                                     Arranger<E> arranger) {
        this(channelSize, arranger);
        for (E e : c)
            add(e);
    }

    public boolean offer(E o) {
        return getChannel(arranger.arrange(o)).offer(o);
    }

    public E poll() {
        return getChannel(arranger.select()).poll();
    }

    public E peek() {
        return getChannel(arranger.select()).peek();
    }

    /**
     * combination of channel iterators
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            List<Iterator<E>> iterators;
            Iterator<E>       curItr;
            int               idx = 1;
            {
                iterators = new ArrayList<Iterator<E>>(workQueues.size());
                for (Q q : workQueues) {
                    iterators.add(q.iterator());
                }
                if (!iterators.isEmpty())
                    curItr = iterators.get(0);
            }

            public boolean hasNext() {
                if (curItr == null)
                    return false;
                boolean ret;
                do {
                    ret = curItr.hasNext();
                    if (ret)
                        return ret;
                    if (idx < iterators.size())
                        curItr = iterators.get(idx++);
                    else
                        curItr = null;
                } while (!ret && curItr != null);
                return false;
            }

            public E next() {
                return curItr.next();
            }

            public void remove() {
                curItr.remove();
            }
        };
    }

    /**
     * 数值不太准确
     */
    public int remainingCapacity() {
        return capacity - size();
    }

    /**
     * 数值不太准确
     */
    @Override
    public int size() {
        int ret = 0;
        for (Q q : workQueues) {
            ret += q.size();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "queues={" + workQueues + "}, arranger={" + arranger + "}";
    }

    public abstract Q newQueue(int subCapacity);

    // ----------------------------------------------------------
    protected Q getChannel(int ch) {
        return workQueues.get(ch);
    }

    // ----------------------------------------------------------
    public Arranger<E> getArranger() {
        return arranger;
    }

    public void setArranger(Arranger<E> arranger) {
        this.arranger = arranger;
        this.arranger.setQueue(this);
    }

    public List<Q> getWorkQueues() {
        return workQueues;
    }

    public int getChannelSize() {
        return channelSize;
    }

    public int getCapacity() {
        return capacity;
    }
}
