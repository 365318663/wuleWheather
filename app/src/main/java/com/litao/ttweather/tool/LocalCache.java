package com.litao.ttweather.tool;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class LocalCache {
	
	private static final File CACHEDIR = new File(Constants.CACHE_DIR+"/images/");

	/**
	 * ����url, �򱾵ش洢һ��ͼƬ
	 * @param url
	 * @param bm
	 */
	public static void putBitmap(String url, Bitmap bm) {
		try {
			String fileName = MD5Encoder.encode(url);
			if(!CACHEDIR.exists()) {
				CACHEDIR.mkdir();
			}
			File cacheFile = new File(CACHEDIR, fileName);
			FileOutputStream fos = new FileOutputStream(cacheFile);
			bm.compress(CompressFormat.JPEG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����url�ӱ��ػ�����ȡ��ͼƬ
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmap(String url) {
		try {
			String fileName = MD5Encoder.encode(url);
			File cacheFile = new File(CACHEDIR, fileName);
			if(cacheFile.exists()) {
				return BitmapFactory.decodeFile(cacheFile.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
