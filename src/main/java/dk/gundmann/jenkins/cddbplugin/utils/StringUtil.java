package dk.gundmann.jenkins.cddbplugin.utils;

public class StringUtil {

	public static final String EMPTY = "";
	public static final String NULL = null;

	public static boolean isEmpty(String value) {
		return value == NULL || value.equals(EMPTY);
	}

	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
	
}
