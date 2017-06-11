package com.cheyifu.business.utils;

import android.util.Log;

public class Logger {
	/*
	 * 全局log
	 */
	public static boolean isShowLog = true;

	/**
	 * 
	 * @param objTag
	 *            可以是任意的对象，如果是Class对象，则获取类名为tag，如果是String则直接使用为tag，如果是其它对象，
	 *            则获取Class再获取类�?
	 * @param msg
	 */
	public static void i(Object objTag, String msg) {
		if (!isShowLog && objTag != null) {
			return;
		}

		String tag;

		// 把objTag转换为String
		if (objTag instanceof Class) {
			tag = ((Class) objTag).getSimpleName();
		} else if (objTag instanceof String) {
			tag = (String) objTag;
		} else {
			tag = objTag.getClass().getSimpleName();
		}

		Log.i(tag, msg);
	}

	
	
/*	public static String sHA1(Context context) {
		try {
		PackageInfo info = context.getPackageManager().getPackageInfo(
		context.getPackageName(), PackageManager.GET_SIGNATURES);

		byte[] cert = info.signatures[0].toByteArray();

		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] publicKey = md.digest(cert);
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < publicKey.length; i++) {
		String appendString = Integer.toHexString(0xFF & publicKey[i])
		.toUpperCase(Locale.US);
		if (appendString.length() == 1)
		hexString.append("0");
		hexString.append(appendString);
		hexString.append(":");
		}
		return hexString.toString();
		} catch (NameNotFoundException e) {
		e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		return null;
		}*/
}
