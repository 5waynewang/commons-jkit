/**
 * 
 */
package commons.cache;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

import commons.lang.NumberUtils;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:00:29 AM Jul 10, 2015
 */
public class TestedModel2 implements Serializable {
	private static final long serialVersionUID = -2021277341069371379L;
	private Long lo;
	private long l;
	private String s;
	private Date date;
	private Timestamp t;
	private byte[] bs;
	private List<String> strList;
	private Map<String, String> strMap;
	private Boolean bool;

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}

	public Long getLo() {
		return lo;
	}

	public void setLo(Long lo) {
		this.lo = lo;
	}

	public long getL() {
		return l;
	}

	public void setL(long l) {
		this.l = l;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Timestamp getT() {
		return t;
	}

	public void setT(Timestamp t) {
		this.t = t;
	}

	public byte[] getBs() {
		return bs;
	}

	public void setBs(byte[] bs) {
		this.bs = bs;
	}

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public Map<String, String> getStrMap() {
		return strMap;
	}

	public void setStrMap(Map<String, String> strMap) {
		this.strMap = strMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		TestedModel2 that = (TestedModel2) obj;
		if (!NumberUtils.eq(that.getLo(), this.getLo())) {
			return false;
		}
		if (!NumberUtils.eq(that.getL(), this.getL())) {
			return false;
		}
		if (!StringUtils.equals(that.getS(), this.getS())) {
			return false;
		}
		if ((that.getDate() == null && this.getDate() != null)
				|| (that.getDate() != null && this.getDate() == null)
				|| (that.getDate() != null && that.getDate().getTime() != this.getDate().getTime())) {
			return false;
		}
		if ((that.getT() == null && this.getT() != null)
				|| (that.getT() != null && this.getT() == null)
				|| (that.getT() != null && that.getT().getTime() != this.getT().getTime())) {
			return false;
		}
		if ((that.getBs() == null && this.getBs() != null)
				|| (that.getBs() != null && this.getBs() == null)
				|| (that.getBs() != null && that.getBs().length != this.getBs().length)) {
			return false;
		}
		if (that.getBs() != null && this.getBs() != null) {
			for (int i = 0; i < that.getBs().length; i++) {
				if (that.getBs()[i] != this.getBs()[i]) {
					return false;
				}
			}
		}
		
		if ((that.getStrList() == null && this.getStrList() != null)
				|| (that.getStrList() != null && this.getStrList() == null)
				|| (that.getStrList() != null && that.getStrList().size() != this.getStrList().size())) {
			return false;
		}
		if (that.getStrList() != null && this.getStrList() != null) {
			for (int i = 0; i < that.getStrList().size(); i++) {
				if (!that.getStrList().get(i).equals(this.getStrList().get(i))) {
					return false;
				}
			}
		}
		
		if ((that.getStrMap() == null && this.getStrMap() != null)
				|| (that.getStrMap() != null && this.getStrMap() == null)
				|| (that.getStrMap() != null && that.getStrMap().size() != this.getStrMap().size())) {
			return false;
		}
		if (that.getStrMap() != null && this.getStrMap() != null) {
			for (Map.Entry<String, String> entry : that.getStrMap().entrySet()) {
				if (!StringUtils.equals(entry.getValue(), this.getStrMap().get(entry.getKey()))) {
					return false;
				}
			}
		}
		
		if ((that.getBool() == null && this.getBool() != null)
				|| (that.getBool() != null && this.getBool() == null)
				|| (that.getBool() != null && that.getBool().booleanValue() != this.getBool().booleanValue())) {
			return false;
		}

		return true;
	}

	public static TestedModel2 createObject() {
		final TestedModel2 result = new TestedModel2();
		result.setLo(ThreadLocalRandom.current().nextLong());
		result.setL(ThreadLocalRandom.current().nextLong());
		result.setS(String.valueOf(ThreadLocalRandom.current().nextLong()));
		result.setDate(new Date());
		result.setT(new Timestamp(System.currentTimeMillis()));
		result.setBs(new byte[] { 1, 2, 3, 4, 5 });
		result.setStrList(new ArrayList<String>());
		result.getStrList().add("a");
		result.getStrList().add("b");
		result.getStrList().add("c");
		result.setStrMap(new HashMap<String, String>());
		result.getStrMap().put("a", "1");
		result.getStrMap().put("b", "2");
		result.getStrMap().put("c", "3");

		return result;
	}
}
