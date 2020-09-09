package com.litao.ttweather.fragment;


import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.CommunityItemAddActivity;
import com.litao.ttweather.activity.HomeActivity;
import com.litao.ttweather.activity.NewsActivity;
import com.litao.ttweather.adapter.EmpityFooterAdapter;
import com.litao.ttweather.adapter.InitHeaderAdapter;
import com.litao.ttweather.adapter.LifeIndexAdapter;
import com.litao.ttweather.entity.LifeIndex;
import com.litao.ttweather.entity.WeatherItem;
import com.litao.ttweather.listener.OnFooterRefreshListener;
import com.litao.ttweather.listener.OnHeaderRefreshListener;
import com.litao.ttweather.tool.GetWeatherUtils;
import com.litao.ttweather.view.UltimateRefreshView;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * ����fragment
 *
 * @author Administrator
 */
public class OutFragment extends BaseFrament {
    private String TAG = "OutFragment";
    private View view; // ������
    private ListView lv_suggest;
    private LifeIndexAdapter adapter;
    private ArrayList<LifeIndex> lifeIndices;
    private LifeIndex index;
    private boolean isAdded = false;
    private UltimateRefreshView mUltimateRefreshView;
    private static final int HAND_ADAPTER_FRESH_ADAPTER = 0;
    private static final int HAND_ADAPTER_FRESH_TEXTVIEW = 1;
    private static final int HAND_CITY_ERROR = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HAND_ADAPTER_FRESH_ADAPTER) {
                lifeIndices.add((LifeIndex) msg.obj);
                adapter.notifyDataSetChanged();
            } else if (msg.what == HAND_ADAPTER_FRESH_TEXTVIEW) {

            } else if (msg.what == HAND_CITY_ERROR) {
                adapter = new LifeIndexAdapter(mActivity, lifeIndices);
                lv_suggest.setAdapter(adapter);
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
        init();
    }

    private void init() {
        lv_suggest = view.findViewById(R.id.lv_suggest);
        lifeIndices = new ArrayList<>();
        adapter = new LifeIndexAdapter(mActivity, lifeIndices);
        View emptyHeadView = LayoutInflater.from(mActivity).inflate(R.layout.activity_common, null, false);
        lv_suggest.addHeaderView(emptyHeadView);
        lv_suggest.setAdapter(adapter);
        mUltimateRefreshView = (UltimateRefreshView) view.findViewById(R.id.refreshView);
        mUltimateRefreshView.setBaseHeaderAdapter(new InitHeaderAdapter(getContext()));
        mUltimateRefreshView.setBaseFooterAdapter(new EmpityFooterAdapter(getContext()));
        mUltimateRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(UltimateRefreshView view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lifeIndices.clear();
                        getLifeIndex();
                        mUltimateRefreshView.onHeaderRefreshComplete();
                    }
                }, 500);
            }
        });
        mUltimateRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(UltimateRefreshView view) {

            }
        });
        lv_suggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://21dg.taobao.com/?spm=2013.1.1000126.3.442e60e7EbGG8d");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "" + hidden);
        if (!hidden) {
            if (HomeActivity.isNeedRefreshSuggest) {
                mUltimateRefreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        mUltimateRefreshView.headerRefreshing();
                    }
                });
            } else if (lifeIndices.size() == 0) {
                mUltimateRefreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        mUltimateRefreshView.headerRefreshing();
                    }
                });
            }
        }
        HomeActivity.isNeedRefreshSuggest = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lifeIndices.size() == 0) {
            mUltimateRefreshView.post(new Runnable() {
                @Override
                public void run() {
                    mUltimateRefreshView.headerRefreshing();
                }
            });
        }
    }

    private void getLifeIndex() {
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
                try {


                    getLifeIndex(cityCode);
                    //item =sw.get(0);
                    //ad.what =HAND_ADAPTER_FRESH_ADAPTER;
                    //handler.sendMessage(ad);
                    //ad.what =HAND_ADAPTER_FRESH_TEXTVIEW;
                    //handler.sendMessage(ad);


                } catch (Exception e) {
                    Message ad = new Message();
                    ad.what = HAND_CITY_ERROR;
                    handler.sendMessage(ad);
                    System.out.println("发生网络错误或解析错误 city" + HomeActivity.province + HomeActivity.city);
                    return;
                }

            }
        }).start();
    }


    public void getLifeIndex(String cityCode) throws Exception {
        String urlString = "http://www.weather.com.cn/weather1d/" + cityCode + ".shtml";
        // ArrayList<WeatherItem> sevenDayWeather = new ArrayList<WeatherItem>();
        Parser parser = new Parser((HttpURLConnection) new URL(urlString).openConnection());

        HasAttributeFilter livezs = new HasAttributeFilter("class", "livezs");
        NodeIterator iterator = parser.extractAllNodesThatMatch(livezs).elements();
        Node ul = iterator.nextNode();
        NodeList lis = ul.getChildren();
        Log.i(TAG, ul.toHtml());
        //System.out.println(ul.toHtml());
        TagNameFilter ul_tag = new TagNameFilter("ul");
        NodeList uls = lis.extractAllNodesThatMatch(ul_tag);
        Node ul_node = uls.elementAt(0);
        NodeList ul_child = ul_node.getChildren();
        TagNameFilter li_tag = new TagNameFilter("li");
        NodeList list = ul_child.extractAllNodesThatMatch(li_tag);
        Log.i(TAG, list.toHtml());
        TagNameFilter span = new TagNameFilter("span");
        TagNameFilter p = new TagNameFilter("p");
        TagNameFilter em = new TagNameFilter("em");
        for (int i = 0; i < list.size(); i++) {
            NodeList children = list.elementAt(i).getChildren();
            Log.e(TAG, "\nlistsize" + list.size() + "\n" + children.toHtml());
            NodeList spans = children.extractAllNodesThatMatch(span);
            NodeList ps = children.extractAllNodesThatMatch(p);
            NodeList ems = children.extractAllNodesThatMatch(em);

            String title = ems.elementAt(0).toPlainTextString().trim();//

            String index;

            if (i == 1) {
                String emms[] = children.toHtml().split("<em>");
                int star_num = children.toHtml().split("star").length - 1;
                title = emms[emms.length - 1].split("</em>")[0].trim();
                index = star_num + "星";
            } else {
                title = children.toHtml().split("<em>")[1].split("</em>")[0].trim();
                index = spans.elementAt(0).toPlainTextString().trim();
            }
            String notice = ps.elementAt(0).toPlainTextString().trim();
            LifeIndex lifeIndex = new LifeIndex();
            lifeIndex.setIndex(index);
            lifeIndex.setName(title);
            lifeIndex.setNotice(notice);
            Message message = new Message();
            message.what = HAND_ADAPTER_FRESH_ADAPTER;
            message.obj =lifeIndex;
            handler.sendMessage(message);
        }

    }


}
