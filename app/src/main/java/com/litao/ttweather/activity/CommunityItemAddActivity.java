package com.litao.ttweather.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.adapter.CommunityItemAddAdapter;
import com.litao.ttweather.alximageloader.AlxImageLoader;
import com.litao.ttweather.alximageloader.AlxListViewCommonAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoActivity;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoAdapterWithNoSelect;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.CityWeatherItem;
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.tool.Constants;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.UtilMethod;
import com.litao.ttweather.view.MyGridView;

import java.util.ArrayList;

public class CommunityItemAddActivity extends Activity {
    String TAG="CommunityItemAddActivity";
    MyGridView gv_photoes;
    ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photos;
    //int imageWidth;
    SelectPhotoAdapterWithNoSelect adapter;
    EditText et_content;
    Button bt_finish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_item_add);
        gv_photoes = findViewById(R.id.gv_photoes);
        bt_finish = findViewById(R.id.bt_finish);
        et_content = findViewById(R.id.et_content);
        gv_photoes.setAdapter(adapter);
        photos = new ArrayList<>();
        adapter = new SelectPhotoAdapterWithNoSelect((Activity) this, photos);
        gv_photoes.setAdapter(adapter);
        //int width = this.getWindowManager().getDefaultDisplay().getWidth();
        //imageWidth = (width - UtilMethod.dp2px(this, 20) - 10) / 3;
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=et_content.getText().toString().trim();
                if(TextUtils.isEmpty(content)&&photos.size()==0){
                    Toast.makeText(getApplicationContext(),"请说点什么吧！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                SharedPreferences sp = getSharedPreferences("userinfo", 0);
                int u_id = sp.getInt("u_id", 0);
                values.put("u_id", u_id);
                values.put("content", content);
                Time time = new Time();
                time.setToNow();
                String publishTime =time.year+"-"+(time.month+1)+"-"+time.monthDay+" "+time.hour+":"+time.minute;
                Log.e(TAG,publishTime);
                values.put("Publish_time", publishTime);
                DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.beginTransaction(); // 开启事务
                try {
                    //调用insert方法，将数据插入数据库
                    //参数1：表名
                    //参数2：如果你想插入空值，那么你必须指定它的所在的列
                    int Ci_id=0;
                    if(db.insert("Community_item", null, values)==-1) throw new Exception("Insert Community_item Error");
                    Cursor cursor1 = db.query("Community_item", new String[]{"Ci_id"}, "u_id=?", new String[]{String.valueOf(u_id)}, null, null, "Ci_id desc");
                    if(cursor1.moveToNext()){
                        Ci_id = cursor1.getInt(cursor1.getColumnIndex("Ci_id"));
                    }
                    for(SelectPhotoAdapter.SelectPhotoEntity entity:photos){
                        ContentValues values1 = new ContentValues();
                        values1.put("Ci_id",Ci_id);
                        values1.put("Img_path",entity.url);
                        if(db.insert("Community_item_img", null, values1)==-1) throw new Exception("Insert Community_item Error");
                    }
                    db.setTransactionSuccessful();
                    CommunityFragment.isNeedRefresh =true;
                    CommunityAboutMeActivity.isNeedRefresh =true;
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction(); // 结束事务
                    db.close();
                }

            }
        });
    }

    public void back(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != SelectPhotoActivity.SELECT_PHOTO_OK) return;
        boolean isFromCamera = data.getBooleanExtra("isFromCamera", false);
        ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos = data.getParcelableArrayListExtra("selectPhotos");
        for (SelectPhotoAdapter.SelectPhotoEntity entity : selectedPhotos) {
            photos.add(entity);
            adapter.notifyDataSetChanged();
        }
        Log.i("Alex", "选择的图片是" + selectedPhotos);


    }


}
