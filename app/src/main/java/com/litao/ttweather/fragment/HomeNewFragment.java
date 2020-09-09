package com.litao.ttweather.fragment;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.litao.ttweather.R;
import com.litao.ttweather.activity.CityManagerActivity;
import com.litao.ttweather.activity.HomeActivity;
import com.litao.ttweather.adapter.WeatherAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.config.LocationApplication;
import com.litao.ttweather.entity.CityWeatherItem;
import com.litao.ttweather.entity.WeatherItem;
import com.litao.ttweather.service.LocationService;
import com.litao.ttweather.tool.GetWeatherUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * ϵͳ��ҳ
 *
 * @author Administrator
 */

public class HomeNewFragment extends BaseFrament {

    private static final int SAVA_TO_DATABASE =3 ;
    private LocationService locationService;


    private String longitude = "116.23", latitude = "39.54";// ��γ��
    boolean isStarted = false;
    /**
     * ��λ���
     */
    private static final int HAND_ADAPTER_FRESH_ADAPTER = 0;
    private static final int HAND_ADAPTER_FRESH_TEXTVIEW = 1;
    private static final int HAND_CITY_ERROR = 2;
    private View view; // ������
    private ListView lv_weather;
    private TextView tv_city;
    private TextView tv_progress;
    private TextView tv_temperature;
    private TextView tv_weather;
    private TextView tv_wind;
    private TextView tv_wind_value;
    private TextView tv_humidity_value;
    private TextView tv_pm25;
    private TextView tv_air_quality;
    private LinearLayout ll_weather, ll_pm;
    private LinearLayout ll_progress;
    private ProgressBar pb_progress;
    private ImageView img_menu;
    private ArrayList<WeatherItem> weatherItems;
    private WeatherAdapter weatherAdapter;
    private boolean isGotLocation = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HAND_ADAPTER_FRESH_ADAPTER) {
                weatherItems.add((WeatherItem) msg.obj);
                weatherAdapter.notifyDataSetChanged();
            } else if (msg.what == HAND_ADAPTER_FRESH_TEXTVIEW) {
                if (weatherItems.size() > 0) {
                    // WeatherItem item = weatherItems.get(0);
                    //tv_wind.setText(item.getWind());
                    //tv_wind_value.setText(item.getWindValue());
                    //tv_weather.setText(item.getWeather());
                    ll_weather.setVisibility(View.VISIBLE);
                    ll_progress.setVisibility(View.GONE);
                }
            } else if (msg.what == HAND_CITY_ERROR) {
                tv_progress.setText("城市输入错误或网络错误");
                pb_progress.setVisibility(View.GONE);
            }else if(msg.what==SAVA_TO_DATABASE){

            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        findView();
        init();
        initProgress();

    }

    private void init() {
        weatherItems = new ArrayList<WeatherItem>();
        weatherAdapter = new WeatherAdapter(mActivity, weatherItems);
        lv_weather.setAdapter(weatherAdapter);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mActivity,"img_menu",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mActivity, CityManagerActivity.class);
                startActivity(intent);
            }
        });
        //getTodayWeaher();
        //getWeaher();

    }

    private void findView() {
        lv_weather = view.findViewById(R.id.lv_weather);
        tv_city = view.findViewById(R.id.tv_city);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_weather = view.findViewById(R.id.tv_weather);
        tv_wind = view.findViewById(R.id.tv_wind);
        tv_wind_value = view.findViewById(R.id.tv_wind_value);
        tv_humidity_value = view.findViewById(R.id.tv_humidity_value);
        tv_air_quality = view.findViewById(R.id.tv_air_quality);
        tv_pm25 = view.findViewById(R.id.tv_pm25);
        tv_progress = view.findViewById(R.id.tv_progress);
        ll_progress = view.findViewById(R.id.ll_progress);
        ll_weather = view.findViewById(R.id.ll_weather);
        pb_progress = view.findViewById(R.id.pb_progress);
        img_menu = view.findViewById(R.id.img_menu);
        ll_pm = view.findViewById(R.id.ll_pm);

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
        isGotLocation = sp.getBoolean("isGotLocation", false);
        if (!isGotLocation) {
            initProgress();
            //Toast.makeText(mActivity,"onStart",Toast.LENGTH_SHORT).show();
            locationService = ((LocationApplication) (getActivity().getApplication())).locationService;
            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
            locationService.registerListener(mListener);
            //注册监听
            int type = mActivity.getIntent().getIntExtra("from", 0);
            if (type == 0) {
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            } else if (type == 1) {
                locationService.setLocationOption(locationService.getOption());
            }
            locationService.start();
//		startLocation.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (startLocation.getText().toString().equals(getString(R.string.startlocation))) {
//					// 定位SDK
//					// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
//					startLocation.setText(getString(R.string.stoplocation));
//				} else {
//					locationService.stop();
//					startLocation.setText(getString(R.string.startlocation));
//				}
//			}
//		});
        } else {
            HomeActivity.city = sp.getString("city", "");
            HomeActivity.province = sp.getString("province", "");
            tv_city.setText(HomeActivity.city);
            getWeaherFromDataBase();
        }

    }

    private void getWeaherFromDataBase() {
        weatherItems.clear();
        DatabaseHelper dbhelper = new DatabaseHelper(mActivity);
        ll_progress.setVisibility(View.GONE);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor_weather_today = db.query("Weather_today", new String[]{"c_name", "weather", "Wind_name", "wind_level", "humidity", "Pm25", "Air_quality", "temperature"}, "c_name=?", new String[]{HomeActivity.city}, null, null, null);
        if (cursor_weather_today.moveToNext()) {
            String air_quality= cursor_weather_today.getString(cursor_weather_today.getColumnIndex("Air_quality"));
            if(!air_quality.equals("")) {
                tv_air_quality.setText(air_quality);
                tv_air_quality.setText(cursor_weather_today.getString(cursor_weather_today.getColumnIndex("Air_quality")));
            }else {
                ll_pm.setVisibility(View.GONE);
            }
            tv_humidity_value.setText(cursor_weather_today.getString(cursor_weather_today.getColumnIndex("humidity")));
            tv_temperature.setText(cursor_weather_today.getString(cursor_weather_today.getColumnIndex("temperature")));
            tv_weather.setText(cursor_weather_today.getString(cursor_weather_today.getColumnIndex("weather")));
            tv_wind.setText(cursor_weather_today.getString(cursor_weather_today.getColumnIndex("wind_name")));
            tv_wind_value.setText(cursor_weather_today.getString(cursor_weather_today.getColumnIndex("wind_level")));
        }
        Cursor cursor_weather_future = db.query("Weather_future", new String[]{"day", "weather", "Temperature_range"}, "c_name=?", new String[]{HomeActivity.city}, null, null, "Wf_id desc");
        while (cursor_weather_future.moveToNext()){
            WeatherItem item = new WeatherItem();
            item.setWeather(cursor_weather_future.getString(cursor_weather_future.getColumnIndex("weather")));
            item.setDate(cursor_weather_future.getString(cursor_weather_future.getColumnIndex("day")));
            item.setTemperature(cursor_weather_future.getString(cursor_weather_future.getColumnIndex("Temperature_range")));
            weatherItems.add(item);
            weatherAdapter.notifyDataSetChanged();
        }
        ll_weather.setVisibility(View.VISIBLE);
    }


    public void onStop() {
        // �˳�ʱ���ٶ�λ
        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        weatherItems.clear();
        weatherAdapter.notifyDataSetChanged();
        super.onStop();


    }

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                //StringBuffer sb = new StringBuffer(256);
                if (HomeActivity.city == null || HomeActivity.city.equals("")) {
                    StringBuffer st = new StringBuffer();
                    st.append(location.getProvince());
                    st.append(location.getCity());
                    String city = st.toString();
                    //isFirstStart&&
                    System.out.print("city :" + city);
                    if (!city.endsWith("nullnull") && city != null && city.endsWith("") && !isStarted) {
                        System.out.print("city :" + city);
                        tv_city.setText(location.getCity());
                        HomeActivity.city = location.getCity().replace("省", "").replace("市", "");
                        HomeActivity.province = location.getProvince().replace("省", "").replace("市", "");
                        longitude = "" + location.getLongitude();
                        latitude = "" + location.getLatitude();
                        isGotLocation = true;
                        insertCity(HomeActivity.city, HomeActivity.province);
                        SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("city", HomeActivity.city);
                        editor.putString("province", HomeActivity.province);
                        editor.putBoolean("isGotLocation", true);
                        editor.commit();
                        isStarted = true;
                        getWeaher();
                        getTodayHeWeaherTH();
                    } else if (!isStarted) {
                        tv_progress.setText("请给定位权限或打开网络");
                        pb_progress.setVisibility(View.GONE);
                        //Toast.makeText(HomeFragment.mActivity,"请给定位权限或打开网络",Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                    }
                } else if (!isStarted) {
                    tv_city.setText(HomeActivity.city);
                    isStarted = true;
                    getWeaher();
                    getTodayHeWeaherTH();
                }
            }
        }

    };

    private void insertCity(String cityName, String cityProvince) {
        ContentValues values = new ContentValues();
        values.put("c_name", cityName);
        Time time = new Time();
        time.setToNow();
        Log.i("syso", time.toString());
        DatabaseHelper dbhelper = new DatabaseHelper(mActivity);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //调用insert方法，将数据插入数据库
        //参数1：表名
        //参数2：如果你想插入空值，那么你必须指定它的所在的列

        db.beginTransaction(); // 开启事务
        try {
            values.put("c_province", cityProvince);
            values.put("c_lastest_refresh", time.toString());
            db.delete("City","c_name=?",new String[]{cityName});
            if (db.insert("City", null, values) == -1) ;
            values.remove("c_province");
            values.remove("c_lastest_refresh");
            int u_id=mActivity.getSharedPreferences("userinfo", 0).getInt("u_id", 0);
            values.put("u_id", u_id);
            db.delete("User_City","u_id=?",new String[]{String.valueOf(u_id)});
            if (db.insert("User_City", null, values) == -1) throw new Exception("抛出异常");
            db.setTransactionSuccessful(); // 标记事务完成
        } catch (Exception e) {
            SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isGotLocation", false);
            editor.commit();
            e.printStackTrace();
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    private void getWeaher() {
        //getTodatWeaher();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetWeatherUtils w = new GetWeatherUtils();
                //getWeaher("gbk");
                AssetManager aM = mActivity.getAssets();
                InputStream is = null;
                BufferedReader reader = null;
                InputStreamReader isr = null;
                try {
                    //InputStream is=aM.open("test.txt");
                    is = aM.open("cities.txt");            // read from res

                    isr = new InputStreamReader(is);
                    reader = new BufferedReader(isr);
                    GetWeatherUtils.initCityMap(reader);

                    //BufferedReader BufferedReader = new BufferedReader(is);
                    //FileReader reader = new FileReader(is);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message ad = new Message();
                    ad.what = HAND_ADAPTER_FRESH_TEXTVIEW;
                    handler.sendMessage(ad);
                    System.out.println("读文件错误sds");
                    return;
                } finally {

                    try {
                        if (is != null)
                            is.close();
                        if (reader != null)
                            reader.close();
                        if (isr != null)
                            isr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                //Scanner sc = new Scanner(System.in);
                //System.out.println("请输入你的城市，例如'广东深圳' ：");
                //city = "湖南湘潭";
                //sc.close();
                String cityCode = w.cityToCode(HomeActivity.province + HomeActivity.city);
                if (cityCode == null) {
                    System.out.println("该城市似乎不存在");
                } else {
                    System.out.println("该城市代码为 " + cityCode);
                }
                ArrayList<WeatherItem> sw = null;
                try {
                    sw = w.getSevenDayWeather(cityCode);


                    //item =sw.get(0);
                    //ad.what =HAND_ADAPTER_FRESH_ADAPTER;
                    //handler.sendMessage(ad);
                    //ad.what =HAND_ADAPTER_FRESH_TEXTVIEW;
                    //handler.sendMessage(ad);

                    for (int i = 0; i < sw.size(); i++) {
                        //System.out.println(sw.get(i));
                        Message ad = new Message();
                        ad.what = HAND_ADAPTER_FRESH_ADAPTER;
                        ad.obj = sw.get(i);
                        handler.sendMessage(ad);

                    }
                    DatabaseHelper dbhelper = new DatabaseHelper(mActivity);
                    //得到可写的SQLiteDatabase对象
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.beginTransaction(); // 开启事务
                    try {
                        db.delete("Weather_future", "c_name=?", new String[]{HomeActivity.city});
                        for (int i = 0; i < sw.size(); i++) {
                            WeatherItem item = sw.get(i);
                            ContentValues values = new ContentValues();
                            values.put("c_name", HomeActivity.city);
                            values.put("day", item.getDate());
                            values.put("weather", item.getWeather());
                            values.put("Temperature_range", item.getTemperature());
                            if (db.insert("Weather_future", null, values) == -1)
                                throw new Exception("插入异常");
                        }
                        ll_progress.setVisibility(View.GONE);
                        ll_weather.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isGotLocation", false);
                        editor.commit();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        db.endTransaction(); // 结束事务
                    }
                    sw.clear();
                    sw = w.getSevenToFifteenDayWeather(cityCode);
                    for (int i = 0; i < sw.size(); i++) {
                        //System.out.println(sw.get(i));
                        Message ad = new Message();
                        ad.what = HAND_ADAPTER_FRESH_ADAPTER;
                        ad.obj = sw.get(i);
                        handler.sendMessage(ad);
                    }

                    db.beginTransaction(); // 开启事务
                    try {
                        for (int i = 0; i < sw.size(); i++) {
                            WeatherItem item = sw.get(i);
                            ContentValues values = new ContentValues();
                            values.put("c_name", HomeActivity.city);
                            values.put("day", item.getDate());
                            values.put("weather", item.getWeather());
                            values.put("Temperature_range", item.getTemperature());
                            if (db.insert("Weather_future", null, values) == -1)
                                throw new Exception("插入异常");
                        }
                        ll_progress.setVisibility(View.GONE);
                        ll_weather.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isGotLocation", false);
                        editor.commit();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        db.endTransaction(); // 结束事务
                        db.close();
                    }
                    sw.clear();
                    Message ad = new Message();
                    ad.what = HAND_ADAPTER_FRESH_TEXTVIEW;
                    handler.sendMessage(ad);
                } catch (Exception e) {
                    Message ad = new Message();
                    ad.what = HAND_CITY_ERROR;
                    handler.sendMessage(ad);
                    System.out.println("发生网络错误或解析错误 city" + HomeActivity.province + HomeActivity.city);
                }
            }
        }).start();
    }

    private void initProgress() {
        isStarted = false;
        ll_weather.setVisibility(View.GONE);
        ll_progress.setVisibility(View.VISIBLE);
        pb_progress.setVisibility(View.VISIBLE);
        tv_progress.setText("正在加载");
        weatherItems.clear();
        weatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void getTodayWeaher() {

        // TODO Auto-generated method stub
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String url = "https://www.sojson.com/open/api/weather/json.shtml?city=" + tv_city.getText().toString();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(HomeNewFragment.mActivity, "网络异常", Toast.LENGTH_SHORT)
                                .show();
                    }

                    //@SuppressLint("NewApi")
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        String s = arg0.result;
                        System.out.println(s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (!jsonObject.getString("status").equals(
                                    "200")) {
                                //Toast.makeText(HomeFragment.mActivity,
                                //		"输入城市有误", Toast.LENGTH_SHORT).show();
                                System.out.println("输入城市有误");
                                return;
                            }
                            JSONObject data = new JSONObject(jsonObject.getString("data"));
                            tv_humidity_value.setText(data.getString("shidu"));
                            tv_temperature.setText(data.getString("wendu") + "℃");
                            tv_air_quality.setText(data.getString("quality"));
                            tv_pm25.setText("PM25:" + data.getString("pm25"));
                            ll_pm.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            tv_pm25.setText("");
                            tv_air_quality.setText("");
                            ll_pm.setVisibility(View.GONE);
                            Log.i("sys", " ll_pm.setVisibility(View.GONE);");
                            e.printStackTrace();

                        } finally {

                        }


                    }
                });
    }

    private void getTodayHeWeaherTH() {

        // TODO Auto-generated method stub
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String url = "https://free-api.heweather.com/s6/weather/now?location=" + tv_city.getText().toString() + "&&key=b806c7be011343c6accd77bf6953dad7&&lang=zh";
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(HomeNewFragment.mActivity, "网络异常", Toast.LENGTH_SHORT)
                                .show();
                    }

                    //@SuppressLint("NewApi")
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        String s = arg0.result;
                        System.out.println(s);
                        String hum = "";
                        String tem = "";
                        String weather = "";
                        String wind_dir = "";
                        String wind_sc = "";
                        ContentValues values = new ContentValues();
                        DatabaseHelper dbhelper = new DatabaseHelper(mActivity);
                        //得到可写的SQLiteDatabase对象
                        SQLiteDatabase db = dbhelper.getWritableDatabase();
                        db.beginTransaction(); // 开启事务
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = new JSONArray(jsonObject.getString("HeWeather6"));
                            jsonObject = array.getJSONObject(0);
                            JSONObject data = jsonObject.getJSONObject("now");
                            hum = data.getString("hum") + "%";
                            tv_humidity_value.setText(hum);
                            tem = data.getString("tmp") + "℃";
                            tv_temperature.setText(tem);
                            weather = data.getString("cond_txt");
                            tv_weather.setText(weather);
                            wind_dir = data.getString("wind_dir");
                            tv_wind.setText(wind_dir);
                            wind_sc = data.getString("wind_sc") + "级";
                            tv_wind_value.setText(wind_sc);
                            getTodayHeWeaherAQ();
                            //调用insert方法，将数据插入数据库
                            //参数1：表名
                            //参数2：如果你想插入空值，那么你必须指定它的所在的列
                            values.put("c_name", HomeActivity.city);
                            values.put("c_province", HomeActivity.province);
                            Time time = new Time();
                            time.setToNow();
                            String publishTime = time.year + "-" + time.month + "-" + (time.monthDay + 1) + " " + time.hour + ":" + time.minute;
                            values.put("c_lastest_refresh", publishTime);
                            db.delete("City", "c_name=?", new String[]{HomeActivity.city});
                            if (db.insert("City", null, values) == -1)
                                throw new Exception("抛出异常");
                            values.remove("c_province");
                            values.remove("c_lastest_refresh");
                            int u_id = mActivity.getSharedPreferences("userinfo", 0).getInt("u_id", 0);
                            values.put("u_id",u_id);
                            db.delete("User_City","u_id=?",new String[]{String.valueOf(u_id)});
                            if (db.insert("User_City", null, values) == -1)
                                throw new Exception("抛出异常");
                            values.remove("u_id");
                            values.put("weather", weather);
                            values.put("Wind_name", wind_dir);
                            values.put("wind_level", wind_sc);
                            values.put("humidity", hum);
                            values.put("temperature", tem);
                            values.put("Pm25", "");
                            values.put("Air_quality", "");
                            db.delete("Weather_today","c_name=?",new String[]{HomeActivity.city});
                            if (db.insert("Weather_today", null, values) == -1)
                                throw new Exception("抛出异常");
                            db.setTransactionSuccessful(); // 标记事务完成
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isGotLocation", false);
                            editor.commit();
                            //  ll_pm.setVisibility(View.GONE);
                        } finally {
                            db.endTransaction(); // 结束事务
                            db.close();
                        }


                    }
                });
    }

    private void getTodayHeWeaherAQ() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String url = "https://free-api.heweather.com/s6/air/now?location=" + tv_city.getText().toString() + "&&key=b806c7be011343c6accd77bf6953dad7&&lang=zh";
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(HomeNewFragment.mActivity, "网络异常", Toast.LENGTH_SHORT)
                                .show();
                    }

                    //@SuppressLint("NewApi")
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        String s = arg0.result;
                        System.out.println(s);
                        DatabaseHelper dbhelper = new DatabaseHelper(mActivity);
                        //得到可写的SQLiteDatabase对象
                        SQLiteDatabase db = dbhelper.getWritableDatabase();
                        db.beginTransaction(); // 开启事务
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = new JSONArray(jsonObject.getString("HeWeather6"));
                            jsonObject = array.getJSONObject(0);
                            JSONObject data = jsonObject.getJSONObject("air_now_city");
                            //tv_humidity_value.setText(data.getString("hum")+"%");
                            //tv_temperature.setText(data.getString("tmp") + "℃");
                            //getTodayHeWeaherAQ();
                            String pm25 = "PM25:" + data.getString("pm25");
                            String Air_quality = data.getString("qlty");
                            tv_air_quality.setText(Air_quality);
                            tv_pm25.setText(pm25);
                            ll_pm.setVisibility(View.VISIBLE);
                            ContentValues values = new ContentValues();
                            values.put("PM25", pm25);
                            values.put("Air_quality", Air_quality);
                            long i = db.update("Weather_today",values , "c_name=?",new String[]{HomeActivity.city});
                            if (i <= 0) {
                                throw new Exception("插入异常");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            ll_pm.setVisibility(View.GONE);
                            SharedPreferences sp = mActivity.getSharedPreferences("userinfo", 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isGotLocation", false);
                            editor.commit();
                        } finally {
                            db.endTransaction(); // 结束事务
                            db.close();
                        }


                    }
                });
    }

    public abstract class BDAbstractLocationListener extends com.baidu.location.BDAbstractLocationListener {
        public BDAbstractLocationListener() {
        }

        public abstract void onReceiveLocation(BDLocation var1);

        public void onConnectHotSpotMessage(String var1, int var2) {
        }

        public void onLocDiagnosticMessage(int var1, int var2, String var3) {
        }
    }

    ;

}
