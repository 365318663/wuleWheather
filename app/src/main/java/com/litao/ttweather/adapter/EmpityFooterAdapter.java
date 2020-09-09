package com.litao.ttweather.adapter;

import android.content.Context;
import android.view.View;

import com.litao.ttweather.R;



/**
 * Created by engineer on 2017/4/29.
 */

public class EmpityFooterAdapter extends BaseFooterAdapter {


    public EmpityFooterAdapter(Context context) {
        super(context);
    }

    @Override
    public View getFooterView() {
        View mFooterView = mInflater.inflate(R.layout.activity_common, null, false);

        return mFooterView;
    }

    @Override
    public void pullViewToRefresh(int deltaY) {

    }


    @Override
    public void releaseViewToRefresh(int deltaY) {

    }

    @Override
    public void footerRefreshing() {

    }

    @Override
    public void footerRefreshComplete() {

    }
}
