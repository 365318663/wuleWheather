package com.litao.ttweather.fragment;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.HomeActivity;
import com.litao.ttweather.activity.NewsActivity;
import com.litao.ttweather.adapter.EmpityFooterAdapter;
import com.litao.ttweather.adapter.InitHeaderAdapter;
import com.litao.ttweather.adapter.LifeIndexAdapter;
import com.litao.ttweather.adapter.NewsAdapter;
import com.litao.ttweather.entity.LifeIndex;
import com.litao.ttweather.entity.News;
import com.litao.ttweather.listener.OnFooterRefreshListener;
import com.litao.ttweather.listener.OnHeaderRefreshListener;
import com.litao.ttweather.view.UltimateRefreshView;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * �ҵ�fragment
 * 
 * @author Administrator
 * 
 */
public class NewsFragment extends BaseFrament {
	private View view; // ������
	private NewsAdapter adapter;
	private boolean isAdded =false;
	private TextView tv_fragment_title;
	private ListView lv_news;
	private String TAG="NewsFragment";
	private static final int HAND_ADAPTER_FRESH_ADAPTER = 0;
	private static final int HAND_ADAPTER_FRESH_TEXTVIEW = 1;
	private static final int HAND_CITY_ERROR = 2;
	private ArrayList<News> news_list;
	private UltimateRefreshView mUltimateRefreshView;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HAND_ADAPTER_FRESH_ADAPTER) {

				news_list.add((News) msg.obj);
				adapter.notifyDataSetChanged();
			} else if (msg.what == HAND_ADAPTER_FRESH_TEXTVIEW) {

			} else if (msg.what == HAND_CITY_ERROR) {

			}
		}
	};
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_out, null);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
		finddview();
		init();
	}
	private void init() {
		tv_fragment_title.setText("新闻");

	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.i(TAG,""+hidden);
		if(!hidden){
			if(news_list.size()==0){
				mUltimateRefreshView.post(new Runnable() {
					@Override
					public void run() {
						mUltimateRefreshView.headerRefreshing();
					}
				});
			}
		}

	}
	private void finddview() {
		lv_news = view.findViewById(R.id.lv_suggest);
		tv_fragment_title = view.findViewById(R.id.tv_fragment_title);
		View emptyHeadView = LayoutInflater.from(mActivity).inflate(R.layout.activity_common, null, false);
		lv_news.addHeaderView(emptyHeadView);
		news_list = new ArrayList<>();
		adapter = new NewsAdapter(mActivity,news_list);
		lv_news.setAdapter(adapter);
		mUltimateRefreshView = (UltimateRefreshView) view.findViewById(R.id.refreshView);
		mUltimateRefreshView.setBaseHeaderAdapter(new InitHeaderAdapter(getContext()));
		mUltimateRefreshView.setBaseFooterAdapter(new EmpityFooterAdapter(getContext()));
		mUltimateRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(UltimateRefreshView view) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						news_list.clear();
						adapter.notifyDataSetChanged();
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									getWeaherNews();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
						mUltimateRefreshView.onHeaderRefreshComplete();
					}
				}, 500);
			}
		});


	}

	@Override
	public void onResume() {
		super.onResume();
		if(news_list.size()==0){
			mUltimateRefreshView.post(new Runnable() {
				@Override
				public void run() {
					mUltimateRefreshView.headerRefreshing();
				}
			});
		}
	}


	public void getWeaherNews() throws Exception {
		String urlString = "http://news.weather.com.cn/";
		// ArrayList<WeatherItem> sevenDayWeather = new ArrayList<WeatherItem>();
		Parser parser = new Parser((HttpURLConnection) (new URL(urlString)).openConnection());
		HasAttributeFilter java_script = new HasAttributeFilter("type", "text/javascript");
		NodeIterator iterator = parser.extractAllNodesThatMatch(java_script).elements();
		Node ul = iterator.nextNode();
		NodeList news = ul.getChildren();
		String newsString = news.toHtml().split("=")[1];
		System.out.println(newsString);
		JSONArray array = new JSONArray(newsString);
		for(int i=0;i<array.length();i++){
			JSONObject object = array.getJSONObject(i);
			JSONArray array1 =  object.getJSONArray("t2");
			for (int i1=0;i1<array1.length();i1++){
				JSONObject object1 = array1.getJSONObject(i1);
				News news1 = new News();
				news1.setAuthor_name(object1.getString("c4"));
				news1.setDate(object1.getString("c5"));
				news1.setIcon_url(object1.getString("c3"));
				news1.setLink(object1.getString("c2"));
				news1.setTime(object1.getString("c6"));
				news1.setTitle(object1.getString("c1"));
				Message message = new Message();
				message.what=HAND_ADAPTER_FRESH_ADAPTER;
				message.obj=news1;
				handler.sendMessage(message);
			}
		}


	}
}
