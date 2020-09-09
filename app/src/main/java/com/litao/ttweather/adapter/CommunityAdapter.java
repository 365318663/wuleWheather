package com.litao.ttweather.adapter;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.CityManagerActivity;
import com.litao.ttweather.activity.CommunityAboutMeActivity;
import com.litao.ttweather.activity.LookCommunityItemDetailsActivity;
import com.litao.ttweather.alximageloader.AlxImageLoader;
import com.litao.ttweather.alximageloader.CommunityPhotoAlxAdapter;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.CommunityItem;
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.tool.UtilMethod;
import com.litao.ttweather.view.MyGridView;

import java.util.ArrayList;

public class CommunityAdapter extends BaseAdapter {
    private static final String TAG = "CommunityAdapter";
    private Activity activity;
    private ArrayList<CommunityItem> communityItems;
    private int icon_width;
    private AlxImageLoader alxImageLoader;
    private Button bt_confirm;
    private EditText et_comment;
    private PopupWindow pop_comment;
    private int community_sub;
    private View pp_comment;
    private int u_id;
    private boolean isLogined;
    private  PopupWindow pop_delete;
    private TextView tv_title;
    private RelativeLayout confirm,cancle;
    private View pp_confirm;
    public CommunityAdapter(Activity mActivity, ArrayList<CommunityItem> communityItems) {
        this.activity = mActivity;
        this.communityItems = communityItems;
        icon_width = UtilMethod.dp2px(activity, 30);
        this.alxImageLoader = new AlxImageLoader(activity);
        u_id = activity.getSharedPreferences("userinfo",0).getInt("u_id",0);
        isLogined = activity.getSharedPreferences("userinfo",0).getBoolean("isLogined",false);
        initPopupWindow();
        initPopupWindowDelete();
    }

    @Override
    public int getCount() {
        return communityItems.size();
    }

    @Override
    public Object getItem(int position) {
        return communityItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         final CommunityItem item = communityItems.get(position);
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.lv_community_item, parent, false);
        } else {
            view = convertView;
        }
        MyGridView gv_photoes = view.findViewById(R.id.gv_photoes);
        final LinearLayout ll_all = view.findViewById(R.id.ll_all);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_publish_time = view.findViewById(R.id.tv_publish_time);
        TextView tv_content = view.findViewById(R.id.tv_content);
        final TextView tv_laud = view.findViewById(R.id.tv_laud);
        LinearLayout ll_laud = view.findViewById(R.id.ll_laud);
        TextView tv_comment = view.findViewById(R.id.tv_comment);
        LinearLayout ll_comment = view.findViewById(R.id.ll_comment);
        final ImageView iv_laud = view.findViewById(R.id.iv_laud);
        LinearLayout ll_comment1 = view.findViewById(R.id.ll_comment1);
        ImageView iv_delete = view.findViewById(R.id.iv_delete);
        //new ImageTools().setImageWithNoHandle(item.getHeader_path(), iv_icon);
        tv_name.setText(item.getName());
        tv_publish_time.setText(item.getPubulish_time());
        tv_content.setText(item.getContent());
        if (item.getLaud_num() != 0)
            tv_laud.setText(String.valueOf(item.getLaud_num()));
        else tv_laud.setText("");
        if (item.getComment_Num() != 0)
            tv_comment.setText(String.valueOf(item.getComment_Num()));
        else tv_comment.setText("");
        if(item.isCanDelete()) {
            iv_delete.setVisibility(View.VISIBLE);
        }else {
            iv_delete.setVisibility(View.GONE);
        }
        iv_laud.setSelected(item.isLaud());
        final ArrayList<SelectPhotoAdapter.SelectPhotoEntity> list_path = item.getPhoto_path();
        //final ArrayList<String> list_path= new ArrayList<>();
        alxImageLoader.setAsyncBitmapFromSD(item.getHeader_path(), iv_icon, icon_width, false, true, true);
        if (list_path.size() == 1) {
            gv_photoes.setNumColumns(1);
        } else if (list_path.size() == 2) {
            gv_photoes.setNumColumns(2);
        } else {
            gv_photoes.setNumColumns(3);
        }
        gv_photoes.setHorizontalSpacing(5);
        gv_photoes.setVerticalSpacing(5);
        gv_photoes.setAdapter(new CommunityPhotoAlxAdapter(activity, list_path));
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                CommunityItem item = communityItems.get(position);
                intent.putParcelableArrayListExtra("list", list_path);
                intent.putExtra("content", item.getContent());
                intent.putExtra("header_path", item.getHeader_path());
                intent.putExtra("id", item.getId());
                intent.putExtra("name", item.getName());
                intent.putExtra("publish_time", item.getPubulish_time());
                intent.putExtra("laud_num", item.getLaud_num());
                intent.putExtra("isLaud", item.isLaud());
                intent.putExtra("canDelete", item.isCanDelete());
                intent.putExtra("isLogined", isLogined);
                intent.putExtra("u_id", u_id);
                intent.setClass(activity, LookCommunityItemDetailsActivity.class);
                activity.startActivity(intent);
            }
        });
        ll_laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbhelper = new DatabaseHelper(activity);
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                boolean isLaud =communityItems.get(position).isLaud();
                if(!isLaud) {
                    ContentValues values = new ContentValues();
                    values.put("u_id", u_id);
                    values.put("Ci_id", communityItems.get(position).getId());
                    //调用insert方法，将数据插入数据库
                    //参数1：表名
                    //参数2：如果你想插入空值，那么你必须指定它的所在的列
                    if(-1==db.insert("Community_item_laud", null, values)){
//                        communityItems.get(position).setLaud(!isLaud);
//                        iv_laud.setSelected(true);
//                        Toast.makeText(activity,"点赞失败",Toast.LENGTH_SHORT).show();
//                        return;
                    }
                    iv_laud.setSelected(true);
                    communityItems.get(position).setLaud_num(communityItems.get(position).getLaud_num()+1);
                    tv_laud.setText(String.valueOf(communityItems.get(position).getLaud_num()));
                }else {
                    if(db.delete("Community_item_laud", "u_id=?", new String[]{String.valueOf(u_id)})<1){
//                        communityItems.get(position).setLaud(!isLaud);
//                        iv_laud.setSelected(false);
//                        Toast.makeText(activity,"取消点赞失败",Toast.LENGTH_SHORT).show();
//                        return;
                    }
                    iv_laud.setSelected(false);
                    communityItems.get(position).setLaud_num(communityItems.get(position).getLaud_num()-1);
                    if (communityItems.get(position).getLaud_num()!= 0)
                        tv_laud.setText(String.valueOf(communityItems.get(position).getLaud_num()));
                    else tv_laud.setText("");
                }
                CommunityFragment.isNeedRefresh=true;
                CommunityAboutMeActivity.isNeedRefresh = true;
                communityItems.get(position).setLaud(!isLaud);
            }
        });
        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogined){
                    Toast.makeText(activity,"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                community_sub = position;
                pop_comment.setFocusable(true);
                pop_comment.showAtLocation(pp_comment,Gravity.BOTTOM, 0, 0);
                UtilMethod.backgroundAlpha(0.5f,activity);
                et_comment.requestFocus();
                //pop_comment.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                // pop_comment.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
        ll_comment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogined){
                    Toast.makeText(activity,"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                community_sub = position;
                pop_comment.setFocusable(true);
                pop_comment.showAtLocation(pp_comment,Gravity.BOTTOM, 0, 0);
                UtilMethod.backgroundAlpha(0.5f,activity);
                et_comment.requestFocus();
                //pop_comment.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
               // pop_comment.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                community_sub = position;
                pop_delete.setFocusable(true);
                pop_delete.showAtLocation(pp_confirm,Gravity.CENTER, 0, 0);
                UtilMethod.backgroundAlpha(0.5f,activity);
            }
        });
        return view;
    }
    private void initPopupWindow() {

        pp_comment = LayoutInflater.from(activity).inflate(
                R.layout.pp_comment, null);
        bt_confirm = pp_comment.findViewById(R.id. bt_confirm);
        et_comment = pp_comment.findViewById(R.id.et_comment);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_comment.getText().toString().trim();
                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(activity,"请输入评论内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                SharedPreferences sp = activity.getSharedPreferences("userinfo", 0);
                int u_id = sp.getInt("u_id", 0);
                values.put("u_id", u_id);
                values.put("content", comment);
                Time time = new Time();
                time.setToNow();
                String publishTime =time.year+"-"+(time.month+1)+"-"+time.monthDay+" "+time.hour+":"+time.minute;
                Log.e(TAG,publishTime);
                values.put("Publish_time", publishTime);
                values.put("Ci_id", communityItems.get(community_sub).getId());
                DatabaseHelper dbhelper = new DatabaseHelper(activity);
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();


                //调用insert方法，将数据插入数据库
                //参数1：表名
                //参数2：如果你想插入空值，那么你必须指定它的所在的列
                if(db.insert("Community_item_comment", null, values)==-1) Toast.makeText(activity,"评论失败",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(activity,"评论成功",Toast.LENGTH_SHORT).show();
                    et_comment.setText("");
                    communityItems.get(community_sub).setComment_Num(communityItems.get(community_sub).getComment_Num()+1);
                    notifyDataSetChanged();
                    CommunityFragment.isNeedRefresh=true;
                    CommunityAboutMeActivity.isNeedRefresh = true;
                    pop_comment.dismiss();
                }
            }
        });
        pop_comment = new PopupWindow(pp_comment,LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_comment.setBackgroundDrawable(new BitmapDrawable());
        pop_comment.setOutsideTouchable(true);
        pop_comment.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                UtilMethod.backgroundAlpha(1,activity);
            }
        });

        //initIsCar();
    }
    private void initPopupWindowDelete() {

        pp_confirm = LayoutInflater.from(activity).inflate(
                R.layout.pp_delete, null);
        pop_delete = new PopupWindow(pp_confirm, UtilMethod.dp2px(activity,300),
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

                DatabaseHelper dbhelper = new DatabaseHelper(activity);
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("PRAGMA foreign_keys = ON;");
                db.beginTransaction(); // 开启事务
                try {
                    if(db.delete("Community_item", "Ci_id=?", new String[]{String.valueOf(communityItems.get(community_sub).getId())})<0) throw  new Exception("delete error!");
                    communityItems.remove(community_sub);
                    notifyDataSetChanged();
                    CommunityAboutMeActivity.isNeedRefresh =true;
                    CommunityFragment.isNeedRefresh = true;
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
                UtilMethod.backgroundAlpha(1,activity);
            }
        });
        //initIsCar();
    }
}
