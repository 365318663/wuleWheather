package com.litao.ttweather.tool;


import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
	private static SharedPreferences mSharedPreferences;

	/**
	 * @param context
	 * @param key Ҫȡ�����ݵļ�
	 * @param defValue ȱʡֵ
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(Constants.CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * �洢һ��boolean��������
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(Constants.CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * �洢һ��String���͵�����
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(Constants.CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putString(key, value).commit();
	}
	
	/**
	 * ����keyȡ��һ��String���͵�ֵ
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(Constants.CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getString(key, defValue);
	}

	
	
	/**
	 * �洢һ��Long���͵�����
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putLong(Context context, String key, long value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(Constants.CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putLong(key, value).commit();
	}
	
	/**
	 * ����keyȡ��һ��Long���͵�ֵ
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(Constants.CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getLong(key, defValue);
	}
}
