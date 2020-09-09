package com.litao.ttweather.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.LoginActivity;
import com.litao.ttweather.activity.SettingActivity;
import com.litao.ttweather.activity.UserInformationActivity;
import com.litao.ttweather.alximageloader.AlxImageLoader;
import com.litao.ttweather.tool.Constants;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.UtilMethod;


/**
 * �ҵ�fragment
 * 
 * @author Administrator
 * 
 */
public class MyFragment extends BaseFrament {
	private View view; // ������
	private RelativeLayout rl_my_information,rl_crm,rl_setting,rl_about;
	private TextView tv_name;
	private ImageView iv_member_photo;
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_my, null);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
		findView();
		init();
	}

	private void init() {
		rl_my_information.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sp = mActivity.getSharedPreferences("userinfo",0);
				if(sp.getBoolean("isLogined",false)) {
					Intent intent = new Intent();
					intent.putExtra("name", sp.getString("u_name","游客"));
					intent.setClass(mActivity, UserInformationActivity.class);
					startActivity(intent);
				}else {
					Intent intent = new Intent();
					intent.setClass(mActivity, LoginActivity.class);
					startActivity(intent);
				}
			}
		});
		rl_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mActivity, SettingActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences sp = mActivity.getSharedPreferences("userinfo",0);
		//boolean isLogined =sp.getBoolean("isLogined",false);
		String u_name = sp.getString("u_name","游客");
		String photo_path =sp.getString("photo_path","");
		tv_name.setText(u_name);
		AlxImageLoader alxImageLoader = new AlxImageLoader(mActivity);
		alxImageLoader.setAsyncBitmapFromSD(photo_path, iv_member_photo,UtilMethod.dp2px(mActivity,80), false, true, true);
		//new ImageTools().setImage(photo_path,iv_member_photo);
	}

	private void findView() {
		rl_my_information = view.findViewById(R.id.rl_my_information);
		rl_setting =view.findViewById(R.id.rl_setting);
		rl_about =view.findViewById(R.id.rl_about);
		tv_name = view.findViewById(R.id.tv_name);
		iv_member_photo = view.findViewById(R.id.iv_member_photo);

	}
}
