package com.litao.ttweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.config.LocationApplication;


public class SettingActivity extends Activity {
    private RelativeLayout  rl_up_psw;
    private Button bt_logoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LocationApplication.addActivity_(this);
        findView();
        init();
    }

    private void init() {
        bt_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, LoginActivity.class);
                LocationApplication.removeALLActivity_();
                startActivity(intent);

            }
        });
        rl_up_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogined = getSharedPreferences("userinfo",0).getBoolean("isLogined",false);
                if(isLogined) {
                    Intent intent = new Intent();
                    intent.setClass(SettingActivity.this, UserUpdataPassWord.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void findView() {
        bt_logoff = findViewById(R.id.bt_logoff);
        rl_up_psw = findViewById(R.id.rl_up_psw);

    }

    public void back(View view) {
        finish();
    }
}
