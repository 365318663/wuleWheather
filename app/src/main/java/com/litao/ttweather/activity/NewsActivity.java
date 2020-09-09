package com.litao.ttweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.litao.ttweather.R;

public class NewsActivity extends Activity {
    WebView wv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        wv = findViewById(R.id.wv_web);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setJavaScriptEnabled(true);//启用js
        wv.getSettings().setBlockNetworkImage(false);//解决图片不显示
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        wv.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                //webview访问出net::ERR_UNKNOWN_URL_SCHEME错的解决地方

                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl(getIntent().getStringExtra("url"));
    }
    public void back(View view) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack(); // goBack()表示返回WebView的上一页面
            return  false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
