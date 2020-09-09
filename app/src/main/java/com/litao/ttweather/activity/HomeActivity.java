package com.litao.ttweather.activity;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.config.LocationApplication;
import com.litao.ttweather.fragment.HomeFragment2;
import com.litao.ttweather.fragment.HomeNewFragment;
import com.litao.ttweather.fragment.NewsFragment;
import com.litao.ttweather.tool.Constants;
import com.litao.ttweather.tool.NetUtil;
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.fragment.HomeFragment;
import com.litao.ttweather.fragment.MyFragment;
import com.litao.ttweather.fragment.OutFragment;
import com.litao.ttweather.view.FragmentTabHost;

import java.io.File;


public class HomeActivity extends FragmentActivity {
    static public FragmentTabHost fragmentTabHost;
    public static boolean isNeedRefreshWeather=false;
    public static boolean isNeedRefreshSuggest=false;
    private LayoutInflater layoutInflater;
    private long exitTime = 0;
    public static String city;
    public static String province;
    static public Activity activity;

    static public Activity getActvity() {
        return activity;
    }

    private Class<?> fragmentArray[] = {

            HomeFragment2.class, OutFragment.class, NewsFragment.class, CommunityFragment.class,
            MyFragment.class};

    private int tabImageViewArray[] = {R.drawable.tab_home,
            R.drawable.tab_out, R.drawable.tab_news, R.drawable.tab_community,
            R.drawable.tab_my};

    private int tabTextviewArray[] = {R.string.tab_home, R.string.tab_out, R.string.tab_news,
            R.string.tab_community, R.string.tab_my};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Constants.PHOTO_PATH_ROOT = getFilesDir().getPath();
        Constants.CACHE_DIR = getCacheDir().getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        activity = this;
        findViewIds();
        // 监听网络状态改变的广播，网络发生变化时候，系统会发送广播
        IntentFilter netFilter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        netFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(netReceive, netFilter);
        SharedPreferences sp = getSharedPreferences("userinfo", 0);
        boolean hasStarted = sp.getBoolean("hasStarted", false);
        boolean isLogined = sp.getBoolean("isLogined", false);
        if (!isLogined ) {
            SharedPreferences.Editor editor = sp.edit();
            if(!hasStarted) {
                register("游客", "");
            }
            loginWithVistor();
            editor.putBoolean("hasStarted",true);
            editor.commit();
        }
        LocationApplication.addActivity_(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void finish() {
        super.finish();

    }

    private void loginWithVistor() {
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //得到可读的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query("User", new String[]{"u_id","u_name","photo_path"}, "u_name=?", new String[]{"游客"}, null, null, null);
        if(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("u_id"));
            String name = cursor.getString(cursor.getColumnIndex("u_name"));
            String photo_path=cursor.getString(cursor.getColumnIndex("photo_path"));
            SharedPreferences sharedPreferences = getSharedPreferences("userinfo",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogined",false);
            editor.putInt("u_id",id);
            editor.putString("u_name",name);
            editor.putString("photo_path",photo_path);
            editor.commit();
        }
    }

    private void register(String name,String pwd) {
        ContentValues values = new ContentValues();
        values.put("u_name", name);
        values.put("password", pwd);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //调用insert方法，将数据插入数据库
        //参数1：表名
        //参数2：如果你想插入空值，那么你必须指定它的所在的列
        db.insert("User", null, values);
        //Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
        //System.out.println("插入了：1, hello");

    }
    private void findViewIds() {
        layoutInflater = LayoutInflater.from(this);
        fragmentTabHost = (FragmentTabHost) this
                .findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(),
                R.id.tabContent);

        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {

            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(
                    getString(tabTextviewArray[i])).setIndicator(
                    getTabItemView(i));
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
            fragmentTabHost.getTabWidget().getChildAt(i)
                    .findViewById(R.id.tabTextview);
        }
        fragmentTabHost.setCurrentTab(0);
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_common, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabImageview);
        TextView textView = (TextView) view.findViewById(R.id.tabTextview);
        imageView.setImageResource(tabImageViewArray[index]);
        textView.setText(getString(tabTextviewArray[index]));
        return view;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(),
                        R.string.twice_time_exit_app, Toast.LENGTH_SHORT)
                        .show();
                exitTime = System.currentTimeMillis();
            } else {
                LocationApplication.removeALLActivity_();
                //System.exit(0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    // 动态判断网络状态的广播
    private BroadcastReceiver netReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 检测我们的网络状态
                ConnectivityManager cm = NetUtil
                        .getConnectivityManager(HomeActivity.this);
                NetworkInfo info = cm.getActiveNetworkInfo();// 获取活动的网络信息

                if (info != null) {
                    boolean available = info.isAvailable();// 网络是否有效
                    boolean conn = info.isConnected();// 网络是否已经连接上
                    if (available && conn) {
                        System.out.println("网络正常");
                    } else {
                        // 有问题的时候给用户提示，将提示的layout显示，并且添加点击事件，进入到设置网络的界面
                        System.out.println("网络不正常");
                    }
                } else {// NetworkInfo = null:说明没有网络，网络也是有问题
                    // 网络有问题
                    System.out.println("网络不正常");
                }
            }
        }

        ;
    };
    public void add(View v){
        boolean isLogined=getSharedPreferences("userinfo",0).getBoolean("isLogined",false);
        if(isLogined) {
            Intent intent = new Intent();
            intent.setClass(this, CommunityItemAddActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    public void about_me(View v){
        boolean isLogined=getSharedPreferences("userinfo",0).getBoolean("isLogined",false);
        if(isLogined) {
            Intent intent = new Intent();
            intent.setClass(this, CommunityAboutMeActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
