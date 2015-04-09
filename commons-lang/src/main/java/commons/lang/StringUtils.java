/**
 * 
 */
package commons.lang;


/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:53 AM Nov 25, 2013
 */
public class StringUtils {

	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * <pre>
	 * 判断字符串是否为空，任何一个为空则返回true
	 * StringUtils.isBlank(null) = true
	 * StringUtils.isBlank("a",null) = true
	 * StringUtils.isBlank("a","") = true
	 * StringUtils.isBlank("a"," ") = true
	 * StringUtils.isBlank("a","b") = false
	 * </pre>
	 * 
	 * @param strings
	 * @return
	 */
	public static boolean isBlank(String... strings) {
		if (strings == null || strings.length == 0) {
			return true;
		}
		for (String string : strings) {
			if (org.apache.commons.lang3.StringUtils.isBlank(string)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <pre>
	 * 判断字符串是否相同，只要匹配一个则返回true
	 * StringUtils.equals(null, null) = true
	 * StringUtils.equals(null, "a") = false
	 * StringUtils.equals(null, "a", null) = true
	 * StringUtils.equals("b", "a", null) = false
	 * StringUtils.equals("b", "a", null, "b") = true
	 * </pre>
	 * 
	 * @param dest
	 * @param strings
	 * @return
	 */
	public static boolean equals(String dest, String... strings) {
		if (dest == null && strings == null) {
			return true;
		}
		if (strings == null || strings.length == 0) {
			return false;
		}
		for (String string : strings) {
			if (org.apache.commons.lang3.StringUtils.equals(dest, string)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <pre>
	 * 按顺序替换字符串，null值忽略
	 * StringUtils.replace("abcabca", "a", "") = "bcabca"
	 * StringUtils.replace("abcabca", "a", (String[]) null) = "abcabca"
	 * StringUtils.replace("abcabca", "a", "", "") = "bcbca"
	 * StringUtils.replace("abcabca", "a", "", "", "") = "bcbc"
	 * StringUtils.replace("abcabca", "a", "a", "b", "c") = "abcbbcc"
	 * StringUtils.replace("abcabca", "bc", "a", "b", "c") = "aaaba"
	 * StringUtils.replace("abcabca", "bc", "12", "34", "c") = "a12a34a"
	 * StringUtils.replace("abcabca", "bc", "12", null, "c") = "a12aca"
	 * </pre>
	 * 
	 * @param text
	 * @param searchString
	 * @param replacementList
	 * @return
	 */
	public static String replace(String text, String searchString, String... replacementList) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(text)
				|| org.apache.commons.lang3.StringUtils.isEmpty(searchString) || replacementList == null
				|| replacementList.length == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == INDEX_NOT_FOUND) {
			return text;
		}

		// get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
		int increase = 0;

		// count the replacement text elements that are larger than their corresponding text being replaced
		for (int i = 0; i < replacementList.length; i++) {
			if (replacementList[i] == null) {
				continue;
			}
			int greater = replacementList[i].length() - searchString.length();
			if (greater > 0) {
				increase += 3 * greater; // assume 3 matches
			}
		}
		// have upper-bound at 20% increase, then let Java take over
		increase = Math.min(increase, text.length() / 5);

		StringBuilder buf = new StringBuilder(text.length() + increase);

		int tempIndex = -1;

		for (int i = 0; i < replacementList.length; i++) {
			if (replacementList[i] == null) {
				continue;
			}
			tempIndex = text.indexOf(searchString, start);
			if (tempIndex == -1) {
				break;
			}
			for (int j = start; j < tempIndex; j++) {
				buf.append(text.charAt(j));
			}
			buf.append(replacementList[i]);

			start = tempIndex + searchString.length();
		}

		buf.append(text.substring(start));
		return buf.toString();
	}
}
