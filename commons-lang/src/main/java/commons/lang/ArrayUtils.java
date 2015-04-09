/**
 * 
 */
package commons.lang;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:22:04 AM Nov 25, 2013
 */
public class ArrayUtils {

	public static byte[] toByteArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toByteObjectArray(arr));
	}

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

	public static short[] toShortArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toShortObjectArray(arr));
	}

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

	public static int[] toIntArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toIntegerObjectArray(arr));
	}

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

	public static long[] toLongArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toLongObjectArray(arr));
	}

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

	public static float[] toFloatArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toFloatObjectArray(arr));
	}

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

	public static double[] toDoubleArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toDoubleObjectArray(arr));
	}

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

	public static char[] toCharArray(String[] arr) {
		return org.apache.commons.lang3.ArrayUtils.toPrimitive(toCharacterObjectArray(arr));
	}

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
}
