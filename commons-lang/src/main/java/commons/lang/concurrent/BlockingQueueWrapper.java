/**
 * 
 */
package commons.lang.concurrent;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:08:41 PM Jul 16, 2014
 */
public class BlockingQueueWrapper<E> extends AbstractQueue<E> implements java.io.Serializable {

	private static final long serialVersionUID = 8374311375832953601L;
	private final Queue<E> queue;
	final ReentrantLock lock;
	private final Condition notEmpty;
	private final Condition notFull;
	private int count;

	public BlockingQueueWrapper(Queue<E> queue) {
		this.queue = queue;
		this.count = queue.size();
		this.lock = new ReentrantLock();
		this.notEmpty = lock.newCondition();
		this.notFull = lock.newCondition();
	}

	private boolean insert(E x) {
		boolean r = queue.add(x);
		++count;
		notEmpty.signal();
		return r;
	}

	private E extract() {
		E x = queue.poll();
		--count;
		notFull.signal();
		return x;
	}

	public E take() throws InterruptedException {
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try {
			while (count == 0)
				notEmpty.await();
			return extract();
		}
		finally {
			lock.unlock();
		}
	}

	public E poll() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			return (count == 0) ? null : extract();
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public E peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(E e) {
		if (e == null) throw new NullPointerException();
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			return insert(e);
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public int size() {
		return this.count;
	}
}
