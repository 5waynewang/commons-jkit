package commons.lang.concurrent.queue;

import java.util.List;
import java.util.Queue;

public interface MultiChannelQueue<E> extends Queue<E> {

    /**
     * @return channel size
     */
    int getChannelSize();

    /**
     * @return queue capacity
     */
    int getCapacity();

    /**
     * @return list of queue channels
     */
    List<? extends Queue<E>> getWorkQueues();

    /**
     * @return the arranger
     */
    Arranger<E> getArranger();

    /**
     * method to create new queue
     */
    <T extends Queue<E>> T newQueue(int subCapacity);

    /**
     * 安排channel的调度器，负责元素进入的channel，以及弹出元素的channel
     * 
     * @author pangu
     * @version $Id: MultiChannelQueue.java,v 0.1 2010-5-31 下午09:35:33 pangu Exp $
     */
    public interface Arranger<E> {

        /**
         * arrange the element to a channel, must be <b>idempotent</b>
         * 
         * @param o
         * @return channel no.
         */
        int arrange(E o);

        /**
         * select a channel for operation.
         * 
         * @return channel no.
         */
        int select();

        /**
         * @param queue the queue been arranged
         */
        void setQueue(MultiChannelQueue<E> queue);
    }
}
