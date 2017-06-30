package com.virtuzoconsultancyservicespvtltd.ahprepaid.utils;


public class StringUtils {
	public static boolean isNotBlank(String str) {
		if (str == null || str.trim().equalsIgnoreCase(""))
			return false;
		return true;
	}

	public static boolean isBlank(String str) {
		if (str == null || str.trim().equalsIgnoreCase(""))
			return true;
		return false;
	}

	public static String getReverseString(String msg) {
		if (msg.equalsIgnoreCase("0")) {
			return "1";
		} else if (msg.equalsIgnoreCase("1")) {
			return "0";
		} else if (msg.equalsIgnoreCase("")) {
			return "1";
		} else {
			return "";
		}
	}

	public static String getColoredString(String text, int color) {
		String str = "";
		str = "<font color='" +color+ "'>" + text + "</font>";
		return str;
	}
	

}
