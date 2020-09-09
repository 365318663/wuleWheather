/**   
* @{#} AsyncImageLoaderLocal.java Create on 2013-8-7 PM 4:04:44   
* @author tuxiaohui
* Copyright (c) 2013 by loongjoy.
* load http image and save to local sd card
*/
package com.litao.ttweather.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class HttpImageLoader {
	public static final String IMAGE_TYPE_OF_PNG = "png";
	public static final String IMAGE_TYPE_OF_JPEG = "jpg";
	
	public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	public static final String APP_DIR_NAME = ".wuye";
	private static ExecutorService executorService;
	private final Handler handler = new Handler();
	private static HttpImageLoader instance;
	public static HttpImageLoader getInstance() {
		if (instance == null) {
			instance = new HttpImageLoader();
		}
		if(executorService == null){
			executorService = Executors.newFixedThreadPool(10);
		}
		return instance;
	}
	/**
	 * clear the imageCache
	 */
	public void clear(){
		for (Entry<String, SoftReference<Drawable>> entry : imageCache.entrySet()) {       
			Drawable drawable = entry.getValue().get();
			if(drawable!=null)
				drawable.setCallback(null);
		} 
		imageCache.clear();
		System.gc();
	}
	
	public void shutDown(){
		if(executorService != null)
			executorService.shutdownNow();
		executorService = null;
	}
	
	private List<String> imageUrlLoadTask = new ArrayList<String>();
	public boolean containsUrl(String url){
		return imageUrlLoadTask.contains(url);
	}
	
	/**
	 * @param imageUrl
	 * @param callback
	 * @return
	 */
	public Drawable loadSoftDrawable(final Map<String, SoftReference<Drawable>> imageCache,int maxSize,final String imageUrl,final ImageCallback callback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		if(imageCache.size() > maxSize){
			clear();
		}
		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawable(imageUrl); 
					if(drawable != null){
						imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					}
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}
	
	/**
	 * 
	 * @param imageUrl
	 * @param callback
	 * @return Drawable
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback callback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		/*
		 * fix the null point error for null imageCache and Service
		 * update by xiaohui_tu 2014-1-14 13:57 
		 */
		if(executorService != null){
			executorService.submit(new Runnable() {
				public void run() {
					try {
						final Drawable drawable = fetchDrawable(imageUrl); 
						if(drawable != null){
							imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
						}
						handler.post(new Runnable() {
							public void run() {
								callback.imageLoaded(drawable,imageUrl);
								imageUrlLoadTask.remove(imageUrl);
							}
						});
					} catch (Exception e) {
						throw new RuntimeException(e);

					}
				}
			});
		}

		return null;
	}
	
	public Drawable loadDrawableAppointType(final String imageUrl,
			final ImageCallback callback, final String imgType) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);

		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawableAppointType(imageUrl, imgType); 
					if(drawable != null){
						imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					}
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);

				}
			}
		});

		return null;
	}

	private static Map<String,String> fetchTaskMap = new HashMap<String, String>();
	public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()+ "/"+APP_DIR_NAME+"/");
	public Drawable fetchDrawableAppointType(String imageUrl, String imgType) {

		InputStream is = null;
		File localFile = null;
		try {
			Drawable drawable;
			if(!PHOTO_DIR.exists())
				PHOTO_DIR.mkdirs();
			localFile  = new File(PHOTO_DIR, UtilHttp.getMD5Str(imageUrl) + ".png");
			if(!localFile.exists() || localFile.length() <= 0){
				if(!fetchTaskMap.containsKey(imageUrl)){
					fetchTaskMap.put(imageUrl, imageUrl);
					is = fetch(imageUrl);
					if(is==null)
						 throw new RuntimeException("stream is null");  
					Bitmap bm = null;
					byte[] data = readStream(is);  
			        if(data!=null){  
			        	bm = BitmapFactory.decodeByteArray(data, 0, data.length);  
			        } 
//			        bm = BitmapFactory.decodeStream(is);
			        if(bm!=null){
			        	OutputStream outStream = new FileOutputStream(localFile,false);  
						if(IMAGE_TYPE_OF_PNG.equals(imgType)){
							bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);  
						}
						if(IMAGE_TYPE_OF_JPEG.equals(imgType)){
							bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);  
						}
			            outStream.flush();  
			            outStream.close();
			        }
					is.close();
					if(bm!=null && !bm.isRecycled())
						bm.recycle();
					fetchTaskMap.remove(imageUrl);
				}
			}
//				localFile  = new File(recordBean.getPhotoUrl());
			if(localFile.length() <= 0){
				return null;
			}
			drawable = fetchLocal(localFile.getPath());
			return drawable;
		} catch (Exception e) {
			if(localFile!=null && localFile.exists()){
				localFile.delete();
			}
			Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		} finally{
			try {
				if(fetchTaskMap.containsKey(imageUrl)){
					  fetchTaskMap.remove(imageUrl);
				}
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Drawable fetchDrawable(String imageUrl) {
	
			InputStream is = null;
			File localFile = null;
			try {
				Drawable drawable;
				if(!PHOTO_DIR.exists())
					PHOTO_DIR.mkdirs();
				localFile  = new File(PHOTO_DIR, UtilHttp.getMD5Str(imageUrl) + ".png");
				if(!localFile.exists() || localFile.length() <= 0){
					if(!fetchTaskMap.containsKey(imageUrl)){
						fetchTaskMap.put(imageUrl, imageUrl);
						is = fetch(imageUrl);
						if(is==null)
							 throw new RuntimeException("stream is null");  
	//					Bitmap bm = null;
	//					byte[] data = readStream(is);  
	//			        if(data!=null){  
	//			        	bm = BitmapFactory.decodeByteArray(data, 0, data.length);  
	//			        } 
				        Bitmap bm = BitmapFactory.decodeStream(is);
				        if(bm!=null){
				        	OutputStream outStream = new FileOutputStream(localFile,false);  
				            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);  
				            outStream.flush();  
				            outStream.close();
				        }
						is.close();
						if(bm!=null && !bm.isRecycled())
							bm.recycle();
						fetchTaskMap.remove(imageUrl);
					}
				}
	//				localFile  = new File(recordBean.getPhotoUrl());
				if(localFile.length() <= 0){
					return null;
				}
				drawable = fetchLocal(localFile.getPath());
				return drawable;
			} catch (Exception e) {
				if(localFile!=null && localFile.exists()){
					localFile.delete();
				}
				Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
			} finally{
				try {
					if(fetchTaskMap.containsKey(imageUrl)){
						  fetchTaskMap.remove(imageUrl);
					}
					if(is!=null){
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	/**
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception{        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();        
        byte[] buffer = new byte[1024];        
        int len = 0;        
        while( (len=inStream.read(buffer,0,1024)) != -1){        
            outStream.write(buffer, 0, len);        
        }    
        outStream.flush();
        outStream.close();        
        inStream.close();        
        return outStream.toByteArray();        
    }  
	/**
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		HttpResponse response = httpClient.execute(request);
		return response.getEntity().getContent();

	}
	/**
	 * @param urlString
	 * @return
	 */
	public Drawable fetchLocal(String urlString){
		try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(urlString, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 512*512);
			opts.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(urlString,opts);
			return new BitmapDrawable(bitmap); 
		}catch(Exception e){
			Log.e(this.getClass().getSimpleName(), "fetchLocal failed", e);
			return null;
		}
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
	
	/**
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
				.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	
}
