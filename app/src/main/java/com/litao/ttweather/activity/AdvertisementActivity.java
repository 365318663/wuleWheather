package com.litao.ttweather.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.litao.ttweather.BuildConfig;
import com.litao.ttweather.R;
import com.litao.ttweather.adapter.CityAdapter;
import com.litao.ttweather.bean.CityBean;
import com.litao.ttweather.bean.CityInfoBean;
import com.litao.ttweather.config.LocationApplication;
import com.litao.ttweather.tool.ImageUtils;
import com.litao.ttweather.view.RecycleViewDividerForList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.litao.ttweather.tool.CityListLoader.BUNDATA;


public class AdvertisementActivity extends Activity {

    private Button bt_cancel;
    private ImageView iv_ad_img;
    private boolean isJumped = false;
    int second = 5;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                second--;
                bt_cancel.setText(second + "S");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                if (second == 0&&!isJumped) {
                    Intent intent = new Intent();
                    intent.setClass(AdvertisementActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

    @Override
    public void finish() {
        isJumped=true;
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        LocationApplication.addActivity_(this);
        findView();
        initView();


    }

    private void findView() {
        iv_ad_img = findViewById(R.id.iv_ad_img);
        bt_cancel = findViewById(R.id.bt_cancel);
    }


    private void initView() {

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdvertisementActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        iv_ad_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_mainHandler = new Handler();
                m_progressDlg = new ProgressDialog(AdvertisementActivity.this);
                m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
                m_progressDlg.setIndeterminate(false);
                m_appNameStr = "com.tengyun.crm.apk";
                doNewVersionUpdate();
            }
        });
        SharedPreferences sp = getSharedPreferences("userinfo", 0);
        Time time = new Time();
        time.setToNow();
        long time_millis = time.toMillis(true);
        final long history_time = sp.getLong("history_time", 0);
        if (time_millis - history_time >= 0) {//30*60000
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("history_time", time_millis);
            editor.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        for (int i = 0; i < 5; i++) {
                            Thread.sleep(1000);
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        } else {
            Intent intent = new Intent();
            intent.setClass(AdvertisementActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }


    String m_appNameStr; // 下载到本地要给这个APP命的名字
    Handler m_mainHandler;
    ProgressDialog m_progressDlg;


    /**
     * 从服务器获取当前最新版本号，如果成功返回TURE，如果失败，返回FALSE
     *
     * @return
     */


    /**
     * 提示更新新版本
     */
    private void doNewVersionUpdate() {
        String str = "唔乐天气";
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("软件下载")
                .setMessage(str)
                // 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                m_progressDlg.setTitle("正在下载");
                                m_progressDlg.setMessage("请稍候...");
                                downFile("http://gdown.baidu.com/data/wisegame/06ed75c9055ec156/baidushoujizhushou_16795029.apk"); // 开始下载
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 点击"取消"按钮之后退出程序
                                dialog.dismiss();
                            }
                        }).create();// 创建

        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

    }


    private void downFile(final String url) {
        m_progressDlg.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();

                    m_progressDlg.setMax((int) length);// 设置进度条的最大值

                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                m_appNameStr);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                m_progressDlg.setProgress(count);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down(); // 告诉HANDER已经下载完成了，可以安装了
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 告诉HANDER已经下载完成了，可以安装了
     */
    private void down() {
        m_mainHandler.post(new Runnable() {
            public void run() {
                if (!isJumped)
                    m_progressDlg.cancel();
                update();
            }
        });
    }

    /**
     * 安装程序
     */
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.litao.ttweather.fileprovider", new File(Environment
                    .getExternalStorageDirectory().getPath()+"/"+m_appNameStr));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), m_appNameStr)),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }


}
