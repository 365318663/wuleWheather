package com.litao.ttweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.litao.ttweather.R;
import com.litao.ttweather.bean.CityBean;
import com.litao.ttweather.tool.CityListLoader;


public class CitypickerThreeListActivity extends AppCompatActivity {
    TextView mListTv;
    
    TextView mResultTv;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citypicker_three_list);
        findView();
    }
    
    private void findView() {
        mListTv = (TextView) findViewById(R.id.list_tv);
        mResultTv = (TextView) findViewById(R.id.result_tv);
        
        mListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list();
            }
        });
        CityListLoader.getInstance().loadProData(this);
    }
    
    public void list() {

        Intent intent = new Intent(CitypickerThreeListActivity.this, ProvinceActivity.class);
        startActivityForResult(intent, ProvinceActivity.RESULT_DATA_CONFORM);
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProvinceActivity.RESULT_DATA_CONFORM) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }

                CityBean city = data.getParcelableExtra("city");
                CityBean province = data.getParcelableExtra("province");
                
                mResultTv.setText("所选省市区城市： " + province.getName() +  "\n" + city.getName());
            }
        }
    }
}
