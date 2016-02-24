package commons.lang.concurrent.queue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通道配置规则为：每个通道weight与队列长度正比的值作为新的weight
 * 
 * @author pangu
 * @version $Id: QueueSizeWeightedArranger.java,v 0.1 2010-6-1 上午10:24:00 pangu Exp $
 */
public class QueueSizeWeightedArranger<E> extends WeightedArranger<E> {

    /**
     * 队列长度与weight的比值越大，权值越高。
     */
    @Override
    protected double calcWeight(AtomicInteger weight, int channel) {
        double workingThread = weight.doubleValue();
        if (workingThread == 0)
         // 近似+0，但不等于0，以体现queuSize的不同
            workingThread = 1e-123;
        int queueSize = queue.getWorkQueues().get(channel).size();
        return queueSize / workingThread;
    }

}
