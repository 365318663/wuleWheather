package com.litao.ttweather.adapter;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.CommunityAboutMeActivity;
import com.litao.ttweather.activity.LookCommunityItemDetailsActivity;
import com.litao.ttweather.alximageloader.AlxImageLoader;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.entity.Comment;
import com.litao.ttweather.entity.LifeIndex;
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.tool.UtilMethod;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    private LookCommunityItemDetailsActivity activity;
    ArrayList<Comment> comments;
    int icon_width;
    AlxImageLoader alxImageLoader;
    private RelativeLayout confirm,cancle;
    private View pp_confirm;
    private  PopupWindow pop_delete;
    private int delete_sub;
    private TextView tv_title;
    public CommentAdapter(LookCommunityItemDetailsActivity mActivity, ArrayList<Comment> comments) {
        this.activity= mActivity;
        this.comments = comments;
        icon_width = UtilMethod.dp2px(activity,30);
        this.alxImageLoader = new AlxImageLoader(activity);
        initPopupWindowDelete();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Comment comment = comments.get(position);
        View view =convertView;
        if( view ==null){
            view = LayoutInflater.from(activity).inflate(R.layout.lv_comment_item, parent, false);
        }
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_publish_time = view.findViewById(R.id.tv_publish_time);
        TextView tv_content = view.findViewById(R.id.tv_content);
        ImageView iv_delete = view.findViewById(R.id.iv_delete);
        if(comment.isCanDelete()) {
            iv_delete.setVisibility(View.VISIBLE);
        }else {
            iv_delete.setVisibility(View.GONE);
        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_sub = position;
                pop_delete.setFocusable(true);
                pop_delete.showAtLocation(pp_confirm, Gravity.CENTER, 0, 0);
                UtilMethod.backgroundAlpha(0.5f,activity);
            }
        });
        tv_name.setText(comment.getName());
        tv_content.setText(comment.getContent());
        tv_publish_time.setText(comment.getPublish_time());
        alxImageLoader.setAsyncBitmapFromSD(comment.getIcon_path(), iv_icon, icon_width, false, true, true);
        return view;
    }
    private void initPopupWindowDelete() {

        pp_confirm = LayoutInflater.from(activity).inflate(
                R.layout.pp_delete, null);
        pop_delete = new PopupWindow(pp_confirm, UtilMethod.dp2px(activity,300),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_delete.setBackgroundDrawable(new BitmapDrawable());
        pop_delete.setOutsideTouchable(true);
        tv_title = (TextView) pp_confirm.findViewById(R.id.tv_title);
        tv_title.setText("您确定要删除此评论？");
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
                    if(db.delete("Community_item_comment", "Cic_id=?", new String[]{String.valueOf(comments.get(delete_sub).getId())})<0) throw new Exception("delete error!");
                    comments.remove(delete_sub);
                    notifyDataSetChanged();
                    CommunityFragment.isNeedRefresh=true;
                    CommunityAboutMeActivity.isNeedRefresh = true;
                    activity.tv_comment_num.setText("评论（"+comments.size()+"）");
                    activity.tv_comment.setText(String.valueOf(comments.size()));
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
