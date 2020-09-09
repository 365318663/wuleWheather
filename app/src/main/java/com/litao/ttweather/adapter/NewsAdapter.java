package com.litao.ttweather.adapter;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.NewsActivity;
import com.litao.ttweather.entity.LifeIndex;
import com.litao.ttweather.entity.News;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.ImageUtils;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {
    private static final String TAG ="NewsAdapter" ;
    private Activity activity;
    ArrayList<News> news;


    public NewsAdapter(Activity mActivity, ArrayList<News> news) {
        this.activity= mActivity;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(activity).inflate(R.layout.lv_news_item,parent,false);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        TextView tv_title=view.findViewById(R.id.tv_index);
        TextView tv_notice=view.findViewById(R.id.tv_notice);
        LinearLayout ll_head = view.findViewById(R.id.ll_head);
        final News news1 = news.get(position);
        if(!TextUtils.isEmpty(news1.getLink())) {
            ImageUtils.getImageFromUrl(news1.getIcon_url(), iv_icon);
        }else {
            iv_icon.setVisibility(View.GONE);
        }
        tv_title.setText(news1.getTitle());
        tv_notice.setText(news1.getAuthor_name()+"  "+news1.getDate()+"  "+news1.getTime());
        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onClick");
                Intent intent = new Intent();
                intent.setClass(activity, NewsActivity.class);
                intent.putExtra("url",news1.getLink());
                activity.startActivity(intent);
            }
        });
        return view;
    }

}
