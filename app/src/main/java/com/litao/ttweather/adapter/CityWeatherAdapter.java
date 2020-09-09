package com.litao.ttweather.adapter;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.litao.ttweather.R;
import com.litao.ttweather.entity.CityWeatherItem;
import com.litao.ttweather.entity.WeatherItem;
import com.litao.ttweather.fragment.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityWeatherAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<CityWeatherItem> cityWeatherItems;


    public CityWeatherAdapter(Activity mActivity, ArrayList<CityWeatherItem> cityWeatherItems) {
        this.activity= mActivity;
        this.cityWeatherItems = cityWeatherItems;
    }

    @Override
    public int getCount() {
        return cityWeatherItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cityWeatherItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(activity).inflate(R.layout.lv_city_item, null);

        CityWeatherItem item= cityWeatherItems.get(position);

            TextView tv_city = view.findViewById(R.id.tv_city);
            TextView tv_province = view.findViewById(R.id.tv_province);
            TextView tv_temperature = view.findViewById(R.id.tv_temperature);
            TextView tv_weather_details = view.findViewById(R.id.tv_weather_details);
            TextView tv_temperature_range = view.findViewById(R.id.tv_temperature_range);
            tv_city.setText(item.getCity());
            tv_city.setText(item.getCity());
            tv_province.setText(item.getProvince());
            tv_temperature_range.setVisibility(View.GONE);
            if(item.isGotWeathter()) {
                tv_temperature.setText(item.getTemperature());
                tv_weather_details.setText(item.getWeather_details());
                tv_temperature.setVisibility(View.VISIBLE);
                tv_weather_details.setVisibility(View.VISIBLE);
            }else{
                tv_temperature.setVisibility(View.GONE);
                tv_weather_details.setVisibility(View.GONE);
                getTodayHeWeaherTH(tv_city, position);
                item.setIsGotWeathter(true);
            }

           // item.setIsGetWeathter(true);




        return view;
    }
    private void getTodayWeaher(TextView tv_city, final int position) {
        System.out.println("getTodayWeaher");
        // TODO Auto-generated method stub
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String url = "https://www.sojson.com/open/api/weather/json.shtml?city="+tv_city.getText().toString();

        httpUtils.send(HttpRequest.HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(activity, "网络异常", Toast.LENGTH_SHORT)
                            .show();
                }

                    //@SuppressLint("NewApi")
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        String s = arg0.result;
                        System.out.println(s);
                        CityWeatherItem item=cityWeatherItems.get(position);
                        String weather_detail = "";
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
                            StringBuffer weatherDetails = new StringBuffer();
                            item.setTemperature(data.getString("wendu")+"℃");
                            weatherDetails.append("湿度");
                            weatherDetails.append(data.getString("shidu"));
                            weatherDetails.append("|");
                            weatherDetails.append("空气");
                            weather_detail = weatherDetails.toString();
                            weatherDetails.append(data.getString("quality"));
                            weather_detail = weatherDetails.toString();


                            //tv_weather_details.setText(item.getWeather_details());
                           // tv_temperature.setText(item.getTemperature());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            weather_detail=weather_detail.replace("|空气","");
                            System.out.println("weather_detail");
                        }finally {
                            System.out.println("setWeather_details"+weather_detail);
                            item.setWeather_details(weather_detail);
                            notifyDataSetChanged();
                        }


                    }
                });
    }
    private void getTodayHeWeaherTH(TextView tv_city, final int position) {

        // TODO Auto-generated method stub
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String url = "https://free-api.heweather.com/s6/weather/now?location=" + tv_city.getText().toString()+"&&key=b806c7be011343c6accd77bf6953dad7&&lang=zh";
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(HomeFragment.mActivity, "网络异常", Toast.LENGTH_SHORT)
                                .show();
                    }

                    //@SuppressLint("NewApi")
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        String s = arg0.result;
                        System.out.println(s);
                        CityWeatherItem item=cityWeatherItems.get(position);
                        String weather_detail = "";
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = new JSONArray(jsonObject.getString("HeWeather6"));
                            jsonObject =array.getJSONObject(0);
                            JSONObject data = jsonObject.getJSONObject("now");
                            //getTodayHeWeaherAQ();
                            StringBuffer weatherDetails = new StringBuffer();
                            item.setTemperature(data.getString("tmp") + "℃");
                            weatherDetails.append(data.getString("cond_txt"));
                            weatherDetails.append("|");
                            weatherDetails.append("湿度");
                            weatherDetails.append(data.getString("hum")+"%");
                            weatherDetails.append("|");
                            weatherDetails.append(data.getString("wind_dir"));
                            weatherDetails.append(data.getString("wind_sc")+"级");
                            weather_detail = weatherDetails.toString();
                            item.setWeather_details(weather_detail);
                            notifyDataSetChanged();
                            //tv_air_quality.setText(data.getString("quality"));
                            // tv_pm25.setText("PM25:" + data.getString("pm25"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            //  ll_pm.setVisibility(View.GONE);
                        }




                            //tv_weather_details.setText(item.getWeather_details());
                            // tv_temperature.setText(item.getTemperature());






                    }
                });
    }
}
