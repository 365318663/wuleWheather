package com.litao.ttweather.activity;

import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;


import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.litao.ttweather.R;
import com.litao.ttweather.bean.LogUtil;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class CESHI extends Activity {
	JSONArray province=new JSONArray();
	boolean isEnd=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		Log.i("oncreate","oncreate");
		city();
		//TextView a=findViewById(R.id.tv_weather);
		//a.setText("ds");


	}
	class a{
		int id;
		String name;
	}
	ArrayList<a> as= new ArrayList<>();
	public void  city(){
		//String URL_PATH="http://www.weather.com.cn/weather/";
		//String cityCode="101250201.shtml";
		 String urlS="http://guolin.tech/api/china/";
		 HttpUtils httpUtils = new HttpUtils();
	     RequestParams params = new RequestParams();
	     httpUtils.send(HttpMethod.GET, urlS, params,
	                new RequestCallBack<String>() {

	                    @Override
	                    public void onFailure(HttpException arg0,
	                                          String arg1) {
	                        // TODO Auto-generated method stub
	                    	Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
	                    }

	                    //@SuppressLint("NewApi")
	                    @Override
	                    public void onSuccess(ResponseInfo<String> arg0) {
	                        // TODO Auto-generated method stub
	                        String s = arg0.result;
                           // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	                        Log.i("onSuccess1",s);
	                        try {
	                        	
	                            JSONArray ja = new JSONArray(s);
	                            for(int i=0;i<ja.length();i++){
	                            	JSONObject ob = ja.getJSONObject(i);
	                            	a a1 = new a();
	                            	a1.id = ob.getInt("id");
	                            	a1.name = ob.getString("name");
	                            	as.add(a1);
	                            }
								for(int i=1;i<as.size()+1;i++){
									Log.i("success",""+i);
									city(i);
								}
	                            Log.i("success",""+as.size());
	                        } catch (org.json.JSONException e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
								Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();
	                        }
	                        

	                        
	                    }
	                    
	                });

		
	}
	public void  city(final int i){
		Log.i("success",""+i);
		String urlS="http://guolin.tech/api/china/"+i;
		 HttpUtils httpUtils = new HttpUtils();
	     RequestParams params = new RequestParams();
	     httpUtils.send(HttpMethod.GET, urlS, params,
	                new RequestCallBack<String>() {

	                    @Override
	                    public void onFailure(HttpException arg0,
	                                          String arg1) {
	                        // TODO Auto-generated method stub
							//Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
	                    }

	                    //@SuppressLint("NewApi")
	                    @Override
	                    public void onSuccess(ResponseInfo<String> arg0) {
	                        // TODO Auto-generated method stub
	                        String s = arg0.result;
	                       // System.out.println(s);
							//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	                        try {
	                        	//province.put(as.get(i).name);
	                            JSONArray ja = new JSONArray(s);
	                            JSONObject p = new JSONObject();
	                            p.put("name", as.get(i-1).name);
	                            p.put("cityList",ja);
	                            province.put(p);
	                          
	                        } catch (org.json.JSONException e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                            
	                        }
							if(i==as.size()) LogUtil.e("end",province.toString());
	                      // Toast.makeText(getApplicationContext(), province.toString(), Toast.LENGTH_SHORT).show();
	                    }
	                });
	}

}
