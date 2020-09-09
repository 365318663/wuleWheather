package com.litao.ttweather.activity;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.adapter.CommentAdapter;
import com.litao.ttweather.adapter.CommunityPhotoDetailsAdapter;
import com.litao.ttweather.alximageloader.AlxImageLoader;
import com.litao.ttweather.alximageloader.CommunityPhotoAlxAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.Comment;
import com.litao.ttweather.entity.CommunityItem;
import com.litao.ttweather.entity.LifeIndex;
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.UtilMethod;
import com.litao.ttweather.view.MyGridView;
import com.litao.ttweather.view.MyListView;

import java.util.ArrayList;

public class LookCommunityItemDetailsActivity extends  AppCompatActivity{
    String TAG="LookCommunityItemDetailsActivity";
    private MyGridView gv_photoes;
    private ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photos;
    private MyListView mlv_comment;
    private CommentAdapter adapter;
    private ArrayList<Comment> comments = new ArrayList<>();
    private EditText et_comment;
    private Button bt_confirm;
    private long id;
    public TextView tv_comment_num;
    private int laud_num ;
    public  TextView tv_comment;
    private ImageView iv_laud;
    private boolean isLaud;
    private TextView tv_laud;
    private int u_id;
    private boolean isLogined;
    private ImageView iv_delete;
    private TextView tv_title;
    private RelativeLayout confirm,cancle;
    private View pp_confirm;
    private  PopupWindow pop_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communityitem_details);
        init();
        initPopupWindowDelete();
    }

    private void init() {
        gv_photoes = findViewById(R.id.gv_photoes);
        Bundle b = this.getIntent().getExtras();
        photos = b.getParcelableArrayList("list");
        if(photos.size()==1) {
            gv_photoes.setNumColumns(1);
        }else if(photos.size()==2){
            gv_photoes.setNumColumns(2);
        }else {
            gv_photoes.setNumColumns(3);
        }
        gv_photoes.setHorizontalSpacing(5);
        gv_photoes.setVerticalSpacing(5);
        gv_photoes.setAdapter(new CommunityPhotoAlxAdapter(this,photos));
        ImageView iv_icon= findViewById(R.id.iv_icon);
        TextView tv_name=  findViewById(R.id.tv_name);
        tv_comment_num = findViewById(R.id.tv_comment_num);
        tv_laud = findViewById(R.id.tv_laud);
        final LinearLayout ll_laud = findViewById(R.id.ll_laud);
        tv_comment = findViewById(R.id.tv_comment);
        final LinearLayout ll_comment = findViewById(R.id.ll_comment);
        iv_laud = findViewById(R.id.iv_laud);
        TextView tv_publish_time=  findViewById(R.id.tv_publish_time);
        final TextView tv_content=  findViewById(R.id.tv_content);
        et_comment = findViewById(R.id.et_comment);
        bt_confirm = findViewById(R.id.bt_confirm);
        iv_delete = findViewById(R.id.iv_delete);
        tv_content.setText(b.getString("content"));;
        tv_name.setText(b.getString("name"));
        tv_publish_time.setText(b.getString("publish_time"));
        laud_num = b.getInt("laud_num");
        isLogined = b.getBoolean("isLogined");
        isLaud = b.getBoolean("isLaud");
        boolean canDelete = b.getBoolean("canDelete");
        if(canDelete){
            iv_delete.setVisibility(View.VISIBLE);
        }else {
            iv_delete.setVisibility(View.GONE);
        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_delete.setFocusable(true);
                pop_delete.showAtLocation(pp_confirm, Gravity.CENTER, 0, 0);
                UtilMethod.backgroundAlpha(0.5f,LookCommunityItemDetailsActivity.this);
            }
        });
        iv_laud.setSelected(isLaud);
        u_id = b.getInt("u_id");
        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_comment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        ll_laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("PRAGMA foreign_keys = ON;");
                if(!isLaud) {
                    ContentValues values = new ContentValues();
                    values.put("u_id", u_id);
                    values.put("Ci_id", id);
                    //调用insert方法，将数据插入数据库
                    //参数1：表名
                    //参数2：如果你想插入空值，那么你必须指定它的所在的列
                    if(-1==db.insert("Community_item_laud", null, values)){
//                        Toast.makeText(getApplicationContext(),"点赞失败",Toast.LENGTH_SHORT).show();
//                        return;
                    }
                    iv_laud.setSelected(true);
                    laud_num =laud_num+1;
                    tv_laud.setText(String.valueOf(laud_num));
                }else {
                    if(db.delete("Community_item_laud", "u_id=?", new String[]{String.valueOf(u_id)})<1){
//                        Toast.makeText(getApplicationContext(),"取消点赞失败",Toast.LENGTH_SHORT).show();
//                        return;
                    }
                    iv_laud.setSelected(false);
                    laud_num--;
                    if (laud_num!= 0)
                        tv_laud.setText(String.valueOf(laud_num));
                    else tv_laud.setText("");
                }
                isLaud=!isLaud;
                CommunityFragment.isNeedRefresh=true;
                CommunityAboutMeActivity.isNeedRefresh = true;
            }
        });
        if (laud_num!= 0)
            tv_laud.setText(String.valueOf(laud_num));
        else tv_laud.setText("");
        AlxImageLoader alxImageLoader = new AlxImageLoader(this);
        alxImageLoader.setAsyncBitmapFromSD(b.getString("header_path"), iv_icon, UtilMethod.dp2px(this,30), false, true, true);
        id=b.getLong("id");
        mlv_comment = findViewById(R.id.mlv_comment);
        adapter = new CommentAdapter(this,comments);
        mlv_comment.setAdapter(adapter);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogined){
                    Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = et_comment.getText().toString().trim();
                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(getApplicationContext(),"请输入评论内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                SharedPreferences sp = getSharedPreferences("userinfo", 0);
                int u_id = sp.getInt("u_id", 0);
                values.put("u_id", u_id);
                values.put("content", comment);
                Time time = new Time();
                time.setToNow();
                String publishTime =time.year+"-"+(time.month+1)+"-"+time.monthDay+" "+time.hour+":"+time.minute;
                Log.e(TAG,publishTime);
                values.put("Publish_time", publishTime);
                values.put("Ci_id", id);
                DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();


                    //调用insert方法，将数据插入数据库
                    //参数1：表名
                    //参数2：如果你想插入空值，那么你必须指定它的所在的列
                if(db.insert("Community_item_comment", null, values)==-1) Toast.makeText(getApplicationContext(),"评论失败",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();
                    et_comment.setText("");
                    tv_content.setFocusable(true);
                    tv_content.setFocusableInTouchMode(true);
                    CommunityFragment.isNeedRefresh=true;
                    CommunityAboutMeActivity.isNeedRefresh = true;
                    getComments();
                }
                //db.close();

            }
        });
        getComments();

    }

    private void getComments() {
        comments.clear();
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor1 = db.query("Community_item_comment", new String[]{"Ci_id","u_id","content","Publish_time","Cic_id"}, "Ci_id=?", new String[]{String.valueOf(id)}, null, null, "Cic_id DESC");
        while (cursor1.moveToNext()){
            Comment item = new Comment();
            //item.setId(cursor1.getInt(cursor1.getColumnIndex("Ci_id")));
            item.setContent(cursor1.getString(cursor1.getColumnIndex("content")));
            String pubulish_time = cursor1.getString(cursor1.getColumnIndex("Publish_time"));
            item.setPublish_time(pubulish_time);
            item.setId(cursor1.getInt(cursor1.getColumnIndex("Cic_id")));
            int u_id = cursor1.getInt(cursor1.getColumnIndex("u_id"));
            Cursor cursor_user = db.query("User", new String[]{"u_name","photo_path"}, "u_id=?", new String[]{String.valueOf(u_id)}, null, null, null);
            if (cursor_user.moveToNext()){
                item.setIcon_path(cursor_user.getString(cursor_user.getColumnIndex("photo_path")));
                item.setName(cursor_user.getString(cursor_user.getColumnIndex("u_name")));
            }
            if(u_id ==this.u_id){
                item.setCanDelete(true);
            }else{
                item.setCanDelete(false);
            }

            comments.add(item);
            adapter.notifyDataSetChanged();
        }
        db.close();
        tv_comment_num.setText("评论（"+comments.size()+"）");
        tv_comment.setText(String.valueOf(comments.size()));
    }

    public void back(View v){
        finish();
    }
    private void initPopupWindowDelete() {
        pp_confirm = LayoutInflater.from(this).inflate(
                R.layout.pp_delete, null);
        pop_delete = new PopupWindow(pp_confirm, UtilMethod.dp2px(this,300),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_delete.setBackgroundDrawable(new BitmapDrawable());
        pop_delete.setOutsideTouchable(true);
        tv_title = (TextView) pp_confirm.findViewById(R.id.tv_title);
        tv_title.setText("您确定要删除此动态？");
        confirm = pp_confirm.findViewById(R.id.rl_confirm);
        cancle = pp_confirm.findViewById(R.id.rl_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.beginTransaction(); // 开启事务
                try {
                    db.execSQL("PRAGMA foreign_keys=ON");
                    db.delete("Community_item", "Ci_id=?", new String[]{String.valueOf(id)});
                    pop_delete.dismiss();
                    CommunityFragment.isNeedRefresh=true;
                    CommunityAboutMeActivity.isNeedRefresh = true;
                    finish();
                    db.setTransactionSuccessful(); // 标记事务完成
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction(); // 结束事务
                    db.close();
                }
                //调用delete方法，删除数据

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
                UtilMethod.backgroundAlpha(1,LookCommunityItemDetailsActivity.this);
            }
        });
        //initIsCar();
    }
}
