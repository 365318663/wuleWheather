package com.litao.ttweather.activity;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.litao.ttweather.tool.Constants;


/**
 * ע�����
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {
	private EditText et_register_name,et_register_pwd,et_register_pwd1;
	private Button bt_regiset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		setUpView();
		setUpListener();
		//init();
		
	}

	private void setUpView() {
		et_register_name=(EditText) findViewById(R.id.et_register_name);
		et_register_pwd=(EditText) findViewById(R.id.et_register_pwd);
		et_register_pwd.setOnTouchListener(new OnTouchListener() {

			boolean isFirst = true;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// et.getCompoundDrawables()�õ�һ������Ϊ4�����飬�ֱ��ʾ������������ͼƬ
				Drawable drawable = et_register_pwd.getCompoundDrawables()[2];
				// ����ұ�û��ͼƬ�����ٴ���
				if (drawable == null)
					return false;
				// ������ǰ����¼������ٴ���
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;
				if (event.getX() > et_register_pwd.getWidth() - et_register_pwd.getPaddingRight()
						- drawable.getIntrinsicWidth()) {
					if (isFirst) {

						et_register_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					} else {
						et_register_pwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
					isFirst=!isFirst;
				}
				return false;
			}
		});
		et_register_pwd1=(EditText) findViewById(R.id.et_register_pwd1);
		et_register_pwd1.setOnTouchListener(new OnTouchListener() {

			boolean isFirst = true;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// et.getCompoundDrawables()�õ�һ������Ϊ4�����飬�ֱ��ʾ������������ͼƬ
				Drawable drawable = et_register_pwd1.getCompoundDrawables()[2];
				// ����ұ�û��ͼƬ�����ٴ���
				if (drawable == null)
					return false;
				// ������ǰ����¼������ٴ���
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;
				if (event.getX() > et_register_pwd1.getWidth() - et_register_pwd1.getPaddingRight()
						- drawable.getIntrinsicWidth()) {
					if (isFirst) {
						et_register_pwd1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					} else {
						et_register_pwd1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

					}
					isFirst=!isFirst;
					Log.i("touch","go");
				}
				return false;
			}
		});
		bt_regiset=(Button) findViewById(R.id.bt_regiset);
	}

	private void setUpListener() {
		bt_regiset.setOnClickListener(this);
	}



	public void back(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		String name=et_register_name.getText().toString().trim();
		String pwd=et_register_pwd.getText().toString().trim();
		String pwd1=et_register_pwd1.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(pwd1)) {
			Toast.makeText(getApplicationContext(), "确认密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!pwd.equals(pwd1)) {
			Toast.makeText(getApplicationContext(), "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
			return;
		}
		//getData(name,pwd);
		//dialog.show();
		//
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
		Cursor cursor = db.query("User", new String[]{"u_name","password"}, "u_name=?", new String[]{name}, null, null, null);
		//System.out.println("查到的数据为：");
		if(cursor.moveToNext()){
			Toast.makeText(getApplicationContext(), "该用户已注册", Toast.LENGTH_SHORT).show();
		}else{
			register(name,pwd);
		}

		
	}

	private void register(String name,String pwd) {
		ContentValues values = new ContentValues();
		values.put("u_name", et_register_name.getText().toString());
		values.put("password", et_register_pwd.getText().toString());
		DatabaseHelper dbhelper = new DatabaseHelper(this);
		//得到可写的SQLiteDatabase对象
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		//调用insert方法，将数据插入数据库
		//参数1：表名
		//参数2：如果你想插入空值，那么你必须指定它的所在的列
		db.insert("User", null, values);
		Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
		finish();
		//System.out.println("插入了：1, hello");
	}

	/**
	 * ���������ȡ����
	 */
	private void getData(final String name,String pwd) {
		HttpUtils httpUtils=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
		params.addBodyParameter("phone", name);
		params.addBodyParameter("password", pwd);
		httpUtils.send(HttpRequest.HttpMethod.POST, Constants.URL_REGISTER, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				if (!TextUtils.isEmpty(result)) {
					if ("exception".equals(result)) {
						Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
					}else if ("registered".equals(result)) {
						Toast.makeText(getApplicationContext(), "该用户名已注册", Toast.LENGTH_SHORT).show();
					}else if ("true".equals(result)) {
						Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), LoginActivity.class);
						intent.putExtra("user", name);
						setResult(1,intent);
						finish();
					}
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
			}
		});

	}

}
