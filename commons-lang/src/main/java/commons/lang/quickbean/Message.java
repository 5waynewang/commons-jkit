package commons.lang.quickbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commons.lang.JsonUtils;

/**
 * 自定义消息
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:25:43 AM Nov 25, 2013
 */
public class Message implements Serializable {

	private static final long serialVersionUID = -8915237759248999534L;
	public static final int NO_MESSAGE = 0;// 无消息
	public static final int NOTE_MESSAGE = 1;// 提示消息
	public static final int CONFIRM_MESSAGE = 2;// 确认消息
	public static final int ERROR_MESSAGE = 3;// 错误消息

	private int type; // 消息类型
	private String msg; // 消息内容
	private boolean success = true;
	private boolean confirmed; // 是否已经确认 true 已确认 false 未确认

	private Map<String, Object> attributes = new HashMap<String, Object>();

	public String toJSONString() {
		JSONObject o = (JSONObject) JSONObject.toJSON(this);
		o.put("pass", this.pass());
		return JSON.toJSONString(o, JsonUtils.defaultSerializeConfig());
	}

	/**
	 * 获取默认消息
	 * 
	 * @return
	 */
	public static Message getDefaultMessage() {
		Message message = new Message();
		message.setType(NO_MESSAGE);
		return message;
	}

	/**
	 * 获取默认消息
	 * 
	 * @return
	 */
	public static String getDefaultMessageString() {
		return getDefaultMessage().toJSONString();
	}

	/**
	 * 获取提示消息
	 * 
	 * @param msg
	 * @return
	 */
	public static Message getNoteMessage(String msg) {
		Message message = new Message();
		message.setType(NOTE_MESSAGE);
		message.setMsg(msg);
		return message;
	}

	/**
	 * 获取提示消息
	 * 
	 * @param msg
	 * @return
	 */
	public static String getNoteMessageString(String msg) {
		return getNoteMessage(msg).toJSONString();
	}

	/**
	 * 获取确认消息
	 * 
	 * @param msg
	 * @return
	 */
	public static Message getConfirmMessage(String msg) {
		Message message = new Message();
		message.setType(CONFIRM_MESSAGE);
		message.setMsg(msg);
		message.setSuccess(true);
		return message;
	}

	/**
	 * 获取确认消息
	 * 
	 * @param msg
	 * @return
	 */
	public static String getConfirmMessageString(String msg) {
		return getConfirmMessage(msg).toJSONString();
	}

	/**
	 * 获取错误消息
	 * 
	 * @param msg
	 * @return
	 */
	public static Message getErrorMessage(String msg) {
		Message message = new Message();
		message.setType(ERROR_MESSAGE);
		message.setMsg(msg);
		return message;
	}

	/**
	 * 获取错误消息
	 * 
	 * @param msg
	 * @return
	 */
	public static String getErrorMessageString(String msg) {
		return getErrorMessage(msg).toJSONString();
	}

	/**
	 * 消息是否已被确认
	 * 
	 * @return
	 */
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 拼接消息内容
	 * 
	 * @param msg
	 */
	public void contactMsg(String msg) {
		this.msg = contactMsg(this.msg, msg);
	}

	/**
	 * 拼接消息内容
	 * 
	 * @param msg0
	 * @param msg1
	 * @return
	 */
	public static String contactMsg(String msg0, String msg1) {
		return contactMsg(msg0, msg1, "<br>");
	}

	/**
	 * 拼接消息内容
	 * 
	 * @param msg0
	 * @param msg1
	 * @param separator
	 *            拼接符
	 * @return
	 */
	public static String contactMsg(String msg0, String msg1, String separator) {
		if (StringUtils.isBlank(msg0)) {
			return msg1;
		}
		StringBuilder result = new StringBuilder(msg0);
		if (StringUtils.isNotBlank(msg1)) {
			if (msg0.endsWith(separator)) {

			} else {
				result.append(separator);
			}
			result.append(msg1);
		}
		return result.toString();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void putAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	public Message attribute(String key, Object value) {
		this.putAttribute(key, value);
		return this;
	}

	/**
	 * 消息是否不通过
	 * 
	 * @return
	 */
	public boolean notpass() {
		return ERROR_MESSAGE == type || (CONFIRM_MESSAGE == type && !confirmed);
	}

	/**
	 * 消息是否通过
	 * 
	 * @return
	 */
	public boolean pass() {
		return !notpass();
	}

	@Override
	public String toString() {
		return toJSONString();
	}
}
