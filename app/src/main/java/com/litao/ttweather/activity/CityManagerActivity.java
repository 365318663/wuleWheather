package com.litao.ttweather.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.adapter.CityWeatherAdapter;
import com.litao.ttweather.bean.CityBean;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.CityWeatherItem;
import com.litao.ttweather.tool.CityListLoader;
import com.litao.ttweather.tool.UtilMethod;

import java.util.ArrayList;

public class CityManagerActivity extends Activity {
    private ImageView img_left;
    private ImageView img_add;
    private  ListView lv_city_weather;
    private ArrayList<CityWeatherItem>  cityWeatherItems;
    private CityWeatherAdapter cityWeatherAdapter;
    public static final int RESULT_DATA_CONFORM = 1001;
    private  PopupWindow pop_delete;
    private TextView tv_title;
    private RelativeLayout confirm,cancle;
    private View pp_confirm;
    private int delete_position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        findView();
        init();
        initPopupWindow();
    }
    private void init() {
        CityListLoader.getInstance().loadProData(this);
       cityWeatherItems = new ArrayList<CityWeatherItem>();
       cityWeatherAdapter = new CityWeatherAdapter(this,cityWeatherItems);
       lv_city_weather.setAdapter(cityWeatherAdapter);
       lv_city_weather.setVerticalScrollBarEnabled(false);
       img_left.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
       img_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setClass(CityManagerActivity.this,ProvinceActivity.class);
               startActivityForResult(intent,RESULT_DATA_CONFORM);
           }
       });
       lv_city_weather.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

               delete_position =position;
               pop_delete.showAtLocation(pp_confirm, Gravity.CENTER, 0, 0);
               tv_title.setText("您需要删除城市："+cityWeatherItems.get(position).getCity()+"？");
               UtilMethod.backgroundAlpha(0.5f,CityManagerActivity.this);
               pop_delete.setFocusable(true);
               return true;
           }
       });
        lv_city_weather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               CityWeatherItem item = cityWeatherItems.get(position);

               HomeActivity.province = item.getProvince();
               HomeActivity.city = item.getCity();
               HomeActivity.isNeedRefreshWeather =true;
               HomeActivity.isNeedRefreshSuggest =true;
                SharedPreferences sp = getSharedPreferences("userinfo", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("city", HomeActivity.city);
                editor.putString("province", HomeActivity.province);
                editor.putBoolean("isGotLocation", true);
                editor.commit();
               CityManagerActivity.this.finish();
            }
        });
        getCityList();
    }

    private void getCityList() {
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
        int u_id = getSharedPreferences("userinfo",0).getInt("u_id",0);
        Cursor cursor = db.query("User_City", new String[]{"c_name"}, "u_id=?", new String[]{String.valueOf(u_id)}, null, null, null);
        while (cursor.moveToNext()){
            String city_name = cursor.getString(cursor.getColumnIndex("c_name"));
            Cursor cursor1 = db.query("City", new String[]{"c_province"}, "c_name=?", new String[]{city_name}, null, null, null);
            if(cursor1.moveToNext()){
                String province = cursor1.getString(cursor1.getColumnIndex("c_province"));
                CityWeatherItem item = new CityWeatherItem();
                item.setProvince(province);
                item.setCity(city_name);
                cityWeatherItems.add(item);
                cityWeatherAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initPopupWindow() {

        pp_confirm = LayoutInflater.from(this).inflate(
                R.layout.pp_delete, null);
        pop_delete = new PopupWindow(pp_confirm, UtilMethod.dp2px(this,300),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_delete.setBackgroundDrawable(new BitmapDrawable());
        pop_delete.setOutsideTouchable(true);
        tv_title = (TextView) pp_confirm.findViewById(R.id.tv_title);
        confirm = pp_confirm.findViewById(R.id.rl_confirm);
        cancle = pp_confirm.findViewById(R.id.rl_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SharedPreferences sp = getSharedPreferences("userinfo", 0);

                String city =sp.getString("city","" );
                if(city.equals(cityWeatherItems.get(delete_position).getCity())){
                    Toast.makeText(CityManagerActivity.this,"该城市已被选中，请选择其他城市后再进行删除操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseHelper dbhelper = new DatabaseHelper(CityManagerActivity.this);
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("PRAGMA foreign_keys = ON;");
                db.beginTransaction(); // 开启事务
                try {
                    if(db.delete("User_City", "c_name=?", new String[]{cityWeatherItems.get(delete_position).getCity()})<0) throw new Exception("delete error!");
                    Cursor cursor = db.query("User_City", new String[]{"c_name"}, "c_name=?", new String[]{cityWeatherItems.get(delete_position).getCity()}, null, null, null);
                    if(!cursor.moveToNext()){
                        if(db.delete("City", "c_name=?", new String[]{cityWeatherItems.get(delete_position).getCity()})<0) throw new Exception("delete error!");
                    }
                    cityWeatherItems.remove(delete_position);
                    cityWeatherAdapter.notifyDataSetChanged();
                    db.setTransactionSuccessful(); // 标记事务完成
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction(); // 结束事务
                    db.close();
                }
                //调用delete方法，删除数据


                pop_delete.dismiss();

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pop_delete.dismiss();

            }
        });
        pop_delete.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                UtilMethod.backgroundAlpha(1,CityManagerActivity.this);
            }
        });
        //initIsCar();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (pop_delete != null && pop_delete.isShowing()&&keyCode==KeyEvent.KEYCODE_BACK) {
            pop_delete.dismiss();

        }
        else if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

    private void findView() {
        img_left = findViewById(R.id.img_left);
        img_add = findViewById(R.id.img_add);
        lv_city_weather = findViewById(R.id.lv_city_weather);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_DATA_CONFORM) {
            // CityBean area = data.getParcelableExtra("area");
            if (data == null) {
                return;
            }
            CityBean city = data.getParcelableExtra("city");
            CityBean provinceBean =data.getParcelableExtra("province");
            //Intent intent = new Intent();
            CityWeatherItem item = new CityWeatherItem();
            item.setCity(city.getName());
            item.setProvince(provinceBean.getName());

            insertCity(item);
            //intent.putExtra("province", provinceBean);
            //intent.putExtra("city", city);
            // intent.putExtra("area", area);
            //setResult(RESULT_OK, intent);
            //finish();
        }
    }
    private void insertCity(CityWeatherItem item) {
        ContentValues values = new ContentValues();
        values.put("c_name", item.getCity());
        Time time = new Time();
        time.setToNow();
        Log.i("syso",time.toString());
        //time.format("");

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //调用insert方法，将数据插入数据库
        //参数1：表名
        //参数2：如果你想插入空值，那么你必须指定它的所在的列
        values.put("u_id",getSharedPreferences("userinfo",0).getInt("u_id",0));
        db.beginTransaction(); // 开启事务
        try {
            if(db.insert("User_City", null, values)==-1)   throw new Exception("抛出异常") ;
           // values.put("c_lastest_refresh", time.toString()) ;
            values.remove("u_id");
            values.put("c_province", item.getProvince());
            if(db.insert("City", null, values)==-1) ;
            cityWeatherItems.add(item);
            cityWeatherAdapter.notifyDataSetChanged();
            db.setTransactionSuccessful(); // 标记事务完成
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }
}
