package com.litao.ttweather.tool;



import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageUtils {
		
	/**
	 * ����url��ȡͼƬ
	 * @param url
	 * @return
	 */
	public static void getImageFromUrl(String url,ImageView imageView) {		
		
		// 1. ȥ�ڴ���ȡ, ȡ����֮��ֱ�ӷ���.
		// �����ڴ滺�����
		MemoryCache mMemoryCache = new MemoryCache();		
		Bitmap bm = mMemoryCache.getBitmap(url);
		if(bm != null) {
			imageView.setImageBitmap(bm);
			return;
		}
		
		// 2. ȥ������ȡ, ȡ����֮��ֱ�ӷ���.
		bm = LocalCache.getBitmap(url);
		if(bm != null) {
			imageView.setImageBitmap(bm);
			return;
		}

		// 3. ȥ������ȡ, �������߳��첽ץȡ, ����ֱ�ӷ���. ��ץȡ��Ϻ�, �õ�ͼƬ, ʹ��handler������Ϣ����������.
		
		// �������绺�����.
		NetCache mNetCache = new NetCache(imageView, mMemoryCache);
		mNetCache.getBitmapFromNet(url);
	
	}
	
}
