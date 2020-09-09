package com.litao.ttweather.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.litao.ttweather.R;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.config.LocationApplication;
import com.litao.ttweather.tool.Constants;


public class UserUpdataPassWord extends AppCompatActivity {
    EditText et_register_name,et_password_old,et_password_new;
    int id;
    Button bt_uppwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_password);

        findView();
        init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("userinfo",0);
        String name =sp.getString("u_name","");
        id = sp.getInt("u_id",0);
        et_register_name.setText(name);
        bt_uppwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uppwd();
            }
        });
        et_password_old.setOnTouchListener(new View.OnTouchListener() {

            boolean isFirst = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()�õ�һ������Ϊ4�����飬�ֱ��ʾ������������ͼƬ
                Drawable drawable = et_password_old.getCompoundDrawables()[2];
                // ����ұ�û��ͼƬ�����ٴ���
                if (drawable == null)
                    return false;
                // ������ǰ����¼������ٴ���
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_password_old.getWidth() - et_password_old.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    if (isFirst) {
                        et_password_old.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        et_password_old.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    isFirst = !isFirst;
                }
                return false;
            }
        });
        et_password_new.setOnTouchListener(new View.OnTouchListener() {

            boolean isFirst = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()�õ�һ������Ϊ4�����飬�ֱ��ʾ������������ͼƬ
                Drawable drawable = et_password_new.getCompoundDrawables()[2];
                // ����ұ�û��ͼƬ�����ٴ���
                if (drawable == null)
                    return false;
                // ������ǰ����¼������ٴ���
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_password_new.getWidth() - et_password_new.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    if (isFirst) {

                        et_password_new.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        et_password_new.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    isFirst=!isFirst;
                }
                return false;
            }
        });
    }

    private void findView() {
        et_register_name =findViewById(R.id.et_register_name);
        et_password_new = findViewById(R.id.et_password_new);
        et_password_old = findViewById(R.id.et_password_old);
        bt_uppwd = findViewById(R.id.bt_uppwd);
    }

    public void back(View view) {
        finish();
    }
    private void uppwd() {
        String name = et_register_name.getText().toString().trim();
        final String new_pwd = et_password_new.getText().toString().trim();
        final String old_pwd = et_password_old.getText().toString().trim();
        //dialog.show();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(new_pwd)) {
            Toast.makeText(getApplicationContext(), "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(old_pwd)) {
            Toast.makeText(getApplicationContext(), "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
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
        Cursor cursor = db.query("User", new String[]{"u_id","u_name","photo_path"}, "u_name=? and password=?", new String[]{name,old_pwd}, null, null, null);
        System.out.println("查到的数据为：");
        if(cursor.moveToNext()){
            SharedPreferences sp = getSharedPreferences("userinfo",0);
            int id = sp.getInt("u_id",0);
            ContentValues values = new ContentValues();
            values.put("password",new_pwd);
            int i=db.update("User", values, "u_id=?", new String[]{String.valueOf(id)});
            if(i>0){
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                LocationApplication.removeALLActivity_();
                startActivity(intent);
            }
        }else{
            cursor = db.query("User", new String[]{"u_name","password"}, "u_name=?", new String[]{name}, null, null, null);
            if(cursor.moveToNext()) {
                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
