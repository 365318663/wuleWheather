package com.litao.ttweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.litao.ttweather.R;
import com.litao.ttweather.alximageloader.SelectPhotoAdapter;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.ImageUtils;

public class LookPhotoGalleryActivity extends Activity implements OnPageChangeListener{  
    /** 
     * ViewPager 
     */  
    private ViewPager viewPager;  
      
    /** 
     * װImageView���� 
     */
    private ArrayList<View> views;
    public ArrayList<SelectPhotoAdapter.SelectPhotoEntity> allPhotoList;

    private TextView tv_locaiton;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);
        tv_locaiton = (TextView) findViewById(R.id.tv_location);
        viewPager = (ViewPager) findViewById(R.id.viewPager);  
        Bundle b = this.getIntent().getExtras();
        allPhotoList =  b.getParcelableArrayList("list");
        int index = b.getInt("index");
        //��ͼƬװ�ص�������
        views = new ArrayList<>();
        for(int i = 0;i<allPhotoList.size();i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.vp_photo, null, false);
            ImageView iv_photo = view.findViewById(R.id.iv_photo);
            new ImageTools().setImageWithNoHandle(allPhotoList.get(i).url, iv_photo);
            views.add(view);
        }
        //����Adapter  
        viewPager.setAdapter(new MyAdapter());  
        //���ü�������Ҫ�����õ��ı���  
        viewPager.setOnPageChangeListener(this);  
        //����ViewPager��Ĭ����, ����Ϊ���ȵ�100���������ӿ�ʼ�������󻬶�  
        viewPager.setCurrentItem(index);  
          
    }  
      
    /** 
     *  
     * @author xiaanming 
     * 
     */  
    public class MyAdapter extends PagerAdapter{  
  
        @Override  
        public int getCount() {  
            return allPhotoList.size();
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public void destroyItem(View container, int position, Object object) {  
            ((ViewPager)container).removeView(views.get(position));
              
        }  
  
        /** 
         * ����ͼƬ��ȥ���õ�ǰ��position ���� ͼƬ���鳤��ȡ�����ǹؼ� 
         */  
        @Override  
        public Object instantiateItem(View container, int position) {

            ((ViewPager)container).addView(views.get(position), 0);
            return views.get(position);
        }  
          
          
          
    }  
  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
          
    }  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
    	tv_locaiton.setText(arg0%allPhotoList.size()+1+"/"+allPhotoList.size());
    }  
  
    @Override  
    public void onPageSelected(int arg0) { 
    	tv_locaiton.setText(arg0%allPhotoList.size()+1+"/"+allPhotoList.size());
    }  
    public void back(View v){
    	finish();
    }
} 