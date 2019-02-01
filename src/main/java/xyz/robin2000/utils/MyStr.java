package xyz.robin2000.utils;

public final class MyStr {
	public static String add(Object... str) {

		StringBuilder sb = new StringBuilder();

		for (Object s : str) {
			sb.append(s);
		}

		return sb.toString();
	}
	
	public static StringBuilder build(Object... str) {

		StringBuilder sb = new StringBuilder();

		for (Object s : str) {
			sb.append(s);
		}

		return sb;
	}
}
