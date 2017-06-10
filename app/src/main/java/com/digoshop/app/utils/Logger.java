package com.digoshop.app.utils;

import android.util.Log;


/**
 * ��־�������
 * 
 * @author ̷��ǿ 2011-9-20
 */
public class Logger {

	private final static String TAG = "miaomiao";

	/**
	 * Send an INFO log message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, Object msg) {
		if (AppConfig.IS_PRINT_LOG) {
			Log.i(TAG, tag + " == " + msg);
		}
	}

	/**
	 * Send an ERROR log message.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, Object msg) {
		if (AppConfig.IS_PRINT_LOG) {
			Log.e(TAG, tag + " == " + msg);
		}
	}

	/**
	 * Send a AppConfig.IS_PRINT_LOG log message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, Object msg) {
		if (AppConfig.IS_PRINT_LOG) {
			Log.d(TAG, tag + " == " + msg);
		}
	}

}
