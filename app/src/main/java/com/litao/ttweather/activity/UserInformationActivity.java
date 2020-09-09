package com.litao.ttweather.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.litao.ttweather.R;
import com.litao.ttweather.alximageloader.AlxImageLoader;
import com.litao.ttweather.config.DatabaseHelper;
import com.litao.ttweather.fragment.CommunityFragment;
import com.litao.ttweather.tool.Constants;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.UtilMethod;


import java.io.File;
import java.util.List;


public class UserInformationActivity extends Activity {
    private RelativeLayout rl_photo;
    private EditText et_nickname;
    private PopupWindow pp_photo, pop_delete;
    private View pp_confirm;
    private View view_pp_photo;
    private TextView tv_cameral, tv_photo, tv_cancell;
    private String oldName;
    private ImageView iv_member_photo;
    private boolean canStop = false;
    private String imgstream = "";
    private final int NEED_CAMERA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        findView();
        init();
        initPopupWindowConform();
        initPopupWindowPhoto();
    }

    private void initPopupWindowPhoto() {
        view_pp_photo = LayoutInflater.from(this).inflate(R.layout.pp_choose_photo, null);
        pp_photo = new PopupWindow(view_pp_photo, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pp_photo.setBackgroundDrawable(new BitmapDrawable());
        pp_photo.setOutsideTouchable(true);
        tv_photo = view_pp_photo.findViewById(R.id.tv_photo);
        tv_cameral = view_pp_photo.findViewById(R.id.tv_cameral);
        tv_cancell = view_pp_photo.findViewById(R.id.tv_cancell);
        tv_cameral.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                pp_photo.dismiss();
                if ((ContextCompat.checkSelfPermission(UserInformationActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)) {
                    UserInformationActivity.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                    Toast.makeText(getApplicationContext(),
                            "请给拍照权限", Toast.LENGTH_SHORT).show();
                } else if ((ContextCompat.checkSelfPermission(UserInformationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
                    UserInformationActivity.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    Toast.makeText(getApplicationContext(),
                            "请给存储权限", Toast.LENGTH_SHORT).show();
                } else {
                    startCamera();
                }

            }
        });
        tv_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pp_photo.dismiss();
            }
        });

        tv_photo.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserInformationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    UserInformationActivity.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    Toast.makeText(getApplicationContext(),
                            "请给存储权限", Toast.LENGTH_SHORT).show();
                } else {
                    choosePicture();
                }
                pp_photo.dismiss();
            }
        });
        pp_photo.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UtilMethod.backgroundAlpha(1, UserInformationActivity.this);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case NEED_CAMERA:
                // 如果权限被拒绝，grantResults 为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {

                }
                break;

        }
    }

    private void init() {
        rl_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pp_photo.showAtLocation(view_pp_photo, Gravity.BOTTOM, 0, 0);
                UtilMethod.backgroundAlpha(0.5f, UserInformationActivity.this);
                pp_photo.setFocusable(true);
            }
        });
        oldName = getIntent().getStringExtra("name");
        et_nickname.setText(oldName);
        SharedPreferences sp = getSharedPreferences("userinfo", 0);
        AlxImageLoader alxImageLoader = new AlxImageLoader(this);
        alxImageLoader.setAsyncBitmapFromSD(sp.getString("photo_path", ""), iv_member_photo,UtilMethod.dp2px(this,80), false, true, true);


    }

    private void findView() {
        rl_photo = findViewById(R.id.rl_photo);
        et_nickname = findViewById(R.id.et_nickname);
        iv_member_photo = findViewById(R.id.iv_member_photo);
    }

    public void back(View view) {
        finish();
    }


    @Override
    public void finish() {
        if (!canStop)
            if (!TextUtils.isEmpty(et_nickname.getText().toString().trim()))
                if (!et_nickname.getText().toString().trim().equals(oldName)) {
                    pop_delete.showAtLocation(pp_confirm, Gravity.CENTER, 0, 0);
                    UtilMethod.backgroundAlpha(0.5f, this);
                    pop_delete.setFocusable(true);
                    return;
                }
        super.finish();
    }

    private void initPopupWindowConform() {

        pp_confirm = LayoutInflater.from(this).inflate(
                R.layout.pp_delete, null);
        pop_delete = new PopupWindow(pp_confirm, UtilMethod.dp2px(this, 300),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_delete.setBackgroundDrawable(new BitmapDrawable());
        pop_delete.setOutsideTouchable(true);
        TextView tv_title = (TextView) pp_confirm.findViewById(R.id.tv_title);
        tv_title.setText("您需要修改用户名？\n\n请注意!\n(修改用户名会引起登录用户名的改变)");
        View confirm = pp_confirm.findViewById(R.id.rl_confirm);
        View cancle = pp_confirm.findViewById(R.id.rl_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                updataUserName();
                pop_delete.dismiss();

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                canStop = true;
                finish();
                pop_delete.dismiss();

            }
        });
        pop_delete.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                UtilMethod.backgroundAlpha(1, UserInformationActivity.this);
            }
        });
        //initIsCar();
    }

    private void updataUserName() {
        final String name = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
        final SharedPreferences sp = getSharedPreferences("userinfo", 0);
        params.addBodyParameter("u_id", sp.getString("u_id", ""));
        params.addBodyParameter("new_u_name", name);

    }


    /**
     * 打开相机获取图片
     */
    private void startCamera() {
        File imagePath = new File(Constants.PHOTO_PATH_ROOT, "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        File newFile = new File(imagePath, "default_image+" + String.valueOf(getSharedPreferences("userinfo", 0).getInt("u_id", 0)) + ".jpg");
        imgstream = newFile.getPath();
        Log.i("sys", imgstream);
        //第二参数是在manifest.xml定义 provider的authorities属性
        Uri contentUri = FileProvider.getUriForFile(this, "com.litao.ttweather.fileprovider", newFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getContentResolver(), "A photo", contentUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, PHOTO_CAMERA);
    }

    private final int PHOTO_CAMERA = 1000;

    /**
     * 选取图片
     */
    private void choosePicture() {
        if (!ImageTools.checkSDCardAvailable()) {
            Toast.makeText(getApplicationContext(), "内存卡错误,请检查您的内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intentphoto = new Intent(Intent.ACTION_PICK);
        intentphoto.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Constants.IMAGE_UNSPECIFIED);
        startActivityForResult(intentphoto, Constants.PHOTOZOOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照
        if (requestCode == PHOTO_CAMERA) {
            if (resultCode == RESULT_OK) {// 返回成功的时候
                // 防止内存溢出
                Bitmap pic = ImageTools.scacleToBitmap(imgstream, this);
                if (iv_member_photo != null && pic != null) {// 这个ImageView是拍照完成后显示图片
                    iv_member_photo.setImageBitmap(pic);
                    iv_member_photo.setBackgroundColor(Color.TRANSPARENT);
                    updataUserIcon(imgstream);
                }
            } else if (resultCode == RESULT_CANCELED) {// 取消的时候
                Toast.makeText(getApplicationContext(), "取消拍照", Toast.LENGTH_SHORT).show();
            } else {
                // 失败的时候
                Toast.makeText(getApplicationContext(), "拍照失败", Toast.LENGTH_SHORT).show();
            }

        }

        // 读取相册缩放图片
        if (requestCode == Constants.PHOTOZOOM && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String path = ImageTools.changeUriToPath(uri, this);
                Bitmap photo = ImageTools.scacleToBitmap(path, this);
                if (photo != null) {
                    if (iv_member_photo != null) {
                        iv_member_photo.setImageBitmap(photo);
                        iv_member_photo.setBackgroundColor(Color.TRANSPARENT);
                    }
                    // 保存照片地址
                    updataUserIcon(path);

                }
            } else {
                Toast.makeText(getApplicationContext(), "图片选取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updataUserIcon(String path) {
        SharedPreferences sp = getSharedPreferences("userinfo", 0);
        int id = sp.getInt("u_id", 0);
        ContentValues values = new ContentValues();
        values.put("photo_path", path);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //得到可写的SQLiteDatabase对象
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //调用insert方法，将数据插入数据库
        //参数3：where 子句 "?"是占位符号，对应后面的"1",这和web开发时的语法是一样的
        int i = db.update("User", values, "u_id=?", new String[]{String.valueOf(id)});
        if (i > 0) {
            Log.i("syso", path);
            //new ImageTools().setImage(imgstream, iv_member_photo);
            //iv_member_photo.setImageBitmap(UtilMethod
            //       .toRoundBitmap(imgstream));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("photo_path", path);
            editor.commit();
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        }
        CommunityFragment.isNeedRefresh =true;
        CommunityAboutMeActivity.isNeedRefresh=true;
    }


}
