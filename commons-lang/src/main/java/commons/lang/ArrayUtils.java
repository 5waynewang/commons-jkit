/**
 * 
 */
package commons.lang;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:22:04 AM Nov 25, 2013
 */
public class ArrayUtils {
	/**
	 * @see #toByteObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static byte[] toByteArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toByteObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Byte[]，调用{@link Byte#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Byte[] toByteObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY;
		}

		final Byte[] results = new Byte[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Byte.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * @see #toShortObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static short[] toShortArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toShortObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Short[]，调用{@link Short#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Short[] toShortObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY;
		}

		final Short[] results = new Short[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Short.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * @see #toIntegerObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static int[] toIntArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toIntegerObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Integer[]，调用{@link Integer#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Integer[] toIntegerObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
		}

		final Integer[] results = new Integer[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Integer.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * @see #toLongObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static long[] toLongArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toLongObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Long[]，调用{@link Long#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Long[] toLongObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;
		}

		final Long[] results = new Long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Long.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * @see #toFloatObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static float[] toFloatArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toFloatObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Float[]，调用{@link Float#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Float[] toFloatObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY;
		}

		final Float[] results = new Float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Float.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * @see #toDoubleObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static double[] toDoubleArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toDoubleObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Double[]，调用{@link Double#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Double[] toDoubleObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY;
		}

		final Double[] results = new Double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Double.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * @see #toCharacterObjectArray(String[])
	 * @param arr
	 * @return
	 */
	public static char[] toCharArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toCharacterObjectArray(arr));
	}

	/**
	 * 数组转换，转换成Character[]，调用{@link Character#valueOf()}
	 * @param arr
	 * @return
	 */
	public static Character[] toCharacterObjectArray(String[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY;
		}

		final Character[] results = new Character[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = Character.valueOf(arr[i].charAt(0));
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link String#valueOf()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(byte[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = String.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link Object#toString()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Byte[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = arr[i].toString();
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link String#valueOf()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(short[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = String.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link Object#toString()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Short[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = arr[i].toString();
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link String#valueOf()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = String.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link Object#toString()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Integer[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = arr[i].toString();
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link String#valueOf()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(long[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = String.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link Object#toString()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Long[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = arr[i].toString();
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link String#valueOf()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(float[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = String.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link Object#toString()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Float[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = arr[i].toString();
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link String#valueOf()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(double[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = String.valueOf(arr[i]);
		}
		return results;
	}

	/**
	 * 数组转换，转换成String[]，调用{@link Object#toString()}
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Double[] arr) {
		if (arr == null) {
			return null;
		}
		else if (arr.length == 0) {
			return org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
		}

		final String[] results = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			results[i] = arr[i].toString();
		}
		return results;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(byte[] arr) {
		long result = 0;
		for (int e : arr) {
			result += e;
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(Byte[] arr) {
		return plusForLong(arr);
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(short[] arr) {
		long result = 0;
		for (short e : arr) {
			result += e;
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(Short[] arr) {
		return plusForLong(arr);
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(int[] arr) {
		long result = 0;
		for (int e : arr) {
			result += e;
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(Integer[] arr) {
		return plusForLong(arr);
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(long[] arr) {
		long result = 0;
		for (long e : arr) {
			result += e;
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plus(Long[] arr) {
		return plusForLong(arr);
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static long plusForLong(Number[] arr) {
		long result = 0;
		for (Number e : arr) {
			if (e != null) {
				result += e.longValue();
			}
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static double plus(float[] arr) {
		double result = 0;
		for (float e : arr) {
			result += e;
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static double plus(Float[] arr) {
		return plusForDouble(arr);
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static double plus(double[] arr) {
		double result = 0;
		for (double e : arr) {
			result += e;
		}
		return result;
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static double plus(Double[] arr) {
		return plusForDouble(arr);
	}

	/**
	 * 数组求和
	 * @param arr
	 * @return
	 */
	public static double plusForDouble(Number[] arr) {
		double result = 0;
		for (Number e : arr) {
			if (e != null) {
				result += e.doubleValue();
			}
		}
		return result;
	}
	
	/**
	 * 是否存在null值
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean containsNullValue(Object[] objects) {
		if (objects == null) {
			return true;
		}
		for (Object object : objects) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}
}
