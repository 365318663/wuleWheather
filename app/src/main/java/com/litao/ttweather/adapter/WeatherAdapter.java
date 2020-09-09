package com.litao.ttweather.adapter;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.entity.WeatherItem;
import com.litao.ttweather.tool.UtilMethod;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<WeatherItem> weatherItems;


    public WeatherAdapter(Activity mActivity, ArrayList<WeatherItem> weatherItems) {
        this.activity= mActivity;
        this.weatherItems = weatherItems;
    }

    @Override
    public int getCount() {
        return weatherItems.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(activity).inflate(R.layout.lv_weather_item,null);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int windth=dm.widthPixels/3;
        TextView tv_day = view.findViewById(R.id.tv_day);
        LinearLayout ll_weather = view.findViewById(R.id.ll_weather);
        ImageView img_weather= view.findViewById(R.id.img_weather);
        TextView tv_weather = view.findViewById(R.id.tv_weather);
        TextView tv_temperature_range= view.findViewById(R.id.tv_temperature_range);

        tv_day.setWidth(windth);
        ViewGroup.LayoutParams para;
        para = ll_weather.getLayoutParams();
        para.width=windth;
        ll_weather.setLayoutParams(para);
        tv_temperature_range.setWidth(windth);

        WeatherItem weatherItem = weatherItems.get(position);
        String date =weatherItem.getDate();
        String dayFragment[] = date.split("（");
        dayFragment = dayFragment[1].split("）");
        date=dayFragment[0];
        tv_weather.setText(weatherItem.getWeather());
        tv_day.setText(date);
        tv_temperature_range.setText(weatherItem.getTemperature());


        return view;
    }
}
