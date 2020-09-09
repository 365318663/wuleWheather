package com.litao.ttweather.fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.litao.ttweather.R;
import com.litao.ttweather.activity.HomeActivity;
import com.litao.ttweather.adapter.CommunityAdapter;
import com.litao.ttweather.adapter.InitHeaderAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.CommunityItem;
import com.litao.ttweather.listener.OnHeaderRefreshListener;
import com.litao.ttweather.view.UltimateRefreshView;

import java.util.ArrayList;


/**
 * ����fragment
 *
 * @author Administrator
 */
public class CommunityFragment extends BaseFrament {

    private View view; // ������
    private CommunityAdapter adapter;
    private TextView tv_fragment_title;
    private ListView lv_community;
    private String TAG = "CommunityFragment";
    private ArrayList<CommunityItem> communityItems;
    static public boolean isNeedRefresh = true;
    private int u_id;
    private UltimateRefreshView mUltimateRefreshView;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_community, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        findview();
        init();
    }

    private void getCommunityItems() {
        u_id = mActivity.getSharedPreferences("userinfo", 0).getInt("u_id", 0);
        DatabaseHelper dbhelper = new DatabaseHelper(mActivity);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor1 = db.query("Community_item", new String[]{"u_id", "Ci_id", "content", "Publish_time"}, null, null, null, null, "Ci_id DESC");
        while (cursor1.moveToNext()) {
            CommunityItem item = new CommunityItem();

            item.setId(cursor1.getInt(cursor1.getColumnIndex("Ci_id")));
            item.setContent(cursor1.getString(cursor1.getColumnIndex("content")));
            String pubulish_time = cursor1.getString(cursor1.getColumnIndex("Publish_time"));
            item.setPubulish_time(pubulish_time);
            ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photoes = new ArrayList<>();
            Cursor cursor_photoes = db.query("Community_item_img", new String[]{"Img_path"}, "Ci_id=?", new String[]{String.valueOf(item.getId())}, null, null, "Cii_id ASC");
            while (cursor_photoes.moveToNext()) {
                SelectPhotoAdapter.SelectPhotoEntity entity = new SelectPhotoAdapter.SelectPhotoEntity();
                entity.url = cursor_photoes.getString(cursor_photoes.getColumnIndex("Img_path"));
                photoes.add(entity);
            }
            item.setPhoto_path(photoes);

            int u_id = cursor1.getInt(cursor1.getColumnIndex("u_id"));
            if (u_id == this.u_id) {
                item.setCanDelete(true);
            } else {
                item.setCanDelete(false);
            }
            Cursor cursor_user = db.query("User", new String[]{"u_name", "photo_path"}, "u_id=?", new String[]{String.valueOf(u_id)}, null, null, null);
            while (cursor_user.moveToNext()) {
                item.setHeader_path(cursor_user.getString(cursor_user.getColumnIndex("photo_path")));
                item.setName(cursor_user.getString(cursor_user.getColumnIndex("u_name")));
            }
            Cursor cursor_comment_num = db.rawQuery("SELECT COUNT(*) FROM Community_item_comment where Ci_id=?", new String[]{String.valueOf(item.getId())});
            if (cursor_comment_num.moveToNext()) {
                item.setComment_Num(cursor_comment_num.getInt(0));
            }
            Cursor cursor_laud_num = db.rawQuery("SELECT COUNT(*) FROM Community_item_laud where Ci_id=?", new String[]{String.valueOf(item.getId())});
            if (cursor_laud_num.moveToNext()) {
                item.setLaud_num(cursor_laud_num.getInt(0));
            }
            Cursor cursor_laud_status = db.query("Community_item_laud", new String[]{"Ci_id"}, "u_id=? and Ci_id=?", new String[]{String.valueOf(this.u_id), String.valueOf(item.getId())}, null, null, null);
            if (cursor_laud_status.moveToNext()) {
                item.setLaud(true);
            } else {
                item.setLaud(false);
            }
            communityItems.add(item);
            adapter.notifyDataSetChanged();
        }
        db.close();
    }

    private void init() {
        u_id = mActivity.getSharedPreferences("userinfo", 0).getInt("u_id", 0);
        tv_fragment_title.setText("社区");
        communityItems = new ArrayList<>();
        adapter = new CommunityAdapter(mActivity, communityItems);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.lv_community_item_head, null, false);
        lv_community.addHeaderView(view);
        lv_community.setAdapter(adapter);

        mUltimateRefreshView.setBaseHeaderAdapter(new InitHeaderAdapter(getContext()));
        mUltimateRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(UltimateRefreshView view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        communityItems.clear();
                        getCommunityItems();
                        mUltimateRefreshView.onHeaderRefreshComplete();
                    }
                }, 500);
            }
        });
    }

    private void findview() {
        mUltimateRefreshView = (UltimateRefreshView) view.findViewById(R.id.refreshView);
        lv_community = view.findViewById(R.id.lv_community);
        tv_fragment_title = view.findViewById(R.id.tv_fragment_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedRefresh) {
            u_id = mActivity.getSharedPreferences("userinfo", 0).getInt("u_id", 0);
            communityItems.clear();
            getCommunityItems();
        } else if (communityItems.size() == 0) {
            mUltimateRefreshView.post(new Runnable() {
                @Override
                public void run() {
                    mUltimateRefreshView.headerRefreshing();
                }
            });
        }

        isNeedRefresh = false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (communityItems.size() == 0) {
            mUltimateRefreshView.post(new Runnable() {
                @Override
                public void run() {
                    mUltimateRefreshView.headerRefreshing();
                }
            });
        }
        u_id = mActivity.getSharedPreferences("userinfo",0).getInt("u_id",0);
    }
}
