package com.litao.ttweather.tool;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCache {
	
	public LruCache<String, Bitmap> mMemoryCache;

	public MemoryCache() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 8);

		// ����Ļ����СΪ ����ʱ�ڴ�İ˷�֮һ
		mMemoryCache = new LruCache<String, Bitmap>(maxMemory) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	public void putBitmap(String url, Bitmap bm) {
		mMemoryCache.put(url, bm);
	}
	
	public Bitmap getBitmap(String url) {
		return mMemoryCache.get(url);
	}
}
