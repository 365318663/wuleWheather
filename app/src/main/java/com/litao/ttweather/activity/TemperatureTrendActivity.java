package com.litao.ttweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.litao.ttweather.R;
import com.litao.ttweather.weathertrendgraph.Weather;
import com.litao.ttweather.weathertrendgraph.WeatherTrendGraph;

import java.util.ArrayList;
import java.util.List;

public class TemperatureTrendActivity extends AppCompatActivity {
    private WeatherTrendGraph mWeatherTrendGraph;
    private List<Weather> mWeathers = new ArrayList<>();
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_trend);
        mWeatherTrendGraph = (WeatherTrendGraph) findViewById(R.id.weatherTrendGraph);
        String date []= getIntent().getStringArrayExtra("date");
        int tem_low[]= getIntent().getIntArrayExtra("tem_low");
        int tem_high[]= getIntent().getIntArrayExtra("tem_high");
        for(int i=0;i<date.length;i++){
            weather = new Weather(tem_high[i], tem_low[i], date[i], "");
            mWeathers.add(weather);
        }

        mWeatherTrendGraph.setWeathers(mWeathers);
    }
    public void back(View view) {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();


    }
}
