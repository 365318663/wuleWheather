package com.litao.ttweather.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.adapter.WeatherAdapter;
import com.litao.ttweather.config.LocationApplication;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                Toast.makeText(getApplicationContext(),
                        "请给定位权限", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        LocationApplication.addActivity_(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                        }
                        Message message = new Message();
                        message.what=1;
                        handler.sendMessage(message);
                        try {
                            Thread.currentThread().sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,HomeActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                finish();
            }
        }).start();

    }


}
