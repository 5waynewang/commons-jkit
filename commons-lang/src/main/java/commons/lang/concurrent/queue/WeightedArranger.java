package commons.lang.concurrent.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 根据channel的权值分配channel，选择首个最大权值的channel
 * 
 * @author pangu
 * @version $Id: WeightedArranger.java,v 0.1 2010-6-1 上午09:45:22 pangu Exp $
 */
public class WeightedArranger<E> implements MultiChannelQueue.Arranger<E> {

    protected final List<AtomicInteger> weights = new ArrayList<AtomicInteger>();
    protected MultiChannelQueue<E>      queue;
    private final Random                rand    = new Random();

    /**
     * 随机
     */
    public int arrange(E o) {
        return rand.nextInt(queue.getChannelSize());
    }

    /**
     * select a channel with first max weight
     */
    public int select() {
        int ch = -1, idx = 0;
        double maxWeight = Double.NEGATIVE_INFINITY;
        for (AtomicInteger w : weights) {// find max weight channel
            double curWeight = calcWeight(w, idx);
            if (curWeight > maxWeight) {
                maxWeight = curWeight;
                ch = idx;
            }
            idx++;
        }
        return ch;
    }

    /**
     * not thread safe
     */
    public void setQueue(MultiChannelQueue<E> queue) {
        this.queue = queue;
        weights.clear();
        for (int i = weights.size(); i < this.queue.getChannelSize(); i++) {
            weights.add(initWeight());
        }
    }

    public void clearWeight() {
        for (int i = 0; i < weights.size(); i++) {
            weights.set(i, initWeight());
        }
    }

    public List<AtomicInteger> getWeights() {
        return weights;
    }

    @Override
    public String toString() {
        return "weights=" + weights;
    }

    protected AtomicInteger initWeight() {
        return new AtomicInteger();
    }

    protected double calcWeight(AtomicInteger w, int channel) {
        return w.doubleValue();
    }
}
