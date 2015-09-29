/**
 * 
 */
package commons.lang.quickbean;

import java.io.Serializable;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 1:27:44 PM Nov 16, 2014
 */
public class ResponseObject<T> implements Serializable {

	private static final long serialVersionUID = -906477148460366748L;
	private int code;
	private String msg; // 消息内容
	private T result;

	public ResponseObject() {
		this(200, null, null);
	}

	public ResponseObject(T result) {
		this(200, null, result);
	}

	public ResponseObject(int code, String msg) {
		this(code, msg, null);
	}

	public ResponseObject(int code, String msg, T result) {
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
	public boolean success() {
		return this.code == 200;
	}
}
