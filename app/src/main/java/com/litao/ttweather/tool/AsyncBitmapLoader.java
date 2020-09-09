package com.litao.ttweather.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * @author oneRain
 **/
public class AsyncBitmapLoader
{
	/**
	 * �ڴ�ͼƬ�����û���
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;
	
	public AsyncBitmapLoader()
	{
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public Bitmap loadBitmap(final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack)
	{
		//���ڴ滺���У��򷵻�Bitmap����
		if(imageCache.containsKey(imageURL))
		{
			SoftReference<Bitmap> reference = imageCache.get(imageURL);
			Bitmap bitmap = reference.get();
			if(bitmap != null)
			{
				return bitmap;
			}
		}
		else
		{
			/**
			 * ����һ���Ա��ػ���Ĳ���
			 */
			String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
			File cacheDir = new File("/mnt/sdcard/test/");
			if(!cacheDir.exists()){
				cacheDir.mkdir();
			}
			File[] cacheFiles = cacheDir.listFiles();
			int i = 0;
			for(; i<cacheFiles.length; i++)
			{
				if(bitmapName.equals(cacheFiles[i].getName()))
				{
					break;
				}
			}
			
			if(i < cacheFiles.length)
			{
				return BitmapFactory.decodeFile("/mnt/sdcard/test/" + bitmapName);
			}
		}
		
		final Handler handler = new Handler()
		{
			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);
			}
		};
		
		//��������ڴ滺���У�Ҳ���ڱ��أ���jvm���յ����������߳�����ͼƬ
		new Thread()
		{
			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Bitmap bitmap =null;
				try {
					URL u = new URL(imageURL);
					HttpURLConnection connection = (HttpURLConnection) u.openConnection();
					InputStream  in = new BufferedInputStream(connection.getInputStream());
					 bitmap = BitmapFactory.decodeStream(in);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
				Message msg = handler.obtainMessage(0, bitmap);
				handler.sendMessage(msg);
				
				File dir = new File(Environment.getExternalStorageDirectory()+"/sima/cache/");
				if(!dir.exists())
				{
					dir.mkdir();
				}
				
				File bitmapFile = new File(Environment.getExternalStorageDirectory()+"/sima/cache/"+ 
						imageURL.substring(imageURL.lastIndexOf("/") + 1));
				if(!bitmapFile.exists())
				{
					try
					{
						bitmapFile.createNewFile();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FileOutputStream fos;
				try
				{
					fos = new FileOutputStream(bitmapFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 
							100, fos);
					fos.close();
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		return null;
	}
	
	/**
	 * �ص��ӿ�
	 * @author onerain
	 *
	 */
	public interface ImageCallBack
	{
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}
