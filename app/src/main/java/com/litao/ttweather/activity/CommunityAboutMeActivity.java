package com.litao.ttweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.adapter.CommunityAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.CommunityItem;

import java.util.ArrayList;

public class CommunityAboutMeActivity extends Activity{
    private CommunityAdapter adapter;
    private TextView tv_fragment_title;
    private ListView lv_community;
    private String TAG="CommunityAboutMeActivity";
    private ArrayList<CommunityItem> communityItems;
    static public boolean isNeedRefresh = true;
    private ImageView iv_back;
    private int u_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_community);
        findview();
        init();
    }



    private void  getCommunityItems(){
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor1 = db.query("Community_item", new String[]{"u_id","Ci_id","content","Publish_time"}, "u_id=?", new String[]{String.valueOf(u_id)}, null, null, "Ci_id DESC");
        while (cursor1.moveToNext()){
            CommunityItem item = new CommunityItem();
            item.setId(cursor1.getInt(cursor1.getColumnIndex("Ci_id")));
            item.setContent(cursor1.getString(cursor1.getColumnIndex("content")));
            String pubulish_time = cursor1.getString(cursor1.getColumnIndex("Publish_time"));
            item.setPubulish_time(pubulish_time);
            ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photoes = new ArrayList<>();
            Cursor cursor_photoes = db.query("Community_item_img", new String[]{"Img_path"}, "Ci_id=?", new String[]{String.valueOf(item.getId())}, null, null, "Cii_id ASC");
            while (cursor_photoes.moveToNext()){
                SelectPhotoAdapter.SelectPhotoEntity entity= new SelectPhotoAdapter.SelectPhotoEntity();
                entity.url=cursor_photoes.getString(cursor_photoes.getColumnIndex("Img_path"));
                photoes.add(entity);
            }
            item.setPhoto_path(photoes);

            int u_id = cursor1.getInt(cursor1.getColumnIndex("u_id"));
            if(u_id == this.u_id){
                item.setCanDelete(true);
            }else {
                item.setCanDelete(false);
            }
            Cursor cursor_user = db.query("User", new String[]{"u_name","photo_path"}, "u_id=?", new String[]{String.valueOf(u_id)}, null, null, null);
            while (cursor_user.moveToNext()){
                item.setHeader_path(cursor_user.getString(cursor_user.getColumnIndex("photo_path")));
                item.setName(cursor_user.getString(cursor_user.getColumnIndex("u_name")));
            }
            Cursor cursor_comment_num = db.rawQuery("SELECT COUNT(*) FROM Community_item_comment where Ci_id=?",new String[]{String.valueOf(item.getId())});
            if (cursor_comment_num.moveToNext()){
                item.setComment_Num(cursor_comment_num.getInt(0));
            }
            Cursor cursor_laud_num = db.rawQuery("SELECT COUNT(*) FROM Community_item_laud where Ci_id=?",new String[]{String.valueOf(item.getId())});
            if (cursor_laud_num.moveToNext()){
                item.setLaud_num(cursor_laud_num.getInt(0));
            }
            Cursor cursor_laud_status = db.query("Community_item_laud", new String[]{"Ci_id"}, "u_id=? and Ci_id=?", new String[]{String.valueOf(this.u_id),String.valueOf(item.getId())}, null, null, null);
            if (cursor_laud_status.moveToNext()){
                item.setLaud(true);
            }else {
                item.setLaud(false);
            }
            communityItems.add(item);
            adapter.notifyDataSetChanged();
        }
        db.close();
    }
    private void init() {
        tv_fragment_title.setText("社区");
        communityItems = new ArrayList<>();
        adapter = new CommunityAdapter(this,communityItems);
        lv_community.setAdapter(adapter);

        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findview() {
        lv_community = findViewById(R.id.lv_community);
        tv_fragment_title = findViewById(R.id.tv_fragment_title);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    public void finish() {
        super.finish();
        isNeedRefresh =true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isNeedRefresh){
            u_id =getSharedPreferences("userinfo",0).getInt("u_id",0);
            communityItems.clear();
            getCommunityItems();
        }
        isNeedRefresh=false;
    }
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
}
