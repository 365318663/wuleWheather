package com.litao.ttweather.tool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * @author andong
 * ���绺����
 */

public class NetCache {

	public static final int SUCCESS = 0;
	public static final int FAILED = 1;
//	private Handler mHandler; 
	private MemoryCache mMemoryCache; // �ڴ滺�����
	private ExecutorService mExecutorService; // �̳߳ض���
    private ImageView iv;//��Ҫ����ͼƬ��view����
    
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.LOAD_IMAGE_SUCCESS:// ����ͼƬ�ɹ�
				Bitmap bitmap = (Bitmap) msg.obj;
				iv.setImageBitmap(bitmap);
				break;

			case Constants.LOAD_IMAGE_FAILED:// ����ͼƬʧ��

				break;
			}

		};
	};
	
	public NetCache(ImageView imageView, MemoryCache memoryCache) {
		this.iv=imageView;
		this.mMemoryCache = memoryCache;
		
		// ����һ���ڲ���5���̵߳��̳߳�
		mExecutorService = Executors.newFixedThreadPool(2);
	}
	
	/**
	 * ��ȡͼƬ��������
	 * @param url
	 */
	public void getBitmapFromNet(String url) {
//		new Thread(new InternalRunnable(url, tag)).start();
		mExecutorService.execute(new InternalRunnable(url));
	}
	
	class InternalRunnable implements Runnable {
		
		private String url; // ��ǰ������Ҫ����������ַ
		//private int tag; // ��ǰ��������ͼƬ�ı�ʶ
		
		public InternalRunnable(String url) {
			this.url = url;
			
		}

		@Override
		public void run() {
			// ��������, ץȡͼƬ
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.connect();
				int responseCode = conn.getResponseCode();
				if(responseCode == 200) {
					InputStream is = conn.getInputStream();
					// ����ת����ͼƬ
					Bitmap bm = BitmapFactory.decodeStream(is);
					
					Message msg = handler.obtainMessage();
					msg.obj = bm;
				//	msg.arg1 = tag;
					msg.what = SUCCESS;
					msg.sendToTarget();
					
					// ���ڴ��һ��  ����ǰ�� һ����ŵ��ڴ���ȥ
					mMemoryCache.putBitmap(url, bm);
					
					// �򱾵ش�һ��
					LocalCache.putBitmap(url, bm);
					
					
					
					
					
					
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(conn != null) {
					conn.disconnect(); // �Ͽ�����
				}
			}
			handler.obtainMessage(FAILED).sendToTarget();
		}
	}
}
