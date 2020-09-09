package com.litao.ttweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.litao.ttweather.R;
import com.litao.ttweather.adapter.WeatherAdapter;
import com.litao.ttweather.entity.WeatherItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.os.Build;

public class WeatherActivity extends Activity {
//	public static final String VERSION_KEY = "User-Agent";// ����ͷ��key
//	public static final String VERSION_VALUE = "AppBuilder_android/";// ����ͷ��ֵ
//	private String longitude = "116.23", latitude = "39.54";// ��γ��
//	boolean isStarted = false;
//	/**
//	 * ��λ���
//	 */
//	private myLocationListener mListener;// ��λ����
//	private LocationClient mLocClient;// ��λ����
//	TextView tv_temperature, tv_city_weather;
//	ListView lv_weather;
//	String baiduWeatherUrl = "http://api.map.baidu.com/telematics/v3/weather";
//	ArrayList<WeatherItem> weatherItems;
//	WeatherAdapter adaptor;
//	EditText et_search;
//	ImageButton imgbt_search;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_weather);
//		setUpView();
//		init();
//	}
//
//	private void setUpView() {
//		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
//		tv_city_weather = (TextView) findViewById(R.id.tv_city_weather);
//		lv_weather = (ListView) findViewById(R.id.lv_weather);
//		et_search = (EditText) findViewById(R.id.et_search);
//		imgbt_search = (ImageButton) findViewById(R.id.imgbt_search);
//	}
//
//	private void init() {
//		weatherItems = new ArrayList<WeatherItem>();
//		adaptor = new WeatherAdapter(this, weatherItems);
//		lv_weather.setAdapter(adaptor);
//		SDKInitializer.initialize(getApplicationContext());
//		imgbt_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				getCityWeather();
//			}
//
//			private void getCityWeather() {
//				// TODO Auto-generated method stub
//				HttpUtils httpUtils = new HttpUtils();
//				RequestParams params = new RequestParams();
//				// params.addBodyParameter("location", longitude + "," +
//				// latitude);
//				// params.addBodyParameter("output", "json");
//				// params.addBodyParameter("ak",
//				// "tgvAEPRbNGZM1hB1mljoS5Tq1PWt9BrG");
//				// params.addBodyParameter("mcode",
//				// "35:F6:03:03:BD:2E:2D:FC:F4:E6:68:EB:04:E8:FB:1D:9E:B7:1D:A5;com.weather");
//				String url = baiduWeatherUrl
//						+ "?location="
//						+ et_search.getText().toString()
//						+ "&output=json&ak=tgvAEPRbNGZM1hB1mljoS5Tq1PWt9BrG&mcode=35:F6:03:03:BD:2E:2D:FC:F4:E6:68:EB:04:E8:FB:1D:9E:B7:1D:A5;com.weather";
//				httpUtils.send(HttpMethod.GET, url, params,
//						new RequestCallBack<String>() {
//
//							@Override
//							public void onFailure(HttpException arg0,
//									String arg1) {
//								// TODO Auto-generated method stub
//								Toast.makeText(MainActivity.this, "�����쳣", 0)
//										.show();
//							}
//
//							@SuppressLint("NewApi")
//							@Override
//							public void onSuccess(ResponseInfo<String> arg0) {
//								// TODO Auto-generated method stub
//								String s = arg0.result;
//								System.out.println(s);
//								System.out.println(1);
//								try {
//									JSONObject jsonObject = new JSONObject(s);
//									if (!jsonObject.getString("status").equals(
//											"success")) {
//										Toast.makeText(MainActivity.this,
//												"�����������", 0).show();
//										return;
//									}
//									weatherItems.clear();
//									adaptor.notifyDataSetChanged();
//									JSONArray array = new JSONArray(jsonObject
//											.getString("results"));
//									JSONArray array2 = new JSONArray(array
//											.getJSONObject(0).getString(
//													"weather_data"));
//									tv_city_weather.setText(array
//											.getJSONObject(0).getString(
//													"currentCity"));
//									System.out.println();
//									for (int i = 0; i < array2.length(); i++) {
//										JSONObject object = array2
//												.getJSONObject(i);
//										String weather;
//										if (i == 0) {
//											tv_city_weather.append("|"
//													+ object.getString("weather"));
//											tv_temperature.setText(object
//													.getString("date").split(
//															"��")[1].replace(
//													")", ""));
//										}
//										System.out.println("i");
//										WeatherItem item = new WeatherItem();
//										if (i != 0) {
//											item.date = object
//													.getString("date");
//										} else if (i == 1) {
//											item.date = "����";
//										} else {
//											item.date = "����";
//										}
//										item.temperature = object
//												.getString("temperature");
//										item.weather = object
//												.getString("weather");
//										item.iconUrl = object
//												.getString("dayPictureUrl");
//										System.out.println(item.date + " "
//												+ item.temperature + " "
//												+ item.weather + " "
//												+ item.iconUrl);
//										weatherItems.add(item);
//										adaptor.notifyDataSetChanged();
//
//									}
//								} catch (JSONException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//								for (int i = 0; i < weatherItems.size(); i++) {
//									WeatherItem item = weatherItems.get(i);
//									System.out
//											.println(item.date + " "
//													+ item.temperature + " "
//													+ item.weather + " "
//													+ item.iconUrl);
//								}
//
//							}
//						});
//			}
//		});
//		// ((AppApplication) getApplicationContext()).addActivity(this);
//	}
//
//	/**
//	 * �ж�ѡ����һ����Ϊ��λ����
//	 *
//	 * @param isselect
//	 * @param ll
//	 */
//
//	class myLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location != null) {
//
//				if (!TextUtils.isEmpty(location.getDistrict())
//						&& !TextUtils.isEmpty(location.getProvince())
//						&& !TextUtils.isEmpty(location.getCity())
//						&& !TextUtils.isEmpty(location.getStreet())) {
//					// tv_city_weather.setText(location.getCity());
//
//					System.out.println("����" + location.getLongitude());
//					System.out.println("γ��" + location.getLatitude());
//					longitude = String.valueOf(location.getLongitude());
//					latitude = String.valueOf(location.getLatitude());
//					if (!isStarted) {
//						getWeather();
//					}
//					isStarted = true;
//					// page = new Page(0, pageSize);
//
//					// ��λ�ɹ��͹رն�λ
//					if (mLocClient != null)
//						mLocClient.stop();
//				}
//
//			}
//
//		}
//	}
//
//	private void getWeather() {
//		// TODO Auto-generated method stub
//		HttpUtils httpUtils = new HttpUtils();
//		RequestParams params = new RequestParams();
//		// params.addBodyParameter("location", longitude + "," + latitude);
//		// params.addBodyParameter("output", "json");
//		// params.addBodyParameter("ak", "tgvAEPRbNGZM1hB1mljoS5Tq1PWt9BrG");
//		// params.addBodyParameter("mcode",
//		// "35:F6:03:03:BD:2E:2D:FC:F4:E6:68:EB:04:E8:FB:1D:9E:B7:1D:A5;com.weather");
//		String url = baiduWeatherUrl
//				+ "?location="
//				+ longitude
//				+ ","
//				+ latitude
//				+ "&output=json&ak=tgvAEPRbNGZM1hB1mljoS5Tq1PWt9BrG&mcode=35:F6:03:03:BD:2E:2D:FC:F4:E6:68:EB:04:E8:FB:1D:9E:B7:1D:A5;com.weather";
//		httpUtils.send(HttpMethod.GET, url, params,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						// TODO Auto-generated method stub
//						Toast.makeText(MainActivity.this, "�����쳣", 0).show();
//					}
//
//					@SuppressLint("NewApi")
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						// TODO Auto-generated method stub
//						String s = arg0.result;
//						System.out.println(s);
//						System.out.println(1);
//						try {
//							JSONObject jsonObject = new JSONObject(s);
//							JSONArray array = new JSONArray(jsonObject
//									.getString("results"));
//							JSONArray array2 = new JSONArray(array
//									.getJSONObject(0).getString("weather_data"));
//							tv_city_weather.setText(array.getJSONObject(0)
//									.getString("currentCity"));
//							System.out.println();
//							for (int i = 0; i < array2.length(); i++) {
//								JSONObject object = array2.getJSONObject(i);
//								String weather;
//								if (i == 0) {
//									tv_city_weather.append("|"
//											+ object.getString("weather"));
//									tv_temperature.setText(object.getString(
//											"date").split("��")[1].replace(")",
//											""));
//								}
//								System.out.println("i");
//								WeatherItem item = new WeatherItem();
//								if (i != 0) {
//									item.date = object.getString("date");
//								} else if (i == 1) {
//									item.date = "����";
//								} else {
//									item.date = "����";
//								}
//								item.temperature = object
//										.getString("temperature");
//								item.weather = object.getString("weather");
//								item.iconUrl = object
//										.getString("dayPictureUrl");
//								System.out.println(item.date + " "
//										+ item.temperature + " " + item.weather
//										+ " " + item.iconUrl);
//								weatherItems.add(item);
//								adaptor.notifyDataSetChanged();
//
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						for (int i = 0; i < weatherItems.size(); i++) {
//							WeatherItem item = weatherItems.get(i);
//							System.out.println(item.date + " "
//									+ item.temperature + " " + item.weather
//									+ " " + item.iconUrl);
//						}
//
//					}
//				});
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
//		// �˳�ʱ���ٶ�λ
//		if (mLocClient != null && mLocClient.isStarted()) {
//			mLocClient.stop();
//			mLocClient = null;
//		}
//
//	}
//
//	@Override
//	public void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//
//		// ��λ��ʼ��
//		mListener = new myLocationListener();
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(mListener);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// ��gps
//		option.setCoorType("bd09ll"); // ������������
//		option.setAddrType("all");
//		option.setScanSpan(5000);
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//
//	}

}