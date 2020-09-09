package com.litao.ttweather.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.tool.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ��¼����
 *
 * @author Administrator
 */
public class LoginActivity extends Activity implements OnClickListener {
    private EditText et_name, et_pwd;
    private Button bt_login;
    private TextView tv_register;
    private TextView tv_login_with_visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpView();
        setUpListener();
    }

    private void setUpView() {
        LocationApplication.addActivity_(this);
        SharedPreferences sp = getSharedPreferences("userinfo", 0);
        SharedPreferences.Editor editor = sp.edit();
        HomeActivity.city = null;
        editor.putBoolean("isGotLocation", false);
        editor.putBoolean("isLogined", false);
        editor.commit();
        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd.setOnTouchListener(new OnTouchListener() {

            boolean isFirst = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()�õ�һ������Ϊ4�����飬�ֱ��ʾ������������ͼƬ
                Drawable drawable = et_pwd.getCompoundDrawables()[2];
                // ����ұ�û��ͼƬ�����ٴ���
                if (drawable == null)
                    return false;
                // ������ǰ����¼������ٴ���
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_pwd.getWidth() - et_pwd.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    if (isFirst) {
                        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    isFirst = !isFirst;
                }
                return false;
            }
        });
        tv_login_with_visitor  = findViewById(R.id.tv_login_with_visitor);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        CommunityAboutMeActivity.isNeedRefresh =true;
        CommunityFragment.isNeedRefresh = true;
        HomeActivity.isNeedRefreshWeather=false;
        HomeActivity.isNeedRefreshWeather=false;
    }

    private void setUpListener() {
        bt_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_login_with_visitor.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:// ��¼
                /*
                 * Toast.makeText(getApplicationContext(), "��¼�ɹ�", 0).show();
                 * CacheUtils.putBoolean(LoginActivity.this, Constants.IS_LOGIN,
                 * true); //���õ�¼����� if (ll_login_huozhu.isSelected()) {
                 * CacheUtils.putString(LoginActivity.this, Constants.IDENTITY,
                 * "����"); }else if(ll_login_chezhu.isSelected()){
                 * CacheUtils.putString(LoginActivity.this, Constants.IDENTITY,
                 * "����"); } finish();
                 */
                login1();
                break;

            case R.id.tv_register:// ע��
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_login_with_visitor:// ע��
                Intent intent1 = new Intent();
                intent1.setClass(getApplicationContext(), HomeActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    private void login1() {
        String name = et_name.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();
        //dialog.show();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
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
        Cursor cursor = db.query("User", new String[]{"u_id","u_name","photo_path"}, "u_name=? and password=?", new String[]{name,pwd}, null, null, null);
        System.out.println("查到的数据为：");
        if(cursor.moveToNext()){
            Intent intent = new Intent();
            intent.setClass(this,HomeActivity.class);
            int id = cursor.getInt(cursor.getColumnIndex("u_id"));
            name = cursor.getString(cursor.getColumnIndex("u_name"));
            String photo_path=cursor.getString(cursor.getColumnIndex("photo_path"));
            SharedPreferences sharedPreferences = getSharedPreferences("userinfo",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogined",true);
            editor.putInt("u_id",id);
            editor.putString("u_name",name);
            editor.putString("photo_path",photo_path);
            editor.commit();
            startActivity(intent);
            finish();
        }else{
            cursor = db.query("User", new String[]{"u_name","password"}, "u_name=?", new String[]{name}, null, null, null);
            if(cursor.moveToNext()) {
                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "该用户没注册", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ��¼
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == 1) {
            String user = data.getExtras().getString("user");
            et_name.setText(user);
        }
    }

    private void login() {
        String name = et_name.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();
        //dialog.show();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
       // params.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
        params.addBodyParameter("phone", name);
        params.addBodyParameter("password", pwd);
        httpUtils.send(HttpRequest.HttpMethod.POST, Constants.URL_LOGIN, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println("result:" + responseInfo.result);
                        String result = responseInfo.result;
                        if ("exception".equals(result)) {
                            Toast.makeText(getApplicationContext(),
                                    "服务器异常", Toast.LENGTH_SHORT).show();
                        } else if ("unregist".equals(result)) {
                            Toast.makeText(getApplicationContext(),
                                    "该用户名未注册", Toast.LENGTH_SHORT).show();
                        } else if ("wrong_password".equals(result)) {
                            Toast.makeText(getApplicationContext(), "密码错误",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(result);
                                obj = obj.getJSONObject("memberinfo");
                            } catch (JSONException e1) {

                                e1.printStackTrace();
                            }

                            // 用sp把值给保存起来
                            try {
                                String u_id = obj.getString("userid");
                                String u_name = obj.getString("u_name");
                                String photo_path = obj.getString("photo_path");
                                SharedPreferences sp = getSharedPreferences(
                                        "userinfo", 0);
                                SharedPreferences.Editor e = sp.edit();
                                e.putString("u_id", u_id);
                                e.putString("u_name", u_name);
                                e.putString("photo_path", photo_path);
                                e.putBoolean("isLogined", true);
                                e.commit();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "异常", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(getApplicationContext(), "登录成功",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }


                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LocationApplication.removeALLActivity_();
        }
        return super.onKeyDown(keyCode, event);
    }
}




