/**
 * 
 */
package commons.cache;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class TestedModel implements Serializable {
	private static final long serialVersionUID = -2021277341069371379L;
	private Long lo;
	private long l;
//	private String str;
	private Date d;
	private Timestamp t;
	private byte[] bs;
	private List<String> strList;
	private Map<String, String> strMap;
	private List<TestedModel2> testedModel2List;
	private Boolean bool;
	private BigDecimal bd;

	public BigDecimal getBd() {
		return bd;
	}

	public void setBd(BigDecimal bd) {
		this.bd = bd;
	}

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}

	public List<TestedModel2> getTestedModel2List() {
		return testedModel2List;
	}

	public void setTestedModel2List(List<TestedModel2> testedModel2List) {
		this.testedModel2List = testedModel2List;
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

//	public String getStr() {
//		return str;
//	}
//
//	public void setStr(String str) {
//		this.str = str;
//	}

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
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
		TestedModel that = (TestedModel) obj;
		if (!NumberUtils.eq(that.getLo(), this.getLo())) {
			return false;
		}
		if (!NumberUtils.eq(that.getL(), this.getL())) {
			return false;
		}
//		if (!StringUtils.equals(that.getStr(), this.getStr())) {
//			return false;
//		}
		if ((that.getD() == null && this.getD() != null)
				|| (that.getD() != null && this.getD() == null)
				|| (that.getD() != null && that.getD().getTime() != this.getD().getTime())) {
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
		
		if ((that.getTestedModel2List() == null && this.getTestedModel2List() != null)
				|| (that.getTestedModel2List() != null && this.getTestedModel2List() == null)
				|| (that.getTestedModel2List() != null && that.getTestedModel2List().size() != this.getTestedModel2List().size())) {
			return false;
		}
		if (that.getTestedModel2List() != null && this.getTestedModel2List() != null) {
			for (int i = 0; i < that.getTestedModel2List().size(); i++) {
				if (!that.getTestedModel2List().get(i).equals(this.getTestedModel2List().get(i))) {
					return false;
				}
			}
		}
		
		if ((that.getBool() == null && this.getBool() != null)
				|| (that.getBool() != null && this.getBool() == null)
				|| (that.getBool() != null && that.getBool().booleanValue() != this.getBool().booleanValue())) {
			return false;
		}
		
		if ((that.getBd() == null && this.getBd() != null)
				|| (that.getBd() != null && this.getBd() == null)
				|| (that.getBd() != null && that.getBd().compareTo(this.getBd()) != 0)) {
			return false;
		}

		return true;
	}
	
	public static TestedModel createObject() {
		final TestedModel result = new TestedModel();
		result.setLo(ThreadLocalRandom.current().nextLong());
		result.setL(ThreadLocalRandom.current().nextLong());
//		result.setStr(String.valueOf(ThreadLocalRandom.current().nextLong()));
		result.setD(new Date());
		result.setT(new Timestamp(System.currentTimeMillis()));
		result.setBs(new byte[] { 1, 2, 3, 4, 5 });
		result.setBd(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()));
//		result.setBd(BigDecimal.valueOf(ThreadLocalRandom.current().nextInt()));
		result.setStrList(new ArrayList<String>());
		result.getStrList().add("a");
		result.getStrList().add("b");
		result.getStrList().add("c");
		result.setStrMap(new HashMap<String, String>());
		result.getStrMap().put("a", "1");
		result.getStrMap().put("b", "2");
		result.getStrMap().put("c", "3");
		
		result.setTestedModel2List(new ArrayList<TestedModel2>());
		result.getTestedModel2List().add(TestedModel2.createObject());
		result.getTestedModel2List().add(TestedModel2.createObject());
		result.getTestedModel2List().add(TestedModel2.createObject());

		return result;
	}
}
