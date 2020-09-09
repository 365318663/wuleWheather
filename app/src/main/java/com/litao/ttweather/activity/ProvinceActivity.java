package com.litao.ttweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.litao.ttweather.R;
import com.litao.ttweather.adapter.CityAdapter;
import com.litao.ttweather.bean.CityBean;
import com.litao.ttweather.bean.CityInfoBean;
import com.litao.ttweather.tool.CityListLoader;
import com.litao.ttweather.view.RecycleViewDividerForList;


import java.util.List;

import static com.litao.ttweather.tool.CityListLoader.BUNDATA;


public class ProvinceActivity extends Activity {

    private TextView mCityNameTv;
    
    private RecyclerView mCityRecyclerView;
    
    public static final int RESULT_DATA_CONFORM = 1001;

    public static final int RESULT_DATA_CANCEL = 1002;

    private CityBean provinceBean = new CityBean();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist);
        initView();
        setData();
        
    }
    
    private void setData() {
        
        final List<CityInfoBean> cityList = CityListLoader.getInstance().getProListData();
        if (cityList == null) {

            return;
        }
        
        CityAdapter cityAdapter = new CityAdapter(ProvinceActivity.this, cityList);
        mCityRecyclerView.setAdapter(cityAdapter);
        cityAdapter.setOnItemClickListener(new CityAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                
                provinceBean.setId(cityList.get(position).getId());
                provinceBean.setName(cityList.get(position).getName());
                Intent intent = new Intent(ProvinceActivity.this, CityActivity.class);
                intent.putExtra(BUNDATA, cityList.get(position));
                startActivityForResult(intent, RESULT_DATA_CONFORM);
                
            }
        });
        
    }
    public void back(View view) {
        finish();
    }
    private void initView() {
        mCityNameTv = (TextView) findViewById(R.id.cityname_tv);
        mCityNameTv.setText("选择省份");
        mCityRecyclerView = (RecyclerView) findViewById(R.id.city_recyclerview);
        mCityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCityRecyclerView.addItemDecoration(new RecycleViewDividerForList(this, LinearLayoutManager.HORIZONTAL, true));
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_DATA_CONFORM) {
            // CityBean area = data.getParcelableExtra("area");
            if (data == null) {
                return;
            }
            CityBean city = data.getParcelableExtra("city");
            Intent intent = new Intent();
            intent.putExtra("province", provinceBean);
            intent.putExtra("city", city);
            // intent.putExtra("area", area);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
