package com.litao.ttweather.tool;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetUtil {
	
	private static ConnectivityManager cm;

	/**
	 * ȷ����һ���������������ڶ��̻߳��߲����������
	 * @param context
	 * @return
	 */
	public static ConnectivityManager getConnectivityManager(Context context) {
		if (cm == null) {
			synchronized (NetUtil.class) {
				if (cm == null) {
					cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				}
			}
		}
		return cm;
	}
}
