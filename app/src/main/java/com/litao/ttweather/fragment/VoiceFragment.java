package com.litao.ttweather.fragment;


import android.view.LayoutInflater;
import android.view.View;


import com.litao.ttweather.R;


/**
 * ����fragment
 * 
 * @author Administrator
 * 
 */
public class VoiceFragment extends BaseFrament  {
	private View view; // ������
	
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_common, null);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
		

	}



}
