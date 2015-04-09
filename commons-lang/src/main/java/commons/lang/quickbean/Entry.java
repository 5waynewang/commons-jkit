/**
 * 
 */
package commons.lang.quickbean;

import java.io.Serializable;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:25:33 AM Nov 25, 2013
 */
public class Entry<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;

	private K key;
	private V value;

	public Entry() {
	}

	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
