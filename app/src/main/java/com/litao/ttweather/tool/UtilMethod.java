package com.litao.ttweather.tool;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.EncodingUtils;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * APP Common method
 * 
 */
public class UtilMethod {
	/**
	 * get mobile device id
	 * 
	 * @return
	 */


	/**
	 * get app version name and version code
	 */
	public static String getAppVersion(Context context) {
		String versionName = "0.0.0";
		int versionCode = 1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			versionCode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName + "-" + versionCode;
	}

	/**
	 * get app version name
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "0.0.0";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * get app version code
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
			return versionCode;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionCode;
	}

	/**
	 * get mobile model
	 */
	public static String getDevice() {
		return Build.MODEL;
	}

	/**
	 * get mobile phone number and replace china number
	 */


	/**
	 * get mobile system version
	 */
	public static String getOs() {
		return "android" + Build.VERSION.RELEASE;
	}

	/**
	 * get screen resolution
	 */


	/**
	 * get mobile latitude and longitude
	 */

	/**
	 * close the soft keyboard
	 * 
	 * @param context
	 */
	public static void closeKeyBox(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		final View v = ((Activity) context).getWindow().peekDecorView();
		imm.hideSoftInputFromWindow(v.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * exit the app and finish all the activity
	 * 
	 * @param context
	 */


	/**
	 * exit the app and finish all the activity
	 * 
	 * @param context
	 */
	public static String getFileFromAssets(Context context, String fileName) {
		String result = "";
		try {
			InputStream is = context.getResources().getAssets().open(fileName);
			int lenght = is.available();
			byte[] buffer = new byte[lenght];
			is.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * change the url image as bitmap
	 */
	public static Bitmap getImageByURL(String url) {
		try {
			URL imgURL = new URL(url);
			URLConnection conn = imgURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			if (bm == null) {
				Log.e("MO", "httperror");
			}
			return bm;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * check the string is null
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * check the network is avalable
	 */
	public static boolean isNetworkAvailable(Activity mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getWeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		// ȡ�ô�������
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// ���ڵĿ��
		int screenWidth = dm.widthPixels;
		return screenWidth;

	}

	/**
	 * ��ȡ��Ļ�ĸ߶�
	 * 
	 * @param activity
	 * @return
	 */
	public static int getHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		// ȡ�ô�������
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// ���ڵĿ��
		int screenWidth = dm.heightPixels;
		return screenWidth;

	}

	/**
	 * ��תactivity
	 * 
	 * @param context
	 * @param cla
	 */
	public static <T> void startIntent(Context context, Class<T> cla) {
		Intent intent = new Intent(context, cla);
		context.startActivity(intent);
	}

	public static <T> void startIntentForPay(Context context, Class<T> cla,
			String value) {
		Intent intent = new Intent(context, cla);
		intent.putExtra("type", value);
		context.startActivity(intent);
	}

	/**
	 * ��0-30ת��Ϊ 1-7
	 */
	public static int change(int level) {

		if (level >= 0 && level <= 4) {
			return 1;
		} else if (level >= 5 && level <= 8) {
			return 2;
		} else if (level >= 9 && level <= 12) {
			return 3;
		} else if (level >= 13 && level <= 16) {
			return 4;
		} else if (level >= 17 && level <= 20) {
			return 5;
		} else if (level >= 21 && level <= 24) {
			return 6;
		} else if (level >= 25 && level <= 30) {
			return 7;
		}

		return 1;

	}

	/**
	 * �����ֻ��ķֱ��ʴ� dip �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// ���Բ��ͼƬ�ķ���
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;
				top = 0;
				left = 0;
				bottom = width;
				right = width;
				height = width;
				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;
				float clip = (width - height) / 2;
				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;
				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}

			Bitmap output = Bitmap
					.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top,
					(int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);

			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(4);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);

			paint.reset();
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(4);
			paint.setAntiAlias(true);
			canvas.drawCircle(width / 2, width / 2, width / 2 - 4 / 2, paint);

			return output;
		} else {
			return null;
		}
	}

	/**
	 * �ж��Ƿ��¼�������
	 * 
	 * @param context
	 * @param cla
	 *            ����
	 * @param car����
	 */


	/**
	 * �ж��Ƿ��¼
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isLogin(Context context) {
		return CacheUtils.getBoolean(context, Constants.IS_LOGIN, false);
	}

	/**
	 * �ж��ǻ������ǳ���
	 * 
	 * @param context
	 * @return
	 */
	public static String isCar(Context context) {
		return CacheUtils.getString(context, Constants.IDENTITY, "����");
	}

	/**
	 * 02. * �������popupwindow��Ļ�ı���͸���� 03. * @param bgAlpha 04.
	 */
	public static void backgroundAlpha(float bgAlpha, Activity activity) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * �õ��Զ����progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
//	public static Dialog createLoadingDialog(Context context, String msg) {
//
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.loading_dialog, null);// �õ�����view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
//		// main.xml�е�ImageView
//		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
//		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// ��ʾ����
//		// ���ض���
//		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//				context, R.anim.loading_animation);
//		// ʹ��ImageView��ʾ����
//		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//		tipTextView.setText(msg);// ���ü�����Ϣ
//
//		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog
//
//		loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��
//		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.FILL_PARENT));// ���ò���
//		return loadingDialog;
//
//	}

}
