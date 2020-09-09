package com.litao.ttweather.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.entity.LifeIndex;

import java.util.ArrayList;

public class LifeIndexAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<LifeIndex> lifeIndices;


    public LifeIndexAdapter(Activity mActivity, ArrayList<LifeIndex> lifeIndices) {
        this.activity= mActivity;
        this.lifeIndices = lifeIndices;
    }

    @Override
    public int getCount() {
        return lifeIndices.size();
    }

    @Override
    public Object getItem(int position) {
        return lifeIndices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(activity).inflate(R.layout.lv_life_index_item,parent,false);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        TextView tv_index=view.findViewById(R.id.tv_index);
        TextView tv_notice=view.findViewById(R.id.tv_notice);
        LifeIndex item= lifeIndices.get(position);
        if(item.getName().contains("紫外线")){
            iv_icon.setImageDrawable(activity.getResources().getDrawable((R.drawable.uv)));
        }else if(item.getName().contains("血糖")){
            iv_icon.setImageDrawable(activity.getResources().getDrawable((R.drawable.blood_sogar)));
        }else if(item.getName().contains("减肥")){
            iv_icon.setImageDrawable(activity.getResources().getDrawable((R.drawable.cold)));
        }else if(item.getName().contains("穿衣")){
            iv_icon.setImageDrawable(activity.getResources().getDrawable((R.drawable.clothes)));
        }else if(item.getName().contains("洗车")){
            iv_icon.setImageDrawable(activity.getResources().getDrawable((R.drawable.wash_car)));
        }else if(item.getName().contains("空气")){
            iv_icon.setImageDrawable(activity.getResources().getDrawable((R.drawable.air_pollute)));
        }
        tv_index.setText(item.getName()+"："+item.getIndex());
        tv_notice.setText(item.getNotice());

        return view;
    }

}
