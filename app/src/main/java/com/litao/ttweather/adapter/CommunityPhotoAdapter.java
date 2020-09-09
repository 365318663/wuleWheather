package com.litao.ttweather.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


import com.litao.ttweather.R;
import com.litao.ttweather.activity.LookPhotoGalleryActivity;
import com.litao.ttweather.tool.ImageUtils;
import com.litao.ttweather.tool.UtilMethod;

public class CommunityPhotoAdapter extends BaseAdapter {
    private static final String TAG ="CommunityPhotoAdapter" ;
    private ArrayList<String> list_path;
    private Activity mActivity;
    private int width;
    private int imageWidth;

    public CommunityPhotoAdapter(Activity mActivity, ArrayList<String> list_path) {
        this.list_path = list_path;
        this.mActivity = mActivity;
        width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        if (0 < list_path.size() && list_path.size() < 4) {
            //Log.i(TAG,"width"+"")
            imageWidth = (width - UtilMethod.dp2px(mActivity, 20) - (list_path.size() - 1) * 5) / list_path.size();
        } else {
            imageWidth = (width - UtilMethod.dp2px(mActivity, 20) - 10) / 3;
        }
    }

    @Override
    public int getCount() {
        int count;
        if (0 < list_path.size() && list_path.size() < 9) {
            count = list_path.size();
        } else {
            count = 9;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        Log.i(TAG,String.valueOf(position));
        if (position < 8) {
            View v1 = LayoutInflater.from(mActivity).inflate(R.layout.gv_photo, null);
            final ImageView img = (ImageView) v1.findViewById(R.id.imageView1);
            img.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
            ImageUtils.getImageFromUrl(list_path.get(position), img);
            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) list_path);
                    intent.setClass(mActivity, LookPhotoGalleryActivity.class);
                    mActivity.startActivity(intent);
                }
            });
            view = v1;
        } else if (list_path.size() > 8 && position == 8) {
            TextView tv = new TextView(mActivity);
            tv.setText("+" + String.valueOf(list_path.size() - 8));
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new AbsListView.LayoutParams(imageWidth, imageWidth));
            tv.setBackgroundColor(Color.BLACK);
            tv.setTextSize(20f);
            tv.setClickable(true);
            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) list_path);
                    intent.setClass(mActivity, LookPhotoGalleryActivity.class);
                    mActivity.startActivity(intent);
                }
            });
            view = tv;



        }
        return view;
    }
}
